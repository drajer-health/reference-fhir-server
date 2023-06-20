package com.interopx.fhir.auth.server.controller;



import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PathVariable;

import com.interopx.fhir.auth.server.dao.UserRoleDao;
import com.interopx.fhir.auth.server.dao.impl.UsersDaoImpl;
import com.interopx.fhir.auth.server.jwtservice.JwtTokenUtil;
import com.interopx.fhir.auth.server.model.UserDto;
import com.interopx.fhir.auth.server.model.UserResponse;
import com.interopx.fhir.auth.server.model.UserResponseMessage;
import com.interopx.fhir.auth.server.model.Users;
import com.interopx.fhir.auth.server.service.EmailService;
import com.interopx.fhir.auth.server.service.UsersService;
import com.interopx.fhir.auth.server.util.AuthUtil;
import com.interopx.fhir.auth.server.util.UserMapper;

import ca.uhn.fhir.rest.annotation.Validate;



@RunWith(SpringRunner.class)
@SpringBootTest
class UserControllerTest {
	

	@InjectMocks
	UserController userController;
	@InjectMocks
	AuthUtil  emailutil;
	@Mock UsersService userRegistrationService; 
	@Mock UserMapper userMapper;
	@Mock EmailService emailService;
	@Mock UserResponse userresponse;
	@Mock Users user;
	@Mock MessageFormat messageFormat;
	//@Mock UserDto userdto;
	 
	

	 @Mock private JwtTokenUtil jwtTokenUtil;
	 @Mock private UsersDaoImpl usersDaoImpl;
	 @Mock private UserRoleDao sOFUserRoleDao;
	 @Mock private PasswordEncoder passwordEncoder;
	  
