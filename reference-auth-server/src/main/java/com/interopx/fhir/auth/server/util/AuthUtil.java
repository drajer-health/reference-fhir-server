package com.interopx.fhir.auth.server.util;

import ch.qos.logback.classic.Logger;
import java.util.Random;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthUtil {
  private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthUtil.class);

  private static final String TOKEN_CHARS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final int TOKEN_LENGTH = 30;

  public static String generateToken() {
    logger.debug("Start in generateToken() of EmailUtil class ");

    StringBuilder token = new StringBuilder();
    try {
      Random rnd = new Random();
      while (token.length() < TOKEN_LENGTH) {
        int index = (int) (rnd.nextFloat() * TOKEN_CHARS.length());
        token.append(TOKEN_CHARS.charAt(index));
      }
    } catch (Exception e) {
      logger.error("Exception in generateToken() of EmailUtil class ", e);
    }
    logger.debug("End in generateToken() of EmailUtil class ");

    return token.toString();
  }

  /**
   * Client Approved status enum
   *
   * @author admin
   */
  public static enum ApprovedStatus {
    APPROVED,
    PENDING,
    REJECTED
  };

  /**
   * get Client Approved status enum object by string
   *
   * @param value
   * @return
   */
  public static ApprovedStatus getEnumValue(String value) {
    if (value.equals("APPROVED")) return ApprovedStatus.APPROVED;
    if (value.equals("PENDING")) return ApprovedStatus.PENDING;
    if (value.equals("REJECTED")) return ApprovedStatus.REJECTED;
    return null;
  }
  
  
  /**
   * Algorithm Used enum
   * 
   *
   */
  public static enum AlgorithmUsed {
	  RS384
  }
  
  public static AlgorithmUsed getAlgorithmUsedEnum(String value) {
	    if (value.equals("RS384")) return AlgorithmUsed.RS384;
	    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Algotithom " + value + " is not supported or valid" );
	  }
}
