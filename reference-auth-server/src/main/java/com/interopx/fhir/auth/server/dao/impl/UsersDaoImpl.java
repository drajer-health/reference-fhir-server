package com.interopx.fhir.auth.server.dao.impl;

import com.interopx.fhir.auth.server.dao.AbstractDao;
import com.interopx.fhir.auth.server.dao.UsersDao;
import com.interopx.fhir.auth.server.model.SystemValues;
import com.interopx.fhir.auth.server.model.Users;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UsersDaoImpl extends AbstractDao implements UsersDao {
  Session session = null;
  private final Logger log = (Logger) LoggerFactory.getLogger(UsersDaoImpl.class);

  @SuppressWarnings({"deprecation", "unchecked"})
  @Override
  public String register(Users user) {
    log.debug("Start in register() of UserRegistrationDaoImpl class ");
    String message = null;
    try {
      session = getSession();
      Criteria criteria =
          getSession()
              .createCriteria(Users.class)
              .add(Restrictions.eq("user_name", user.getUser_name()).ignoreCase());
      List<Users> existedUser = criteria.list();
      if (existedUser != null && existedUser.size() > 0) {
        message = "Username already exists. Please use a different Username.";
      } else {
        int i = (Integer) session.save(user);
        if (i > 0) {
          message = "User Registerd Successfully. Please Login to register Clients.";

        } else {
          message = "User Registration failed. Please contact Admin.";
        }
      }
    } catch (Exception e) {
      log.error("Exception in register() of UserRegistrationDaoImpl class ");
    }
    log.debug("End in register() of UserRegistrationDaoImpl class ");
    return message;
  }

  public String updateUser(Users user) {
    log.debug("Start in updateUser() of UserRegistrationDaoImpl class ");
    String message = null;
    try {
      session = getSession();
      session.update(user);
      message = "User details updated.";
    } catch (Exception e) {
      log.error("Failed to update User details. Please contact Admin.", e);
      message = "Failed to update User details. Please contact Admin.";
    }
    log.debug("End in updateUser() of UserRegistrationDaoImpl class ");
    return message;
  }

  @Override
  public Users getUserById(String id) {
    log.debug("Start in getUserById() of UserRegistrationDaoImpl class ");
    Users dafUserRegister = null;
    try {
      dafUserRegister = (Users) getSession().get(Users.class, id);
    } catch (Exception e) {
      log.error("Exception in getUserById() of UserRegistrationDaoImpl class ", e);
    }
    log.debug("End in getUserById() of UserRegistrationDaoImpl class ");
    return dafUserRegister;
  }

  @SuppressWarnings("deprecation")
  @Override
  public Users getUserByDetails(String userName, String password) {
    log.debug("Start in getUserByDetails() of UserRegistrationDaoImpl class ");
    Users user = null;
    try {
      Criteria criteria =
          getSession()
              .createCriteria(Users.class)
              .add(Restrictions.eq("user_name", userName))
              .add(Restrictions.eq("user_password", password));
      user = new Users();
      user = (Users) criteria.uniqueResult();
    } catch (Exception e) {
      log.error("Exception in getUserByDetails() of UserRegistrationDaoImpl class ", e);
    }
    log.debug("End in getUserByDetails() of UserRegistrationDaoImpl class ");
    return user;
  }

  @SuppressWarnings("deprecation")
  @Override
  public Users getUserByName(String userName) {
    log.debug("Start in getUserByName() of UserRegistrationDaoImpl class ");
    Users user = null;
    try {
      Criteria criteria =
          getSession().createCriteria(Users.class).add(Restrictions.eq("user_name", userName));
      user = new Users();
      user = (Users) criteria.uniqueResult();
    } catch (Exception e) {
      log.error("Exception in getUserByName() of UserRegistrationDaoImpl class ", e);
    }
    log.debug("End in getUserByName() of UserRegistrationDaoImpl class ");
    return user;
  }

  @Override
  public Users getUserByEmail(String email) {
    log.debug("Start in getUserByEmail() of UserRegistrationDaoImpl class ");
    Users user = null;
    try {
      Criteria criteria =
          getSession().createCriteria(Users.class).add(Restrictions.eq("user_email", email));
      user = new Users();
      user = (Users) criteria.uniqueResult();
    } catch (Exception e) {
      log.error("Exception in getUserByEmail() of UserRegistrationDaoImpl class ", e);
    }
    log.debug("End in getUserByEmail() of UserRegistrationDaoImpl class ");
    return user;
  }

  @SuppressWarnings("deprecation")
  @Override
  public String getPropertyValue(String key) {
    log.debug("Start in getPropertyValue() of UserRegistrationDaoImpl class ");
    String message = null;
    try {
      Criteria criteria =
          getSession()
              .createCriteria(SystemValues.class)
              .setProjection(Projections.property("value"))
              .add(Restrictions.eq("key", key));
      message = (String) criteria.list().get(0);
    } catch (Exception e) {
      log.error("Exception in getPropertyValue() of UserRegistrationDaoImpl class ", e);
    }
    log.debug("End in getPropertyValue() of UserRegistrationDaoImpl class ");
    return message;
  }

  @SuppressWarnings("deprecation")
  @Override
  public Users verifyToken(String token, String email) {
    log.debug("Start in getUserByName() of UserRegistrationDaoImpl class ");
    Users user = null;
    try {
      session = getSession();
      Criteria criteria =
          getSession()
              .createCriteria(Users.class)
              .add(Restrictions.eq("emailActivationKey", token))
              .add(Restrictions.eq("user_email", email))
              .add(Restrictions.eq("isEmailActivated", false));
      user = new Users();
      user = (Users) criteria.uniqueResult();
    } catch (Exception e) {
      log.error("Exception in getUserByName() of UserRegistrationDaoImpl class ", e);
    }
    log.debug("End in getUserByName() of UserRegistrationDaoImpl class ");
    return user;
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Override
  public List<Users> getAllUsers() {
    log.debug("Start in getAllUsers() of UserRegistrationDaoImpl class ");
    List<Users> list = null;
    try {
      Criteria criteria = getSession().createCriteria(Users.class);
      list = new ArrayList<>();
      list = (List<Users>) criteria.list();
    } catch (Exception e) {
      log.error("Exception in getAllUsers() of UserRegistrationDaoImpl class ", e);
    }
    log.debug("End in getAllUsers() of UserRegistrationDaoImpl class ");
    return list;
  }
}
