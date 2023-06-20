package com.interopx.fhir.auth.server.configuration;

import com.interopx.fhir.auth.server.properties.DbConfigProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/** DataBaseConnectionImpl. */
@Configuration
public class DataBaseConnectionImpl implements DataBaseConnection {

  @Autowired DbConfigProperties dbConfigProperties;

  private Connection connection;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  /**
   * getConnectionObject
   *
   * @return Connection value
   */
  public Connection getConnectionObject() {
    try {
      if (connection == null) {
        Class.forName(dbConfigProperties.getDriverClassName());
        connection = DriverManager.getConnection(dbConfigProperties.getUrl(), getProperties());

        statement = connection.createStatement();
        statement.execute("SET graph_path=" + dbConfigProperties.getGraphPath() + ";");
      }
    } catch (Exception ex) {
      logger.error("\n Exception in getConnectionObject of DataBaseConnectionImpl :: ", ex);
    }
    return connection;
  }
  /**
   * getProperties
   *
   * @return Properties value
   */
  private Properties getProperties() {
    Properties properties = new Properties();
    try {
      properties.setProperty("password", dbConfigProperties.getPassword());
      properties.setProperty("graph_path", dbConfigProperties.getGraphPath());
      properties.setProperty("user", dbConfigProperties.getUsername());
    } catch (Exception ex) {
      logger.error("\n Exception in getProperties of DataBaseConnectionImpl :: ", ex);
    }
    return properties;
  }
  /**
   * createStatement
   *
   * @return Statement value
   */
  public Statement createStatement() {
    try {
      statement = getConnectionObject().createStatement();
    } catch (Exception ex) {
      logger.error("\n Exception in createStatement of DataBaseConnectionImpl :: ", ex);
    }
    return statement;
  }
  /**
   * preparedStatement
   *
   * @param query value
   * @return PreparedStatement value
   */
  public PreparedStatement preparedStatement(String query) {
    try {
      preparedStatement = getConnectionObject().prepareStatement(query);
    } catch (Exception ex) {
      logger.error("\n Exception in preparedStatement of DataBaseConnectionImpl :: ", ex);
    }
    return preparedStatement;
  }
  /**
   * getResultSet
   *
   * @param preparedStatement value
   * @return ResultSet value
   */
  public ResultSet getResultSet(PreparedStatement preparedStatement) {
    try {
      resultSet = preparedStatement.executeQuery();
    } catch (Exception ex) {
      logger.error("\n Exception in getResultSet of DataBaseConnectionImpl :: ", ex);
    }
    return resultSet;
  }
  /**
   * getResultSet
   *
   * @param query value
   * @return ResultSet value
   */
  public ResultSet getResultSet(String query) {
    try {
      resultSet = createStatement().executeQuery(query);
    } catch (Exception ex) {
      logger.error("\n Exception in getResultSet  String Query of DataBaseConnectionImpl :: ", ex);
    }
    return resultSet;
  }
  /**
   * getExecuteUpdate
   *
   * @param preparedStatement value
   * @return int value
   */
  public int getExecuteUpdate(PreparedStatement preparedStatement) {
    int result = 0;
    try {
      result = preparedStatement.executeUpdate();
    } catch (Exception ex) {
      logger.error("\n Exception in getResultSet of DataBaseConnectionImpl :: ", ex);
    }
    return result;
  }
  /** close */
  public void close() {
    closeStatement(statement);
    closePreparedStatement(preparedStatement);
  }
  /**
   * closeStatement
   *
   * @param statement value
   */
  public void closeStatement(Statement statement) {
    try {
      if (statement != null) {
        statement.close();
      }
    } catch (Exception ex) {
      logger.error("\n Exception in closeStatement  of DataBaseConnectionImpl :: ", ex);
    }
    closeResultSet(resultSet);
  }
  /**
   * closePreparedStatement
   *
   * @param preparedStatement value
   */
  public void closePreparedStatement(PreparedStatement preparedStatement) {
    try {
      if (preparedStatement != null) {
        preparedStatement.close();
      }
    } catch (Exception ex) {
      logger.error("\n Exception in closePreparedStatement  of DataBaseConnectionImpl :: ", ex);
    }
    closeResultSet(resultSet);
  }
  /**
   * closeResultSet
   *
   * @param resultSet value
   */
  public void closeResultSet(ResultSet resultSet) {
    try {
      if (resultSet != null) {
        resultSet.close();
      }
    } catch (Exception ex) {
      logger.error("\n Exception in closeResultSet  of DataBaseConnectionImpl :: ", ex);
    }
  }
  /**
   * closeConnection
   *
   * @param connection value
   */
  public void closeConnection(Connection connection) {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (Exception ex) {
      logger.error("\n Exception in closeConnection  of DataBaseConnectionImpl :: ", ex);
    }
  }
}
