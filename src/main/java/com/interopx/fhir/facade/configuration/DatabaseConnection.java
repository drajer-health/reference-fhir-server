package com.interopx.fhir.facade.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
/**
 * This class is for establishing jdbc connectivity
 * @author xyram
 *
 */
@Service
public class DatabaseConnection
{
	private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static String driverClassName;
    private static String url;
    private static String username;
    private static String password;
    static Connection c;
    public static Properties properties;
    
    public static Connection getConnection() {
        fetchPropertiesNEW();
        if (DatabaseConnection.c == null) {
            final String jdbcDriverClassName = DatabaseConnection.driverClassName;
            final String jdbcUrl = DatabaseConnection.url;
            final String jdbcUsername = DatabaseConnection.username;
            final String jdbcPassword = DatabaseConnection.password;
            logger.debug("Using Database Details to connect to Database:: Driver ClassName:{} , URL:{} , Username: {} , Password: {}" , jdbcDriverClassName , jdbcUrl ,jdbcUsername , jdbcPassword);
            try {
                Class.forName(jdbcDriverClassName);
                DatabaseConnection.c = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            }
            catch (Exception e) {
                logger.error(e.getClass().getName() ,e.getMessage());
                System.exit(0);
            }
            logger.debug("Opened database successfully");
        }
        return DatabaseConnection.c;
    }
    
    @PostConstruct
    public static Properties fetchPropertiesNEW() {
        final DatabaseConnection connection = new DatabaseConnection();
        try {
            final InputStream instr = connection.getClass().getClassLoader().getResourceAsStream("application.properties");
            DatabaseConnection.properties.load(instr);
            DatabaseConnection.driverClassName = DatabaseConnection.properties.getProperty("jdbc.driverClassName");
            DatabaseConnection.url = DatabaseConnection.properties.getProperty("jdbc.url");
            DatabaseConnection.username = DatabaseConnection.properties.getProperty("jdbc.username");
            DatabaseConnection.password = DatabaseConnection.properties.getProperty("jdbc.password");
        }
        catch (IOException e) {
            logger.debug("Exception in reading the Properties File");
        }
        return DatabaseConnection.properties;
    }
    
    static {
        DatabaseConnection.properties = new Properties();
    }
}