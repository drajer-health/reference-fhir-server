package com.interopx.fhir.auth.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserDto implements Serializable {

  private static final long serialVersionUID = 1L;
  private String userId;
  private String userName;
  private String email;
  private String fullName;
  private String firstName;
  private String lastName;
  private String middleName;
  private String emailActivationKey;
  private String password;
  private String mobileNumber;
  private String patientId;
  private Date lastUpdatedTime;
  private String token;
  private List<String> userRole;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public String getEmailActivationKey() {
    return emailActivationKey;
  }

  public void setEmailActivationKey(String emailActivationKey) {
    this.emailActivationKey = emailActivationKey;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public boolean isUserActive() {
    return isUserActive;
  }

  public void setUserActive(boolean isUserActive) {
    this.isUserActive = isUserActive;
  }

  public List<String> getUserRole() {
    return userRole;
  }

  public void setUserRole(List<String> userRole) {
    this.userRole = userRole;
  }
}
