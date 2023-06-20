package com.interopx.fhir.auth.server.model;

public class TokenRefreshResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType = "Bearer";
  private int expires_in;

  public TokenRefreshResponse(String accessToken, String refreshToken, int expires_in) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expires_in = expires_in;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String token) {
    this.accessToken = token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public int getExpires_in() {
    return expires_in;
  }

  public void setExpires_in(int expires_in) {
    this.expires_in = expires_in;
  }
}
