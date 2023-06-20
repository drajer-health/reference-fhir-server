package com.interopx.fhir.auth.server.controller;

import com.interopx.fhir.auth.server.model.UserRole;
import com.interopx.fhir.auth.server.service.UserRoleService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SOFUserRoleController {

  private static final Logger logger = LoggerFactory.getLogger(SOFUserRoleController.class);

  @Autowired UserRoleService sOFUserRoleService;

  @RequestMapping(value = "/api/sofroles", method = RequestMethod.GET)
  @ResponseBody
  public List<UserRole> getAllSOFUserRole() {
    logger.debug("Request received to get all sof user roles");
    return sOFUserRoleService.getAllRoles();
  }
}
