package com.interopx.fhir.facade.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.interopx.fhir.facade.model.AuthConfiguration;
import com.interopx.fhir.facade.service.AuthConfigurationService;
import com.interopx.fhir.facade.util.CommonUtil;

/**
 * 
 * @author xyram
 * This class provides list of metadata supported by Smart Launch app and
 * openid connect
 *
 */

@RestController
@RequestMapping("/.well-known")
public class SMARTController extends HttpServlet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AuthConfigurationService authConfigurationService;
	
	public static final String SMART_CONFIGURATION_URL = "smart-configuration";
	public static final String OPENID_CONFIGURATION_URL = "openid-configuration";
	
	private static final long serialVersionUID = 936659617930557226L;
	
	/** Provides metadata about SMART APP launch
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/" + SMART_CONFIGURATION_URL, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getWellKnowllSMATConfig(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> wellKnownConfig= new HashMap<>();
        logger.debug("inside the smart-configuration");
        try {
            
            List<AuthConfiguration> authConfigList = authConfigurationService.getAuthConfiguration();
    		if(authConfigList != null && !authConfigList.isEmpty()) {
    			  wellKnownConfig.put("authorization_endpoint",authConfigList.get(0).getAuthorizationEndpointUrl());
    		      wellKnownConfig.put("token_endpoint",authConfigList.get(0).getTokenEndpointUrl());
    		}
            String[] stringArrayMethods = { "client_secret_basic","client_secret_post","private_key_jwt" };      
    		String[] stringArrayScopes = { "openid","profile","fhirUser","patient/*.*","user/*.*","offline_access","launch","launch/patient","patient/Medication.read","patient/AllergyIntolerance.read","patient/CarePlan.read","patient/CareTeam.read","patient/Condition.read","patient/Device.read","patient/DiagnosticReport.read","patient/DocumentReference.read","patient/Encounter.read","patient/Goal.read","patient/Immunization.read","patient/Location.read","patient/MedicationRequest.read","patient/Observation.read","patient/Organization.read","patient/Patient.read","patient/Practitioner.read","patient/PractitionerRole.read","patient/Procedure.read","patient/Provenance.read","user/Medication.read","user/AllergyIntolerance.read","user/CarePlan.read","user/CareTeam.read","user/Condition.read","user/Device.read","user/DiagnosticReport.read","user/DocumentReference.read","user/Encounter.read","user/Goal.read","user/Immunization.read","user/Location.read","user/MedicationRequest.read","user/Observation.read","user/Organization.read","user/Patient.read","user/Practitioner.read","user/PractitionerRole.read","user/Procedure.read","user/Provenance.read","system/Medication.read","system/AllergyIntolerance.read","system/CarePlan.read","system/CareTeam.read","system/Condition.read",	"system/Device.read","system/DiagnosticReport.read","system/DocumentReference.read","system/Encounter.read","system/Goal.read","system/Immunization.read","system/Location.read","system/MedicationRequest.read","system/Observation.read","system/Organization.read","system/Patient.read","system/Practitioner.read","system/PractitionerRole.read","system/Procedure.read","system/Provenance.read","system/Group.read"};

    		String[] stringArrayResponce = {"code"};
            String[] stringArrayCapabilities = {"launch-ehr",
                    "launch-standalone",
                    "client-public",
                    "client-confidential-symmetric",
                    "context-passthrough-banner",
                    "context-passthrough-style",
                    "context-ehr-patient",
                    "context-ehr-encounter",
                    "context-standalone-patient",
                    "context-standalone-encounter",
                    "permission-offline",
                    "permission-patient",
                    "permission-user",
                    "sso-openid-connect", 
                    "context-banner", 
                    "context-style",
                    "permission-v2"};
            
            String[] codeChallengeMethodsSupported = {"S256"};
            String[] grantTypesSupported = {"authorization_code"};
            wellKnownConfig.put("token_endpoint_auth_methods_supported", stringArrayMethods);
            wellKnownConfig.put("scopes_supported", stringArrayScopes);
            wellKnownConfig.put("response_types_supported", stringArrayResponce);
            wellKnownConfig.put("capabilities", stringArrayCapabilities);
            wellKnownConfig.put("grant_types_supported", grantTypesSupported);
            wellKnownConfig.put("code_challenge_methods_supported", codeChallengeMethodsSupported);
        }
		catch(Exception e) {
			logger.error("Exception in getWellKnowllSMATConfig() of SmartController class ", e);
		}
        return wellKnownConfig;

    }
    
	/** Provides metadata about openid configuration
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	
    @RequestMapping(value = "/" + OPENID_CONFIGURATION_URL, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAuthorization(HttpServletRequest request, HttpServletResponse response){
    	logger.debug("inside the openid-configuration");
		Map<String, Object> m = new HashMap<>();
		try {
			m.put("issuer", CommonUtil.getFhirFacadeServerUrl());
			 List<AuthConfiguration> authConfigList = authConfigurationService.getAuthConfiguration();
			if(authConfigList != null && !authConfigList.isEmpty()) {
				  m.put("authorization_endpoint",authConfigList.get(0).getAuthorizationEndpointUrl());
			      m.put("token_endpoint",authConfigList.get(0).getTokenEndpointUrl());
			      m.put("jwks_uri",authConfigList.get(0).getJwksUri());
			}
			m.put("response_types_supported", Arrays.asList("code")); 
			m.put("claims_supported", Arrays.asList(
					"sub",
					"aud",
					"iss",
					"fhiruser"
					));
			m.put("id_token_signing_alg_values_supported", Arrays.asList("RS256"));
			m.put("token_endpoint_auth_methods_supported", Arrays.asList(
					"client_secret_basic",
					"client_secret_post",
					"client_secret_jwt",
					"private_key_jwt"
					));
		}
		catch(Exception e) {
			logger.error("Exception in getAuthorization() of SmartController class ", e);
		}
		return m;
	}
}

