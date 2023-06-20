package com.interopx.fhir.auth.server.controller;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interopx.fhir.auth.server.exception.AuthServerException;
import com.interopx.fhir.auth.server.jwtservice.JWKSCache;
import com.interopx.fhir.auth.server.jwtservice.JWTStoredKeyService;
import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.service.AuthorizationDetailsService;
import com.interopx.fhir.auth.server.service.BackendTokenService;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.util.CommonUtil;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ch.qos.logback.classic.Logger;

@Controller
@RequestMapping("/api/backendservice")
@ResponseBody
public class BackendTokenEndpoint extends HttpServlet {

	Logger log = (Logger) LoggerFactory.getLogger(BackendTokenEndpoint.class);

	private static final long serialVersionUID = 1L;

//	@Autowired
//	private ClientsService clientsService;
//
//	@Autowired
//	private CommonUtil commonutil;
//
//	@Autowired
//	private AuthorizationDetailsService authorizationDetailsService;
//
//	@Autowired
//	private JWKSCache validationServices;

	@Autowired
	BackendTokenService backendTokenService;
	
	@SuppressWarnings("unused")
	@PostMapping(value = "/token")
	public ResponseEntity<Map<String, String>> getAuthorization(HttpServletRequest request,
			@RequestParam Map<String, String> formData) throws Exception {

		Map<String, String> headerMap = new HashMap<String, String>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			headerMap.put(key.toLowerCase(), value);
		}

		Map<String, String> accessTokenResponse = backendTokenService.getAccessTokenResponse(formData, headerMap);

		return new ResponseEntity<>(accessTokenResponse, HttpStatus.OK);
	}

//	@Value("${token.expiration-time}")
//	private Integer tokenExpirationTime;

