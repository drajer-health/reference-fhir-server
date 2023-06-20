package com.interopx.fhir.auth.server.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.interopx.fhir.auth.server.jwtservice.JwtTokenUtil;
import com.interopx.fhir.auth.server.jwtservice.JwtUserDetailsService;
import com.interopx.fhir.auth.server.model.CurrentUserDetails;
import com.interopx.fhir.auth.server.util.ChimeraClient;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

  @Autowired private JwtUserDetailsService jwtUserDetailsService;
  
  @Autowired private JwtTokenUtil jwtTokenUtil;
  
  @Autowired private CurrentUserDetails currentUserDetails;
  
  @Autowired private ChimeraClient chimeraClient;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String accessToken = request.getHeader("authorization");

    String jwtToken = null;
    boolean isValidated = false;
    if (accessToken != null && accessToken.startsWith("Bearer ")) {
    	
    	try {
    		HashMap userDetails = chimeraClient.getAuth0UserDetails(accessToken);
//    		JSONObject jwtDecodedObj = JwtDecoder.decodeAuth0TokenGetUserId(requestTokenHeader.split(" ")[1]);
    		
    		String userId = null, name = null, email = null, userRole = null;
    		if(userDetails.containsKey("sub"))
    			userId = userDetails.get("sub").toString().split("\\|")[1];
    		if(userDetails.containsKey("nickname"))
    			name = (String) userDetails.get("nickname");
    		if(userDetails.containsKey("email"))
    			email = (String) userDetails.get("email");
    		
        	currentUserDetails.setUserId(userId);
        	currentUserDetails.setName(name);
        	currentUserDetails.setEmail(email);
        	
        	List roles = new ArrayList<String>();
        	roles.add("Admin");
        	
        	if(userHasAccess(accessToken, userId)) {
                currentUserDetails.setUserRole(roles);        		
        	}
        	else {
        		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to access this application");
        	}

    	} catch(ResponseStatusException e) {
    		response.sendError(
                    e.getStatus().value(),
                    e.getReason());
    	} catch(Exception e) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized: Authentication token is invalid.");
    	}

    	chain.doFilter(request, response);
    	
//    	jwtToken = requestTokenHeader.substring(7);
//      try {
//        isValidated = jwtTokenUtil.validateloginJwtToken(jwtToken);
//        if (isValidated) {
//        	String userEmail1 = jwtTokenUtil.getUserNameFromJwtToken(jwtToken);
//        	Map claims =  jwtTokenUtil.getClaimsFromJwtToken(jwtToken);
//        	Map userDetails = (Map) claims.get("userdetails");
//        	currentUserDetails.setUserId(userDetails.get("userId").toString());
//        	currentUserDetails.setUserRole((List<Map>) userDetails.get("userRole"));
//        	currentUserDetails.setEmail(userEmail1);
//          chain.doFilter(request, response);
//        } else {
//          logger.info("Authentication token is invalid.");
//          response.sendError(
//              HttpServletResponse.SC_UNAUTHORIZED,
//              "Unauthorized: Authentication token is invalid.");
//        }
//      } catch (IllegalArgumentException e) {
//        System.out.println("Unable to get JWT Token");
//      } catch (ExpiredJwtException e) {
//        System.out.println("JWT Token has expired");
//      }
      
    } else if (
//    		request.getRequestURI().contains("/sofroles") || 
    		request.getRequestURI().contains("/login")
//        || request.getRequestURI().contains("user/register")
        || request.getRequestURI().contains("api/authorize")
        || request.getRequestURI().contains("api/token")
        || request.getRequestURI().contains("token")
        || request.getRequestURI().contains("/api/introspect")
        || request.getRequestURI().contains("/config")
//        || request.getRequestURI().contains("/scopes")
//        || request.getRequestURI().contains("activate")
//        || request.getRequestURI().contains("client/list")
//        || request.getRequestURI().contains("/client/")
        || request.getRequestURI().contains("/metadata")
        || request.getRequestURI().contains("/api/jwk")) {
      chain.doFilter(request, response);
    } else if (request.getRequestURI().equals("/ix-auth-server/")
        || (request.getRequestURI().contains("/ix-auth-server/")
            && (request.getRequestURI().contains(".")))) {
      chain.doFilter(request, response);
    } else {
      logger.info("Authentication token is required.");
      response.sendError(
          HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Authentication token is not found.");
    }
  }

private boolean userHasAccess(String accessToken, String userId) {
	// Call the Access API to check if it contains FHIR Auth Server
	return true;
}
}
