package com.interopx.fhir.auth.server.service.impl;

import com.interopx.fhir.auth.server.dao.AuthorizationDetailsDao;
import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import com.interopx.fhir.auth.server.service.AuthorizationDetailsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authTempService")
@Transactional
public class AuthorizationDetailsServiceImpl implements AuthorizationDetailsService {

  @Autowired private AuthorizationDetailsDao authorizationDetailsDao;

  @Override
  @Transactional
  public AuthorizationDetails saveOrUpdate(AuthorizationDetails auth) {
    return this.authorizationDetailsDao.saveOrUpdate(auth);
  }

  @Override
  @Transactional
  public AuthorizationDetails getAuthByClientId(String clientId, String clientSecret) {
    return this.authorizationDetailsDao.getAuthByClientId(clientId, clientSecret);
  }

  @Override
  @Transactional
  public List<AuthorizationDetails> getList() {
    return this.authorizationDetailsDao.getList();
  }

  @Override
  @Transactional
  public AuthorizationDetails getAuthorizationByAccessToken(String accessToken) {
    return this.authorizationDetailsDao.getAuthorizationByAccessToken(accessToken);
  }

  @Override
  @Transactional
  public AuthorizationDetails getAuthenticationById(String transactionId) {
    return this.authorizationDetailsDao.getAuthenticationById(transactionId);
  }

  @Override
  @Transactional
  public AuthorizationDetails getAuthById(String clientId) {
    return this.authorizationDetailsDao.getAuthById(clientId);
  }
}
