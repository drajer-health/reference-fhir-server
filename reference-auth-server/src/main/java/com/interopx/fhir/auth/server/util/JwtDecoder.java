package com.interopx.fhir.auth.server.util;

import java.util.Base64;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class JwtDecoder {
	

	
	/**
	 * 
	 * @param token
	 * @return decoded JSONObject
	 */
	public static JSONObject decodeAuth0TokenGetUserId(String token) {
		try {
			String[] parts = token.split("\\.");
			return new JSONObject(decode(parts[1]));
		} catch(ResponseStatusException e) {
			throw e;
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error while decoding auth0 token");
		}
	}
	
	/**
	 * 
	 * @param token
	 * @return userId
	 */
	public static String readvalueFromAuth0Payload(JSONObject tokenPayload, String key) {
		try {
			return (String) tokenPayload.get(key);
		} catch(ResponseStatusException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error while reading userId from auth0 token payload");
		}
	}
	
	/**
	 * 
	 * @param encodedString
	 * @return decodedString
	 */
	private static String decode(String encodedString) {
	    return new String(Base64.getUrlDecoder().decode(encodedString));
	}

}
