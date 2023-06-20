package com.interopx.fhir.auth.server.dao;

import com.interopx.fhir.auth.server.model.SmartAuthorizeationDetails;

public interface SmartAuthorizeationDetailsDao {

  public SmartAuthorizeationDetails getAuthorizationDetailsById(int id);
}
