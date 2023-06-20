package com.interopx.fhir.auth.server.dao.impl;

import com.interopx.fhir.auth.server.dao.AbstractDao;
import com.interopx.fhir.auth.server.dao.RefreshTokenDao;
import com.interopx.fhir.auth.server.model.RefreshToken;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class RefreshTokenDaoImpl extends AbstractDao implements RefreshTokenDao {
  private final Logger log = (Logger) LoggerFactory.getLogger(RefreshTokenDaoImpl.class);

  Session session = null;

  @SuppressWarnings("deprecation")
  @Override
  public RefreshToken findByToken(String token) {
    log.debug("Start in findByToken() of RefreshTokenDaoImpl class ");
    RefreshToken user = null;
    try {
      session = getSession();
      Criteria criteria =
          getSession().createCriteria(RefreshToken.class).add(Restrictions.eq("token", token));
      user = new RefreshToken();
      user = (RefreshToken) criteria.uniqueResult();
    } catch (Exception e) {
      log.error("Exception in findByToken() of RefreshTokenDaoImpl class ");
    }
    log.debug("End in findByToken() of RefreshTokenDaoImpl class ");
    return user;
  }

  @Override
  public String save(RefreshToken refreshToken) {
    log.debug("Start in save() of RefreshTokenDaoImpl class ");
    try {
      session = getSession();
      session.save(refreshToken);
    } catch (Exception e) {
      log.error("Exception in save() of RefreshTokenDaoImpl class ");
    }
    log.debug("End in save() of RefreshTokenDaoImpl class ");
    return refreshToken.getToken();
  }

  @Override
  public int deleteByUser(Integer Id) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void delete(RefreshToken token) {
    // TODO Auto-generated method stub
  }
}
