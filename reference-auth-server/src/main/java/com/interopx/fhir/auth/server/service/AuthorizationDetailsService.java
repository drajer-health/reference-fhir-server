package com.interopx.fhir.auth.server.service;

import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import java.util.List;

public interface AuthorizationDetailsService {
  public AuthorizationDetails saveOrUpdate(AuthorizationDetails auth);

  public AuthorizationDetails getAuthByClientId(String clientId, String clientSecret);

  public List<AuthorizationDetails> getList();

  public AuthorizationDetails getAuthorizationByAccessToken(String accessToken);

  public AuthorizationDetails getAuthenticationById(String transactionId);

  public AuthorizationDetails getAuthById(String clientId);
}
