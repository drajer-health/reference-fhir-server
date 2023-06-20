package com.interopx.fhir.auth.server.service.impl;

import com.interopx.fhir.auth.server.dao.UsersDao;
import com.interopx.fhir.auth.server.model.Users;
import com.interopx.fhir.auth.server.service.UsersService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userRegistrationService")
@Transactional
public class UsersServiceImpl implements UsersService {
  private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

  @Autowired private UsersDao userDao;

  @Override
  public String registerUser(Users user) {
    return userDao.register(user);
  }

  public String updateUser(Users user) {
    return userDao.updateUser(user);
  }

  @Override
  public Users getUserById(String id) {
    return userDao.getUserById(id);
  }

  @Override
  public Users getUserByDetails(String userName, String password, HttpServletRequest request) {
    logger.debug("Start in getUserByDetails() of UserRegistrationServiceImpl class ");
    Users user = new Users();
    try {
      user = userDao.getUserByDetails(userName, password);

      HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();
      long time =
          System.currentTimeMillis()
              + (15 * 60 * 1000); // 4 * 60 * 60 * 1000 3600 seconds times 1000
      // milliseconds(1 hour)

      Integer expiryTime = (int) (time / 1000L);
      sessionMap.put("expiry", expiryTime);
      if (user != null) {
        HttpSession session = request.getSession();
        session.setAttribute("user" + user.getUser_id(), sessionMap);
      }
    } catch (Exception e) {
      logger.error("Exception in getUserByDetails() of UserRegistrationServiceImpl class ", e);
    }

    logger.debug("End in getUserByDetails() of UserRegistrationServiceImpl class ");

    return user;
  }

  @Override
  public Users getUserByName(String userName, HttpServletRequest request) {
    logger.debug("Start in getUserByDetails() of UserRegistrationServiceImpl class ");
    Users user = new Users();
    try {
      user = userDao.getUserByName(userName);

      HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();
      long time =
          System.currentTimeMillis()
              + (15 * 60 * 1000); // 4 * 60 * 60 * 1000 3600 seconds times 1000
      // milliseconds(1 hour)

      Integer expiryTime = (int) (time / 1000L);
      sessionMap.put("expiry", expiryTime);
      if (user != null) {
        HttpSession session = request.getSession();
        session.setAttribute("user" + user.getUser_id(), sessionMap);
      }
    } catch (Exception e) {
      logger.error("Exception in getUserByDetails() of UserRegistrationServiceImpl class ", e);
    }

    logger.debug("End in getUserByDetails() of UserRegistrationServiceImpl class ");
    return user;
  }

  @Override
  public Users getUserByEmail(String email, HttpServletRequest request) {
    logger.debug("Start in getUserByEmail() of UserRegistrationServiceImpl class ");
    Users user = new Users();
    try {
      user = userDao.getUserByEmail(email);

      HashMap<String, Integer> sessionMap = new HashMap<String, Integer>();
      long time =
          System.currentTimeMillis()
              + (15 * 60 * 1000); // 4 * 60 * 60 * 1000 3600 seconds times 1000
      // milliseconds(1 hour)

      Integer expiryTime = (int) (time / 1000L);
      sessionMap.put("expiry", expiryTime);
      if (user != null) {
        HttpSession session = request.getSession();
        session.setAttribute("user" + user.getUser_id(), sessionMap);
      }
    } catch (Exception e) {
      logger.error("Exception in getUserByEmail() of UserRegistrationServiceImpl class ", e);
    }

    logger.debug("End in getUserByEmail() of UserRegistrationServiceImpl class ");
    return user;
  }

  public Users loadUserByName(String userName) {
    logger.debug("Start in loadUserByName() of UserRegistrationServiceImpl class ");
    Users user = new Users();
    try {
      user = userDao.getUserByName(userName);
      if (user == null) {
        logger.error("User not found with username: " + userName);
        throw new UsernameNotFoundException("User not found with username: " + userName);
      }
    } catch (Exception e) {
      logger.error("Exception in loadUserByName() of UserRegistrationServiceImpl class ", e);
    }

    logger.debug("End in loadUserByName() of UserRegistrationServiceImpl class ");
    return user;
  }

  @Override
  public String getPropertyValue(String key) {
    return userDao.getPropertyValue(key);
  }

  @Override
  public Users verifyToken(String token, String email) {
    logger.debug("Start in verifyToken() of UserRegistrationServiceImpl class ");
    Users dafUserRegister = null;
    try {
      dafUserRegister = userDao.verifyToken(token, email);
      if (dafUserRegister != null) {
        dafUserRegister.setEmailActivated(true);
        dafUserRegister.setUserActive(true);
        List<String> userRole = new ArrayList<>();
        userRole.add("3");
        dafUserRegister.setUserRole(userRole);
        userDao.updateUser(dafUserRegister);
      }
    } catch (Exception e) {
      logger.error("Exception in verifyToken() of UserRegistrationServiceImpl class ", e);
    }
    logger.debug("End in verifyToken() of UserRegistrationServiceImpl class ");
    return dafUserRegister;
  }

  @Override
  public List<Users> getAllUsers() {
    logger.debug("Start in getAllUsers() of UserRegistrationServiceImpl class ");
    List<Users> dafUserRegisterList = null;
    try {
      dafUserRegisterList = userDao.getAllUsers();
    } catch (Exception e) {
      logger.error("Exception in getAllUsers() of UserRegistrationServiceImpl class ", e);
    }
    logger.debug("End in getAllUsers() of UserRegistrationServiceImpl class ");
    return dafUserRegisterList;
  }
}