    @SuppressWarnings("static-access")
	@Test
	void testRegisterNewUserAccount() throws Exception {
    	
    	
    	MockHttpServletRequest request = new MockHttpServletRequest();
 //   	request.setRequestURI("");
    	HttpServletResponse response = mock(HttpServletResponse.class);
    	UserDto accountDetails=new UserDto();
    	accountDetails.setEmail("harish@gmail.com");
    	accountDetails.setEmailActivationKey("gmail1234");
    	accountDetails.setFirstName("harish");
    	accountDetails.setFullName("harishbabu");
    	accountDetails.setLastName("babu");
    	accountDetails.setMiddleName("shankar");
    	accountDetails.setMobileNumber("8892923219");
    	accountDetails.setPassword("12345");
    	accountDetails.setPatientId("1");
    	accountDetails.setToken("890");
    	accountDetails.setUserActive(true);
    	accountDetails.setUserId("1");
    	accountDetails.setUserName("harishbabuGNbabu");
    	
    	String str="User details updated";
    	String str1="User Registerd Successfully";
    	String str2="Username already exists";
    	List<String> userRole = new ArrayList<>();
    	
    	Users user=new Users();
    	user.setUser_id("0");
    	user.setMobile_number("8892923219");
    	user.setEmailActivated(true);
    	user.setEmailActivationKey("1234");
    	user.setFirstName("harish");
    	user.setLastName("babu");
    	user.setMiddleName("shankar");
 //   	user.setPatient_id("12345");
    	user.setUser_email("babu@gmail.com");
    	user.setUser_password("0987654321");
    	user.setUserActive(true);
    	
    	
    	AuthUtil emailutil=mock(AuthUtil.class);
    	when(userRegistrationService.getUserById(accountDetails.getUserId())).thenReturn(user);
    	//when(userMapper.encodePassword(accountDetails.getPassword())).thenReturn(str);
    	Users user1=new Users();
    	
    	when(userMapper.convertDtoToEntity(accountDetails)).thenReturn(user);
    	//when(userRegistrationService.registerUser(user)).thenReturn(str1);
    	//when(request.getRequestURL().toString()).thenReturn(str1);
    	//when(userRegistrationService.registerUser(user)).thenReturn(str2);
    	when(userRegistrationService.getUserById(accountDetails.getUserId())).thenReturn(user);
     	when(userMapper.encodePassword(accountDetails.getPassword())).thenReturn(str);
    	when(userRegistrationService.updateUser(user)).thenReturn(str);
    	when(userRegistrationService.registerUser(Mockito.any())).thenReturn(str1);
    	assertEquals(HttpStatus.CREATED, userController.registerNewUserAccount(accountDetails, request).getStatusCode());
    	
    	
    	UserDto accountDetails21=new UserDto();
    	accountDetails21.setEmail("harish@gmail.com");
    	accountDetails21.setEmailActivationKey("gmail1234");
    	accountDetails21.setFirstName("harish");
    	accountDetails21.setFullName("harishbabu");
    	accountDetails21.setLastName("babu");
    	accountDetails21.setMiddleName("shankar");
    	accountDetails21.setMobileNumber("8892923219");
    	accountDetails21.setPassword("12345");
    	accountDetails21.setPatientId("1");
    	accountDetails21.setToken("890");
    	accountDetails21.setUserActive(true);
    	accountDetails21.setUserId("0");
    	accountDetails21.setUserName("harishbabuGNbabu");
    	
    	String str12="User details updated";
    	String str13="User Registerd Successfully";
    	String str14="Username already exists";
    	List<String> userRole5 = new ArrayList<>();
    	
    	Users user21=new Users();
    	user21.setUser_id("1");
    	user21.setMobile_number("8892923219");
    	user21.setEmailActivated(true);
    	user21.setEmailActivationKey("1234");
    	user21.setFirstName("harish");
    	user21.setLastName("babu");
    	user21.setMiddleName("shankar");
    	user21.setPatient_id("12345");
    	user21.setUser_email("babu@gmail.com");
    	user21.setUser_password("0987654321");
    	user21.setUserActive(true);
    	
    	
    	AuthUtil emailutil3=mock(AuthUtil.class);
    	when(userRegistrationService.getUserById(accountDetails21.getUserId())).thenReturn(user21);
    	//when(userMapper.encodePassword(accountDetails.getPassword())).thenReturn(str);
    	//Users user21=new Users();
    	
    	when(userMapper.convertDtoToEntity(accountDetails21)).thenReturn(user21);
    	//when(userRegistrationService.registerUser(user)).thenReturn(str1);
    	//when(request.getRequestURL().toString()).thenReturn(str1);
    	//when(userRegistrationService.registerUser(user)).thenReturn(str2);
    	when(userRegistrationService.getUserById(accountDetails21.getUserId())).thenReturn(user21);
     	when(userMapper.encodePassword(accountDetails21.getPassword())).thenReturn(str12);
    	when(userRegistrationService.updateUser(user21)).thenReturn(str12);
    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userController.registerNewUserAccount(accountDetails21, request).getStatusCode());
    	
    	
    	
    	
    	UserDto accountDetails1=new UserDto();
    	accountDetails1.setUserId("0");
    	AuthUtil emailutil1=mock(AuthUtil.class);
        when(userMapper.convertDtoToEntity(accountDetails1)).thenReturn(user);
    	assertEquals(HttpStatus.BAD_REQUEST, userController.registerNewUserAccount(accountDetails1, request).getStatusCode());
    	
    	
    	
    	UserDto accountDetails2=new UserDto();
    	accountDetails2.setUserId("3");
    	AuthUtil emailutil2=mock(AuthUtil.class);
        when(userMapper.convertDtoToEntity(accountDetails2)).thenReturn(user);
        assertEquals(HttpStatus.BAD_REQUEST, userController.registerNewUserAccount(accountDetails2, request).getStatusCode());
        
        
        UserDto accountDetails3=new UserDto();
        accountDetails3.setEmail("harish@gmail.com");
    	accountDetails3.setEmailActivationKey("gmail1234");
    	accountDetails3.setFirstName("harish");
    	accountDetails3.setFullName("harishbabu");
    	accountDetails3.setLastName("babu");
    	accountDetails3.setMiddleName("shankar");
    	accountDetails3.setMobileNumber("8892923219");
    	accountDetails3.setPassword("12345");
    	accountDetails3.setPatientId("1");
    	accountDetails3.setToken("890");
    	accountDetails3.setUserActive(false);
    	accountDetails3.setUserId("1");
    	accountDetails3.setUserName("harishbabuGNbabu");
    	
