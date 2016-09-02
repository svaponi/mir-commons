package it.miriade.commons.model.entities.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.miriade.commons.utils.ExHandler;
import it.miriade.commons.utils.StringHandler;

/**
 * Generatore di stringhe analogo partendo dai valori di una sequence. Esempio:
 * <blockquote>
 * &#64;Id <br/>
 * &#64;GenericGenerator(name = "idGenerator",
 * strategy = "it.miriade.commons.model3.entities.utils.StringIdGenerator",
 * parameters = {<br/>
 * &#64;Parameter(value = "field_name_id_seq", name = "sequence"),
 * &#64;Parameter(value = "12", name = "length")<br/>
 * })<br/>
 * &#64;GeneratedValue(generator = "idGenerator")<br/>
 * &#64;Column(name = "field_name")<br/>
 * private fieldName;
 * </blockquote>
 * 
 * @author sam
 */
public class StringIdGenerator implements IdentifierGenerator, Configurable {

	public static final String defaultSequence = "hibernate_sequence";

	public static final String sequenceNameParam = "sequence";

	public static final String uidLengthParam = "length";

	private static final int defaultlength = 8;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String sequence;

	private Integer length;

	/**
	 * Genera un UID valido
	 * 
	 * @param object
	 *            è la entity per la quale si sta generando il UID
	 */
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {

		logger.warn("Argument: %s", object);
		String key = null;
		try {
			Connection connection = session.getJdbcConnectionAccess().obtainConnection();
			String SQL = String.format("SELECT nextval('%s') as nextval", sequence);
			PreparedStatement ps = connection.prepareStatement(SQL);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String id = "" + rs.getInt("nextval");
				key = StringHandler.left(id, length, '0');
			} else {
				throw new Exception("Ivalid nextval for " + sequence);
			}

		} catch (Exception e) {
			key = UUID.randomUUID().toString();
			logger.warn(ExHandler.getRoot(e));
			logger.debug(ExHandler.getStackTraceButRoot(e));
		} finally {
			logger.warn(String.format("Generated value: %s (sequence: %s)", key, sequence));
		}
		return key;
	}

	/**
	 * Controlla se tra i parametri di inizializzazione è stat specificata una sequence da usare al posto del default
	 * {@link StringIdGenerator#defaultSequence}
	 */
	@Override
	public void configure(Type type, Properties params, Dialect d) throws MappingException {

		if (params.containsKey(sequenceNameParam))
			sequence = params.getProperty(sequenceNameParam);
		else
			sequence = defaultSequence;

		if (params.containsKey(uidLengthParam))
			length = Integer.decode(params.getProperty(uidLengthParam));
		else
			length = defaultlength;
	}

}
