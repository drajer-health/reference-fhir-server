package com.interopx.fhir.auth.server.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;




@RunWith(SpringRunner.class)
@SpringBootTest
class JSONObjectUserTypeTest {
	
	
	@InjectMocks
	JSONObjectUserType JSONobjectUserType;
	
	@Mock
	ResultSet resultset;
	@Mock
	SharedSessionContractImplementor session;
	@Mock
	PreparedStatement st;
	
	

	@Test
	void testSqlTypes() {
		
		int[] i= {1,2,3};
	    int[] j=JSONobjectUserType.sqlTypes();
		assertNotNull(j);
	}

	@Test
	void testReturnedClass() {
		
		Class<String> c=JSONobjectUserType.returnedClass();
		assertNotNull(c);
	}

	@Test
	void testEqualsObjectObject() {
		
		Object x=new Object();
		Object y=new Object();
	    boolean value=JSONobjectUserType.equals(x, y);
	    Object x1=null;
	    Object y1=null;
	    boolean value1=JSONobjectUserType.equals(x1, y1);
	}

	@Test
	void testHashCodeObject() {

		Object x=new Object();
		int i=JSONobjectUserType.hashCode(x);
		assertNotNull(i);
		
	}

	@Test
	void testNullSafeGet() throws HibernateException, SQLException {

		 String[] names= {"hari","babu"};
		 Object owner=new Object();
		 JSONobjectUserType.nullSafeGet(resultset, names, session, owner);
		 
//		 String[] names1= {"hari","babu"};
//		 Object owner1=new Object();
//		 ResultSet resultset1=null;
//		 JSONobjectUserType.nullSafeGet(resultset1, names1, session, owner1);
		
	}

	@Test
	void testNullSafeSet() throws HibernateException, SQLException {
		
		Object value=new Object();
		value.equals(getClass());
		int index=10;
		JSONobjectUserType.nullSafeSet(st, value, index, session);
		Object value1=null;
		int index1=10;
		JSONobjectUserType.nullSafeSet(st, value1, index1, session);
	}

	@Test
	void testDeepCopy() {

		Object value=new Object();
		Object value1=JSONobjectUserType.deepCopy(value);
		assertNotNull(value1);
		
	}

//	@Test
//	void testIsMutable() {
//		
//		
//	}

	@Test
	void testDisassemble() {

		Object value=null;
		Serializable serializable=JSONobjectUserType.disassemble(value);
		
	}

	@Test
	void testAssemble() {
		
		Serializable cached = null;
		Object owner=new Object();
		Object object=JSONobjectUserType.assemble(cached, owner);
		
	}

	@Test
	void testReplace() {
		Object original=new Object();
		Object target=new Object();
		Object owner=new Object();
		Object object=JSONobjectUserType.replace(original, target, owner);
		
	}

}
