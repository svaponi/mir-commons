package it.miriade.commons.model.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Utility per esporre all'utente oggetti a basso livello per l'accesso al DB, come {@link Connection},
 * {@link DataSource} o {@link JdbcTemplate}.
 * 
 * @author svaponi
 */
public class DbUtil {

	protected JdbcTemplate jdbcTemplate;

	@Autowired(required = false)
	protected DriverManagerDataSource dataSource;

	@Autowired(required = false)
	protected ComboPooledDataSource pooledDataSource;

	public DbUtil() {
	}

	@Autowired
	public DbUtil(DriverManagerDataSource dataSource) {
		this.setDataSource(dataSource);
	}

	@Autowired
	public DbUtil(ComboPooledDataSource pooledDataSource) {
		this.setPooledDataSource(pooledDataSource);
	}

	public DriverManagerDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DriverManagerDataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public ComboPooledDataSource getPooledDataSource() {
		return pooledDataSource;
	}

	public void setPooledDataSource(ComboPooledDataSource pooledDataSource) {
		this.pooledDataSource = pooledDataSource;
		this.jdbcTemplate = new JdbcTemplate(pooledDataSource);
	}

	public DataSource dataSource() throws SQLException {
		if (pooledDataSource != null)
			return pooledDataSource;
		if (dataSource != null)
			return dataSource;
		throw new RuntimeException("No data source");
	}

	public Connection getConnection() throws SQLException {
		return dataSource().getConnection();
	}

	public JdbcTemplate jdbc() {
		return this.jdbcTemplate;
	}

}
