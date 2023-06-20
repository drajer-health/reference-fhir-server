package com.interopx.fhir.auth.server.service;

import com.interopx.fhir.auth.server.model.UserRole;
import java.util.List;

public interface UserRoleService {

  public List<UserRole> getAllRoles();
}
