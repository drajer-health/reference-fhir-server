package com.interopx.fhir.auth.server.util;

import ch.qos.logback.classic.Logger;
import com.interopx.fhir.auth.server.dao.UserRoleDao;
import com.interopx.fhir.auth.server.model.UserDto;
import com.interopx.fhir.auth.server.model.UserResponse;
import com.interopx.fhir.auth.server.model.UserRole;
import com.interopx.fhir.auth.server.model.Users;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  private static final Logger logger = (Logger) LoggerFactory.getLogger(UserMapper.class);

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private UserRoleDao sOFUserRoleDao;

  public UserResponse convertEntityToDto(Users userEntity) {
    logger.debug("Start in convertEntityToDto() of UserMapper class ");
    UserResponse responseDto = new UserResponse();
    try {
      String middleName =
          (userEntity.getMiddleName() != null) ? " " + userEntity.getMiddleName() + " " : "";
      if (ObjectUtils.isNotEmpty(userEntity)) {
        responseDto.setUserId(userEntity.getUser_id());
        responseDto.setUserName(userEntity.getUser_name());
        responseDto.setEmail(userEntity.getUser_email());
        responseDto.setFullName(userEntity.getFirstName() + middleName + userEntity.getLastName());
        responseDto.setFirstName(userEntity.getFirstName());
        responseDto.setLastName(userEntity.getLastName());
        responseDto.setMiddleName(userEntity.getMiddleName());
        responseDto.setMobileNumber(userEntity.getMobile_number());
        responseDto.setPatientId(userEntity.getPatient_id());
        responseDto.setLastUpdatedTime(userEntity.getLast_updated_ts());
        List<UserRole> sOFUserRoleList = new ArrayList<>();
        if (userEntity.getUserRole().size() > 0) {
          for (String roleId : userEntity.getUserRole()) {
            sOFUserRoleList.add(sOFUserRoleDao.getRoleById(roleId));
          }
        }
        responseDto.setUserRole(sOFUserRoleList);
        responseDto.setUserActive(userEntity.isUserActive());
        return responseDto;
      }
    } catch (Exception e) {
      logger.error("Exception in convertEntityToDto() of UserMapper class ", e);
    }
    logger.debug("End in convertEntityToDto() of UserMapper class ");
    return responseDto;
  }

  public Users convertDtoToEntity(UserDto userDto) {
    logger.debug("Start in convertDtoToEntity() of UserMapper class ");
    Users userEntity = new Users();
    try {
      userEntity.setUser_name(userDto.getUserName());
      userEntity.setUser_email(userDto.getEmail());
      userEntity.setFirstName(userDto.getFirstName());
      userEntity.setMiddleName(userDto.getMiddleName());
      userEntity.setLastName(userDto.getLastName());
      if (userDto.getPassword() == null) {
        userDto.setPassword("dummy123");
      }
      userEntity.setUser_password(passwordEncoder.encode(userDto.getPassword()));
      userEntity.setMobile_number(userDto.getMobileNumber());
      userEntity.setPatient_id(userDto.getPatientId());
      userEntity.setLast_updated_ts(new Date());
    } catch (Exception e) {
      logger.error("Exception in convertDtoToEntity() of UserMapper class ", e);
    }
    logger.debug("End in convertDtoToEntity() of UserMapper class ");
    return userEntity;
  }

  public String encodePassword(String unencodedPassword) {
    return passwordEncoder.encode(unencodedPassword);
  }
}
