package com.interopx.fhir.auth.server.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import ch.qos.logback.classic.Logger;

@Component
public class ChimeraClient {
	
	static Logger logger = (Logger) LoggerFactory.getLogger(CommonUtil.class);
	
	protected final RestTemplate restTemplate = new RestTemplate();
	
	  @Value("${auth0.client-url}")
	  private String auth0Clientbaseurl;
	  
	
	  public HashMap getAuth0UserDetails(String accessToken) {
		  HashMap auth0UserDetailsMap = null;
		    try {
		      MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		      headers.add("authorization", accessToken);
		      HttpEntity<Object> request = new HttpEntity<>(headers);
		      ResponseEntity<HashMap> exchange =
		          restTemplate.exchange(auth0Clientbaseurl + "/userinfo", HttpMethod.GET, request, HashMap.class);
		      auth0UserDetailsMap = exchange.getBody();
		    } catch (Exception e) {
		    	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting user details from auth0 client api");
		    }
		    return auth0UserDetailsMap;
		  }
	
//	  public List<String> getUserRoles(String url, String accessToken) {
//		  List<String> roles = null;
//		    try {
//		      MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//		      headers.add("authorization", "Bearer eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiaXNzIjoiaHR0cHM6Ly9kZXYtMnlxNHl4emcudXMuYXV0aDAuY29tLyJ9..NZrFolAwc_8NJuxr.v4QCd2GzwELvmbsbDpknYingrx4qq-qh20Gf7y0SIORvooIcqHa2g4xKFqce1SMTc4Y1ZeuyavWUktnRo72GetjOtwC4dMaa-m5xXqsCL0V2TM_IielhTPVEH70i57kBqlPwKYyAJ2YpTCR2WWU6LOOITyv9IEVb4Hx1e-vbCoziihHvinCirs2oZRU-bmhkO3mz4j-N-XdFD1OA6SiJsvoWkxu1Jvh6aUTia1HlENT5d45lC7Qd1U64DKFe2LNvkMvVCWgSJAGcJUM8JlCQ4IDjUNN9eDHiZ7BDKh1C1AJBugqfoGdUqnaBQy_32YOj4DIuXcincCAPOVMm0EyHgL6KElnuw4EMaDC3H7nMgy1wFW7K_gBRz9d1Fwwp4bmYkw.soQN9aaUCnb9HZOI13bwyw");
//		      HttpEntity<Object> request = new HttpEntity<>(headers);
//		      ResponseEntity<String> exchange =
//		          restTemplate.exchange("https://dev-2yq4yxzg.us.auth0.com/userinfo", HttpMethod.GET, request, String.class);
////		      roles = getRoleFromResponse(exchange.getBody());
//		    } catch(ResponseStatusException e) {
//		    	throw e;
//		    } catch (Exception e) {
//		    	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting roles from chimera client api");
//		    }
//		    return roles;
//		  }
	  
		@SuppressWarnings("unchecked")
		public List<String> getRoleFromResponse(HashMap responseObject, String key) {
			try {
				List<String> roleList = new ArrayList<>();
//				JSONObject responseObject = new JSONObject(responseString);
				ArrayList<String> roleArray = (ArrayList) responseObject.get(key);
				if(roleArray != null) {
					for(Object obj: roleArray) {
						roleList.add(obj.toString());
					}
				}
				return roleList;
			} catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error while reading role from chimera client api respons string");
			}
		}

}
