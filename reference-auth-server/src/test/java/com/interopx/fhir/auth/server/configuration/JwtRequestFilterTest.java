package com.interopx.fhir.auth.server.configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import com.interopx.fhir.auth.server.util.ChimeraClient;

@SpringBootTest
@ActiveProfiles("test")
class JwtRequestFilterTest extends JwtRequestFilter{
	
	@Spy
	@InjectMocks
	JwtRequestFilter jwtRequestFilter;
	
	@Mock
	ChimeraClient chimeraClient;
	
//	@MockBean
//	CurrentUserDetails currentUserDetails;

	@Test
	void doFilterInternal() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);
				
		request.addHeader("authorization", "Bearer dfghjdzsfjkfdfghklzdfcghkzdxbjkdfghjxdf");
		
		HashMap userDetails = new HashMap();
		userDetails.put("sub", "12|654abc");
		userDetails.put("nickname", "ravi");
		userDetails.put("email", "ravi@xyram.com");
		
		Mockito.when(chimeraClient.getAuth0UserDetails(Mockito.anyString())).thenReturn(userDetails);
//		Mockito.when(currentUserDetails.setUserId(Mockito.anyString())).thenReturn(currentUserDetails);
		
		jwtRequestFilter.doFilterInternal(request, response, chain);
		Mockito.verify(jwtRequestFilter, times(1)).doFilterInternal(request, response, chain);
		
	}
	
	@Test
	void doFilterInternal1() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);
						
		request.setRequestURI("/api/jwk");
		
		jwtRequestFilter.doFilterInternal(request, response, chain);
		Mockito.verify(jwtRequestFilter, times(1)).doFilterInternal(request, response, chain);
		
	}
	
	@Test
	void doFilterInternal2() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);
				
		request.setRequestURI("/ix-auth-server/");
		
		jwtRequestFilter.doFilterInternal(request, response, chain);
		Mockito.verify(jwtRequestFilter, times(1)).doFilterInternal(request, response, chain);
		
	}
	
	@Test
	void doFilterInternal3() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);
		
		jwtRequestFilter.doFilterInternal(request, response, chain);
		Mockito.verify(jwtRequestFilter, times(1)).doFilterInternal(request, response, chain);
		
	}

}
