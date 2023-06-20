package com.interopx.fhir.auth.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserResponse implements Serializable {

  private static final long serialVersionUID = 5356290622191532190L;

  private String userId;
  private String userName;
  private String email;
  private String fullName;
  private String firstName;
  private String lastName;
  private String middleName;
  private String mobileNumber;
  private String patientId;
  private Date lastUpdatedTime;
  private List<UserRole> userRole;
  private String accessToken;
  private boolean isUserActive;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public Date getLastUpdatedTime() {
    return lastUpdatedTime;
  }

  public void setLastUpdatedTime(Date lastUpdatedTime) {
    this.lastUpdatedTime = lastUpdatedTime;
  }

  public List<UserRole> getUserRole() {
    return userRole;
  }

  public void setUserRole(List<UserRole> userRole) {
    this.userRole = userRole;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public boolean isUserActive() {
    return isUserActive;
  }

  public void setUserActive(boolean isUserActive) {
    this.isUserActive = isUserActive;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }
}
