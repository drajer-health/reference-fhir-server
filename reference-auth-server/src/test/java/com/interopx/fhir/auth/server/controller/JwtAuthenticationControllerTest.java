//package com.interopx.fhir.auth.server.controller;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.http.HttpStatus;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.interopx.fhir.auth.server.jwtservice.JwtTokenUtil;
//import com.interopx.fhir.auth.server.jwtservice.RefreshTokenService;
//import com.interopx.fhir.auth.server.model.JwtRequest;
//
//@SpringBootTest
//class JwtAuthenticationControllerTest {
//
//	@InjectMocks
//	JwtAuthenticationController jwtAuthenticationController;
//	
//	@Mock
//	UserDetailsService userDetailsService;
//		
//	@Mock
//	JwtTokenUtil jwtTokenUtil;
//	
//	@Mock
//	RefreshTokenService refreshTokenService;
//	
//	@Mock
//	AuthenticationManager authenticationManager;
//	
//	MockMvc mockMvc;
//	
//	@Test
//	void createAuthenticationToken() throws Exception {
////		JwtRequest authenticationRequest = new JwtRequest("user@gmail.com", "test123", "dcf54d771abdab93bbaca51e9ebc8f39");
////		JwtResponse authenticationResponse = new JwtResponse("anyjwt", null, null, 0);
//		
//		JwtRequest authenticationRequest = mock(JwtRequest.class);
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		
//		authenticationRequest.setUsername("shail@mail.com");
//		authenticationRequest.setPassword("shail@123");
//		
//		
//		Authentication authentication = mock(Authentication.class);
//	    authentication.setAuthenticated(true);
//	    when(authentication.isAuthenticated()).thenReturn(true);
//
//	    when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
//	    
////		when(authenticationManager.authenticate(
////	            new UsernamePasswordAuthenticationToken(
////	                Mockito.anyString(), Mockito.anyString()))).thenReturn(authentication);
////		
////		doNothing().when(jwtAuthenticationController).authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//		UserDetails userdetail = mock(UserDetails.class);
//		when(authentication.getPrincipal()).thenReturn(userdetail);
//		
//		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		final User.UserBuilder userBuilder = User.builder().passwordEncoder(passwordEncoder::encode);
//        List<GrantedAuthority> adminAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
//        UserDetails adminUser = userBuilder.username("shail@mail.com").password("shail@123").authorities(adminAuthorities).build();
//
////		SimpleGrantedAuthority sa = new SimpleGrantedAuthority("ROLE_TEST");
////		List<SimpleGrantedAuthority> gu = new ArrayList<>();
////		gu.add(sa);
////		UserDetails user = User.builder()
////				.username("admin")
////				.password("test")
////				.authorities(gu)
////				.build();
//	  // final UserDetails userDetails = mock(UserDetails.class);  
//		   
//		when(userDetailsService.loadUserByUsername(any(String.class))).thenReturn(adminUser);
//		
//		String token = "dcf54d771abdab93bbaca51e9ebc8f39";		
//		when(jwtTokenUtil.generateJwtToken(Mockito.any())).thenReturn(token);
//		
//		String refreshToken = "dcf54d771abdab93bbaca51e9ebc8f39";
//		when(refreshTokenService.createRefreshToken(Mockito.anyString())).thenReturn(refreshToken);
//		
//		//assertEquals(HttpStatus.SC_OK, jwtAuthenticationController.createAuthenticationToken(authenticationRequest, request));
//		
//		 mockMvc.perform(MockMvcRequestBuilders.get("/authenticate")
//                .accept(MediaType.ALL))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("manager@company.com")));
//		
//	}
//	
////	@Test
////	void createAuthenticationToken1() throws Exception {
////		JwtRequest authenticationRequest = new JwtRequest("user@gmail.com", "test123", "dcf54d771abdab93bbaca51e9ebc8f39");
////		JwtResponse authenticationResponse = new JwtResponse("anyjwt", null, null, 0);
////
////		HttpServletRequest request = mock(HttpServletRequest.class);
////		
////		String jsonRequest = authenticationRequest.toString();
////		String jsonResponse = authenticationResponse.toString();
////		
////		authenticationRequest.setUsername("navya67@gmail.com");
////		authenticationRequest.setPassword("test123");
////		
////		Authentication authentication = mock(Authentication.class);
////	    authentication.setAuthenticated(true);
////	    when(authentication.isAuthenticated()).thenReturn(true);
////
////	    when(authenticationManager.authenticate(any())).thenReturn(authentication);
//////		when(authenticationManager.authenticate(
//////	            new UsernamePasswordAuthenticationToken(
//////	                Mockito.anyString(), Mockito.anyString()))).thenReturn(authentication);
//////		
//////		doNothing().when(jwtAuthenticationController).authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
////		UserDetails userDetails = mock(UserDetails.class);		
////		
////		when(userDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetails);
////		
////		String token = "dcf54d771abdab93bbaca51e9ebc8f39";		
////		when(jwtTokenUtil.generateJwtToken(Mockito.any())).thenReturn(token);
////		
////		String refreshToken = "dcf54d771abdab93bbaca51e9ebc8f39";
////		when(refreshTokenService.createRefreshToken(Mockito.anyString())).thenReturn(refreshToken);
////		
//////		assertEquals(HttpStatus.SC_OK, jwtAuthenticationController.createAuthenticationToken(authenticationRequest, request));
////		
////		RequestBuilder request1 = MockMvcRequestBuilders
////	            .post("/authenticate")
////	            .content(jsonRequest)
////	            .contentType(MediaType.APPLICATION_JSON_VALUE)
////	            .accept(MediaType.APPLICATION_JSON);
////		
////		MvcResult mvcResult = mockMvc.perform(request1)
////	            .andExpect(status().is2xxSuccessful())
////	            .andExpect(content().json(jsonResponse, true))
////	            .andExpect(jsonPath("$.jwt").value(isNotNull()))
////	            .andReturn();
////	
////		
////	}
//
//}
