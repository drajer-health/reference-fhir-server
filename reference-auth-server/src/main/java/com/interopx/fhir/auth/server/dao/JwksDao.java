package com.interopx.fhir.auth.server.dao;

import com.interopx.fhir.auth.server.model.Jwks;

public interface JwksDao {

  public Jwks getById(Integer id);

  public void updateById(Integer id, String jwks);

  public Jwks saveOrUpdate(Jwks jwks);
}