    	String str4="User details updated";
    	String str5="User Registerd Successfully";
    	String str6="Username already exists";
    	List<String> userRole1 = new ArrayList<>();
    	
    	Users user11=new Users();
    	user11.setUser_id("0");
    	user11.setMobile_number("8892923219");
    	user11.setEmailActivated(true);
    	user11.setEmailActivationKey("1234");
    	user11.setFirstName("harish");
    	user11.setLastName("babu");
    	user11.setMiddleName("shankar");
    	user11.setPatient_id("12345");
    	user11.setUser_email("babu@gmail.com");
    	user11.setUser_password("0987654321");
    	user11.setUserActive(false);
    	
    	
    	
    	
    	AuthUtil emailutil8=mock(AuthUtil.class);
        when(userMapper.convertDtoToEntity(accountDetails3)).thenReturn(user11);
    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userController.registerNewUserAccount(accountDetails3, request).getStatusCode());

    	
    	
    	
    	UserDto accountDetails4=new UserDto();
    	accountDetails4.setEmail("harish@gmail.com");
    	accountDetails4.setEmailActivationKey("gmail1234");
      	accountDetails4.setFirstName("harish");
      	accountDetails4.setFullName("harishbabu");
      	accountDetails4.setLastName("babu");
      	accountDetails4.setMiddleName("shankar");
      	accountDetails4.setMobileNumber("8892923219");
      	accountDetails4.setPassword("12345");
      	accountDetails4.setPatientId("1");
      	accountDetails4.setToken("890");
      	accountDetails4.setUserActive(false);
      	accountDetails4.setUserId("1");
      	accountDetails4.setUserName("harishbabuGNbabu");
      	
 
      	String str9="User Registerd Successfully";
      	String str10="Username already exists";
      	List<String> userRole12 = new ArrayList<>();
      	
      	Users user12=new Users();
      	user12.setUser_id("0");
      	user12.setMobile_number("8892923219");
      	user12.setEmailActivated(true);
      	user12.setEmailActivationKey("1234");
      	user12.setFirstName("harish");
      	user12.setLastName("babu");
      	user12.setMiddleName("shankar");
      	user12.setPatient_id("12345");
      	user12.setUser_email("babu@gmail.com");
      	user12.setUser_password("0987654321");
      	user12.setUserActive(false);
    	
    	
    	String str8="User Registerd Successfully";
    	when(userRegistrationService.getUserById(accountDetails4.getUserId())).thenReturn(user12);
    	when(userRegistrationService.updateUser(user12)).thenReturn(str8);
    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userController.registerNewUserAccount(accountDetails4, request).getStatusCode());
    	
    	UserDto accountDetails7=new UserDto();
    	Users user7=new Users();
    	String str16="User Registerd Successfully";
    	when(userRegistrationService.getUserById(accountDetails7.getUserId())).thenReturn(user7);
    	when(userRegistrationService.updateUser(user7)).thenReturn(str16);
    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userController.registerNewUserAccount(accountDetails4, request).getStatusCode());
    	
    	
    	
    	
    	//////
    	UserDto accountDetails31=new UserDto();
    	accountDetails31.setEmail("harish@gmail.com");
    	accountDetails31.setEmailActivationKey("gmail1234");
    	accountDetails31.setFirstName("harish");
    	accountDetails31.setFullName("harishbabu");
    	accountDetails31.setLastName("babu");
    	accountDetails31.setMiddleName("shankar");
    	accountDetails31.setMobileNumber("8892923219");
    	accountDetails31.setPassword("12345");
    	accountDetails31.setPatientId("1");
    	accountDetails31.setToken("890");
    	accountDetails31.setUserActive(true);
    	accountDetails31.setUserId("0");
    	accountDetails31.setUserName("harishbabuGNbabu");
    	
    	//String str="User details updated";
    	String str0="Username already exists";
    	//String str2="Username already exists";
    	List<String> userRole2 = new ArrayList<>();
    	
    	Users user17=new Users();
    	user17.setUser_id("0");
    	user17.setMobile_number("8892923219");
    	user17.setEmailActivated(true);
    	user17.setEmailActivationKey("1234");
    	user17.setFirstName("harish");
    	user17.setLastName("babu");
    	user17.setMiddleName("shankar");
    	user17.setPatient_id("12345");
    	user17.setUser_email("babu@gmail.com");
    	user17.setUser_password("0987654321");
    	user17.setUserActive(true);
    	
    	
    	AuthUtil emailutil9=mock(AuthUtil.class);
    	when(userRegistrationService.getUserById(accountDetails31.getUserId())).thenReturn(user17);
    	when(userMapper.encodePassword(accountDetails31.getPassword())).thenReturn(str0);
    	//Users user1=new Users();
    	
    	when(userMapper.convertDtoToEntity(accountDetails31)).thenReturn(user17);
    	when(userRegistrationService.registerUser(user)).thenReturn(str0);
    	//when(request.getRequestURL().toString()).thenReturn(str0);
    	when(userRegistrationService.registerUser(user17)).thenReturn(str0);
    	when(userRegistrationService.getUserById(accountDetails31.getUserId())).thenReturn(user17);
     	when(userMapper.encodePassword(accountDetails31.getPassword())).thenReturn(str0);
     	//when(request.getRequestURL().toString()).thenReturn(str0);
    	//when(userRegistrationService.updateUser(user)).thenReturn(str);
     	String emailSubject="emailsubject";
     	String emailBody="emailbody";
     	StringBuilder url=new StringBuilder();
     	url.append("jhgajldsflnz");
     	//Mockito.when(MessageFormat.format(emailBody, accountDetails.getFirstName(), url)).thenReturn(emailBody);
     	
    	assertEquals(HttpStatus.CONFLICT, userController.registerNewUserAccount(accountDetails31, request).getStatusCode());
    	
  
    	
    	
    	String str3="User Registerd Successfully";
    	
    	AuthUtil emailutil10=mock(AuthUtil.class);
    	when(userRegistrationService.getUserById(accountDetails31.getUserId())).thenReturn(user17);
    	when(userMapper.encodePassword(accountDetails31.getPassword())).thenReturn(str3);
    	//Users user1=new Users();
    	
    	when(userMapper.convertDtoToEntity(accountDetails31)).thenReturn(user17);
    	when(userRegistrationService.registerUser(user)).thenReturn(str3);
    	//when(request.getRequestURL().toString()).thenReturn(str3);
    	when(userRegistrationService.registerUser(user17)).thenReturn(str3);
    	when(userRegistrationService.getUserById(accountDetails31.getUserId())).thenReturn(user17);
     	when(userMapper.encodePassword(accountDetails31.getPassword())).thenReturn(str3);
     	//when(request.getRequestURL().toString()).thenReturn(str0);
    	//when(userRegistrationService.updateUser(user)).thenReturn(str);
     	String emailSubject1="emailsubject";
     	String emailBody1="emailbody";
     	StringBuilder url1=new StringBuilder();
     	url.append("jhgajldsflnz");
     	//Mockito.when(MessageFormat.format(emailBody, accountDetails.getFirstName(), url)).thenReturn(emailBody);
     	
    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userController.registerNewUserAccount(accountDetails31, request).getStatusCode());
    	
    	
    
