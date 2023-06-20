package com.interopx.fhir.auth.server.service;

import com.interopx.fhir.auth.server.model.Jwks;

public interface JwksService {

  public Jwks getById(Integer id);

  public void updateById(Integer id, String jwks);

  public Jwks saveOrUpdate(Jwks jwks);
}
