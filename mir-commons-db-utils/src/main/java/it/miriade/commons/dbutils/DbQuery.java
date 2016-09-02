package it.miriade.commons.dbutils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import it.miriade.commons.collections.CollectionUtils;
import it.miriade.commons.utils.StringHandler;

/**
 * Permette di fare query e gestisce in modo trasparente la connessione. Torna un oggetto che incapsula il risultato
 * della query.
 * 
 * @author svaponi
 * @created Jul 21, 2016 12:51:40 PM
 */
@Component
public class DbQuery {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataSource dataSource;

	@Autowired
	public DbQuery(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@PostConstruct
	public void notNull() throws Exception {
		Assert.notNull(dataSource, "DataSource is null");
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Inject params into {@link PreparedStatement} according to their runtime type.
	 * 
	 * @param pstmt
	 * @param params
	 * @throws SQLException
	 */
	private void injectParams(PreparedStatement pstmt, final Collection<Object> params) throws SQLException {
		int i = 1; // i parametri sql partono da 1
		if (params != null)
			for (final Object p : params) {
				final String type = p.getClass().getName();
				// log.debug("type[" + p + "]: {}", type);
				switch (type) {
				case "java.lang.String":
					pstmt.setString(i, (String) p);
					log.debug("{} param is String, value: {} [{}]", i, p, type);
					break;
				case "java.lang.Boolean":
					pstmt.setBoolean(i, (Boolean) p);
					log.debug("{} param is Boolean, value: {} [{}]", i, p, type);
					break;
				case "java.lang.Double":
					pstmt.setDouble(i, (Double) p);
					log.debug("{} param is Double, value: {} [{}]", i, p, type);
					break;
				case "java.lang.Float":
					pstmt.setFloat(i, (Float) p);
					log.debug("{} param is Float, value: {} [{}]", i, p, type);
					break;
				case "java.lang.Long":
					pstmt.setLong(i, (Long) p);
					log.debug("{} param is String, value: {} [{}]", i, p, type);
					break;
				case "java.lang.Integer":
					pstmt.setInt(i, (Integer) p);
					log.debug("{} param is Integer, value: {} [{}]", i, p, type);
					break;
				case "java.lang.Short":
					pstmt.setShort(i, (Short) p);
					log.debug("{} param is Short, value: {} [{}]", i, p, type);
					break;
				case "java.math.BigDecimal":
					pstmt.setBigDecimal(i, (BigDecimal) p);
					log.debug("{} param is BigDecimal, value: {} [{}]", i, p, type);
					break;
				case "java.util.Date":
					java.util.Date date = (java.util.Date) p;
					Date sqlDate = new Date(date.getTime());
					pstmt.setDate(i, sqlDate);
					log.debug("{} param is Date, value: {} [{}]", i, p, type);
					break;
				case "java.sql.Date":
					pstmt.setDate(i, (Date) p);
					log.debug("{} param is Date, value: {} [{}]", i, p, type);
					break;
				case "java.lang.Byte":
					pstmt.setBytes(i, (byte[]) p);
					log.debug("{} param is Byte, value: {} [{}]", i, p, type);
					break;
				default:
					pstmt.setObject(i, p);
					log.debug("{} param is Object, value: {} [{}]", i, p, type);
					break;
				}
				i++;
			}
	}

	/**
	 * Executes the SQL statement in this PreparedStatement object, which must be an SQL Data Manipulation Language
	 * (DML) statement, such as INSERT, UPDATE or DELETE; or an SQL statement that returns nothing, such as a DDL
	 * statement.
	 * 
	 * @see {@link PreparedStatement#executeUpdate()}
	 * @param query
	 * @param params
	 * @return
	 */
	public int update(final String query, final Collection<Object> params) {

		log.debug("Query: {}", query);
		if (CollectionUtils.notEmpty(params))
			log.debug("Params: {}", params);

		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = -1;

		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(query);
			injectParams(pstmt, params);

			result = pstmt.executeUpdate();

			pstmt.close();
		} catch (final Exception e) {
			log.error(e.getMessage());
		} finally {
			close(pstmt);
			close(conn);
		}

		return result;
	}

	/**
	 * @see {@ DbQuery#update(String, Collection)}
	 * @param query
	 * @return
	 */
	public int update(final String query) {
		return update(query, Collections.emptyList());
	}

	public int update(final String query, Object... args) {
		return update(query, Arrays.asList(args));
	}

	/**
	 * Executes the SQL query in this PreparedStatement object and returns the ResultSet object generated by the query.
	 * 
	 * @see {@link PreparedStatement#executeQuery()}
	 * @param query
	 * @param params
	 * @return
	 */
	public DbResult select(final String query, final Collection<Object> params) {

		log.debug("Query: {}", query);
		if (CollectionUtils.notEmpty(params))
			log.debug("Params: {}", params);

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		final DbResult result = new DbResult();
		List<Object> row;

		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(query);
			injectParams(pstmt, params);
			rs = pstmt.executeQuery();
			final int rowSize;
			if (rs.next()) {
				meta = rs.getMetaData();
				rowSize = meta.getColumnCount();
				log.debug("Result: {} row{}", rowSize, rowSize == 1 ? "" : "s");
				for (int i = 1; i <= rowSize; i++) {
					result.columns.add(meta.getColumnName(i).toUpperCase());
					result.types.add(meta.getColumnTypeName(i).toUpperCase());
					result.classes.add(meta.getColumnClassName(i).toUpperCase());
				}
				// rs.beforeFirst();
				do {
					row = new ArrayList<Object>();
					for (int j = 1; j <= rowSize; j++) {
						row.add(rs.getObject(j));
					}
					result.set.add(row);
				} while (rs.next());
			}
		} catch (final Exception e) {
			log.error(e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
			close(conn);
		}

		return result;
	}

	/**
	 * @see {@ DbQuery#select(String, Collection)}
	 * @param query
	 * @return
	 */
	public DbResult select(final String query) {
		return select(query, Collections.emptyList());
	}

	public DbResult select(final String query, Object... args) {
		return select(query, Arrays.asList(args));
	}

	/**
	 * Submits a batch of commands to the database for execution and if all commands execute successfully, returns an
	 * array of update counts. The int elements of the array that is returned are ordered to correspond to the commands
	 * in the batch, which are ordered according to the order in which they were added to the batch. The elements in the
	 * array returned by the method executeBatch may be one of the following:
	 * A number greater than or equal to zero -- indicates that the command was processed successfully and is an update
	 * count giving the number of rows in the database that were affected by the command's execution
	 * A value of SUCCESS_NO_INFO -- indicates that the command was processed successfully but that the number of rows
	 * affected is unknown
	 * If one of the commands in a batch update fails to execute properly, this method throws a BatchUpdateException,
	 * and a JDBC driver may or may not continue to process the remaining commands in the batch. However, the driver's
	 * behavior must be consistent with a particular DBMS, either always continuing to process commands or never
	 * continuing to process commands. If the driver continues processing after a failure, the array returned by the
	 * method BatchUpdateException.getUpdateCounts will contain as many elements as there are commands in the batch, and
	 * at least one of the elements will be the following:
	 * A value of EXECUTE_FAILED -- indicates that the command failed to execute successfully and occurs only if a
	 * driver continues to process commands after a command fails
	 * The possible implementations and return values have been modified in the Java 2 SDK, Standard Edition, version
	 * 1.3 to accommodate the option of continuing to proccess commands in a batch update after a BatchUpdateException
	 * obejct has been thrown.
	 * TODO implementare la versione con il PreparedStatement
	 * 
	 * @see {@link Statement#executeBatch()}
	 * @param queries
	 * @return
	 */
	public int[] batch(final Collection<String> queries) {

		int[] result = { -1 };
		if (CollectionUtils.isEmpty(queries)) {
			log.warn("Missing batch queries!");
			return result;
		} else
			log.debug("Batch: {} quer{}\n{}", queries.size(), queries.size() == 1 ? "y" : "ies",
					StringHandler.join("\n", "  <sql>%s</sql>", queries.toArray()));

		Connection conn = null;
		Statement stmt = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			for (String query : queries)
				stmt.addBatch(query);

			result = stmt.executeBatch();

		} catch (final Exception e) {
			log.error(e.getMessage());
		} finally {
			close(stmt);
			close(conn);
		}

		return result;
	}

