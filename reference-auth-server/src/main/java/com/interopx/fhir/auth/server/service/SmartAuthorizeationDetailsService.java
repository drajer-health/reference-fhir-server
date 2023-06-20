package com.interopx.fhir.auth.server.service;

import com.interopx.fhir.auth.server.model.SmartAuthorizeationDetails;

public interface SmartAuthorizeationDetailsService {

  public SmartAuthorizeationDetails getAuthorizationDetailsById(int id);
}
