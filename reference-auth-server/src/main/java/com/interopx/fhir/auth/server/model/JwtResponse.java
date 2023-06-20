package com.interopx.fhir.auth.server.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

  private static final long serialVersionUID = -8091879091924046844L;

  private String token;
  private String type = "Bearer";
  private String refreshToken;
  private String username;
  private int expires_in;

  public JwtResponse(String accessToken, String refreshToken, String username, int expires_in) {
    this.token = accessToken;
    this.refreshToken = refreshToken;
    this.username = username;
    this.expires_in = expires_in;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getExpires_in() {
    return expires_in;
  }

  public void setExpires_in(int expires_in) {
    this.expires_in = expires_in;
  }
}