	/**
	 * @see {@link DbQuery#batch(String[])}
	 * @param queries
	 * @return
	 */
	public int[] batch(final String[] queries) {
		return batch(Arrays.asList(queries));
	}

	/**
	 * Ritorna una lista di entities, definite a runtime dalla classe parssata in input, e valorizzate con i dati della
	 * query
	 * 
	 * @param query
	 * @param resultBeanClass
	 * @return
	 */
	public <T> List<T> getEntityList(String query, Class<T> resultBeanClass) {
		PreparedStatement stat = null;
		ResultSet result = null;
		Connection conn = null;
		Object tmp;
		String type;
		try {
			conn = dataSource.getConnection();
			stat = conn.prepareStatement(query);
			result = stat.executeQuery();
			BeanInfo resultBeanInfo = Introspector.getBeanInfo(resultBeanClass);
			ArrayList<T> results = new ArrayList<T>();
			while (result.next()) {
				T resultBean = resultBeanClass.newInstance();
				PropertyDescriptor[] arrpropertyDescriptor = resultBeanInfo.getPropertyDescriptors();
				for (PropertyDescriptor property : arrpropertyDescriptor) {
					Method methodSet = property.getWriteMethod();
					if (methodSet != null)
						try {
							type = methodSet.getParameterTypes()[0].getSimpleName();
							tmp = ResultSet.class.getDeclaredMethod("get" + StringHandler.capitalize(type), String.class).invoke(result, property.getName());
							log.debug("{} {} = {}; types: {}", type, property.getName(), tmp, Arrays.asList(methodSet.getParameterTypes()));
							methodSet.invoke(resultBean, tmp);
						} catch (Exception e) {
							log.warn("{}", e.getMessage());
						}
				}
				results.add(resultBean);
			}
			return results;
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			close(result);
			close(stat);
			close(conn);
		}
		return Collections.emptyList();
	}

	/**
	 * Come {@link DbQuery#getEntityList(String, Class)} però torna una sola entity entity. Se la query ritorna più di
	 * una riga le altre vengono ignorate
	 * 
	 * @param query
	 * @param resultBeanClass
	 * @return
	 */
	public <T> T getEntity(String query, Class<T> resultBeanClass) {
		List<T> tmp = getEntityList(query, resultBeanClass);
		if (CollectionUtils.notEmpty(tmp))
			return tmp.get(0);
		return null;
	}

	/**
	 * Chiude tutto quello che gli si passa e ignora eventuali eccezioni
	 * 
	 * @param closable
	 */
	private static void close(AutoCloseable closable) {
		if (closable != null)
			try {
				closable.close();
			} catch (Exception e) {
				// ignore
			}
	}

}