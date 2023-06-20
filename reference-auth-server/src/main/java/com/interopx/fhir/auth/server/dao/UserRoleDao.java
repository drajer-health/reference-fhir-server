package com.interopx.fhir.auth.server.dao;

import com.interopx.fhir.auth.server.model.UserRole;
import java.util.List;

public interface UserRoleDao {

  public UserRole getRoleById(String id);

  public List<UserRole> getAllRoles();
}
