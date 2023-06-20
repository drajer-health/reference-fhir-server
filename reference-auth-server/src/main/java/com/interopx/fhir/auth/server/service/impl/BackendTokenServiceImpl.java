package com.interopx.fhir.auth.server.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.interopx.fhir.auth.server.exception.AuthServerException;
import com.interopx.fhir.auth.server.jwtservice.JWKSCache;
import com.interopx.fhir.auth.server.jwtservice.JWTStoredKeyService;
import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.service.AuthorizationDetailsService;
import com.interopx.fhir.auth.server.service.BackendTokenService;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.util.CommonUtil;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ch.qos.logback.classic.Logger;

@Service
public class BackendTokenServiceImpl implements BackendTokenService {

	Logger log = (Logger) LoggerFactory.getLogger(BackendTokenServiceImpl.class);

	@Autowired
	private ClientsService clientsService;

	@Autowired
	private CommonUtil commonutil;

	@Autowired
	private AuthorizationDetailsService authorizationDetailsService;

	@Autowired
	private JWKSCache validationServices;

	@Value("${token.expiration-time}")
	private Integer tokenExpirationTime;

	@Override
	public Map<String, String> getAccessTokenResponse(Map<String, String> formData, Map<String, String> headerMap)
			throws Exception {
		validateHeader(headerMap);

		String requestedScope = formData.get("scope");

		JWT jsonWebToken = validateRequestAndGetJWT(formData);

		Clients client = verifyAndGetClientDetails(requestedScope, jsonWebToken);

		String accessToken = validateSignatureAndGetAccessToken(jsonWebToken, client.getJku());

		Map<String, String> accessTokenResponse = new HashMap<>();
		accessTokenResponse.put("access_token", accessToken);
		accessTokenResponse.put("token_type", "bearer");
		accessTokenResponse.put("expires_in", tokenExpirationTime.toString());

		saveAuthorizationDetails(requestedScope, client, accessToken);
		return accessTokenResponse;
	}

	private void validateHeader(Map<String, String> headerMap) {
		if (!(headerMap.get("content-type") != null
				&& headerMap.get("content-type").contains("application/x-www-form-urlencoded")
				&& headerMap.get("accept") != null && headerMap.get("accept").equals("application/json"))) {
			log.error("Error code 400 : incorrect headers or content type");
			throw new InvalidRequestException("Incorrect headers. Check Content-Type or Accept");
		}
	}

	@SuppressWarnings("unused")
	private JWT validateRequestAndGetJWT(Map<String, String> formData) throws Exception {

		if (StringUtils.isEmpty(formData.get("grant_type"))
				|| !(formData.get("grant_type").equals(GrantType.CLIENT_CREDENTIALS.toString()))) {
			log.error("Error code 400 : Invalid grant");
			throw new InvalidRequestException("grant_type is either not present or may not be valid");
		} else if (StringUtils.isEmpty(formData.get("client_assertion"))) {
			log.error("Error code 400 : client_assertion is not present");
			throw new InvalidRequestException("client_assertion is either not present or may not be valid");
		} else if (StringUtils.isEmpty(formData.get("client_assertion_type")) || !(formData.get("client_assertion_type")
				.equals("urn:ietf:params:oauth:client-assertion-type:jwt-bearer"))) {
			log.error("Error code 400 : client_assertion_type is either not present or may not be valid ");
			throw new InvalidRequestException("client_assertion_type is either not present or may not be valid");
		} else if (StringUtils.isEmpty(formData.get("scope"))) {
			log.error("Error code 400 : scope is null ");
			throw new InvalidRequestException("scope is not present");
		}
		try {
			JWT jsonWebToken = JWTParser.parse(formData.get("client_assertion"));

			if (!commonutil.isValid(jsonWebToken)) {
				log.error("Error code 400 : Invalid jsonWebToken");
				throw new InvalidRequestException("Invalid jsonWebToken");
			}
			return jsonWebToken;
		} catch (ParseException e) {
			log.error("Error code 401 : Invalid client_assertion");
			throw new AuthServerException(401, "Invalid client_assertion");
		}
	}

	private Clients verifyAndGetClientDetails(String requestedScope, JWT jsonWebToken) throws Exception {

		JWTClaimsSet idClaims = jsonWebToken.getJWTClaimsSet();
		String clientIdFromJWT = idClaims.getSubject();
		Clients regClient = clientsService.getClient(clientIdFromJWT);
		String scopes = regClient.getScope().replaceAll("\\s+", ",");
		List<String> registeredScopes = Arrays.asList(scopes.split(","));
		if (!regClient.getApprovedStatus().toString().equals("APPROVED")) {
			log.error("Error code 401 : Client is not APPROVED please contact admin");
			throw new AuthServerException(401, "Client is not APPROVED please contact admin");
		}
		if (!commonutil.isValidScopes(requestedScope, registeredScopes)) {
			log.error("Error code 400 : scope is either not present or may not be valid. ");
			throw new InvalidRequestException("Invalid Scope");
		}
		return regClient;
	}

	@SuppressWarnings("unused")
	private String validateSignatureAndGetAccessToken(JWT jsonWebToken, String publicKeyUrl) throws Exception {

		SignedJWT signedJWT = (SignedJWT) jsonWebToken;
		JWTStoredKeyService jWTStoredKeyService = validationServices.getJWTStoredKeyService(publicKeyUrl);

		if (!jWTStoredKeyService.validateSignature(signedJWT)) {
			log.error("Error code 401 : Invalid signature in the client_assertion");
			throw new AuthServerException(401, "Invalid signature in the client_assertion");
		} else {
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
			final String accessToken = oauthIssuerImpl.accessToken();
			return accessToken;
		}
	}

	private void saveAuthorizationDetails(String requestedScope, Clients regClient, final String accessToken) {
		AuthorizationDetails authorizationDetails = new AuthorizationDetails();
		authorizationDetails.setClientId(regClient.getClientId());
		authorizationDetails.setScope(requestedScope);
		authorizationDetails.setAccessToken(accessToken);
		String issuesTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Timestamp(System.currentTimeMillis()));
		authorizationDetails.setIssuedAt(issuesTimeStamp);
		String expiryTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Timestamp(System.currentTimeMillis() + (tokenExpirationTime * 1000)));
		authorizationDetails.setExpiry(expiryTimeStamp);
		authorizationDetailsService.saveOrUpdate(authorizationDetails);
	}

}
