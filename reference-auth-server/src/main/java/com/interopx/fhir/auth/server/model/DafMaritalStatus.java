package com.interopx.fhir.auth.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** Created by Prabhushankar.Byrapp on 8/23/2015. */
@Entity
@Table(name = "marital_status")
public class DafMaritalStatus {

  @Id
  @Column(name = "code")
  private String code;

  @Column(name = "display")
  private String display;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }
}