//	@RequestMapping(value = "/token", method = RequestMethod.POST)
//	public void getAuthorization(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		Map<String, String> headerMap = new HashMap<String, String>();
//
//		Enumeration headerNames = request.getHeaderNames();
//		while (headerNames.hasMoreElements()) {
//			String key = (String) headerNames.nextElement();
//			String value = request.getHeader(key);
//			headerMap.put(key.toLowerCase(), value);
//		}
////validate data
//		if (headerMap.get("content-type") != null && headerMap.get("accept") != null) {
//			if (headerMap.get("content-type").contains("application/x-www-form-urlencoded")
//					&& headerMap.get("accept").equals("application/json")) {
//
//				PrintWriter out = response.getWriter();
//				response.setContentType("application/json");
//
//				String client_secret = null;
//				String token = null;
//				String grant_type = null;
//				String requestedScope = null;
//				String client_assertion_type = null;
////				String client_id = null;
//				String client_assertion = null;
//				if (request.getParameter("client_assertion_type") != null) {
//					grant_type = request.getParameter("grant_type");
//					requestedScope = request.getParameter("scope");
//					client_assertion = request.getParameter("client_assertion");
//					client_assertion_type = request.getParameter("client_assertion_type");
////					client_id = request.getParameter("client_id");
//
//				} else {
//					String paramReaderString = IOUtils.toString(request.getReader());
//					try {
//						String sb = java.net.URLDecoder.decode(paramReaderString, StandardCharsets.UTF_8.name());
//
//						if (!sb.toString().isEmpty() && sb.toString() != null) {
//
//							if (sb.toString().contains("client_assertion")) {
//
//								log.debug(" Found Client assertion ");
//								String[] params = sb.toString().split("&");
//
//								for (String p : params) {
//									String[] pair = p.split("=");
//									if ("client_assertion".equalsIgnoreCase(pair[0])) {
//										client_assertion = pair[1];
//									}
//
//									if ("grant_type".equalsIgnoreCase(pair[0])) {
//										grant_type = pair[1];
//									}
//
//									if ("client_assertion_type".equalsIgnoreCase(pair[0]) && (pair.length > 1)) {
//										client_assertion_type = pair[1];
//									}
//
//									if ("scope".equalsIgnoreCase(pair[0]) && (pair.length > 1)) {
//										requestedScope = pair[1];
//									}
//								}
//							}
//						}
//					} catch (UnsupportedEncodingException e) {
//						log.warn("UnsupportedEncodingException occured ", e);
//					}
//				}
//
//				if (grant_type == null || !(grant_type.equals(GrantType.CLIENT_CREDENTIALS.toString()))) {
//					log.error("Error code 400 : grant_type is not present");
//					response.sendError(400, "grant_type is not present");// invalid grant
//					// } else if (grant_type.equals(GrantType.CLIENT_CREDENTIALS.toString())) {
//				} else if (client_assertion == null) {
//					log.error("Error code 400 : client_assertion is not present");
//					response.sendError(400, "client_assertion is not present");
//				} else if (client_assertion_type == null
//						|| !(client_assertion_type.equals("urn:ietf:params:oauth:client-assertion-type:jwt-bearer"))) {
//					log.error("Error code 400 : client_assertion_type is either not present or may not be valid ");
//					response.sendError(400, "client_assertion_type is either not present or may not be valid");
//
//				} else if (requestedScope == null) {
//					log.error("Error code 400 : scope is null ");
//					response.sendError(400, "scope is null ");
//				} else {
//					JWT jsonWebToken = null;
//					try {
//						jsonWebToken = JWTParser.parse(client_assertion);
//					} catch (Exception ex) {
//						log.error("Error code 400 : JSON Web Token is invalid ");
//						response.sendError(400, "JSON Web Token is invalid");
//						return;
//					}
//					if (jsonWebToken != null && commonutil.isValid(jsonWebToken)) {
//						Algorithm tokenAlg = jsonWebToken.getHeader().getAlgorithm(); // check later
//						SignedJWT signedJWT = (SignedJWT) jsonWebToken;
//						// validate our ID Token over a number of tests
//						JWTClaimsSet idClaims = jsonWebToken.getJWTClaimsSet();
//						String clientIdFromJWT = idClaims.getSubject();
//						Clients regClient = clientsService.getClient(clientIdFromJWT);
//						String scopes = regClient.getScope().replaceAll("\\s+", ",");
//						List<String> registeredScopes = Arrays.asList(scopes.split(","));
//						if (!commonutil.isValidScopes(requestedScope, registeredScopes)) {
//							log.error("Error code 400 : scope is either not present or may not be valid. ");
//							response.sendError(400, "scope is either not present or may not be valid. ");
//						} else {
//							JWTStoredKeyService jWTStoredKeyService = null;
//							try {
//								jWTStoredKeyService = validationServices.loadPublicKey(clientIdFromJWT);
//							} catch (AuthServerException e) {
//								log.info("Exception while loading public key");
//								response.sendError(e.getStatus(), e.getMessage());
//								return;
//							}
//
//							if (jWTStoredKeyService != null) {
//								if (!jWTStoredKeyService.validateSignature(signedJWT)) {
//									log.error("Error code 401 : public key could not validate the JWT");
//									response.sendError(401, "public key could not validate the JWT");
//								} else {
//									if (regClient != null) {
//										if (regClient.getApprovedStatus().toString().equals("APPROVED")) {
//
//											OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
//											final String accessToken = oauthIssuerImpl.accessToken();
//
//											saveAuthorizationDetails(requestedScope, regClient, accessToken);
//
//											JSONObject accessTokenResponse = new JSONObject();
//											accessTokenResponse.put("access_token", accessToken);
//											accessTokenResponse.put("token_type", "bearer");
//											accessTokenResponse.put("expires_in", tokenExpirationTime);
////											accessTokenResponse.put("scope", requestedScope);
////											response.addHeader("Content-Type", "application/json");
//											out.println(accessTokenResponse.toString());
//										} else {
//											log.error("Error code 401 : Client is not APPROVED please contact admin");
//											response.sendError(401, "Client is not APPROVED please contact admin");
//										}
//									} else {
//										log.error("Error code 401 : client id doesn't match with registerd one");
//										response.sendError(401, "client id doesn't match with registerd one");
//									}
//								}
//
//							} else {
//								log.error("Error code 401 : client id doesn't match with registerd one");
//								response.sendError(401, "client id doesn't match with registerd one");
//							}
//
//						}
//
//					} else {
//						log.error("Error code 401 : JSON Web Token is invalid");
//						response.sendError(400, "JSON Web Token is invalid");
//					}
//				}
//			} else {
//				log.error("Error code 400 : content type or accept headers is incorrect ");
//				response.sendError(400, "content type or accept headers is incorrect");
//			}
//		} else {
//			log.error("Error code 400 : incorrect headers ");
//			response.sendError(400, "incorrect headers ");
//		}
//	}


//	private void saveAuthorizationDetails(String requestedScope, Clients regClient, final String accessToken) {
//		AuthorizationDetails authorizationDetails = new AuthorizationDetails();
//		authorizationDetails.setClientId(regClient.getClientId());
//		authorizationDetails.setScope(requestedScope);
//		authorizationDetails.setAccessToken(accessToken);
//		String issuesTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//				.format(new Timestamp(System.currentTimeMillis()));
//		authorizationDetails.setIssuedAt(issuesTimeStamp);
//		String expiryTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//				.format(new Timestamp(System.currentTimeMillis() + (tokenExpirationTime * 1000)));
//		authorizationDetails.setExpiry(expiryTimeStamp);
//		authorizationDetailsService.saveOrUpdate(authorizationDetails);
//	}
}
