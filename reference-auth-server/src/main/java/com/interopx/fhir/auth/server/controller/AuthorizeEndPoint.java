package com.interopx.fhir.auth.server.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.model.Users;
import com.interopx.fhir.auth.server.service.AuthorizationDetailsService;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.service.PatientService;
import com.interopx.fhir.auth.server.service.UsersService;
import com.interopx.fhir.auth.server.util.CommonUtil;

@RestController
public class AuthorizeEndPoint extends HttpServlet {

  private static final long serialVersionUID = 1L;

  Logger logger = (Logger) LoggerFactory.getLogger(AuthorizeEndPoint.class);

  @Autowired private AuthorizationDetailsService authTempService;

  @Autowired private ClientsService service;

  @Autowired private PatientService patientService;

  @Autowired private UsersService userService;

  @Autowired private UsersService userRegistrationService;

  @Autowired private PasswordEncoder passwordEncoder;

//  @Autowired private AuditMapper auditMapper;

  public static final String EHR_SERVER = "/InteropXFHIR/fhir";

  @RequestMapping(value = "/api/authorize", method = RequestMethod.GET)
  @ResponseBody
  @SuppressWarnings("unchecked")
  public void getAuthorization(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    logger.debug(
        "Authorize url: " + request.getRequestURL().append('?').append(request.getQueryString()));
    HttpSession session = request.getSession();

    String ehr_url =
        request.getScheme()
            + "://"
            + request.getServerName()
            + ("http".equals(request.getScheme()) && request.getServerPort() == 80
                    || "https".equals(request.getScheme()) && request.getServerPort() == 443
                ? ""
                : ":" + request.getServerPort())
            + EHR_SERVER;

    String response_type = request.getParameter("response_type");
    String client_id = request.getParameter("client_id"); // the id of the client
    String client_secret = request.getParameter("client_secret");
    String redirect_uri = request.getParameter("redirect_uri");
    String scope = request.getParameter("scope");
    String state = request.getParameter("state");
    String aud = request.getParameter("aud");
    String launch = request.getParameter("launch");
    String codeChallenge = request.getParameter("code_challenge");
    String codeChallengeMethod = request.getParameter("code_challenge_method");

    Clients client = service.getClient(client_id);
    // Newly added code to resolve scopes issue

    if (client_id == null || client == null) {
      response.sendRedirect(
          redirect_uri
              + "?error=invalid_client_id&error_description=Unauthorized - Invalid client_id&state="
              + state);
    } else if (scope == null) {
      response.sendRedirect(
          redirect_uri + "?error=invalid_scope&error_description=scope is missing&state=" + state);
    } else if (state == null) {
      response.sendRedirect(
          redirect_uri + "?error=invalid_state&error_description=state is missing");
    } else if (redirect_uri == null) {
      logger.error("Error code 401 : Unauthorized - redirect_uri is missing");
      response.sendError(401, "Unauthorized - redirect_uri is missing");
    } else if (!client.getApprovedStatus().toString().equals("APPROVED")) {
      response.sendRedirect(
          redirect_uri
              + "?error=client is not APPROVED please contact admin&error_description=Unauthorized - Invalid client_id&state="
              + state);
      //        logger.error("Error code 401 : Unauthorized - client is not APPROVED please contact
      // admin");
      //        response.sendError(401, "Unauthorized - client is not APPROEVD please contact
      // admin");
    } else if (response_type == null) {
      response.sendRedirect(
          redirect_uri
              + "?error=invalid_response_type&error_description=response_type is missing&state="
              + state);
    } else if (aud == null) {
      response.sendRedirect(
          request.getScheme()
              + "://"
              + request.getServerName()
              + "/"
              + "ix-auth-server"
              + "/#/error"
              + "?error=missing_aud&error_description=aud_is_missing&state="
              + state);

    } else if ((aud != null) && !(aud.contains(ehr_url))) {
      response.sendRedirect(
          request.getScheme()
              + "://"
              + request.getServerName()
              + "/"
              + "ix-auth-server"
              + "/#/error"
              + "?error=invalid_aud&error_description=aud_is_invalid&state="
              + state);
    } else if ((launch != null) && !(launch.contains(client.getLaunchId()))) {
      response.sendRedirect(
          aud + "?error=launch &error_description=launch is incorrect &state=" + state);

    } else {
      Users user = userService.getUserById(client.getUserId());
      String registeredScopes = client.getScope().replace(" ", ",");

      List<String> scopes = Arrays.asList(registeredScopes.split(","));
      // scope = scope.replace(" ", ",");
      scope = scope.replaceAll("\\s+", ",");
      List<String> reqScopes = Arrays.asList(scope.split(","));

      if (scopes.containsAll(reqScopes)) {

        try {
          OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

          // It has 3 method :-  1.accessToken 2.refreshToken 3.authorizationCode
          OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

          String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

          String transctionId = CommonUtil.generateRandomString(5);
          if (responseType.equals(ResponseType.CODE.toString())) {

            String authorizationCode = oauthIssuerImpl.authorizationCode();
            AuthorizationDetails auth = new AuthorizationDetails();
            auth.setClientId(client_id);
            auth.setClientSecret(client_secret);
            auth.setRedirectUri(redirect_uri);
            auth.setScope(scope);
            auth.setAuthCode(authorizationCode);
            auth.setState(state);
            auth.setTransactionId(transctionId);
            auth.setAud(aud);
            auth.setLaunchPatientId(client.getPatientId());
            auth.setCodeChallenge(codeChallenge);
            auth.setCodeChallengeMethod(codeChallengeMethod);
            authTempService.saveOrUpdate(auth);

            if (!client.isAuthorized() && !client.isDynamicClient()) {

              String url =
                  CommonUtil.getBaseUrl(request)
                      + "#/login?client_id="
                      + client_id
                      + "&response_type="
                      + response_type
                      + "&redirect_uri="
                      + redirect_uri
                      + "&scope="
                      + scope
                      + "&state="
                      + state
                      + "&transaction_id="
                      + auth.getTransactionId().trim()
                      + "&name="
                      + user.getFirstName().trim()
                      + " "
                      + user.getLastName().trim()
                      + "&cName="
                      + client.getName().trim()
                      + "&appType="
                      + client.getAppType().trim();
              response.sendRedirect(url);
            } else {
              if (!client.isAuthorized()) {
                String url =
                    CommonUtil.getBaseUrl(request)
                        + "#/authentication?client_id="
                        + client_id
                        + "&response_type="
                        + response_type
                        + "&redirect_uri="
                        + redirect_uri
                        + "&scope="
                        + scope
                        + "&state="
                        + state
                        + "&transaction_id="
                        + auth.getTransactionId().trim()
                        + "&name="
                        + user.getFirstName().trim()
                        + " "
                        + user.getLastName().trim()
                        + "&cName="
                        + client.getName().trim()
                        + "&appType="
                        + client.getAppType().trim();
                response.sendRedirect(url);
              } else {
                String url =
                    auth.getRedirectUri().trim()
                        + "?code="
                        + auth.getAuthCode().trim()
                        + "&state="
                        + auth.getState().trim();
                logger.debug("Authorize endpoint formed url: " + url);
                response.sendRedirect(url);
              }
            }

          } else {
            logger.error(
                "Error code 401 : Unauthorized - Requested Scope not authorized for Client");
            response.sendError(401, "Unauthorized - Requested Scope not authorized for Client");
          }

        } catch (OAuthSystemException e) {
          logger.error("Exception in getAuthorization() of AuthorizeEndPoint class ", e);
        } catch (OAuthProblemException e) {
          logger.error("Exception in getAuthorization() of AuthorizeEndPoint class ", e);
        }

//       addAuditLog(client, 200, request);

      } else {
        String url =
            redirect_uri
                + "?error=invalid_scope&error_description=Unauthorized - Requested Scope not authorized for Client&state="
                + state
                + "&scope="
                + client.getScope().trim();
        response.sendRedirect(url);

//        addAuditLog(client, 500, request);
      }
    }
  }

//  public void addAuditLog(Clients client, Integer status, HttpServletRequest request) {
//
//    try {
//      AuditDto audit = new AuditDto();
//
//      if (client != null) {
//        audit.setPatientId(client.getPatientId());
//        audit.setUserId(client.getUserId());
//      }
//
//      audit.setId(CommonUtil.generateRandomString(30));
//      audit.setClientId(CommonUtil.generateRandomString(30));
//      audit.setServiceName("ix-auth-server");
//      audit.setRequestUrl(CommonUtil.getBaseUrl(request));
//      audit.setRequestMethod("GET");
//      audit.setClassName("AuthorizeEndPoint");
//      audit.setMethodName("getAuthorization");
//      audit.setAuditType("RESTAPI");
//      audit.setRequestMapping("/api/authorize");
//      audit.setRequestStatusCode(status);
//
//      auditMapper.mapDtoToEntity(audit);
//    } catch (Exception e) {
//      logger.debug("Exception adding audit logger addAuditLog() :::::{}", e);
//    }
//  }

