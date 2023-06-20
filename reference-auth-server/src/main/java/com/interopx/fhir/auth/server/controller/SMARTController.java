package com.interopx.fhir.auth.server.controller;

import com.interopx.fhir.auth.server.model.SmartAuthorizeationDetails;
import com.interopx.fhir.auth.server.service.SmartAuthorizeationDetailsService;
import com.interopx.fhir.auth.server.util.CommonUtil;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides list of metadata supported by openid connect
 *
 * @author admin
 */
@RestController
public class SMARTController extends HttpServlet {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired SmartAuthorizeationDetailsService smartAuthorizeationDetailsService;

  //	@Value("${auth_server_Base.url}")
  //	private String auth_server_Base_url;

  public static final String SMART_CONFIGURATION_URL = "smart-configuration";
  public static final String OPENID_CONFIGURATION_URL = "openid-configuration";
  public static final String AUTHORIZE_URL = "api/authorize";
  public static final String TOKEN_URL = "api/token";
  public static final String INTROSPECTION_URL = "introspect";
  public static final String REGISTRATION_URL = "view/newuser.html";

  private static final long serialVersionUID = 936659617930557226L;

  /**
   * Provides matadata about openid configuration
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(
      value = "/api/.well-known/" + OPENID_CONFIGURATION_URL,
      method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getAuthorization(
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    Map<String, Object> m = new HashMap<>();
    String baseUrl = CommonUtil.getBaseUrl(request);

    SmartAuthorizeationDetails smartAuthorizeationDetails =
        smartAuthorizeationDetailsService.getAuthorizationDetailsById(1);

    m.put("issuer", baseUrl + "api/");
    m.put("authorization_endpoint", baseUrl + AUTHORIZE_URL);
    m.put("token_endpoint", baseUrl + TOKEN_URL);
    // m.put("introspection_endpoint", baseUrl + INTROSPECTION_URL);

    // m.put("userinfo_endpoint", baseUrl + UserInfoEndpoint.URL);

    m.put("jwks_uri", baseUrl + "api/jwk");
    m.put("response_types_supported", smartAuthorizeationDetails.getResponseTypes());
    m.put("subject_types_supported", smartAuthorizeationDetails.getSubjectTypes());
    m.put("claims_supported", smartAuthorizeationDetails.getClaims());
    m.put("id_token_signing_alg_values_supported", smartAuthorizeationDetails.getAlgorithmTypes());

    m.put("claims_parameter_supported", smartAuthorizeationDetails.getIsClaimsParamSupported());
    m.put("request_parameter_supported", smartAuthorizeationDetails.getIsRequestParamSupported());
    m.put(
        "request_uri_parameter_supported",
        smartAuthorizeationDetails.getIsRequestUriParamSupported());
    m.put(
        "require_request_uri_registration",
        smartAuthorizeationDetails.getIsRequestUriRegistration());

    //    m.put("jwks_uri", baseUrl + "api/jwk");
    //    m.put("response_types_supported", Arrays.asList("code"));
    //    m.put("subject_types_supported", Arrays.asList("public"));
    //    m.put("claims_supported", Arrays.asList("sub", "name", "profile", "email"));
    //    m.put("id_token_signing_alg_values_supported", Arrays.asList("RS256"));
    //
    //    m.put("claims_parameter_supported", false);
    //    m.put("request_parameter_supported", false);
    //    m.put("request_uri_parameter_supported", true);
    //    m.put("require_request_uri_registration", false);
    return m;
  }
}
