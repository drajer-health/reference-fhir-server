package com.interopx.fhir.auth.server.dao.impl;

import com.interopx.fhir.auth.server.dao.AbstractDao;
import com.interopx.fhir.auth.server.dao.UserRoleDao;
import com.interopx.fhir.auth.server.model.UserRole;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserRoleDaoImpl extends AbstractDao implements UserRoleDao {

  private final Logger log = (Logger) LoggerFactory.getLogger(UsersDaoImpl.class);

  @Override
  public UserRole getRoleById(String id) {
    log.debug("Start in getRoleById() of SOFUserRoleDaoImpl class ");
    Integer userId = Integer.parseInt(id);
    UserRole sofRole = null;
    try {
      sofRole = (UserRole) getSession().get(UserRole.class, userId);
    } catch (Exception e) {
      log.error("Exception in getRoleById() of SOFUserRoleDaoImpl class ", e);
    }
    log.debug("End in getRoleById() of SOFUserRoleDaoImpl class ");
    return sofRole;
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Override
  public List<UserRole> getAllRoles() {
    log.debug("Start in getAllRoles() of SOFUserRoleDaoImpl class ");
    List<UserRole> list = null;
    try {
      Criteria criteria = getSession().createCriteria(UserRole.class);
      list = new ArrayList<>();
      list = (List<UserRole>) criteria.list();
    } catch (Exception e) {
      log.error("Exception in getAllRoles() of SOFUserRoleDaoImpl class ");
    }
    log.debug("End in getAllRoles() of SOFUserRoleDaoImpl class ");
    return list;
  }
}
