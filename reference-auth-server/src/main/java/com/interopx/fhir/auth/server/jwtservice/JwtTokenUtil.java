package com.interopx.fhir.auth.server.jwtservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil implements Serializable {

  /** The logger. */
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final long serialVersionUID = -2550185165626007488L;

  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpirationInSeconds;

  @Value("${jwt.refresh.expiration}")
  private int refreshExpirationInSeconds;

  @Value("${jwt.loginsecret}")
  private byte[] loginTokenSecret;

  @Value("${jwt.logintoken.expiration}")
  private int loginTokenExpiration;

  public String generateJwtToken(UserDetails userPrincipal) {
    return generateTokenFromUsername(userPrincipal.getUsername(), userPrincipal.getPassword());
  }

  public String generateTokenFromUsername(String username, String password) {
    System.out.println(" USER Name And Password :: " + username + " ---  " + password);
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationInSeconds))
        .signWith(SignatureAlgorithm.HS512, password)
        .compact();
  }

  public String generateTokenForLogin(String username) {
    System.out.println(" USER Name And Password :: " + username);
    byte[] secret = loginTokenSecret;
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + loginTokenExpiration * 60 * 1000))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  // retrieve username from jwt token
  public String getUsernameFromToken(String token) {
    String name = getClaimFromToken(token, Claims::getSubject);
    System.out.println(" USER Name And Password :: " + name);
    return name;
  }

  // retrieve expiration date from jwt token
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  // for retrieveing any information from token we will need the secret key
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
  }

  // check if the token has expired
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  // generate token for user
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }

  public String doGenerateToken(Map<String, Object> claims, String subject) {

    byte[] secret = loginTokenSecret;
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + loginTokenExpiration * 60 * 1000))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  // validate token
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(loginTokenSecret).parseClaimsJws(token).getBody().getSubject();
  }
  
  public Map getClaimsFromJwtToken(String token) {
	  Map claims = Jwts.parser().setSigningKey(loginTokenSecret).parseClaimsJws(token).getBody();
    return claims;
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  public boolean validateloginJwtToken(String authToken) {
    byte[] secret = loginTokenSecret;
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
