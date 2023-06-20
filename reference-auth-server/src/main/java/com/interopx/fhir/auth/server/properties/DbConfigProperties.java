package com.interopx.fhir.auth.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("datasource")
public class DbConfigProperties {

  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private String graphPath;

  public String getDriverClassName() {
    return driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getGraphPath() {
    return graphPath;
  }

  public void setGraphPath(String graphPath) {
    this.graphPath = graphPath;
  }
}
