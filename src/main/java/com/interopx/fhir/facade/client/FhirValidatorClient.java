package com.interopx.fhir.facade.client;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.interopx.fhir.facade.service.FacadeConfigurationService;

import ca.uhn.fhir.context.FhirContext;

@Component
public class FhirValidatorClient {

	private static final Logger logger = LoggerFactory.getLogger(FhirValidatorClient.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	FacadeConfigurationService facadeConfigurationService;

	public boolean validateResource(@RequestBody Resource theResource) {
		try {
			String theResponse = null;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			if (theResource != null) {
				String url = facadeConfigurationService.getFhirValidatorUrl();
				if(StringUtils.isNotBlank(url) && !url.endsWith("/")) {
					url = url+"/";
				}
				url = url+"r4/resource/validate";
				logger.info("FHIR Validator url {} ", url);
				String stringResource = FhirContext.forR4().newJsonParser().encodeResourceToString(theResource);
				HttpEntity<String> entity = new HttpEntity<String>(stringResource, headers);
				theResponse = restTemplate.exchange(url,
						HttpMethod.POST, entity, String.class).getBody();

			} 
			if (!theResponse.contains("error")) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Exception for validateResource in FhirValidatorClient class ", e);
		}
		return false;
	}
}
