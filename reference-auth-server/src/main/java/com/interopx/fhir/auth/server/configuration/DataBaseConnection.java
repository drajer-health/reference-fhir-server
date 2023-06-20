package com.interopx.fhir.auth.server.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/** DataBaseConnection. */
public interface DataBaseConnection {
  /**
   * getConnectionObject
   *
   * @return Connection value
   */
  Connection getConnectionObject();
  /**
   * createStatement
   *
   * @return Statement value
   */
  Statement createStatement();
  /**
   * preparedStatement
   *
   * @param query value
   * @return PreparedStatement value
   */
  PreparedStatement preparedStatement(String query);
  /**
   * getResultSet
   *
   * @param preparedStatement value
   * @return ResultSet value
   */
  ResultSet getResultSet(PreparedStatement preparedStatement);
  /**
   * getResultSet
   *
   * @param query **query**
   * @return ResultSet value
   */
  ResultSet getResultSet(String query);
  /**
   * getExecuteUpdate
   *
   * @param preparedStatement **preparedStatement**
   * @return int value
   */
  int getExecuteUpdate(PreparedStatement preparedStatement);
  /** Close */
  void close();
  /**
   * closeStatement
   *
   * @param statement value
   */
  void closeStatement(Statement statement);
  /**
   * closePreparedStatement
   *
   * @param preparedStatement value
   */
  void closePreparedStatement(PreparedStatement preparedStatement);
  /**
   * closeResultSet
   *
   * @param resultSet value
   */
  void closeResultSet(ResultSet resultSet);
  /**
   * closeConnection
   *
   * @param connection value
   */
  void closeConnection(Connection connection);
}
