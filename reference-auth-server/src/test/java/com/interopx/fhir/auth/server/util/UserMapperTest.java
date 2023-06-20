package com.interopx.fhir.auth.server.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.interopx.fhir.auth.server.dao.UserRoleDao;
import com.interopx.fhir.auth.server.model.UserDto;
import com.interopx.fhir.auth.server.model.UserResponse;
import com.interopx.fhir.auth.server.model.UserRole;
import com.interopx.fhir.auth.server.model.Users;


@RunWith(SpringRunner.class)
@SpringBootTest
class UserMapperTest {

	 @Autowired
	 UserMapper userMapper;
	 @Mock
	 private PasswordEncoder passwordEncoder;
	 @Mock
	 private UserRoleDao sOFUserRoleDao;
	 
	 
	
	@Test
	void testConvertEntityToDto() {
		Users userEntity =new Users();
		userEntity.setEmailActivated(true);
		userEntity.setEmailActivationKey("Activation");
		userEntity.setFirstName("Harish");
		userEntity.setLastName("babu");
		userEntity.setMiddleName("madesha");
		userEntity.setMobile_number("8892923219");
		userEntity.setPatient_id("1234");
		userEntity.setUser_email("user@gmail.com");
		userEntity.setUser_id("2");
		userEntity.setUser_name("Hari");
		userEntity.setUser_password("123456789");
		userEntity.setUserActive(true);
        List<String>userrole = new ArrayList<>();
	    userEntity.setUserRole(userrole);
	    List<String> sOFUserRoleList = new ArrayList<>();
	    userEntity.setUserRole(sOFUserRoleList);
		UserResponse userDto=userMapper.convertEntityToDto(userEntity);
		assertNotNull(userDto);	
	}

	@Test
	void testConvertDtoToEntity() {
		
		UserDto userDto=new UserDto();
		userDto.setEmail("harish@gmail.com");
		userDto.setEmailActivationKey("active");
		userDto.setFirstName("harish");
		userDto.setFullName("harishbabu");
		userDto.setLastName("babu");
		userDto.setMiddleName("madesha");
		userDto.setMobileNumber("8892923219");
		userDto.setPassword("1234567890");
		userDto.setPatientId("12345");
		userDto.setToken("890");
		userDto.setUserActive(true);
		userDto.setUserId("2");
		userDto.setUserName("harii");
		
		Users users=userMapper.convertDtoToEntity(userDto);
		assertNotNull(users);
		
		//assertThat(users).usingRecursiveComparison().isEqualTo(userDto);
		
	}

	@Test
	void testEncodePassword() {

		String password="1234556789";
		String str=userMapper.encodePassword(password);
		when(passwordEncoder.encode(str)).thenReturn(str);
		assertNotNull(str);
	}

}
