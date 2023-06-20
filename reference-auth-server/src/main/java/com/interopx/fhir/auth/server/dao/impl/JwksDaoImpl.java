package com.interopx.fhir.auth.server.dao.impl;

import com.interopx.fhir.auth.server.dao.AbstractDao;
import com.interopx.fhir.auth.server.dao.JwksDao;
import com.interopx.fhir.auth.server.model.Jwks;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("JwksDao")
public class JwksDaoImpl extends AbstractDao implements JwksDao {
  Session session = null;
  final Logger log = (Logger) LoggerFactory.getLogger(JwksDaoImpl.class);

  @Override
  public Jwks getById(Integer id) {
    log.debug("Start in getById() of JwksDaoImpl class ");
    Jwks jwks = null;
    try {
      jwks = (Jwks) getSession().get(Jwks.class, id);
    } catch (Exception e) {
      log.error("Exception in getById() of JwksDaoImpl class ");
    }
    log.debug("End in getById() of JwksDaoImpl class ");
    return jwks;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void updateById(Integer id, String jwkString) {
    log.debug("Start in updateById() of JwksDaoImpl class ");
    try {
      Criteria criteria = getSession().createCriteria(Jwks.class).add(Restrictions.eq("id", id));
      Jwks jwks = (Jwks) criteria.uniqueResult();
      jwks.setJwk(jwkString);
      getSession().update(jwks);
    } catch (Exception e) {
      log.error("Exception in updateById() of JwksDaoImpl class ");
    }
    log.debug("End in updateById() of JwksDaoImpl class ");
  }

  @Override
  public Jwks saveOrUpdate(Jwks jwks) {
    log.debug("Start in saveOrUpdate() of JwksDaoImpl class ");
    Jwks jwks1 = null;
    try {
      jwks1 = getById(jwks.getId());
      if (jwks1 != null) {
        jwks1.setJwk(jwks.getJwk());
        jwks1.setLastUpdatedDatetime(jwks.getLastUpdatedDatetime());
      }
    } catch (Exception e) {
      log.error("Exception in saveOrUpdate() of JwksDaoImpl class ");
    }
    log.debug("End in saveOrUpdate() of JwksDaoImpl class ");
    return jwks1;
  }
}
