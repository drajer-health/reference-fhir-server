package com.interopx.fhir.facade.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class provides the version and build status for fhir facade
 * 
 * @author admin
 *
 */
@RestController
@RequestMapping("/build")
public class BuildVersionController {
	private static final Logger logger = LoggerFactory.getLogger(BuildVersionController.class);

	/**
	 * This method provides the build version of fhir facade
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getBuildVersionAndDate(HttpServletRequest request) {
		try {
			JSONObject buildNumber = new JSONObject();
		    InputStream is =
		            request.getServletContext().getResourceAsStream(
		                "/META-INF/MANIFEST.MF");
		    try (InputStream stream = new BufferedInputStream(is)) {
		        Manifest manifest = new Manifest(stream);
		        buildNumber.put("build-number", manifest.getMainAttributes().getValue("Build-Number"));
		        buildNumber.put("build-date", manifest.getMainAttributes().getValue("Build-Date"));
		    } catch (IOException e) {
		    	logger.error(e.getMessage(), e);
		    }
		    return new ResponseEntity<>(buildNumber.toString(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception in getBuildVersionAndDate() of  BuildVersionController class ", e);
		}
		return null;
	}
}
