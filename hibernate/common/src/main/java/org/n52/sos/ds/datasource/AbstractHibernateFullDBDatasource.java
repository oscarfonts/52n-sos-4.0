package org.n52.sos.ds.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.n52.sos.config.SettingDefinition;
import org.n52.sos.config.settings.IntegerSettingDefinition;
import org.n52.sos.config.settings.StringSettingDefinition;
import org.n52.sos.ds.hibernate.util.HibernateConstants;
import org.n52.sos.util.CollectionHelper;

public abstract class AbstractHibernateFullDBDatasource extends
		AbstractHibernateDatasource {
	protected String usernameDefault, usernameDescription;
	protected String passwordDefault, passwordDescription;
	protected String databaseDefault, databaseDescription;
	protected String hostDefault, hostDescription;
	protected int portDefault;
	protected String portDescription;
	protected String catalogDefault, catalogDescription;

	AbstractHibernateFullDBDatasource() {
	}

	public AbstractHibernateFullDBDatasource(String usernameDefault,
			String usernameDescription, String passwordDefault,
			String passwordDescription, String databaseDefault,
			String databaseDescription, String hostDefault,
			String hostDescription, int portDefault, String portDescription,
			String catalogDefault, String catalogDescription) {
		super();
		this.usernameDefault = usernameDefault;
		this.usernameDescription = usernameDescription;
		this.passwordDefault = passwordDefault;
		this.passwordDescription = passwordDescription;
		this.databaseDefault = databaseDefault;
		this.databaseDescription = databaseDescription;
		this.hostDefault = hostDefault;
		this.hostDescription = hostDescription;
		this.portDefault = portDefault;
		this.portDescription = portDescription;
		this.catalogDefault = catalogDefault;
		this.catalogDescription = catalogDescription;
	}

	@Override
	public Set<SettingDefinition<?, ?>> getSettingDefinitions() {
		return CollectionHelper.<SettingDefinition<?, ?>> set(
				createUsernameDefinition(usernameDefault),
				createPasswordDefinition(passwordDefault),
				createDatabaseDefinition(databaseDefault),
				createHostDefinition(hostDefault),
				createPortDefinition(portDefault),
				createCatalogDefinition(catalogDefault),
				getTransactionalDefiniton());
	}

	@Override
	public Set<SettingDefinition<?, ?>> getChangableSettingDefinitions(
			Properties current) {
		Map<String, Object> settings = parseDatasourceProperties(current);
		return CollectionHelper.<SettingDefinition<?, ?>> set(
				createUsernameDefinition((String) settings.get(USERNAME_KEY)),
				createPasswordDefinition((String) settings.get(PASSWORD_KEY)),
				createDatabaseDefinition((String) settings.get(DATABASE_KEY)),
				createHostDefinition((String) settings.get(HOST_KEY)),
				createPortDefinition((Integer) settings.get(PORT_KEY)),
				createCatalogDefinition((String) settings.get(CATALOG_KEY)));
	}

	protected StringSettingDefinition createUsernameDefinition(
			String defaultValue) {
		return createUsernameDefinition().setDescription(usernameDescription)
				.setDefaultValue(defaultValue);
	}

	protected StringSettingDefinition createPasswordDefinition(
			String defaultValue) {
		return createPasswordDefinition().setDescription(passwordDescription)
				.setDefaultValue(defaultValue);
	}

	protected StringSettingDefinition createDatabaseDefinition(
			String defaultValue) {
		return createDatabaseDefinition().setDescription(databaseDescription)
				.setDefaultValue(defaultValue);
	}

	protected StringSettingDefinition createHostDefinition(String defaultValue) {
		return createHostDefinition().setDescription(hostDescription)
				.setDefaultValue(defaultValue);
	}

	protected IntegerSettingDefinition createPortDefinition(int defaultValue) {
		return createPortDefinition().setDescription(portDescription)
				.setDefaultValue(defaultValue);
	}

	protected StringSettingDefinition createCatalogDefinition(
			String defaultValue) {
		return createCatalogDefinition().setDefaultValue(defaultValue);
	}

	@Override
	public Properties getDatasourceProperties(Map<String, Object> settings) {
		Properties p = new Properties();
		p.put(HibernateConstants.DEFAULT_CATALOG, settings.get(CATALOG_KEY));
		p.put(HibernateConstants.CONNECTION_USERNAME,
				settings.get(USERNAME_KEY));
		p.put(HibernateConstants.CONNECTION_PASSWORD,
				settings.get(PASSWORD_KEY));
		p.put(HibernateConstants.CONNECTION_URL, toURL(settings));
		p.put(HibernateConstants.CONNECTION_PROVIDER_CLASS,
				"org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider");
		p.put(HibernateConstants.DIALECT, getDialectClass());
		p.put(HibernateConstants.DRIVER_CLASS, getDriverClass());
		p.put(HibernateConstants.C3P0_MIN_SIZE, "10");
		p.put(HibernateConstants.C3P0_MAX_SIZE, "30");
		p.put(HibernateConstants.C3P0_IDLE_TEST_PERIOD, "1");
		p.put(HibernateConstants.C3P0_ACQUIRE_INCREMENT, "1");
		p.put(HibernateConstants.C3P0_TIMEOUT, "0");
		p.put(HibernateConstants.C3P0_MAX_STATEMENTS, "0");
		p.put(HibernateConstants.CONNECTION_AUTO_RECONNECT, "true");
		p.put(HibernateConstants.CONNECTION_AUTO_RECONNECT_FOR_POOLS, "true");
		p.put(HibernateConstants.CONNECTION_TEST_ON_BORROW, "true");
		addMappingFileDirectories(settings, p);

		return p;
	}

	@Override
	protected Map<String, Object> parseDatasourceProperties(Properties current) {
		Map<String, Object> settings = new HashMap<String, Object>(
				current.size());
		settings.put(AbstractHibernateDatasource.CATALOG_KEY,
				current.getProperty(HibernateConstants.DEFAULT_CATALOG));
		settings.put(AbstractHibernateDatasource.USERNAME_KEY,
				current.getProperty(HibernateConstants.CONNECTION_USERNAME));
		settings.put(AbstractHibernateDatasource.PASSWORD_KEY,
				current.getProperty(HibernateConstants.CONNECTION_PASSWORD));
		settings.put(AbstractHibernateDatasource.TRANSACTIONAL_KEY,
				isTransactional(current));
		String url = current.getProperty(HibernateConstants.CONNECTION_URL);

		String[] parsed = parseURL(url);
		String host = parsed[0];
		String port = parsed[1];
		String db = parsed[2];

		settings.put(createHostDefinition().getKey(), host);
		settings.put(createPortDefinition().getKey(), port == null ? null
				: Integer.valueOf(port));
		settings.put(createDatabaseDefinition().getKey(), db);
		return settings;
	}

	@Override
	protected Connection openConnection(Map<String, Object> settings)
			throws SQLException {
		try {
			String jdbc = toURL(settings);
			Class.forName(getDriverClass());
			String pass = (String) settings
					.get(HibernateConstants.CONNECTION_PASSWORD);
			String user = (String) settings
					.get(HibernateConstants.CONNECTION_USERNAME);
			return DriverManager.getConnection(jdbc, user, pass);
		} catch (ClassNotFoundException ex) {
			throw new SQLException(ex);
		}
	}

	private String getDialectClass() {
		return createDialect().getClass().getCanonicalName();
	}

	/**
	 * Converts the given connection settings into a valid JDBC string.
	 * 
	 * @param settings
	 *            the connection settings, containing keys from
	 *            {@link AbstractHibernateDatasource} (<code>HOST_KEY</code>,
	 *            <code>PORT_KEY</code>, ...).
	 * @return a valid JDBC connection string
	 */
	protected abstract String toURL(Map<String, Object> settings);

	/**
	 * Parses the given JDBC string searching for host, port and database
	 * 
	 * @param url
	 *            the JDBC string to parse
	 * @return an array with three strings:
	 *         <ul>
	 *         <li>[0] - Host
	 *         <li>[1] - Port (parseable int as string)
	 *         <li>[2] - Database
	 *         </ul>
	 */
	protected abstract String[] parseURL(String url);

	/**
	 * Gets the qualified name of the driver class.
	 * 
	 * @return the driver class.
	 */
	protected abstract String getDriverClass();
}