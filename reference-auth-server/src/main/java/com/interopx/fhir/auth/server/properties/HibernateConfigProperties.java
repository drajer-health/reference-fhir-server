package com.interopx.fhir.auth.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("hibernate")
public class HibernateConfigProperties {

  private String dialect;
  private String formatSql;
  private String hbm2ddlAuto;
  private String showSql;
  private String packagesScan;

  public String getDialect() {
    return dialect;
  }

  public void setDialect(String dialect) {
    this.dialect = dialect;
  }

  public String getFormatSql() {
    return formatSql;
  }

  public void setFormatSql(String formatSql) {
    this.formatSql = formatSql;
  }

  public String getHbm2ddlAuto() {
    return hbm2ddlAuto;
  }

  public void setHbm2ddlAuto(String hbm2ddlAuto) {
    this.hbm2ddlAuto = hbm2ddlAuto;
  }

  public String getShowSql() {
    return showSql;
  }

  public void setShowSql(String showSql) {
    this.showSql = showSql;
  }

  public String getPackagesScan() {
    return packagesScan;
  }

  public void setPackagesScan(String packagesScan) {
    this.packagesScan = packagesScan;
  }
}
