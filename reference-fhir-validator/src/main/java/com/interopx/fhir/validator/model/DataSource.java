package com.interopx.fhir.validator.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DataSource implements Serializable {

  private static final long serialVersionUID = -322930125952074273L;

  private Integer dataSourceId;

  private String dataSourceName;

  private String endPointUrl;

  private Boolean isSecure;

  private String securityMethod;

  private Integer connectorId;

  private String credentials;

  private String ehrAdminEmail;

  private String databaseServer;

  private String ehrAdminContact;

  private Date lastUpdated;

  private Boolean isProvider;

  private List<String> patientId;

  private Boolean isLinked;

  private String startCommand;

  private String stopCommand;

  private String datasourceConfigValues;

  private String fhirVersion;

  private String ccdaExtractionType;

  private String awsAccessKey;

  private String awsSecretKey;

  private String awsBucketName;

  private String awsRegion;

  private String ccdaPushedType;

  private String emrVersion;

  private String adaptorVersion;

  private String modality;

  /** The ccda File Path. */
  private String connectorFilePath;

  private String azureConnectionString;

  private String azureContainerName;

  public String getConnectorFilePath() {
    return connectorFilePath;
  }

  public void setConnectorFilePath(String connectorFilePath) {
    this.connectorFilePath = connectorFilePath;
  }

  public String getCcdaExtractionType() {
    return ccdaExtractionType;
  }

  public void setCcdaExtractionType(String ccdaExtractionType) {
    this.ccdaExtractionType = ccdaExtractionType;
  }

  public String getAwsAccessKey() {
    return awsAccessKey;
  }

  public void setAwsAccessKey(String awsAccessKey) {
    this.awsAccessKey = awsAccessKey;
  }

  public String getAwsSecretKey() {
    return awsSecretKey;
  }

  public void setAwsSecretKey(String awsSecretKey) {
    this.awsSecretKey = awsSecretKey;
  }

  public String getAwsBucketName() {
    return awsBucketName;
  }

  public void setAwsBucketName(String awsBucketName) {
    this.awsBucketName = awsBucketName;
  }

  public String getAwsRegion() {
    return awsRegion;
  }

  public void setAwsRegion(String awsRegion) {
    this.awsRegion = awsRegion;
  }

  public Integer getDataSourceId() {
    return dataSourceId;
  }

  public void setDataSourceId(Integer dataSourceId) {
    this.dataSourceId = dataSourceId;
  }

  public String getDataSourceName() {
    return dataSourceName;
  }

  public void setDataSourceName(String dataSourceName) {
    this.dataSourceName = dataSourceName;
  }

  public String getEndPointUrl() {
    return endPointUrl;
  }

  public void setEndPointUrl(String endPointUrl) {
    this.endPointUrl = endPointUrl;
  }

  public Boolean getIsSecure() {
    return isSecure;
  }

  public void setIsSecure(Boolean isSecure) {
    this.isSecure = isSecure;
  }

  public String getSecurityMethod() {
    return securityMethod;
  }

  public void setSecurityMethod(String securityMethod) {
    this.securityMethod = securityMethod;
  }

  public Integer getConnectorId() {
    return connectorId;
  }

  public void setConnectorId(Integer connectorId) {
    this.connectorId = connectorId;
  }

  public String getCredentials() {
    return credentials;
  }

  public void setCredentials(String credentials) {
    this.credentials = credentials;
  }

  public String getEhrAdminEmail() {
    return ehrAdminEmail;
  }

  public void setEhrAdminEmail(String ehrAdminEmail) {
    this.ehrAdminEmail = ehrAdminEmail;
  }

  public String getDatabaseServer() {
    return databaseServer;
  }

  public void setDatabaseServer(String databaseServer) {
    this.databaseServer = databaseServer;
  }

  public String getEhrAdminContact() {
    return ehrAdminContact;
  }

  public void setEhrAdminContact(String ehrAdminContact) {
    this.ehrAdminContact = ehrAdminContact;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public Boolean getIsProvider() {
    return isProvider;
  }

  public void setIsProvider(Boolean isProvider) {
    this.isProvider = isProvider;
  }

  public List<String> getPatientId() {
    return patientId;
  }

  public void setPatientId(List<String> patientId) {
    this.patientId = patientId;
  }

  public Boolean getIsLinked() {
    return isLinked;
  }

  public void setIsLinked(Boolean isLinked) {
    this.isLinked = isLinked;
  }

  public String getStartCommand() {
    return startCommand;
  }

  public void setStartCommand(String startCommand) {
    this.startCommand = startCommand;
  }

  public String getStopCommand() {
    return stopCommand;
  }

  public void setStopCommand(String stopCommand) {
    this.stopCommand = stopCommand;
  }

  public String getDatasourceConfigValues() {
    return datasourceConfigValues;
  }

  public void setDatasourceConfigValues(String datasourceConfigValues) {
    this.datasourceConfigValues = datasourceConfigValues;
  }

  public String getFhirVersion() {
    return fhirVersion;
  }

  public void setFhirVersion(String fhirVersion) {
    this.fhirVersion = fhirVersion;
  }

  public String getCcdaPushedType() {
    return ccdaPushedType;
  }

  public void setCcdaPushedType(String ccdaPushedType) {
    this.ccdaPushedType = ccdaPushedType;
  }

  public String getAzureConnectionString() {
    return azureConnectionString;
  }

  public void setAzureConnectionString(String azureConnectionString) {
    this.azureConnectionString = azureConnectionString;
  }

  public String getAzureContainerName() {
    return azureContainerName;
  }

  public void setAzureContainerName(String azureContainerName) {
    this.azureContainerName = azureContainerName;
  }

  public String getEmrVersion() {
    return emrVersion;
  }

  public void setEmrVersion(String emrVersion) {
    this.emrVersion = emrVersion;
  }

  public String getAdaptorVersion() {
    return adaptorVersion;
  }

  public void setAdaptorVersion(String adaptorVersion) {
    this.adaptorVersion = adaptorVersion;
  }

  public String getModality() {
    return modality;
  }

  public void setModality(String modality) {
    this.modality = modality;
  }
}
