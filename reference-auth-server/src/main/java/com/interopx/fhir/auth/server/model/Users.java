package com.interopx.fhir.auth.server.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "sof_app_users")
public class Users {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String user_id;

  @Column(name = "user_name", unique = true)
  private String user_name;

  @Column(name = "user_first_name")
  private String firstName;

  @Column(name = "user_last_name")
  private String lastName;

  @Column(name = "user_middle_name")
  private String middleName;

  @Column(name = "user_email", unique = true)
  private String user_email;

  @Column(name = "is_email_activated")
  private boolean isEmailActivated;

  @Column(name = "email_activation_key")
  private String emailActivationKey;

  @Column(name = "user_password")
  private String user_password;

  @Column(name = "user_mobile_number")
  private String mobile_number;

  @Column(name = "patient_id")
  private String patient_id;

  @Column(name = "last_updated_ts")
  private Date last_updated_ts;

  @Type(type = "list-array")
  @JoinColumn(name = "user_id", columnDefinition = "text[]")
  private List<String> user_role;

  @Column(name = "is_user_active")
  private boolean isUserActive;

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
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

  public String getUser_email() {
    return user_email;
  }

  public void setUser_email(String user_email) {
    this.user_email = user_email;
  }

  public boolean isEmailActivated() {
    return isEmailActivated;
  }

  public void setEmailActivated(boolean isEmailActivated) {
    this.isEmailActivated = isEmailActivated;
  }

  public String getEmailActivationKey() {
    return emailActivationKey;
  }

  public void setEmailActivationKey(String emailActivationKey) {
    this.emailActivationKey = emailActivationKey;
  }

  public String getUser_password() {
    return user_password;
  }

  public void setUser_password(String user_password) {
    this.user_password = user_password;
  }

  public String getMobile_number() {
    return mobile_number;
  }

  public void setMobile_number(String mobile_number) {
    this.mobile_number = mobile_number;
  }

  public String getPatient_id() {
    return patient_id;
  }

  public void setPatient_id(String patient_id) {
    this.patient_id = patient_id;
  }

  public Date getLast_updated_ts() {
    return last_updated_ts;
  }

  public void setLast_updated_ts(Date last_updated_ts) {
    this.last_updated_ts = last_updated_ts;
  }

  public List<String> getUserRole() {
    return user_role;
  }

  public void setUserRole(List<String> user_role) {
    this.user_role = user_role;
  }

  public boolean isUserActive() {
    return isUserActive;
  }

  public void setUserActive(boolean isUserActive) {
    this.isUserActive = isUserActive;
  }
}
