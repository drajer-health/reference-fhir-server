package com.interopx.fhir.auth.server.dao.impl;

import com.interopx.fhir.auth.server.dao.AbstractDao;
import com.interopx.fhir.auth.server.dao.AuthorizationDetailsDao;
import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class AuthorizationDetailsDaoImpl extends AbstractDao implements AuthorizationDetailsDao {
  Session session = null;
  final Logger log = (Logger) LoggerFactory.getLogger(AuthorizationDetailsDaoImpl.class);

  @Override
  public AuthorizationDetails saveOrUpdate(AuthorizationDetails auth) {
    log.debug("Start in saveOrUpdate() of AuthTempDaoImpl class ");
    try {
      AuthorizationDetails auths = getAuthById(auth.getClientId());
      if (auths != null) {

        auths.setAccessToken(auth.getAccessToken());
        auths.setAud(auth.getAud());
        auths.setAuthCode(auth.getAuthCode());
        auths.setClientId(auth.getClientId());
        auths.setClientSecret(auth.getClientSecret());
        auths.setExpiry(auth.getExpiry());
        auths.setRedirectUri(auth.getRedirectUri());
        auths.setScope(auth.getScope());
        auths.setTransactionId(auth.getTransactionId());
        auths.setState(auth.getState());
        auths.setRefreshToken(auth.getRefreshToken());
        auths.setRefreshTokenExpiryTime(auth.getRefreshTokenExpiryTime());
        auths.setLaunchPatientId(auth.getLaunchPatientId());
        auths.setCodeChallenge(auth.getCodeChallenge());
        auths.setCodeChallengeMethod(auth.getCodeChallengeMethod());
        session = getSession();
        session.update(auths);

      } else {

        session = getSession();
        session.saveOrUpdate(auth);
      }
    } catch (Exception e) {
      log.error("Exception in saveOrUpdate() of AuthTempDaoImpl class ", e);
    }
    log.debug("End in saveOrUpdate() of AuthTempDaoImpl class ");
    return auth;
  }

  @SuppressWarnings("deprecation")
  @Override
  public AuthorizationDetails getAuthByClientId(String clientId, String clientSecret) {
    log.debug("Start in getAuthByClientId() of AuthTempDaoImpl class ");
    AuthorizationDetails auth = null;
    try {
      Criteria crit =
          getSession()
              .createCriteria(AuthorizationDetails.class)
              .add(Restrictions.eq("clientId", clientId))
              .add(Restrictions.eq("clientSecret", clientSecret));
      auth = new AuthorizationDetails();
      auth = (AuthorizationDetails) crit.uniqueResult();
    } catch (Exception e) {
      log.error("Exception in getAuthByClientId() of AuthTempDaoImpl class ", e);
    }
    log.debug("End in getAuthByClientId() of AuthTempDaoImpl class ");
    return auth;
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Override
  public List<AuthorizationDetails> getList() {
    log.debug("Start in getList() of AuthTempDaoImpl class ");
    List<AuthorizationDetails> list = new ArrayList<>();
    try {
      list = getSession().createCriteria(AuthorizationDetails.class).list();
    } catch (Exception e) {
      log.error("Exception in getList() of AuthTempDaoImpl class ", e);
    }
    log.debug("End in getList() of AuthTempDaoImpl class ");
    return list;
    // return null;
  }

  @SuppressWarnings("deprecation")
  @Override
  public AuthorizationDetails getAuthorizationByAccessToken(String accessToken) {
    log.debug("Start in validateAccessToken() of AuthTempDaoImpl class ");
    AuthorizationDetails auth = null;
    try {
      auth =
          (AuthorizationDetails)
              getSession()
                  .createCriteria(AuthorizationDetails.class)
                  .add(Restrictions.eq("accessToken", accessToken))
                  .uniqueResult();
    } catch (Exception e) {
      log.error("Exception in validateAccessToken() of AuthTempDaoImpl class ", e);
    }
    log.debug("End in validateAccessToken() of AuthTempDaoImpl class ");
    return auth;
  }

  @SuppressWarnings("deprecation")
  @Override
  public AuthorizationDetails getAuthenticationById(String transactionId) {
    log.debug("Start in getAuthenticationById() of AuthTempDaoImpl class ");
    AuthorizationDetails auth = new AuthorizationDetails();
    try {
      auth =
          (AuthorizationDetails)
              getSession()
                  .createCriteria(AuthorizationDetails.class)
                  .add(Restrictions.eq("transactionId", transactionId))
                  .uniqueResult();
    } catch (Exception e) {
      log.error("Exception in getAuthenticationById() of AuthTempDaoImpl class ", e);
    }
    log.debug("End in getAuthenticationById() of AuthTempDaoImpl class ");
    return auth;
  }

  @SuppressWarnings("deprecation")
  @Override
  public AuthorizationDetails getAuthById(String clientId) {
    log.debug("Start in getAuthById() of AuthTempDaoImpl class ");
    AuthorizationDetails auth = new AuthorizationDetails();
    try {
      Criteria crit =
          getSession()
              .createCriteria(AuthorizationDetails.class)
              .add(Restrictions.eq("clientId", clientId));
      auth = new AuthorizationDetails();
      auth = (AuthorizationDetails) crit.uniqueResult();
    } catch (Exception e) {
      log.error("Exception in getAuthById() of AuthTempDaoImpl class ", e);
    }
    log.debug("End in getAuthById() of AuthTempDaoImpl class ");
    return auth;
  }
}
