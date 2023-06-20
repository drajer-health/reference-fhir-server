package com.interopx.fhir.auth.server.util;

import ch.qos.logback.classic.Logger;
import com.google.common.base.Strings;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
  private static final String CHAR_LIST =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_";

  static Logger logger = (Logger) LoggerFactory.getLogger(CommonUtil.class);

  @Autowired private ClientsService clientRegistrationService;

  // private static final int RANDOM_STRING_LENGTH = 250;

  public static String generateRandomString(int length) {

    StringBuffer randStr = new StringBuffer();
    for (int i = 0; i < length; i++) {
      int number = getRandomNumber();
      char ch = CHAR_LIST.charAt(number);
      randStr.append(ch);
    }
    return randStr.toString();
  }

  private static int getRandomNumber() {
    int randomInt = 0;
    Random randomGenerator = new Random();
    randomInt = randomGenerator.nextInt(CHAR_LIST.length());
    if (randomInt - 1 == -1) {
      return randomInt;
    } else {
      return randomInt - 1;
    }
  }

  public static String base64Encoder(String string) {

    // encoding byte array into base 64
    byte[] encoded = Base64.encodeBase64(string.getBytes());

    return new String(encoded);
  }

  public static String base64Decoder(String encodedString) {

    // decoding byte array into base64
    byte[] decoded = Base64.decodeBase64(encodedString);

    return new String(decoded);
  }

  public static int downloadFIleByName(File downloadFile, HttpServletResponse response)
      throws IOException {

    if (downloadFile.exists()) {
      FileInputStream inputStream = null;
      OutputStream outStream = null;

      try {
        inputStream = new FileInputStream(downloadFile);

        response.setContentLength((int) downloadFile.length());
        // response.setContentType(context.getMimeType("C:/JavaHonk/CustomJar.jar"));

        // response header
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // Write response
        outStream = response.getOutputStream();
        return IOUtils.copy(inputStream, outStream);

      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (null != inputStream) inputStream.close();
          if (null != inputStream) outStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return 1;
    } else {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found.");
      return 404;
    }
  }

  /**
   * provides base url
   *
   * @param request
   * @return
   */
  public static String getBaseUrl(HttpServletRequest request) {
    String baseUrl =
        request.getScheme()
            + "://"
            + request.getServerName()
            + ("http".equals(request.getScheme()) && request.getServerPort() == 80
                    || "https".equals(request.getScheme()) && request.getServerPort() == 443
                ? ""
                : ":" + request.getServerPort())
            + request.getContextPath();

    if (!baseUrl.endsWith("/")) {
      baseUrl = baseUrl.concat("/");
    }

    return baseUrl;
  }

  public static Integer convertTimestampToUnixTime(String timestamp) throws ParseException {
    if (timestamp != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

      int epoch = (int) (sdf.parse(timestamp).getTime() / 1000);

      return epoch;
    } else {
      return null;
    }
  }

  public static Date convertToDateFormat(String timeStamp) {
    SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

    Date parsedDate = null;
    try {
      parsedDate = sdf.parse(timeStamp);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return parsedDate;
  }

  public boolean isValid(JWT jsonWebToken) throws ParseException {
    logger.info("Start isValid() in Common class ");
    if (!(jsonWebToken instanceof SignedJWT)) {
      // unsigned assertion
      return false;
    }
    // else {

    JWTClaimsSet claims;
    try {
      claims = jsonWebToken.getJWTClaimsSet();
    } catch (ParseException e) {
      logger.debug("Invalid assertion claims");
      return false;
    }

    if (claims == null) {
      logger.debug("No claims found ");
      return false;
    } else {
      if (claims.getExpirationTime().before(new Date())) {
        logger.debug("Token Expired ");
        return false;
      }
    }
    
    if (Strings.isNullOrEmpty(claims.getSubject())) {
        logger.debug("subject not found, rejecting");
        return false;
    }
    
    Clients client = null;
    try {
      client = clientRegistrationService.getClient(claims.getSubject());
    } catch (Exception e) {
      return false;
    }
    if (client == null) {
      logger.debug("subject is not valid");
      return false;
    }

    return true;
  }

  // verify the requested scopes
  public boolean isValidScopes(
      String scopesRequested, List<String> registeredScope) {
    String scope = scopesRequested.replaceAll("\\s+", ",");
    List<String> reqScopes = Arrays.asList(scope.split(","));
    if (registeredScope.containsAll(reqScopes)) {
      return true;
    }
    return false;
  }

  public static String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }
}
