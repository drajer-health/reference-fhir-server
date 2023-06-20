package com.interopx.fhir.auth.server.service.impl;

import com.interopx.fhir.auth.server.dao.UserRoleDao;
import com.interopx.fhir.auth.server.model.UserRole;
import com.interopx.fhir.auth.server.service.UserRoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sofuserroleservice")
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

  @Autowired UserRoleDao sOFUserRoleDao;

  @Override
  @Transactional
  public List<UserRole> getAllRoles() {
    return sOFUserRoleDao.getAllRoles();
  }
}
