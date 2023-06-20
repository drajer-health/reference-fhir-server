package com.interopx.fhir.auth.server.dao;

import com.interopx.fhir.auth.server.model.RefreshToken;

public interface RefreshTokenDao {
  RefreshToken findByToken(String token);

  String save(RefreshToken refreshToken);

  int deleteByUser(Integer Id);

  void delete(RefreshToken token);
}
