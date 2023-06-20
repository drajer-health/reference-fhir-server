package com.interopx.fhir.auth.server.controller;

import com.interopx.fhir.auth.server.jwtservice.JwtTokenUtil;
import com.interopx.fhir.auth.server.jwtservice.JwtUserDetailsService;
import com.interopx.fhir.auth.server.jwtservice.RefreshTokenService;
import com.interopx.fhir.auth.server.model.JwtRequest;
import com.interopx.fhir.auth.server.model.JwtResponse;
import com.interopx.fhir.auth.server.model.RefreshToken;
import com.interopx.fhir.auth.server.model.TokenRefreshRequest;
import com.interopx.fhir.auth.server.model.TokenRefreshResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

//  /** The logger. */
//  private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//  @Value("${jwt.expiration}")
//  private int jwtExpirationInSeconds;
//
//  @Value("${jwt.refresh.expiration}")
//  private int jwtrefreshExpirationInSeconds;
//
//  @Autowired private AuthenticationManager authenticationManager;
//
//  @Autowired private JwtTokenUtil jwtTokenUtil;
//
//  @Autowired private JwtUserDetailsService userDetailsService;
//
//  @Autowired RefreshTokenService refreshTokenService;
//
//  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
//  public ResponseEntity<?> createAuthenticationToken(
//      @RequestBody JwtRequest authenticationRequest, HttpServletRequest request) throws Exception {
//    logger.debug("Request received for authenticate ");
//    Authentication authentication =
//        authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(
//                authenticationRequest.getUsername(), authenticationRequest.getPassword()));
//
//    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//    final UserDetails userDetail = (UserDetails) authentication.getPrincipal();
//    final UserDetails userDetails =
//        userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//
//    final String token = jwtTokenUtil.generateJwtToken(userDetails);
//
//    String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//    logger.debug("End in createAuthenticationToken() of JwtAuthenticationController class ");
//    return ResponseEntity.ok(
//        new JwtResponse(token, refreshToken, userDetail.getUsername(), jwtExpirationInSeconds));
//  }
//
//  private void authenticate(String username, String password) throws Exception {
//    try {
//      authenticationManager.authenticate(
//          new UsernamePasswordAuthenticationToken(username, password));
//    } catch (BadCredentialsException e) {
//      logger.error(
//          "Exception in authenticate() of JwtAuthenticationController class with INVALID_CREDENTIALS ",
//          e);
//      throw new Exception("INVALID_CREDENTIALS", e);
//    }
//  }
//
//  @PostMapping("/refreshtoken")
//  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
//    logger.debug("Request received for refreshtoken ");
//    String requestRefreshToken = request.getRefreshToken();
//    RefreshToken token = refreshTokenService.findByToken(requestRefreshToken);
//    RefreshToken verifiedToken = refreshTokenService.verifyExpiration(token);
//    final UserDetails userDetails =
//        userDetailsService.loadUserByUsername(verifiedToken.getUser().getUser_name());
//    String accessToken = jwtTokenUtil.generateJwtToken(userDetails);
//    String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
//    logger.debug("End in refreshtoken() of JwtAuthenticationController class ");
//    return ResponseEntity.ok(
//        new TokenRefreshResponse(accessToken, refreshToken, jwtExpirationInSeconds));
//  }
}
