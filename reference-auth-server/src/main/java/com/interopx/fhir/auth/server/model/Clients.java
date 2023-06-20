package com.interopx.fhir.auth.server.model;

import com.interopx.fhir.auth.server.util.AuthUtil.AlgorithmUsed;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "clients")
public class Clients {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "client_id", unique = true)
  private String clientId;

  @Column(name = "client_secret", unique = true)
  private String clientSecret;

  @Column(name = "register_token")
  private String registerToken;

  @Column(name = "name")
  private String name;

  @Column(name = "org_name")
  private String orgName;

  @Column(name = "contact_name")
  private String contactName;

  @Column(name = "contact_mail")
  private String contactMail;

  @Column(name = "scope")
  private String scope;

  @Column(name = "redirect_uri")
  private String redirectUri;

  @Column(name = "status")
  private Boolean status;

  @Column(name = "files")
  private String files;

  @Column(name = "is_backend_client")
  private Boolean isBackendClient;

  @Column(name = "issuer")
  private String issuer;

  @Column(name = "dir_path")
  private String publicKey;

  @Transient private String tokenUrl;

  @Column(name = "launch_id")
  private String launchId;

  @Column(name = "launch_uri")
  private String launchUri;

  @Column(name = "jku")
  private String jku;

  @Column(name = "app_type")
  private String appType;

  @Column(name = "is_dynamic_client")
  private boolean isDynamicClient;

  @Column(name = "is_authorized")
  private boolean isAuthorized;

  @Column(name = "is_confidential_client")
  private boolean isConfidentialClient;

  @Column(name = "token_endpoint")
  private String tokenEndPoint;

  @Enumerated(EnumType.STRING)
  @Column(name = "approved_status")
  private ApprovedStatus approvedStatus;

  @Column(name = "algorithm_used")
  private AlgorithmUsed algorithmUsed;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "user_email")
  private String userEmail;

  @Column(name = "patient_id")
  private String patientId;

  @Column(name = "review_comments")
  private String reviewComments;

  @Column(name = "scope_type")
  private String scopeType;

  @Column(name = "customerId")
  private String customerId;

  @Column(name = "centerId")
  private String centerId;

  public String getTokenEndPoint() {
    return tokenEndPoint;
  }

  public void setTokenEndPoint(String tokenEndPoint) {
    this.tokenEndPoint = tokenEndPoint;
  }

  public boolean isDynamicClient() {
    return isDynamicClient;
  }

  public void setDynamicClient(boolean isDynamicClient) {
    this.isDynamicClient = isDynamicClient;
  }

  public String getAppType() {
    return appType;
  }

  public void setAppType(String appType) {
    this.appType = appType;
  }

  public Boolean getIsBackendClient() {
    return isBackendClient;
  }

  public void setIsBackendClient(Boolean isBackendClient) {
    this.isBackendClient = isBackendClient;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getRegisterToken() {
    return registerToken;
  }

  public void setRegisterToken(String registerToken) {
    this.registerToken = registerToken;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getContactMail() {
    return contactMail;
  }

  public void setContact_mail(String contactMail) {
    this.contactMail = contactMail;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public String getFiles() {
    return files;
  }

  public void setFiles(String files) {
    this.files = files;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getTokenUrl() {
    return tokenUrl;
  }

  public void setTokenUrl(String tokenUrl) {
    this.tokenUrl = tokenUrl;
  }

  public String getLaunchId() {
    return launchId;
  }

  public void setLaunchId(String launchId) {
    this.launchId = launchId;
  }

  public String getLaunchUri() {
    return launchUri;
  }

  public void setLaunchUri(String launchUri) {
    this.launchUri = launchUri;
  }

  public String getJku() {
    return jku;
  }

  public void setJku(String jku) {
    this.jku = jku;
  }

  public boolean isAuthorized() {
    return isAuthorized;
  }

  public void setAuthorized(boolean isAuthorized) {
    this.isAuthorized = isAuthorized;
  }

  public ApprovedStatus getApprovedStatus() {
    return approvedStatus;
  }

  public void setApprovedStatus(ApprovedStatus approvedStatus) {
    this.approvedStatus = approvedStatus;
  }

  public AlgorithmUsed getAlgorithmUsed() {
    return algorithmUsed;
  }

  public void setAlgorithmUsed(AlgorithmUsed algorithmUsed) {
    this.algorithmUsed = algorithmUsed;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getReviewComments() {
    return reviewComments;
  }

  public void setReviewComments(String reviewComments) {
    this.reviewComments = reviewComments;
  }

  public boolean isConfidentialClient() {
    return isConfidentialClient;
  }

  public void setConfidentialClient(boolean isConfidentialClient) {
    this.isConfidentialClient = isConfidentialClient;
  }

  public String getScopeType() {
    return scopeType;
  }

  public void setScopeType(String scopeType) {
    this.scopeType = scopeType;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getCenterId() {
    return centerId;
  }

  public void setCenterId(String centerId) {
    this.centerId = centerId;
  }
}
