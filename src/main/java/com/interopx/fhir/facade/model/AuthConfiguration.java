package com.interopx.fhir.facade.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="auth_configuration")
public class AuthConfiguration {
	@Id	
	@Column(name="auth_config_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer authConfigId;
	
	@Column(name="authorization_endpoint_url")
	private String authorizationEndpointUrl;
	
	@Column(name="token_endpoint_url")
	private String tokenEndpointUrl;
	
	@Column(name="introspection_endpoint_url")
	private String introspectionEndpointUrl;
	
	@Column(name="client_id")
	private String clientId;
	
	@Column(name="client_secret")
	private String clientSecret;
	
	@Column(name="jwks_uri")
	private String jwksUri;
	
	public Integer getAuthConfigId() {
		return authConfigId;
	}

	public void setAuthConfigId(Integer authConfigId) {
		this.authConfigId = authConfigId;
	}

	public String getAuthorizationEndpointUrl() {
		return authorizationEndpointUrl;
	}

	public void setAuthorizationEndpointUrl(String authorizationEndpointUrl) {
		this.authorizationEndpointUrl = authorizationEndpointUrl;
	}

	public String getTokenEndpointUrl() {
		return tokenEndpointUrl;
	}

	public void setTokenEndpointUrl(String tokenEndpointUrl) {
		this.tokenEndpointUrl = tokenEndpointUrl;
	}

	public String getIntrospectionEndpointUrl() {
		return introspectionEndpointUrl;
	}

	public void setIntrospectionEndpointUrl(String introspectionEndpointUrl) {
		this.introspectionEndpointUrl = introspectionEndpointUrl;
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

	public String getJwksUri() {
		return jwksUri;
	}

	public void setJwksUri(String jwksUri) {
		this.jwksUri = jwksUri;
	}
}
