package com.interopx.fhir.auth.server.dao.impl;

import com.interopx.fhir.auth.server.dao.AbstractDao;
import com.interopx.fhir.auth.server.dao.SmartAuthorizeationDetailsDao;
import com.interopx.fhir.auth.server.model.SmartAuthorizeationDetails;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class SmartAuthorizeationDetailsDaoImpl extends AbstractDao
    implements SmartAuthorizeationDetailsDao {
  Session session = null;
  private final Logger log =
      (Logger) LoggerFactory.getLogger(SmartAuthorizeationDetailsDaoImpl.class);

  @Override
  public SmartAuthorizeationDetails getAuthorizationDetailsById(int id) {
    log.debug("Start in getAuthorizationDetailsById() of SmartAuthorizeationDetailsDaoImpl class ");
    SmartAuthorizeationDetails smartAuthorizeationDetails = null;
    try {
      smartAuthorizeationDetails =
          (SmartAuthorizeationDetails) getSession().get(SmartAuthorizeationDetails.class, id);
    } catch (Exception e) {
      log.error(
          "Exception in getAuthorizationDetailsById() of SmartAuthorizeationDetailsDaoImpl class ",
          e);
    }
    log.debug("End in getAuthorizationDetailsById() of SmartAuthorizeationDetailsDaoImpl class ");
    return smartAuthorizeationDetails;
  }
}
