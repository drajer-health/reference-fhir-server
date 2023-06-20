package com.interopx.fhir.auth.server.controller;

import com.interopx.fhir.auth.server.model.AuthorizationDetails;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.model.Users;
import com.interopx.fhir.auth.server.service.AuthorizationDetailsService;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.service.UsersService;
import com.interopx.fhir.auth.server.util.CommonUtil;
import io.jsonwebtoken.Jwts;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenEndPoint extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final int HOUR = 3600 * 1000; // in milli-seconds.
  public static final String SMART_STYLE_URL = "smart_v1.json";

  private static final Logger log = (Logger) LoggerFactory.getLogger(TokenEndPoint.class);

  @Autowired private ClientsService service;

  @Autowired private UsersService userService;

  @Autowired private AuthorizationDetailsService authTempService;

  @Autowired private JwtGenerator jwtGenerator;

  @Value("${refresh_token_expiry_time_in_days}")
  private int refreshTokenExpiration;

  // HashMap<String ,String> hm1 = new HashMap<String, String>();

  @RequestMapping(value = "/api/token", method = RequestMethod.POST)
  @ResponseBody
  public void getAuthorization(HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    PrintWriter out = response.getWriter();

    log.debug("Token url:" + request.getRequestURL().append('?').append(request.getQueryString()));

    String credentials = request.getHeader("Authorization");
    String client_id = null;
    String client_secret = null;
    String code = null;
    String token = request.getParameter("refresh_token");
    String grant_type = null;
    String scope = null;
    String client_assertion_type = null;
    String client_assertion = null;
    String code_verifier = null;

    if (credentials != null) {

      credentials = credentials.substring(6);
      String cred = CommonUtil.base64Decoder(credentials);
      String[] credList = cred.split(":", 2);
      client_id = credList[0];
      client_secret = credList[1];
      code = request.getParameter("code");
      grant_type = request.getParameter("grant_type");
      scope = request.getParameter("scope");
      client_assertion = request.getParameter("client_assertion");
      client_assertion_type = request.getParameter("client_assertion_type");
      code_verifier = request.getParameter("code_verifier");
    } else if (request.getParameter("client_id") != null) {

      log.debug(" Client Id found " + request.getParameter("client_id"));

      client_id = request.getParameter("client_id");
      client_secret = request.getParameter("client_secret");
      code = request.getParameter("code");
      grant_type = request.getParameter("grant_type");
      scope = request.getParameter("scope");
      client_assertion = request.getParameter("client_assertion");
      client_assertion_type = request.getParameter("client_assertion_type");
      code_verifier = request.getParameter("code_verifier");

    } else {
      StringBuffer sb = new StringBuffer();
      BufferedReader bufferedReader = null;

      try {
        bufferedReader =
            request.getReader(); // new BufferedReader(new InputStreamReader(inputStream));
        char[] charBuffer = new char[128];
        int bytesRead;
        while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
          sb.append(charBuffer, 0, bytesRead);
        }

      } catch (IOException ex) {
        throw ex;
      } finally {
        if (bufferedReader != null) {
          try {
            bufferedReader.close();
          } catch (IOException ex) {
            throw ex;
          }
        }
      }

      log.debug(" Body = " + (sb == null ? "Null Body" : sb.toString()));

      String jsonString;
      if (!sb.toString().isEmpty() && sb.toString() != null) {

        if (sb.toString().contains("client_assertion")) {

          log.debug(" Found Client assertion ");
          String[] params = sb.toString().split("&");

          for (String p : params) {
            String[] pair = p.split("=");
            if ("client_assertion".equalsIgnoreCase(pair[0])) {
              client_assertion = pair[1];
            }

            if ("grant_type".equalsIgnoreCase(pair[0])) {
              grant_type = pair[1];
            }

            if ("client_assertion_type".equalsIgnoreCase(pair[0])) {
              client_assertion_type = pair[1];
            }

            if ("scope".equalsIgnoreCase(pair[0])) {
              scope = pair[1];
            }
          }

        } else {

          log.debug(" No client assertion found ");

          if (sb.toString().contains("{") && sb.toString().contains("}")) {
            jsonString = sb.toString().replace("=", ":").replace("&", ",");
          } else {
            jsonString = "{" + sb.toString().replace("=", ":").replace("&", ",") + "}";
          }

          log.debug("body" + jsonString);

          JSONObject payLoad = new JSONObject(jsonString);

          client_id = payLoad.getString("client_id");
          client_secret = payLoad.getString("client_secret");
          code = payLoad.getString("code");
          grant_type = payLoad.getString("grant_type");
        }
      }
    }

    log.debug("client id : " + client_id);
    log.debug("Client_secret: " + client_secret);
    log.debug("code:" + code);
    log.debug("grant_type:" + grant_type);
    log.info("CodeVerifier:" + code_verifier);

    if (grant_type == null) {
      log.error("Error code 401 : grant_type is not present");
      response.sendError(401, "grant_type is not present");
    } else if (grant_type.equals(GrantType.CLIENT_CREDENTIALS.toString())) {
      if (client_assertion == null) {
        log.error("Error code 401 : client_assertion is not present");
        response.sendError(401, "client_assertion is not present");
      } else if (client_assertion_type == null
          || !"urn:ietf:params:oauth:client-assertion-type:jwt-bearer"
              .equals(client_assertion_type)) {
        log.error("Error code 401 : client_assertion_type is not present or not valid");
        response.sendError(401, "client_assertion_type is not present or not valid");
      } else if (scope == null) {
        log.error("Error code 401 : scope is not present");
        response.sendError(401, "scope is not present");
      } else {

        String clientId = null;
        // get jwt body
        String[] split_string = client_assertion.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));
        log.debug("JWT Header : " + header);

        String body = new String(base64Url.decode(base64EncodedBody));

        JSONObject jwtBody = new JSONObject(body);
        if (jwtBody != null && jwtBody.has("sub")) {
          clientId = jwtBody.getString("sub");
        }

        if (clientId != null) {
          Clients client = service.getClient(clientId);

          if (client == null) {

          } else {

            String contextPath = System.getProperty("catalina.base");
            String mainDirPath = client.getPublicKey();
            String fileName = client.getFiles();

            File publicFile = new File(contextPath + mainDirPath + fileName);

            if (publicFile.exists()) {

              byte[] keyBytes = Files.readAllBytes(Paths.get(publicFile.getAbsolutePath()));

              X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
              KeyFactory kf = KeyFactory.getInstance("RSA");

              PublicKey key2 = kf.generatePublic(spec);

              boolean asserter =
                  Jwts.parser()
                      .setSigningKey(key2)
                      .parseClaimsJws(client_assertion)
                      .getBody()
                      .getSubject()
                      .equals(client.getClientId());

              if (asserter) {
                AuthorizationDetails authTemp = new AuthorizationDetails();
                OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                final String accessToken = oauthIssuerImpl.accessToken();

                JSONObject jsonOb = new JSONObject();
                jsonOb.put("access_token", accessToken);
                jsonOb.put("token_type", "bearer");
                jsonOb.put("expires_in", "900");

                response.addHeader("Content-Type", "application/json");
                String timeStamp =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Timestamp(System.currentTimeMillis()));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date()); // Using today's date
                calendar.add(Calendar.DATE, 90); // Adding 90 days
                String refereshTokenExpiryTime =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
                authTemp.setRefreshTokenExpiryTime(refereshTokenExpiryTime);
                authTemp.setClientSecret(client_secret);
                authTemp.setAccessToken(accessToken);
                authTemp.setExpiry(timeStamp);
                authTemp.setRefreshToken(authTemp.getRefreshToken());

                authTempService.saveOrUpdate(authTemp);

                out.println(jsonOb.toString());

              } else {
                log.error("Error code 401 : Invalid client_assertion");
                response.sendError(401, "Invalid client_assertion");
              }

            } else {
              log.error("Error code 500 : Public key was not found");
              response.sendError(500, "Public key was not found");
            }
          }
        } else {
          response.sendError(401, "JWT - subject is missing");
        }
      }

    } else {

      if ((client_secret != null) && (client_id != null)) {
        Clients client = service.getClientByCredentials(client_id, client_secret);
        if (client != null) {

          String userId = client.getUserId();
          Users user = userService.getUserById(userId);

          AuthorizationDetails authTemp = authTempService.getAuthById(client_id);

          if (authTemp != null) {
            List<String> scopes = Arrays.asList(authTemp.getScope().split(","));
            StringBuilder stringScope = new StringBuilder();
            int scopesint = scopes.size();
            for (int i = 0; i < scopesint; i++) {
              stringScope.append(scopes.get(i));
              if (i < scopesint) {
                stringScope.append(" ");
              }
            }
            stringScope.toString();

            try {
              OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
              if (grant_type.equals(GrantType.AUTHORIZATION_CODE.toString())) {
                if (!code.equalsIgnoreCase(authTemp.getAuthCode())) {
                  log.error("Error code 400 : Invalid authorization code");
                  response.sendError(400, "Invalid authorization code");
                  out.println(response);
                } else if (!client.getClientId().equals(client_id)) {
                  log.error("Error code 401 : Invalid Client ID");
                  response.sendError(401, "Invalid Client ID");
                  out.println(response);
                } else {
                  if (authTemp.getCodeChallenge() != null) {
                    log.info("Validating the Code Challenge");
                    if (code_verifier != null) {
                      if (Boolean.FALSE.equals(
                          codeChallengeVerifier(authTemp.getCodeChallenge(), code_verifier))) {
                        log.error("Error code 401 : Code Challenge Failed");
                        response.sendError(401, "Code Challenge Failed");
                        out.println(response);
                      }
                    }
                  }
                  final String accessToken = oauthIssuerImpl.accessToken();
                  // org.json.JSONObject jsonHash = new org.json.JSONObject(new HashMap<K, V>());

                  JSONObject jsonOb = new JSONObject();
                  jsonOb.put("access_token", accessToken);
                  jsonOb.put("token_type", "bearer");
                  jsonOb.put("expires_in", 3600);
                  jsonOb.put("scope", stringScope.toString());

                  String refreshToken = null;
                  String idToken = null;
                  String fhirUser = "";
                  if (scopes.contains("launch/patient") || scopes.contains("launch")) {
                    fhirUser = authTemp.getLaunchPatientId();
                    jsonOb.put("patient", String.valueOf(authTemp.getLaunchPatientId()));
                    jsonOb.put("need_patient_banner", true);
                    jsonOb.put("smart_style_url", CommonUtil.getBaseUrl(request) + SMART_STYLE_URL);
                  }
                  if (scopes.contains("offline_access") || scopes.contains("online_access")) {
                    refreshToken = oauthIssuerImpl.refreshToken();
                    jsonOb.put("refresh_token", refreshToken);
                  }
                  if (scopes.contains("openid")) {
                    String sub = user.getUser_name();
                    String aud = client.getClientId();
                    String email = user.getUser_email();
                    String userName = user.getUser_name();
                    String timeStamp =
                        new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy")
                            .format(new Timestamp(System.currentTimeMillis()));
                    Date issueDate = CommonUtil.convertToDateFormat(timeStamp);
                    Date expiryTime = new Date(issueDate.getTime() + 2 * HOUR);
                    Map<String, Object> payloadData = new HashMap<>();
                    payloadData.put("sub", sub);
                    payloadData.put("aud", aud);
                    payloadData.put("email", email);
                    payloadData.put("issueDate", issueDate);
                    payloadData.put("expiryTime", expiryTime);
                    payloadData.put("userName", userName);
                    payloadData.put("fhirUser", fhirUser);

                    idToken = jwtGenerator.generate(payloadData, request);
                    jsonOb.put("id_token", idToken);
                  }
                  response.addHeader("Content-Type", "application/json");
                  response.addHeader("Cache-Control", "no-store");
                  response.addHeader("Pragma", "no-cache");

                  String timeStamp =
                      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                          .format(new Timestamp(System.currentTimeMillis()));
                  Calendar calendar = Calendar.getInstance();
                  calendar.setTime(new Date()); // Using today's date
                  calendar.add(Calendar.DATE, 90); // Adding 90 days
                  String refereshTokenExpiryTime =
                      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
                  authTemp.setRefreshTokenExpiryTime(refereshTokenExpiryTime);
                  authTemp.setClientSecret(client_secret);
                  authTemp.setAccessToken(accessToken);
                  authTemp.setExpiry(timeStamp);
                  authTemp.setRefreshToken(refreshToken);

                  authTempService.saveOrUpdate(authTemp);

                  out.println(jsonOb.toString());
                }
              } else if (grant_type.equals(GrantType.REFRESH_TOKEN.toString())) {
                if (authTemp.getRefreshToken() != null
                    && authTemp.getRefreshToken().equals(token)) {

                  Integer currentTime = (int) (System.currentTimeMillis() / 1000L);
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                  Date refreshTokenExpiryDate = sdf.parse(authTemp.getRefreshTokenExpiryTime());
                  long inMilliseconds = refreshTokenExpiryDate.getTime();
                  if (scopes.contains("online_access") && currentTime > inMilliseconds) {
                    response.sendError(401, "Invalid refresh_token");
                    out.println(response);
                  } else if (!client.getClientId().equals(client_id)) {
                    log.error("Error code 401 : Invalid Client ID");
                    response.sendError(401, "Invalid Client ID");
                    out.println(response);
                  } else {

                    final String accessToken = oauthIssuerImpl.accessToken();
                    final String refreshToken = oauthIssuerImpl.refreshToken();
                    JSONObject jsonOb = new JSONObject();
                    jsonOb.put("access_token", accessToken);
                    jsonOb.put("patient", String.valueOf(authTemp.getLaunchPatientId()));
                    jsonOb.put("token_type", "bearer");
                    jsonOb.put("expires_in", 3600); // 3600 second is 1hour
                    jsonOb.put("scope", stringScope.toString());
                    jsonOb.put("refresh_token", token);

                    // String refreshToken = null;
                    if (scopes.contains("launch/patient")) {
                      jsonOb.put("patient", String.valueOf(authTemp.getLaunchPatientId()));
                    }

                    response.addHeader("Content-Type", "application/json");
                    response.addHeader("Cache-Control", "no-store");
                    response.addHeader("Pragma", "no-cache");
                    String timeStamp =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(new Timestamp(System.currentTimeMillis()));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date()); // Using today's date
                    calendar.add(Calendar.DATE, refreshTokenExpiration); // Adding 90 days
                    String refereshTokenExpiryTime =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
                    authTemp.setRefreshTokenExpiryTime(refereshTokenExpiryTime);
                    authTemp.setClientSecret(client_secret);
                    authTemp.setAccessToken(accessToken);
                    authTemp.setExpiry(timeStamp);
                    authTemp.setRefreshToken(token);

                    authTempService.saveOrUpdate(authTemp);

                    out.println(jsonOb.toString());
                  }
                } else {
                  log.error("Error code 401 : Invalid refresh_token");
                  response.sendError(401, "Invalid refresh_token");
                }
              }

            } catch (OAuthSystemException e) {
              log.error("Exception in getAuthorization() of TokenEndPoint class ", e);
            }

          } else {
            log.error("Error code 401 : Invalid authentication");
            // the auth codes don't match.
            response.sendError(401, "Invalid authentication");
          }
        } else {
          log.error("Error code 401 : Invalid client_secret");
          response.sendError(401, "Invalid client_secret");
        }
      } else {
        // Code for Validation of Token Request of Public Clients
        // verifying Public client_id

        Clients client = service.getClient(client_id);

        String userId = client.getUserId();
        Users user = userService.getUserById(userId);

        AuthorizationDetails authTemp = authTempService.getAuthById(client_id);

        if (authTemp != null) {
          List<String> scopes = Arrays.asList(authTemp.getScope().split(","));
          StringBuilder stringScope = new StringBuilder();
          int scopesint = scopes.size();
          for (int i = 0; i < scopesint; i++) {
            stringScope.append(scopes.get(i));
            if (i < scopesint) {
              stringScope.append(" ");
            }
          }
          stringScope.toString();

          try {
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            if (grant_type.equals(GrantType.AUTHORIZATION_CODE.toString())) {
              if (!code.equalsIgnoreCase(authTemp.getAuthCode())) {
                log.error("Error code 400 : Invalid authorization code");
                response.sendError(400, "Invalid authorization code");
                out.println(response);
              } else if (!client.getClientId().equals(client_id)) {
                log.error("Error code 401 : Invalid Client ID");
                response.sendError(401, "Invalid Client ID");
                out.println(response);
              } else {
                if (authTemp.getCodeChallenge() != null) {
                  log.info("Validating the Code Challenge");
                  if (Boolean.FALSE.equals(
                      codeChallengeVerifier(authTemp.getCodeChallenge(), code_verifier))) {
                    log.error("Error code 401 : Code Challenge Failed");
                    response.sendError(401, "Code Challenge Failed");
                    out.println(response);
                  }
                }
                final String accessToken = oauthIssuerImpl.accessToken();
                // org.json.JSONObject jsonHash = new org.json.JSONObject(new HashMap<K, V>());

                JSONObject jsonOb = new JSONObject();
                jsonOb.put("access_token", accessToken);
                jsonOb.put("token_type", "bearer");
                jsonOb.put("expires_in", 3600);

                String refreshToken = null;
                String idToken = null;
                if (scopes.contains("launch/patient") || scopes.contains("launch")) {
                  jsonOb.put("patient", String.valueOf(authTemp.getLaunchPatientId()));
                }
                if (scopes.contains("offline_access")) {
                  refreshToken = oauthIssuerImpl.refreshToken();
                  jsonOb.put("refresh_token", refreshToken);

                } else if (scopes.contains("online_access")) {
                  refreshToken = oauthIssuerImpl.refreshToken();
                  jsonOb.put("refresh_token", refreshToken);
                }
                if (scopes.contains("openid")) {
                  String sub = user.getUser_name();
                  String aud = client.getClientId();
                  String email = user.getUser_email();
                  String userName = user.getUser_name();
                  String timeStamp =
                      new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy")
                          .format(new Timestamp(System.currentTimeMillis()));
                  Date issueDate = CommonUtil.convertToDateFormat(timeStamp);
                  Date expiryTime = new Date(issueDate.getTime() + 2 * HOUR);
                  Map<String, Object> payloadData = new HashMap<>();
                  payloadData.put("sub", sub);
                  payloadData.put("aud", aud);
                  payloadData.put("email", email);
                  payloadData.put("issueDate", issueDate);
                  payloadData.put("expiryTime", expiryTime);
                  payloadData.put("userName", userName);

                  idToken = jwtGenerator.generate(payloadData, request);
                  jsonOb.put("id_token", idToken);
                  jsonOb.put("scope", stringScope.toString());
                }
                response.addHeader("Content-Type", "application/json");
                response.addHeader("Cache-Control", "no-store");
                response.addHeader("Pragma", "no-cache");

                String timeStamp =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Timestamp(System.currentTimeMillis()));
                // hm1.put(accessToken, timeStamp);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date()); // Using today's date
                calendar.add(Calendar.DATE, refreshTokenExpiration); // Adding 90 days
                String refereshTokenExpiryTime =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
                authTemp.setRefreshTokenExpiryTime(refereshTokenExpiryTime);
                authTemp.setClientSecret(client_secret);
                authTemp.setAccessToken(accessToken);
                authTemp.setExpiry(timeStamp);
                authTemp.setRefreshToken(refreshToken);
                //								authTemp.setIdToken(idToken);

                authTempService.saveOrUpdate(authTemp);

                if (!client.isAuthorized() && client.isDynamicClient()) {
                  client.setAuthorized(true);
                  service.updateClient(client);
                }

                out.println(jsonOb.toString());
              }
            } else if (grant_type.equals(GrantType.REFRESH_TOKEN.toString())) {
              if (authTemp.getRefreshToken() != null
                  && authTemp.getRefreshToken().equals(token)) {

                Integer currentTime = (int) (System.currentTimeMillis() / 1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date refreshTokenExpiryDate = sdf.parse(authTemp.getRefreshTokenExpiryTime());
                long inMilliseconds = refreshTokenExpiryDate.getTime();
                if (scopes.contains("online_access") && currentTime > inMilliseconds) {
                  log.error("Error code 401 : Invalid refresh_token");
                  response.sendError(401, "Invalid refresh_token");
                  out.println(response);
                } else if (!client.getClientId().equals(client_id)) {
                  log.error("Error code 401 : Invalid Client ID");
                  response.sendError(401, "Invalid Client ID");
                  out.println(response);
                } else {
                  final String accessToken = oauthIssuerImpl.accessToken();
                  final String refreshToken = oauthIssuerImpl.refreshToken();
                  JSONObject jsonOb = new JSONObject();
                  jsonOb.put("access_token", accessToken);
                  jsonOb.put("patient", String.valueOf(authTemp.getLaunchPatientId()));
                  jsonOb.put("token_type", "bearer");
                  jsonOb.put("expires_in", 3600); // 3600 second is 1hour
                  jsonOb.put("scope", stringScope.toString());
                  jsonOb.put("refresh_token", token);

                  // String refreshToken = null;
                  if (scopes.contains("launch/patient")) {
                    jsonOb.put("patient", String.valueOf(authTemp.getLaunchPatientId()));
                  }

                  response.addHeader("Content-Type", "application/json");
                  response.addHeader("Cache-Control", "no-store");
                  response.addHeader("Pragma", "no-cache");
                  String timeStamp =
                      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                          .format(new Timestamp(System.currentTimeMillis()));
                  Calendar calendar = Calendar.getInstance();
                  calendar.setTime(new Date()); // Using today's date
                  calendar.add(Calendar.DATE, 90); // Adding 90 days
                  String refereshTokenExpiryTime =
                      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
                  authTemp.setRefreshTokenExpiryTime(refereshTokenExpiryTime);
                  authTemp.setClientSecret(client_secret);
                  authTemp.setAccessToken(accessToken);
                  authTemp.setExpiry(timeStamp);
                  authTemp.setRefreshToken(token);
                  authTempService.saveOrUpdate(authTemp);

                  out.println(jsonOb.toString());
                }
              } else {
                log.error("Error code 401 : Invalid refresh_token");
                response.sendError(401, "Invalid refresh_token");
              }
            }

          } catch (OAuthSystemException e) {
            log.error("Exception in getAuthorization() of TokenEndPoint class ", e);
          }

        } else {
          log.error("Error code 401 : Invalid authentication");
          // the auth codes don't match.
          response.sendError(401, "Invalid authentication");
        }
      }
    }
  }

  @RequestMapping(value = "/api/token/revoke", method = RequestMethod.POST)
  public ResponseEntity<?> revokeToken(HttpServletRequest request, HttpServletResponse response) {
    log.debug(
        "Revoke Token url:" + request.getRequestURL().append('?').append(request.getQueryString()));

    try {
      ResponseEntity<?> responseObj = null;
      String credentials = request.getHeader("Authorization");
      String token = request.getParameter("token");
      String tokenType = request.getParameter("token_type");
      log.debug("Received Credentials:::::{}", credentials);
      log.debug("Received Token:::::{}", token);
      log.debug("Received TokenType:::::{}", tokenType);
      if (!credentials.isEmpty()) {
        credentials = credentials.substring(6);
        String cred = CommonUtil.base64Decoder(credentials);
        String[] credList = cred.split(":", 2);
        String clientId = credList[0];
        String clientSecret = credList[1];
        AuthorizationDetails authTemp = authTempService.getAuthById(clientId);
        if (authTemp != null) {
          if (tokenType.equals("access_token") && authTemp.getAccessToken().equals(token)) {
            authTemp.setAccessToken("revoked_access_token");
            authTempService.saveOrUpdate(authTemp);
          } else if (tokenType.equals("refresh_token")
              && authTemp.getRefreshToken().equals(token)) {
            authTemp.setRefreshToken("revoked_refresh_token");
            authTempService.saveOrUpdate(authTemp);
          }
        } else {
          log.error("Error code 400 : Invalid Client Details");
          return new ResponseEntity<>("Invalid Client Details", HttpStatus.BAD_REQUEST);
        }
        log.debug("Error code 200 : Successfully revoked the " + tokenType);
        return new ResponseEntity<>("Successfully revoked the " + tokenType, HttpStatus.OK);
      }
      return responseObj;
    } catch (Exception e) {
      log.error("Error code 400 : Error in Processing the Request");
      return new ResponseEntity<>("Error in Processing the Request", HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/api/token/introspect", method = RequestMethod.POST)
  public ResponseEntity<?> introspectToken(
      HttpServletRequest request, HttpServletResponse response) {
    log.info(
        "Revoke Token url:" + request.getRequestURL().append('?').append(request.getQueryString()));

    try {
      Map<String, Object> m = new HashMap<String, Object>();
      ResponseEntity<?> responseObj = null;
      String credentials = request.getHeader("Authorization");
      String token = request.getParameter("token");
      log.info("Received Credentials:::::{}", credentials);
      log.info("Received Token:::::{}", token);
      if (!credentials.isEmpty()) {
        credentials = credentials.substring(6);
        log.info("credentials = " + credentials);
        String cred = CommonUtil.base64Decoder(credentials);
        String[] credList = cred.split(":", 2);
        log.info(credList[0]);
        log.info(credList[1]);
        String clientId = credList[0];
        String clientSecret = credList[1];
        AuthorizationDetails authTemp = authTempService.getAuthById(clientId);
        if (authTemp != null) {
          String expiryTime = authTemp.getExpiry();
          Integer currentTime =
              CommonUtil.convertTimestampToUnixTime(
                  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                      .format(new Timestamp(System.currentTimeMillis())));
          if (authTemp.getAccessToken().equals(token)
              && CommonUtil.convertTimestampToUnixTime(expiryTime) + 3600 > currentTime) {
            log.info("Token is valid");
            m.put("exp", CommonUtil.convertTimestampToUnixTime(expiryTime) + 3600);
            // m.put("sub", clientDetails.getContact_name());
            m.put("grant_type", "authorization_code");
            m.put("active", "true");
            m.put("token_type", "Bearer");
            m.put("scope", authTemp.getScope());
            m.put("client_id", authTemp.getClientId());
            m.put("iat", CommonUtil.convertTimestampToUnixTime(expiryTime));
            response.addHeader("Content-Type", "application/json");
            response.addHeader("Cache-Control", "no-store");
            response.addHeader("Pragma", "no-cache");
          } else {
            m.put("active ", "false");
          }
        } else {
          m.put("error", "Invalid Client Credentials");
          response.addHeader("Content-Type", "application/json");
          response.addHeader("Cache-Control", "no-store");
          response.addHeader("Pragma", "no-cache");
          return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
        }
      }
      return new ResponseEntity<>(m, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Error in Processing the Request");
      return new ResponseEntity<>("Error in Processing the Request", HttpStatus.BAD_REQUEST);
    }
  }

  private boolean codeChallengeVerifier(String codeChallenge, String codeVerifier)
      throws UnsupportedEncodingException, NoSuchAlgorithmException {
    log.info(
        "Verifying the CodeChallenge:::::{} with CodeVerifier:::::{}", codeChallenge, codeVerifier);
    boolean isCodeMatches = false;
    byte[] bytes = codeVerifier.getBytes("US-ASCII");
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    messageDigest.update(bytes, 0, bytes.length);
    byte[] digest = messageDigest.digest();
    String codeChallengeResult =
        java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    if (codeChallengeResult.equals(codeChallenge)) {
      isCodeMatches = true;
      log.info("Code Challenge Matches as Expected");
    }
    return isCodeMatches;
  }
}
