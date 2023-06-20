package com.interopx.fhir.auth.server.controller;

import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.service.AuthorizationDetailsService;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.util.CommonUtil;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IntrospectionEndpoint extends HttpServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = 4919755524639593435L;

	@Autowired
	private ClientsService clientsService;

	@Autowired
	private AuthorizationDetailsService authorizationDetailsService;

	@Value("${token.expiration-time}")
	private Integer tokenExpirationTime;

	Logger logger = (Logger) LoggerFactory.getLogger(IntrospectionEndpoint.class);

	/**
	 * Provides description about access token
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/api/introspect", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> introspection(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.debug("Request received for introspect. the url: "
				+ request.getRequestURL().append('?').append(request.getQueryString()));
		Map<String, Object> introspectionResponse = new HashMap<String, Object>();
		String accessToken = request.getParameter("token");

		if (accessToken == null) {
			logger.error("Error code 404 : access token is missing");
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "access token is missing");
		}

		String patientId = request.getParameter("patientId"); 
		logger.debug("Received Patient Id:::::{}", patientId);

		AuthorizationDetails authorizationDetails = authorizationDetailsService.getAuthorizationByAccessToken(accessToken);
		if (authorizationDetails == null) {
			logger.error("Error code 404 : access token is invalid");
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "access token is invalid");
		}

		if (authorizationDetails != null) {
			try {
				Clients clientDetails = clientsService.getClient(authorizationDetails.getClientId());
				String expiryTime = authorizationDetails.getExpiry();
				String issuedAt = authorizationDetails.getIssuedAt();
				Integer currentTime = CommonUtil.convertTimestampToUnixTime(
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis())));
				logger.debug("CLientId::::{},DynamicClient::::{}", clientDetails.getClientId(),
						clientDetails.isDynamicClient());
				if (patientId != null && clientDetails.isDynamicClient()) {
					logger.debug(
							"Checking if Launch Patient and Requested Patient are same:::: LaunchPatient:{}:::::RequestedPatient:{}",
							authorizationDetails.getLaunchPatientId(), patientId);
					if (CommonUtil.convertTimestampToUnixTime(expiryTime) > currentTime
							&& patientId.equals(authorizationDetails.getLaunchPatientId())) {
						introspectionResponse.put("exp", CommonUtil.convertTimestampToUnixTime(expiryTime));
						introspectionResponse.put("sub", clientDetails.getClientId());
						introspectionResponse.put("grant_type", "authorization_code");
						introspectionResponse.put("active", "true");
						introspectionResponse.put("token_type", "Bearer");
						introspectionResponse.put("client_id", authorizationDetails.getClientId());
						introspectionResponse.put("iat", CommonUtil.convertTimestampToUnixTime(issuedAt));
						introspectionResponse.put("scope", authorizationDetails.getScope());
						introspectionResponse.put("customerId", clientDetails.getCustomerId());
						introspectionResponse.put("centerId", clientDetails.getCenterId());
						response.addHeader("Content-Type", "application/json");
						response.addHeader("Cache-Control", "no-store");
						response.addHeader("Pragma", "no-cache");

					} else {
						introspectionResponse.put("active", "false");
					}
				} else {
					if (CommonUtil.convertTimestampToUnixTime(expiryTime) > currentTime) {
						introspectionResponse.put("exp", CommonUtil.convertTimestampToUnixTime(expiryTime) );
						introspectionResponse.put("sub", clientDetails.getClientId());
						introspectionResponse.put("grant_type", "authorization_code");
						introspectionResponse.put("active", "true");
						introspectionResponse.put("token_type", "Bearer");
						introspectionResponse.put("client_id", authorizationDetails.getClientId());
						introspectionResponse.put("iat", CommonUtil.convertTimestampToUnixTime(issuedAt));
						introspectionResponse.put("scope", authorizationDetails.getScope());
						introspectionResponse.put("customerId", clientDetails.getCustomerId());
						introspectionResponse.put("centerId", clientDetails.getCenterId());
						response.addHeader("Content-Type", "application/json");
						response.addHeader("Cache-Control", "no-store");
						response.addHeader("Pragma", "no-cache");

					} else {
						introspectionResponse.put("active", "false");
					}
				}
			} catch (ParseException e) {
				logger.error("Exception in introspection() of IntrospectionEndpoint class ", e);
			}
		}
		return introspectionResponse;
	}

	/**
	 * This api will give scopes allowed for the client based on access token
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/api/scopes", method = RequestMethod.POST)
	@ResponseBody
	public String getScopeByAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("Request received for get scopes by access token ");
		String scopes = "";
		String accessToken = request.getParameter("token");
		AuthorizationDetails authData = authorizationDetailsService.getAuthorizationByAccessToken(accessToken);
		if (authData != null) {
			scopes = authData.getScope();
		}
		return scopes;
	}
}