  @RequestMapping(value = "/api/authorize", method = RequestMethod.POST)
  @ResponseBody
  public String getAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam("transaction_id") String transactionId,
      @RequestParam("request_type") String requestType,
      @RequestParam("scopes") String scopes)
      throws IOException {
    logger.debug("Received Scopes from Authentication page:::::{}", scopes);
    AuthorizationDetails tempAuth = authTempService.getAuthenticationById(transactionId);
    if (tempAuth != null) {
      if (requestType != null && requestType.equalsIgnoreCase("Allow")) {
        Clients clientDetails = service.getClient(tempAuth.getClientId());
        List<String> scopeList = Arrays.asList(scopes.split(","));
        if (scopeList.contains("launch/patient") || scopeList.contains("launch")) {
          tempAuth.setScope(scopes);
          authTempService.saveOrUpdate(tempAuth);
          String url =
              CommonUtil.getBaseUrl(request) + "#/patientlist?transaction_id=" + transactionId;
          //                	response.sendRedirect(url.trim());
          logger.debug("launch/patient -> formed url: " + url);
          return url;

        } else {
          tempAuth.setScope(scopes);
          authTempService.saveOrUpdate(tempAuth);
          if (clientDetails != null && clientDetails.isDynamicClient()) {
            clientDetails.setScope(scopes);
            service.updateClient(clientDetails);
          }
          String url =
              tempAuth.getRedirectUri().trim()
                  + "?code="
                  + tempAuth.getAuthCode().trim()
                  + "&state="
                  + tempAuth.getState().trim();
          logger.debug("Authorize endpoint -> formed url: " + url);
          return url;
        }
      } else {
        Clients dafClientRegister = service.getClient(tempAuth.getClientId());
        if (dafClientRegister != null) {
          Users dafUserRegister = userService.getUserById(dafClientRegister.getUserId());
          dafUserRegister.setPatient_id("");
          userService.updateUser(dafUserRegister);
        }
        String url =
            tempAuth.getRedirectUri().trim()
                + "?error=Access Denied&error_description=Access Denied&state="
                + tempAuth.getState().trim();
        return url;
      }
    } else {
      String url = CommonUtil.getBaseUrl(request) + "?Invalid transaction_id";
      return url;
    }
  }

  /**
   * This function will load patient launch page and redirect it
   *
   * @param request
   * @param response
   * @param transactionId
   * @param launchPatientId
   * @return
   * @throws IOException
   * @throws ServletException
   */
  @RequestMapping(value = "/api/authorize/launchpatient", method = RequestMethod.POST)
  @ResponseBody
  public String authorizeAfterLaunchPatient(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam("transaction_id") String transactionId,
      @RequestParam("id") String launchPatientId)
      throws IOException, ServletException {
    AuthorizationDetails tempAuth = authTempService.getAuthenticationById(transactionId);
    tempAuth.setLaunchPatientId(launchPatientId);
    authTempService.saveOrUpdate(tempAuth);

    String url =
        tempAuth.getRedirectUri().trim()
            + "?code="
            + tempAuth.getAuthCode().trim()
            + "&state="
            + tempAuth.getState().trim();
    logger.debug("Authorize endpoint -> formed url: " + url);
    return url;
  }

  @RequestMapping(value = "/api/authorize/userValidate", method = RequestMethod.POST)
  @ResponseBody
  public String validateUser(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam("userName") String userName,
      @RequestParam("password") String password,
      @RequestParam("transaction_id") String transactionId)
      throws IOException {
    AuthorizationDetails tempAuth = authTempService.getAuthenticationById(transactionId);
    Clients client = service.getClient(tempAuth.getClientId());
    String uri =
        request.getScheme()
            + "://"
            + request.getServerName()
            + ("http".equals(request.getScheme()) && request.getServerPort() == 80
                    || "https".equals(request.getScheme()) && request.getServerPort() == 443
                ? ""
                : ":" + request.getServerPort())
            + request.getContextPath();

    Users user = userRegistrationService.getUserByEmail(userName, request);
    if (user != null && (passwordEncoder.matches(password, user.getUser_password()))) {
      String url =
          CommonUtil.getBaseUrl(request)
              + "#/authentication?client_id="
              + tempAuth.getClientId()
              + "&redirect_uri="
              + tempAuth.getRedirectUri().trim()
              + "&scope="
              + tempAuth.getScope()
              + "&state="
              + tempAuth.getState()
              + "&transaction_id="
              + transactionId
              + "&name="
              + user.getFirstName().trim()
              + " "
              + user.getLastName().trim()
              + "&cName="
              + client.getName().trim()
              + "&appType="
              + client.getAppType().trim();
      //      response.sendRedirect(url);
      return url;
    } else {
      return uri
          + "#/login?error=Invalid Username or Password.&transaction_id="
          + transactionId.trim();
    }
  }
}
