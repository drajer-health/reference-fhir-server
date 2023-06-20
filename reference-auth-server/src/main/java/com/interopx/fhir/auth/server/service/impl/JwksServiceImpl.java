package com.interopx.fhir.auth.server.service.impl;

import com.interopx.fhir.auth.server.dao.JwksDao;
import com.interopx.fhir.auth.server.model.Jwks;
import com.interopx.fhir.auth.server.service.JwksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("JwksService")
public class JwksServiceImpl implements JwksService {

  @Autowired private JwksDao jwksDao;

  @Override
  public Jwks getById(Integer id) {
    return jwksDao.getById(id);
  }

  @Override
  public void updateById(Integer id, String jwks) {
    jwksDao.updateById(id, jwks);
  }

  @Override
  public Jwks saveOrUpdate(Jwks jwks) {
    return jwksDao.saveOrUpdate(jwks);
  }
}
