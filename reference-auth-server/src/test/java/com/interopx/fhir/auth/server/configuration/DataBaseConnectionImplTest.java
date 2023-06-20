package com.interopx.fhir.auth.server.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.interopx.fhir.auth.server.properties.DbConfigProperties;

import net.bitnine.agensgraph.jdbc.AgResultSet;


@SpringBootTest
class DataBaseConnectionImplTest {

	
	@InjectMocks
	DataBaseConnectionImpl dataBaseConnectionImpl;
	
	@Mock
	DbConfigProperties dbConfigProperties;
	
	
	@Mock
	Statement statement;
	@Mock
	AgResultSet agResultSet;
	
//	@Test
//	void testGetConnectionObject() {
//		fail("Not yet implemented");
//	}

	@Test
	void testCreateStatement() {
		
		 Statement statement=dataBaseConnectionImpl.createStatement();
	}

	@Test
	void testPreparedStatement() {
		
		
		String str="45ghdx63789";
		PreparedStatement preparedStatement=dataBaseConnectionImpl.preparedStatement(str);
	}

	@Test
	void testGetResultSetPreparedStatement() {
		
		PreparedStatement preparedStatement=null;
		ResultSet preparedStatement1=dataBaseConnectionImpl.getResultSet(preparedStatement);
	}

	@Test
	void testGetResultSetString() {
		String str1="HJsb uerqwyg";
		ResultSet resultSet=dataBaseConnectionImpl.getResultSet(str1);
	}

	@Test
	void testGetExecuteUpdate() {
		
		PreparedStatement preparedStatement=null;
		int i=dataBaseConnectionImpl.getExecuteUpdate(preparedStatement);
	}

	@Test
	void testClose() {
		
		dataBaseConnectionImpl.close();
	}

	@Test
	void testCloseStatement() {

		Statement statement=null;
		dataBaseConnectionImpl.closeStatement(statement);
	}

	@SuppressWarnings("null")
	@Test
	void testClosePreparedStatement() throws SQLException {
		
		PreparedStatement preparedStatement = null;
		//preparedStatement.setInt(6, 8);
		dataBaseConnectionImpl.closePreparedStatement(preparedStatement);
	}

	@Test
	void testCloseResultSet() {
		ResultSet resultSet=null;
		dataBaseConnectionImpl.closeResultSet(resultSet);
	}

	@Test
	void testCloseConnection() {
		Connection connection=null;
		dataBaseConnectionImpl.closeConnection(connection);
		
	}

}
