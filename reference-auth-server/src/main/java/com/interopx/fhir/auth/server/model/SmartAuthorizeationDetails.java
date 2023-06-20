package com.interopx.fhir.auth.server.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "smart_auth_details")
public class SmartAuthorizeationDetails {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Type(type = "list-array")
  @Column(name = "response_types", columnDefinition = "text[]")
  private List<String> responseTypes;

  @Type(type = "list-array")
  @Column(name = "subject_types", columnDefinition = "text[]")
  private List<String> subjectTypes;

  @Type(type = "list-array")
  @Column(name = "claims_supported", columnDefinition = "text[]")
  private List<String> claims;

  @Type(type = "list-array")
  @Column(name = "algorithm_types", columnDefinition = "text[]")
  private List<String> algorithmTypes;

  @Column(name = "is_claims_parameter_supported")
  private Boolean isClaimsParamSupported;

  @Column(name = "is_request_parameter_supported")
  private Boolean isRequestParamSupported;

  @Column(name = "is_request_uri_parameter_supported")
  private Boolean isRequestUriParamSupported;

  @Column(name = "is_require_request_uri_registration")
  private Boolean isRequestUriRegistration;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<String> getResponseTypes() {
    return responseTypes;
  }

  public void setResponseTypes(List<String> responseTypes) {
    this.responseTypes = responseTypes;
  }

  public List<String> getSubjectTypes() {
    return subjectTypes;
  }

  public void setSubjectTypes(List<String> subjectTypes) {
    this.subjectTypes = subjectTypes;
  }

  public List<String> getClaims() {
    return claims;
  }

  public void setClaims(List<String> claims) {
    this.claims = claims;
  }

  public List<String> getAlgorithmTypes() {
    return algorithmTypes;
  }

  public void setAlgorithmTypes(List<String> algorithmTypes) {
    this.algorithmTypes = algorithmTypes;
  }

  public Boolean getIsClaimsParamSupported() {
    return isClaimsParamSupported;
  }

  public void setIsClaimsParamSupported(Boolean isClaimsParamSupported) {
    this.isClaimsParamSupported = isClaimsParamSupported;
  }

  public Boolean getIsRequestParamSupported() {
    return isRequestParamSupported;
  }

  public void setIsRequestParamSupported(Boolean isRequestParamSupported) {
    this.isRequestParamSupported = isRequestParamSupported;
  }

  public Boolean getIsRequestUriParamSupported() {
    return isRequestUriParamSupported;
  }

  public void setIsRequestUriParamSupported(Boolean isRequestUriParamSupported) {
    this.isRequestUriParamSupported = isRequestUriParamSupported;
  }

  public Boolean getIsRequestUriRegistration() {
    return isRequestUriRegistration;
  }

  public void setIsRequestUriRegistration(Boolean isRequestUriRegistration) {
    this.isRequestUriRegistration = isRequestUriRegistration;
  }
}
