package com.interopx.fhir.auth.server.configuration;

import com.interopx.fhir.auth.server.properties.DbConfigProperties;
import com.interopx.fhir.auth.server.properties.HibernateConfigProperties;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** The Class HibernateConfiguration. */
@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(HibernateConfiguration.class);

  @Autowired DbConfigProperties dbConfigProperties;

  @Autowired HibernateConfigProperties hibernateConfigProperties;

  /**
   * Session factory.
   *
   * @return the local session factory bean
   */
  @Bean
  public LocalSessionFactoryBean sessionFactory() {
    LOGGER.info("Entry - session factory Method in HibernateConfiguration ");

    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    //    sessionFactory.setPackagesToScan(new String[]
    // {hibernateConfigProperties.getPackagesScan()});
    sessionFactory.setPackagesToScan(
        "com.interopx.fhir.auth.server.model", "com.ix.audit.log.entity");
    sessionFactory.setHibernateProperties(hibernateProperties());

    LOGGER.info("Exit - session factory Method in HibernateConfiguration ");
    return sessionFactory;
  }

  /**
   * Data source.
   *
   * @return the data source
   */
  @Bean
  public DataSource dataSource() {
    LOGGER.info("Entry - datasource Method in HibernateConfiguration ");

    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(dbConfigProperties.getDriverClassName());
    dataSource.setUrl(dbConfigProperties.getUrl());
    dataSource.setUsername(dbConfigProperties.getUsername());
    dataSource.setPassword(dbConfigProperties.getPassword());
    LOGGER.info("Exit - dataSource Method in HibernateConfiguration ");
    return dataSource;
  }

  /**
   * Setting Hibernate properties.
   *
   * @return the properties
   */
  private Properties hibernateProperties() {
    LOGGER.info("Entry - hibernateProperties Method in HibernateConfiguration ");

    Properties properties = new Properties();
    properties.put("hibernate.dialect", hibernateConfigProperties.getDialect());
    properties.put("hibernate.show_sql", hibernateConfigProperties.getShowSql());
    properties.put("hibernate.format_sql", hibernateConfigProperties.getFormatSql());
    //    properties.put("hibernate.hbm2ddl.auto", hibernateConfigProperties.getHbm2ddlAuto());
    LOGGER.info("Exit - hibernateProperties Method in HibernateConfiguration ");

    return properties;
  }

  /**
   * Transaction manager.
   *
   * @param s the s
   * @return the hibernate transaction manager
   */
  @Bean
  @Autowired
  public HibernateTransactionManager transactionManager(SessionFactory s) {
    LOGGER.info("Entry - transactionManager Method in HibernateConfiguration ");
    HibernateTransactionManager txManager = new HibernateTransactionManager();
    txManager.setSessionFactory(s);
    LOGGER.info("Exit - transactionManager Method in HibernateConfiguration ");
    return txManager;
  }
}
