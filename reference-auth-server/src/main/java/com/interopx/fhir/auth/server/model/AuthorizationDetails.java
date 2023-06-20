package com.interopx.fhir.auth.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "auth_details")
public class AuthorizationDetails {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "client_secret")
	private String clientSecret;

	@Column(name = "auth_code")
	private String authCode;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "aud")
	private String aud;

	@Column(name = "issued_at")
	private String issuedAt;

	@Column(name = "expiry")
	private String expiry;

	@Column(name = "scope")
	private String scope;

	@Column(name = "state")
	private String state;

	@Column(name = "redirect_uri")
	private String redirectUri;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "launch_patient_id")
	private String launchPatientId;

	@Column(name = "refresh_token_expiry_time")
	private String refreshTokenExpiryTime;

	@Column(name = "code_challenge")
	private String codeChallenge;

	@Column(name = "code_challenge_method")
	private String codeChallengeMethod;

	public String getRefreshTokenExpiryTime() {
		return refreshTokenExpiryTime;
	}

	public void setRefreshTokenExpiryTime(String refreshTokenExpiryTime) {
		this.refreshTokenExpiryTime = refreshTokenExpiryTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAud() {
		return aud;
	}

	public void setAud(String aud) {
		this.aud = aud;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getLaunchPatientId() {
		return launchPatientId;
	}

	public void setLaunchPatientId(String launchPatientId) {
		this.launchPatientId = launchPatientId;
	}

	public String getCodeChallenge() {
		return codeChallenge;
	}

	public void setCodeChallenge(String codeChallenge) {
		this.codeChallenge = codeChallenge;
	}

	public String getCodeChallengeMethod() {
		return codeChallengeMethod;
	}

	public void setCodeChallengeMethod(String codeChallengeMethod) {
		this.codeChallengeMethod = codeChallengeMethod;
	}

	public String getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(String issuedAt) {
		this.issuedAt = issuedAt;
	}

}
