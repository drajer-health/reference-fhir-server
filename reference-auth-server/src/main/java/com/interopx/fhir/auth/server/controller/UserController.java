package com.interopx.fhir.auth.server.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.interopx.fhir.auth.server.jwtservice.JwtTokenUtil;
import com.interopx.fhir.auth.server.model.UserDto;
import com.interopx.fhir.auth.server.model.UserResponse;
import com.interopx.fhir.auth.server.model.UserResponseMessage;
import com.interopx.fhir.auth.server.model.Users;
import com.interopx.fhir.auth.server.service.EmailService;
import com.interopx.fhir.auth.server.service.UsersService;
import com.interopx.fhir.auth.server.util.AuthUtil;
import com.interopx.fhir.auth.server.util.UserMapper;

@RestController
@PropertySource(value = "classpath:/email.notificaion.properties")
public class UserController {

  /** The logger. */
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired UsersService userRegistrationService;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired UserMapper userMapper;

  @Autowired EmailService emailService;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Value("${user.mail.subject}")
  private String emailSubject;

  @Value("${user.activate.htmlcontent}")
  private String emailBody;

  /**
   * Register user.
   *
   * @param accountDetails the user details
   * @return the response entity
   * @throws Exception the exception
   */
  @PostMapping(value = "/user/register")
//  @TrackRequest
  public ResponseEntity<UserResponseMessage> registerNewUserAccount(
      @RequestBody UserDto accountDetails, HttpServletRequest request) throws Exception {
    logger.debug("Start in registerNewUserAccount() of UserController class ");

    try {
      //    	accountDetails.getPassword() == null &&
      if (accountDetails.getUserId() != null ) {
        String validationMessage = validate(accountDetails, true);

        if (validationMessage != null && !validationMessage.equals("")) {
          UserResponseMessage userResponseMessage =
              new UserResponseMessage(
                  new Date(),
                  "User Registration Failed.",
                  validationMessage,
                  HttpStatus.BAD_REQUEST.toString());
          logger.error("Error code 400: User Registration Failed.");
          return new ResponseEntity<UserResponseMessage>(
              userResponseMessage, HttpStatus.BAD_REQUEST);
        }
        //        accountDetails.setPassword("dummy123");
        String token = AuthUtil.generateToken();
        Users user = userMapper.convertDtoToEntity(accountDetails);
        user.setUser_name(user.getUser_email());
        user.setEmailActivationKey(token);
        user.setUserActive(true);
        List<String> userRole = new ArrayList<>();
        userRole.add("2");
        user.setUserRole(userRole);

        String result = userRegistrationService.registerUser(user);
        if (result.contains("User Registerd Successfully")) {
          if (user.getPatient_id() != null) {
            String requetUrl = request.getRequestURL().toString();
            String a[] = requetUrl.split("/");
            String protocol = a[0];
            String host = a[2];
            String encodedEmail =
                Base64.getEncoder().encodeToString(accountDetails.getEmail().getBytes());
            StringBuilder url = new StringBuilder();
            url.append(protocol)
                .append("//")
                .append(host)
                .append("/PatientPortal/verify-email")
                .append("?token=")
                .append(token)
                .append("&token_id=")
                .append(encodedEmail);
            String body = MessageFormat.format(emailBody, accountDetails.getFirstName(), url);
            emailService.sendEmail(accountDetails.getEmail(), emailSubject, body);
          }
          UserResponseMessage userResponseMessage =
              new UserResponseMessage(
                  new Date(),
                  "User Registerd Successfully. Please Login to register Clients",
                  "Success",
                  HttpStatus.CREATED.toString());
          logger.debug(
              "Success code 201: User Registerd Successfully. Please Login to register Clients.");
          return new ResponseEntity<UserResponseMessage>(userResponseMessage, HttpStatus.CREATED);
        } else if (result.contains("Username already exists")) {
          UserResponseMessage userResponseMessage =
              new UserResponseMessage(
                  new Date(),
                  "Email Id is already in use. Please use a different Email Id.",
                  "Conflict",
                  HttpStatus.CONFLICT.toString());
          logger.error(
              "Conflict code 409: Username already exists. Please use a different Username.");
          return new ResponseEntity<UserResponseMessage>(userResponseMessage, HttpStatus.CONFLICT);
        }
      }

      String validationMessage = validate(accountDetails, false);
      if (validationMessage != null && !validationMessage.equals("")) {
        UserResponseMessage userResponseMessage =
            new UserResponseMessage(
                new Date(),
                "User Registration Failed.",
                validationMessage,
                HttpStatus.BAD_REQUEST.toString());
        logger.error("Error code 400: User Registration Failed.");
        return new ResponseEntity<UserResponseMessage>(userResponseMessage, HttpStatus.BAD_REQUEST);
      }
      Users user = userRegistrationService.getUserById(accountDetails.getUserId());

      if (ObjectUtils.isNotEmpty(user)) {
        user.setMobile_number(accountDetails.getMobileNumber());
        user.setUser_password(userMapper.encodePassword(accountDetails.getPassword()));
        String message = userRegistrationService.updateUser(user);
        if (message.contains("User details updated")) {
          UserResponseMessage userResponseMessage =
              new UserResponseMessage(
                  new Date(),
                  "User Registerd Successfully. Please Login to register Clients",
                  "Success",
                  HttpStatus.OK.toString());
          logger.debug(
              "Success code 200: User Registerd Successfully. Please Login to register Clients.");
          return new ResponseEntity<UserResponseMessage>(userResponseMessage, HttpStatus.OK);
        }
        UserResponseMessage userResponseMessage =
            new UserResponseMessage(
                new Date(),
                "User Registration Failed.",
                "Failed to update User details",
                HttpStatus.BAD_REQUEST.toString());
        logger.error("Error code 400: User Registration Failed. Failed to update User details ");
        return new ResponseEntity<UserResponseMessage>(userResponseMessage, HttpStatus.BAD_REQUEST);

      } else {
        UserResponseMessage userResponseMessage =
            new UserResponseMessage(
                new Date(),
                "User Registration Failed",
                "User Not Found",
                HttpStatus.NOT_FOUND.toString());
        logger.error("Error code 404: User Registration Failed. User Not Found ");
        return new ResponseEntity<UserResponseMessage>(userResponseMessage, HttpStatus.NOT_FOUND);
      }
    } catch (Exception ex) {
      UserResponseMessage userResponseMessage =
          new UserResponseMessage(
              new Date(),
              "User Registration failed. Please contact Admin",
              ex.getMessage(),
              HttpStatus.INTERNAL_SERVER_ERROR.toString());
      logger.error("Error code 500: User Registration failed. Please contact Admin ");
      return new ResponseEntity<UserResponseMessage>(
          userResponseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * User login.
   *
   * @param accountDetails the user details
   * @return the response entity
   * @throws Exception the exception
   */
  @PostMapping(value = "/user/login")
//  @TrackRequest
  public ResponseEntity<?> uerLogin(@RequestBody UserDto accountDetails, HttpServletRequest request)
      throws Exception {
    logger.debug("Start in uerLogin() of UserController class ");

    Users user = userRegistrationService.getUserByEmail(accountDetails.getEmail(), request);
    UserResponse loginUser = userMapper.convertEntityToDto(user);
    if (user != null
        && (passwordEncoder.matches(accountDetails.getPassword(), user.getUser_password()))) {
      if (!user.isUserActive()) {
        return new ResponseEntity<String>(
            "User is not Active Please Contact Admin", HttpStatus.UNAUTHORIZED);
      }
      Map<String, Object> temp = new HashMap<>();
      temp.put("userdetails", loginUser);
      final String loginAccessToken = jwtTokenUtil.doGenerateToken(temp, loginUser.getEmail());
      loginUser.setAccessToken(loginAccessToken);
      logger.debug("Success code 200: User logged in Successfully.");
      return new ResponseEntity<UserResponse>(loginUser, HttpStatus.OK);
    }
    logger.error("Error code 401: Invalid username OR password ");
    return new ResponseEntity<String>("Invalid username OR password", HttpStatus.UNAUTHORIZED);
  }

  /**
   * User Update with PatientId.
   *
   * @param accountDetails the user details
   * @return the response entity
   * @throws Exception the exception
   */
  @PutMapping(value = "/user/update")
//  @TrackRequest
  public ResponseEntity<?> updateUser(
      @RequestBody UserDto accountDetails, HttpServletRequest request) throws Exception {
    logger.debug("Start in updateUser() of UserController class ");
    Users user = userRegistrationService.getUserById(accountDetails.getUserId());
    if (ObjectUtils.isNotEmpty(user)) {
      user.setPatient_id(accountDetails.getPatientId());
      userRegistrationService.updateUser(user);
      UserResponse response = userMapper.convertEntityToDto(user);
      logger.debug("Success code 200: User updated successfully.");
      return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }
    logger.error(
        "Error code 400: Failed to update User details. Enter valid details OR Please contact Admin.");
    return new ResponseEntity<String>(
        "Failed to update User details. Enter valid details OR Please contact Admin.",
        HttpStatus.BAD_REQUEST);
  }

  /**
   * User Update with PatientId.
   *
   * @param accountDetails the user details
   * @return the response entity
   * @throws Exception the exception
   */
  @PutMapping(value = "/user/updateuser")
//  @TrackRequest
  public ResponseEntity<?> updateUserDetails(
      @RequestBody UserDto accountDetails, HttpServletRequest request) throws Exception {
    logger.debug("Start in updateUser() of UserController class ");
    Users user = userRegistrationService.getUserById(accountDetails.getUserId());
    if (ObjectUtils.isNotEmpty(user)) {
      user.setFirstName(accountDetails.getFirstName());
      user.setMiddleName(accountDetails.getMiddleName());
      user.setLastName(accountDetails.getLastName());
      user.setMobile_number(accountDetails.getMobileNumber());
      user.setUserRole(accountDetails.getUserRole());
      user.setUserActive(accountDetails.isUserActive());
      user.setPatient_id(accountDetails.getPatientId());
      userRegistrationService.updateUser(user);
      UserResponse response = userMapper.convertEntityToDto(user);
      logger.debug("Success code 200: User updated successfully.");
      return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }
    logger.error(
        "Error code 400: Failed to update User details. Enter valid details OR Please contact Admin.");
    return new ResponseEntity<String>(
        "Failed to update User details. Enter valid details OR Please contact Admin.",
        HttpStatus.BAD_REQUEST);
  }

  /**
   * activate or deactivate user
   *
   * @param userId
   * @param value
   * @param request
   * @return
   * @throws Exception
   */
  @PostMapping(value = "/user/activation/{userId}/{value}")
//  @TrackRequest
  public ResponseEntity<?> activateDeactivateUser(
      @PathVariable String userId, @PathVariable boolean value, HttpServletRequest request)
      throws Exception {
    logger.debug("Start in activateDeactivateUser() of UserController class ");
    Users user = userRegistrationService.getUserById(userId);
    if (ObjectUtils.isNotEmpty(user)) {
      user.setUserActive(value);
      userRegistrationService.updateUser(user);
      UserResponse response = userMapper.convertEntityToDto(user);
      logger.debug("Success code 200: User updated successfully.");
      return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }
    logger.error(
        "Error code 400: Failed to update User details. Enter valid details OR Please contact Admin.");
    return new ResponseEntity<String>(
        "Failed to update User details. Enter valid details OR Please contact Admin.",
        HttpStatus.BAD_REQUEST);
  }

  /**
   * update user role
   *
   * @param accountDetails
   * @param request
   * @return
   * @throws Exception
   */
  @PutMapping(value = "/user/updaterole")
//  @TrackRequest
  public ResponseEntity<?> updateUserRole(
      @RequestBody UserDto accountDetails, HttpServletRequest request) throws Exception {
    logger.debug("Start in activateDeactivateUser() of UserController class ");
    Users user = userRegistrationService.getUserById(accountDetails.getUserId());
    if (ObjectUtils.isNotEmpty(user)) {
      user.setUserRole(accountDetails.getUserRole());
      userRegistrationService.updateUser(user);
      UserResponse response = userMapper.convertEntityToDto(user);
      logger.debug("Success code 200: User updated successfully.");
      return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }
    logger.error(
        "Error code 400: Failed to update User details. Enter valid details OR Please contact Admin.");
    return new ResponseEntity<String>(
        "Failed to update User details. Enter valid details OR Please contact Admin.",
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Method to verify email for patient user registration
   *
   * @param accountDetails
   * @param request
   * @return
   * @throws Exception
   */
  @PostMapping("/user/activate")
//  @TrackRequest
  public ResponseEntity<?> verifyEmail(
      @RequestBody UserDto accountDetails, HttpServletRequest request) throws Exception {
    logger.debug("Started verifyEmail() method in UserController class ");
    Users dafUserRegister =
        userRegistrationService.verifyToken(accountDetails.getToken(), accountDetails.getEmail());
    if (dafUserRegister != null) {
      UserResponse userResponse = userMapper.convertEntityToDto(dafUserRegister);
      logger.debug("Error code 200: Success");
      return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
    }
    logger.error("Error code 400: User token did not match.");
    return new ResponseEntity<String>("User token did not match", HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/user/list")
  public List<UserResponse> getAllUsers() {
    logger.debug("Started getAllUsers() method in UserController class ");
    List<UserResponse> usersDtoList = null;
    try {
      List<Users> userList = userRegistrationService.getAllUsers();
      if (userList.size() > 0) {
        usersDtoList = new ArrayList<>();
        for (Users dafUser : userList) {
          usersDtoList.add(userMapper.convertEntityToDto(dafUser));
        }
      }
    } catch (Exception e) {
      logger.debug("Started getAllUsers() method error getting user list");
    }
    return usersDtoList;
  }

  /**
   * Method to vaidate the incoming fields in User Registration
   *
   * @param userDto
   * @param flag
   * @return
   */
  public String validate(UserDto userDto, boolean flag) {

    StringBuilder sb = new StringBuilder();

    if (flag) {
      if (StringUtils.isEmpty(userDto.getEmail())) {
        sb.append("User email,");
      }
      if (StringUtils.isEmpty(userDto.getFirstName())) {
        sb.append("First Name,");
      }
      if (StringUtils.isEmpty(userDto.getLastName())) {
        sb.append("Last Name,");
      }
      //      if (StringUtils.isEmpty(userDto.getPatientId())) {
      //        sb.append("PatientId,");
      //      }
      if (sb.length() > 0) {
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" Cannot be empty");
      }
      return sb.toString();
    }

    if (StringUtils.isEmpty(userDto.getUserName())) {
      sb.append("User Name,");
    }
    if (StringUtils.isEmpty(userDto.getPassword())) {
      sb.append("User Password,");
    }
    if (StringUtils.isEmpty(userDto.getMobileNumber())) {
      sb.append("Mobile Number,");
    }

    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
      sb.append(" Cannot be empty");
    }
    return sb.toString();
  }
}