//    	UserDto accountDetails=new UserDto();
//    	Users user17=new Users();
//    	when(userRegistrationService.getUserById(accountDetails.getUserId())).thenReturn(user17);
//    	
//    	assertEquals(HttpStatus.BAD_REQUEST, userController.registerNewUserAccount(accountDetails, request).getStatusCode());
//        
    	
    	
    	
   
    }
 
    	@Test
    	void testUserLogin() throws Exception {
    		
    		
    		HttpServletRequest request = mock(HttpServletRequest.class);
        	HttpServletResponse response = mock(HttpServletResponse.class);
        	UserDto accountDetails=new UserDto();
        	accountDetails.setEmail("harish@gmail.com");
        	accountDetails.setEmailActivationKey("gmail1234");
        	accountDetails.setFirstName("harish");
        	accountDetails.setFullName("harishbabu");
        	accountDetails.setLastName("babu");
        	accountDetails.setMiddleName("shankar");
        	accountDetails.setMobileNumber("8892923219");
        	accountDetails.setPassword("12345");
        	accountDetails.setPatientId("1");
        	accountDetails.setToken("890");
        	accountDetails.setUserActive(true);
        	accountDetails.setUserId("1");
        	accountDetails.setUserName("harishbabuGNbabu");
        	
        	Users user = new Users();
        	user.setUser_email("harish23@gmail.com");
        	user.setUser_password("test123");
        	user.setUserActive(true);
        	String str="jhjsdfgsdjhfgsh768364nhjdshg";
        	Map<String, Object> temp = new HashMap<String, Object>();
        	
        when(userRegistrationService.getUserByEmail(accountDetails.getEmail(), request)).thenReturn(user);
        UserResponse loginUser = mock(UserResponse.class);
        loginUser.setEmail("harish456@gmail.com");
        when(passwordEncoder.matches(accountDetails.getPassword(), user.getUser_password())).thenReturn(true);
        when(userMapper.convertEntityToDto(user)).thenReturn(loginUser);
        when(jwtTokenUtil.doGenerateToken(Mockito.any(), Mockito.anyString())).thenReturn(str);
       // assertEquals(loginUser,userController.userLogin(accountDetails, request).getBody());
        assertEquals(HttpStatus.OK, userController.uerLogin(accountDetails, request).getStatusCode());
        
        
        
        UserDto accountDetails1=new UserDto();
        Users user1 = new Users();
        assertEquals(HttpStatus.UNAUTHORIZED, userController.uerLogin(accountDetails1, request).getStatusCode());
        
        
        Users user12 = new Users();
    	user12.setUser_email("harish23@gmail.com");
    	user12.setUser_password("test123");
    	user12.setUserActive(false);
    	String str1="jhjsdfgsdjhfgsh768364nhjdshg";
    	Map<String, Object> temp1 = new HashMap<String, Object>();
    	
    	 when(userRegistrationService.getUserByEmail(accountDetails.getEmail(), request)).thenReturn(user12);
         UserResponse loginUser1 = mock(UserResponse.class);
         loginUser1.setEmail("harish456@gmail.com");
         when(passwordEncoder.matches(accountDetails.getPassword(), user.getUser_password())).thenReturn(true);
         when(userMapper.convertEntityToDto(user12)).thenReturn(loginUser1);
         when(jwtTokenUtil.doGenerateToken(Mockito.any(), Mockito.anyString())).thenReturn(str);
    	assertEquals(HttpStatus.UNAUTHORIZED, userController.uerLogin(accountDetails, request).getStatusCode());
        
        
       
    	}  	  

	@Test
	void testupdateUserDetails() throws Exception {
		
		HttpServletRequest request = mock(HttpServletRequest.class);
    	HttpServletResponse response = mock(HttpServletResponse.class);
    	UserDto accountDetails=new UserDto();
    	accountDetails.setEmail("harish@gmail.com");
    	accountDetails.setEmailActivationKey("gmail1234");
    	accountDetails.setFirstName("harish");
    	accountDetails.setFullName("harishbabu");
    	accountDetails.setLastName("babu");
    	accountDetails.setMiddleName("shankar");
    	accountDetails.setMobileNumber("8892923219");
    	accountDetails.setPassword("12345");
    	accountDetails.setPatientId("1");
    	accountDetails.setToken("890");
    	accountDetails.setUserActive(true);
    	accountDetails.setUserId("1");
    	accountDetails.setUserName("harishbabuGNbabu");
    	
    	when(userRegistrationService.getUserById(accountDetails.getUserId())).thenReturn(user);
    	assertEquals(HttpStatus.OK, userController.updateUserDetails(accountDetails, request).getStatusCode());
    	
    	
    	UserDto accountDetails1=new UserDto();
        Users user1 = new Users();
        assertEquals(HttpStatus.BAD_REQUEST, userController.updateUserDetails(accountDetails1, request).getStatusCode());
		}
	
	@Test
	void testactivateDeactivateUser() throws Exception {
	
		Users user=new Users();
		user.setUser_id("12");
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		UserResponse userResponse=new UserResponse();
		userResponse.setAccessToken("akjhgsjak");
		userResponse.setEmail("babu@gmail.com");
		userResponse.setFirstName("harish");
		userResponse.setFullName("harishbabu");
		userResponse.setLastName("babu");
		userResponse.setMiddleName("shankar");
		userResponse.setMobileNumber("8892923219");
		userResponse.setPatientId("12345");
		
		when(userRegistrationService.getUserById("12")).thenReturn(user);
		when(userRegistrationService.updateUser(user)).thenReturn("updated");
		when(userMapper.convertEntityToDto(user)).thenReturn(userResponse);
		
		assertEquals(HttpStatus.OK, userController.activateDeactivateUser("12",true,request).getStatusCode());
		
		
		UserDto accountDetails1=new UserDto();
        Users user1 = new Users();
        assertEquals(HttpStatus.BAD_REQUEST, userController.activateDeactivateUser("0",false, request).getStatusCode());

		
		
	}
	
	@Test
	void TestupdateUserRole() throws Exception {
		
		
		HttpServletRequest request = mock(HttpServletRequest.class);
    	HttpServletResponse response = mock(HttpServletResponse.class);
    	UserDto accountDetails=new UserDto();
    	accountDetails.setEmail("harish@gmail.com");
    	accountDetails.setEmailActivationKey("gmail1234");
    	accountDetails.setFirstName("harish");
    	accountDetails.setFullName("harishbabu");
    	accountDetails.setLastName("babu");
    	accountDetails.setMiddleName("shankar");
    	accountDetails.setMobileNumber("8892923219");
    	accountDetails.setPassword("12345");
    	accountDetails.setPatientId("1");
    	accountDetails.setToken("890");
    	accountDetails.setUserActive(true);
    	accountDetails.setUserId("1");
    	accountDetails.setUserName("harishbabuGNbabu");
    	List<String> list = new ArrayList<String>();
    	list.add("Traine");
    	accountDetails.setUserRole(list);
    	 
    	
    	UserResponse userResponse=new UserResponse();
		userResponse.setAccessToken("akjhgsjak");
		userResponse.setEmail("babu@gmail.com");
		userResponse.setFirstName("harish");
		userResponse.setFullName("harishbabu");
		userResponse.setLastName("babu");
		userResponse.setMiddleName("shankar");
		userResponse.setMobileNumber("8892923219");
		userResponse.setPatientId("12345");
    	
    	Users user=new Users();
    	user.setUserRole(list);
    	
    	when(userRegistrationService.getUserById(accountDetails.getUserId())).thenReturn(user);
    	when(userRegistrationService.updateUser(user)).thenReturn("value");
    	when(userMapper.convertEntityToDto(user)).thenReturn(userResponse);
    	assertEquals(HttpStatus.OK, userController.updateUserRole(accountDetails,request).getStatusCode());	
    	
    	
    	UserDto accountDetails1=new UserDto();
        Users user1 = new Users();
        assertEquals(HttpStatus.BAD_REQUEST, userController.updateUserRole(accountDetails1, request).getStatusCode());

		
		
	}
	@Test
	void testverifyEmail() throws Exception {
		
		HttpServletRequest request = mock(HttpServletRequest.class);
    	HttpServletResponse response = mock(HttpServletResponse.class);
    	UserDto accountDetails=new UserDto();
    	accountDetails.setEmail("harish@gmail.com");
    	accountDetails.setToken("890");
    	
    	UserResponse userResponse=new UserResponse();
		userResponse.setAccessToken("akjhgsjak");
		userResponse.setEmail("babu@gmail.com");
		userResponse.setFirstName("harish");
		userResponse.setFullName("harishbabu");
		userResponse.setLastName("babu");
		userResponse.setMiddleName("shankar");
		userResponse.setMobileNumber("8892923219");
		userResponse.setPatientId("12345");
    	
    	when(userRegistrationService.verifyToken(accountDetails.getToken(), accountDetails.getEmail())).thenReturn(user);
    	when(userMapper.convertEntityToDto(user)).thenReturn(userResponse);
		assertEquals(HttpStatus.OK, userController.verifyEmail(accountDetails,request).getStatusCode());
    	
    	UserDto accountDetails1=new UserDto();
        Users user1 = new Users();
        assertEquals(HttpStatus.BAD_REQUEST, userController.verifyEmail(accountDetails1, request).getStatusCode());

	}
	
	@Test
	void testupdateUser() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
    	HttpServletResponse response = mock(HttpServletResponse.class);
    	UserDto accountDetails=new UserDto();
    	accountDetails.setEmail("harish@gmail.com");
    	accountDetails.setEmailActivationKey("gmail1234");
    	accountDetails.setFirstName("harish");
    	accountDetails.setFullName("harishbabu");
    	accountDetails.setLastName("babu");
    	accountDetails.setMiddleName("shankar");
    	accountDetails.setMobileNumber("8892923219");
    	accountDetails.setPassword("12345");
    	accountDetails.setPatientId("1");
    	accountDetails.setToken("890");
    	accountDetails.setUserActive(true);
    	accountDetails.setUserId("1");
    	accountDetails.setUserName("harishbabuGNbabu");
    	List<String> list = new ArrayList<String>();
    	list.add("Traine");
    	accountDetails.setUserRole(list);
    	
    	UserResponse userResponse=new UserResponse();
		userResponse.setAccessToken("akjhgsjak");
		userResponse.setEmail("babu@gmail.com");
		userResponse.setFirstName("harish");
		userResponse.setFullName("harishbabu");
		userResponse.setLastName("babu");
		userResponse.setMiddleName("shankar");
		userResponse.setMobileNumber("8892923219");
		userResponse.setPatientId("12345");
    	
		String str="13456789egsfgajse";
    	when(userRegistrationService.updateUser(user)).thenReturn(str);
    	when(userMapper.convertEntityToDto(user)).thenReturn(userResponse);
    	when(userRegistrationService.getUserById(accountDetails.getUserId())).thenReturn(user);
    	assertEquals(HttpStatus.OK, userController.updateUser(accountDetails,request).getStatusCode());
    	
    	
    	UserDto accountDetails1=new UserDto();
        Users user1 = new Users();
        assertEquals(HttpStatus.BAD_REQUEST, userController.updateUser(accountDetails1, request).getStatusCode());

    	
	}
	@Test
	void testgetAllUsers() {
		

    	Users user=new Users();
    	user.setUser_id("12");
    	user.setMobile_number("8892923219");
    	user.setEmailActivated(true);
    	user.setEmailActivationKey("1234");
    	user.setFirstName("harish");
    	user.setLastName("babu");
    	user.setMiddleName("shankar");
    	user.setPatient_id("12345");
    	user.setUser_email("babu@gmail.com");
    	user.setUser_password("0987654321");
    	user.setUserActive(true);
    	
    	UserResponse userResponse=new UserResponse();
		userResponse.setAccessToken("akjhgsjak");
		userResponse.setEmail("babu@gmail.com");
		userResponse.setFirstName("harish");
		userResponse.setFullName("harishbabu");
		userResponse.setLastName("babu");
		userResponse.setMiddleName("shankar");
		userResponse.setMobileNumber("8892923219");
		userResponse.setPatientId("12345");
		
		List<Users> userList=new ArrayList<Users>();	
		userList.add(user);
		when(userRegistrationService.getAllUsers()).thenReturn(userList);
		when(userMapper.convertEntityToDto(user)).thenReturn(userResponse);
		when(userRegistrationService.getAllUsers()).thenReturn(userList);
		assertNotNull(userController.getAllUsers());
		
		
//		UserDto accountDetails1=new UserDto();
//        Users user1 = new Users();
//        
////        user1.setUser_id();
//        userController.getAllUsers();
        
       

	}
	
	
	@Test
	void validate() {
		
		UserDto userdto=new UserDto();
		userdto.setUserName("harish");
		userdto.setPassword("1234567");
		userdto.setMobileNumber("8892923219"); 
		userdto.setEmail("harish@gmail.com");
		userdto.setFirstName("hari");
		userdto.setLastName("babu");
		 
		String validationMessage1 = userController.validate(userdto, true);
		String validationMessage = userController.validate(userdto, false);
		assertNotNull(validationMessage);
		assertNotNull(validationMessage1);
	}
	
}
	






	

