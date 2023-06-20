package com.interopx.fhir.auth.server.model;

import java.util.Date;

public class UserResponseMessage {

  private Date timestamp;
  private String messsage;
  private String details;
  private String status;

  public UserResponseMessage() {}

  public UserResponseMessage(Date timestamp, String messsage, String details, String status) {
    super();
    this.timestamp = timestamp;
    this.messsage = messsage;
    this.details = details;
    this.status = status;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getMesssage() {
    return messsage;
  }

  public void setMesssage(String messsage) {
    this.messsage = messsage;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
