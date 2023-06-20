/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.interopx.fhir.facade.model.RequestResponseLog;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariProxyConnection;

/**
 * The Class is responsible for HibernateConfiguration as well as setting the Hikari connection pooling properties
 */
@Configuration
@EnableTransactionManagement
@PropertySources({@PropertySource("classpath:application.properties")})
@Component
public class HibernateConfiguration {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateConfiguration.class);

	/** The environment. */
	@Autowired
	private Environment environment;

	private HikariDataSource dataSource;

	/**
	 * This method craetes a Session factory.
	 *
	 * @return the local session factory bean
	 */
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LOGGER.debug("Entry - session factory Method in HibernateConfiguration ");

		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setHibernateProperties(hibernateProperties());
		sessionFactory
				.setPackagesToScan(new String[] { "com.interopx.fhir.facade.model"});
		sessionFactory.setAnnotatedClasses(
				new Class[] {
						RequestResponseLog.class });
		LOGGER.debug("Exit - session factory Method in HibernateConfiguration ");
		return sessionFactory;
	}

	/**
	 * To get connection object from HikariDataSource
	 *
	 * @return connection object
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		LOGGER.debug("========= getConnection() method invoked to get connection object from HikariDataSource=========");
		try {
			HikariProxyConnection connection = (HikariProxyConnection) dataSource().getConnection();
			return connection;
		} catch (SQLException e) {
			LOGGER.error("Can't get connection object from HikariDataSource.");
			LOGGER.error(e.getMessage());
			LOGGER.debug(e.getMessage());
		}
		return null;
	}

	/**
	 * To close connection object of HikariDataSource
	 *
	 * @return none
	 * @throws SQLException
	 */
	public void closeConnection() {
		LOGGER.debug("======= closeConnection() method invoked to close connection object(HikariDataSource)==========");
		try {
			Connection con = dataSource().getConnection();
			if (con != null) {
				con.close();
				LOGGER.debug("=========Hikari connection object closed sucessfully=======");
			}
		} catch (SQLException e) {
			LOGGER.error("Exception in closeConnection() {} ", e);
		}
	}

	/**
	 * This method creates a hikari based Data source.
	 *
	 * @return the data source
	 */
	@Bean
	public DataSource dataSource() {
		LOGGER.debug("Entry - datasource Method in HibernateConfiguration ");

		if (dataSource == null || dataSource.isClosed()) {
			dataSource = new HikariDataSource();
			dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
			dataSource.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
			dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
			dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

			// connection pool properties
			dataSource.setConnectionTimeout(
					Long.parseLong(environment.getRequiredProperty("jdbc.hikari.connection-timeout")));
			dataSource.setIdleTimeout(Long.parseLong(environment.getRequiredProperty("jdbc.hikari.idle-timeout")));
			dataSource.setMinimumIdle(Integer.parseInt(environment.getRequiredProperty("jdbc.hikari.minimum-idle"))); //
			dataSource.setMaximumPoolSize(
					Integer.parseInt(environment.getRequiredProperty("jdbc.hikari.maximum-pool-size")));
			dataSource.setMaxLifetime(Long.parseLong(environment.getRequiredProperty("jdbc.hikari.max-lifetime")));
			dataSource.setLeakDetectionThreshold(
					Long.parseLong(environment.getRequiredProperty("jdbc.hikari.leakDetectionThreshold")));
			dataSource.setPoolName(environment.getRequiredProperty("jdbc.hikari.pool-name"));
			dataSource.setAutoCommit(Boolean.parseBoolean(environment.getRequiredProperty("jdbc.hikari.auto-commit")));

			LOGGER.debug("jdbc properties form Environment -> {}" , environment.getRequiredProperty("jdbc.driverClassName"));
			LOGGER.debug("jdbc properties form Environment -> {}" , environment.getRequiredProperty("jdbc.url"));
			LOGGER.debug("jdbc properties form Environment -> {}" , environment.getRequiredProperty("jdbc.username"));
			LOGGER.debug("Exit - dataSource Method in HibernateConfiguration ");

			LOGGER.debug("-------------------------------------------------------------------------------------");
			LOGGER.debug("CP properties-> hikari.connection-timeout from Environment::{}"
					, Long.parseLong(environment.getRequiredProperty("jdbc.hikari.connection-timeout")));
			LOGGER.debug("CP properties-> hikari.idle-timeout from Environment::{}"
					,Long.parseLong(environment.getRequiredProperty("jdbc.hikari.idle-timeout")));
			LOGGER.debug("CP properties-> hikari.minimum-idle from Environment::{}"
					, (Integer.parseInt(environment.getRequiredProperty("jdbc.hikari.minimum-idle"))));
			LOGGER.debug("CP properties-> hikari.maximum-pool-size from Environment::{}"
					, Integer.parseInt(environment.getRequiredProperty("jdbc.hikari.maximum-pool-size")));
			LOGGER.debug("CP properties-> hikari.max-lifetime from Environment::{}"
					, Long.parseLong(environment.getRequiredProperty("jdbc.hikari.max-lifetime")));
			LOGGER.debug("CP properties-> hikari.leakDetectionThreshold from Environment::{}"
					, Long.parseLong(environment.getRequiredProperty("jdbc.hikari.leakDetectionThreshold")));
			LOGGER.debug("CP properties-> hikari.pool-name from Environment::{}"
					, environment.getRequiredProperty("jdbc.hikari.pool-name"));
			LOGGER.debug("CP properties-> hikari.auto-commit from Environment::{}"
					, Boolean.parseBoolean(environment.getRequiredProperty("jdbc.hikari.auto-commit")));
			LOGGER.debug("======================================================================================");
		}
		return dataSource;
	}

	/**
	 * Setting Hibernate properties.
	 *
	 * @return the properties
	 */
	private Properties hibernateProperties() {
		LOGGER.debug("Entry - hibernateProperties Method in HibernateConfiguration ");

		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
		properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.allow_update_outside_transaction", environment.getRequiredProperty("hibernate.allow_update_outside_transaction"));
		LOGGER.debug(environment.getRequiredProperty("hibernate.format_sql"));
		LOGGER.debug("Exit - hibernateProperties Method in HibernateConfiguration ");

		return properties;
	}

	/**
	 * This method creates a Transaction manager bean.
	 *
	 * @param sessionfactory
	 * @return the hibernate transaction manager
	 */
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory s) {
		LOGGER.debug("Entry - transactionManager Method in HibernateConfiguration ");

		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		LOGGER.debug("Exit - transactionManager Method in HibernateConfiguration ");

		return txManager;
	}
}
