package com.interopx.fhir.auth.server.service.impl;

import com.interopx.fhir.auth.server.dao.SmartAuthorizeationDetailsDao;
import com.interopx.fhir.auth.server.model.SmartAuthorizeationDetails;
import com.interopx.fhir.auth.server.service.SmartAuthorizeationDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("smartAuthorizeationDetailsServiceImpl")
@Transactional
public class SmartAuthorizeationDetailsServiceImpl implements SmartAuthorizeationDetailsService {

  private final Logger logger =
      LoggerFactory.getLogger(SmartAuthorizeationDetailsServiceImpl.class);

  @Autowired SmartAuthorizeationDetailsDao smartAuthorizeationDetailsDao;

  @Override
  public SmartAuthorizeationDetails getAuthorizationDetailsById(int id) {
    return smartAuthorizeationDetailsDao.getAuthorizationDetailsById(id);
  }
}
