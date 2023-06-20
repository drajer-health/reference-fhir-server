package com.interopx.fhir.auth.server.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides list of public key(s) for JSON web token which could be used to verify JWT id token
 *
 * @author admin
 */
@RestController
public class JWKSetPublishingEndpoint extends HttpServlet {
  Logger logger = (Logger) LoggerFactory.getLogger(JWKSetPublishingEndpoint.class);
  @Autowired private JwtGenerator jwtGenerator;
  private static final long serialVersionUID = 1L;
  public static final String URL = "jwk";

  /**
   * Provides JSON web key
   *
   * @return
   */
  @RequestMapping(value = "/api/jwk", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, List<JSONObject>> getJwk() {
    logger.debug("Start in getJwk() of JWKSetPublishingEndpoint class ");
    try {
      Map<String, List<JSONObject>> keys = jwtGenerator.getAllPublicKeys();
      return keys;
    } catch (Exception e) {
      logger.error("Exception in getJwk() of JWKSetPublishingEndpoint class ");
    }
    return null;
  }
}
