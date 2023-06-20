package com.interopx.fhir.auth.server.jwtservice;

import ch.qos.logback.classic.Logger;
import com.interopx.fhir.auth.server.dao.RefreshTokenDao;
import com.interopx.fhir.auth.server.dao.UsersDao;
import com.interopx.fhir.auth.server.exception.TokenRefreshException;
import com.interopx.fhir.auth.server.model.RefreshToken;
import com.interopx.fhir.auth.server.model.Users;
import java.time.Instant;
import java.util.UUID;
import javax.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
  private static final Logger logger = (Logger) LoggerFactory.getLogger(RefreshTokenService.class);

  @Value("${jwt.refresh.expiration}")
  private Long refreshTokenDurationMs;

  @Autowired private RefreshTokenDao refreshTokenRepository;

  @Autowired private UsersDao userRepository;

  public RefreshToken findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public String createRefreshToken(String username) {
    logger.debug("Start in createRefreshToken() of RefreshTokenService class ");
    String message = null;
    RefreshToken refreshToken = new RefreshToken();
    try {
      refreshToken.setUser(userRepository.getUserByName(username));
      refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
      refreshToken.setToken(UUID.randomUUID().toString());
      message = refreshTokenRepository.save(refreshToken);
    } catch (Exception e) {
      logger.error("Exception in createRefreshToken() of RefreshTokenService class ", e);
    }
    logger.debug("End in createRefreshToken() of RefreshTokenService class ");
    return message;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    logger.debug("Start in verifyExpiration() of RefreshTokenService class ");
    try {
      if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
        refreshTokenRepository.delete(token);
        logger.error(
            "TokenRefreshException : Refresh token was expired. Please make a new signin request. Token is "
                + token.getToken());
        throw new TokenRefreshException(
            token.getToken(), "Refresh token was expired. Please make a new signin request");
      }
    } catch (Exception e) {
      logger.error("Exception in verifyExpiration() of RefreshTokenService class ", e);
    }
    logger.debug("End in verifyExpiration() of RefreshTokenService class ");
    return token;
  }

  @Transactional
  public int deleteByUserId(String userId) {
    logger.debug("Start in deleteByUserId() of RefreshTokenService class ");
    int value = 0;
    try {
      Users user = userRepository.getUserById(userId);
      value = refreshTokenRepository.deleteByUser(new Integer(user.getUser_id()));
    } catch (Exception e) {
      logger.error("Exception in deleteByUserId() of RefreshTokenService class ", e);
    }
    logger.debug("End in deleteByUserId() of RefreshTokenService class ");
    return value;
  }
}
