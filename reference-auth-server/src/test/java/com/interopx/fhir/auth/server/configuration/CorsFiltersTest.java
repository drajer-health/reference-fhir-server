package com.interopx.fhir.auth.server.configuration;

import static org.junit.jupiter.api.Assertions.*;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CorsFiltersTest {

	@Autowired
	CorsFilters corsFilters;
	
	@Mock
	FilterChain filterChain;
	
	@Mock
	FilterConfig filterConfig;
	
	@Test
	void testDestroy() {	
		corsFilters.destroy();
		assertNotNull(corsFilters);
		
	}
}
