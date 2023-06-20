package com.interopx.fhir.auth.server.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.model.CurrentUserDetails;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.util.AuthUtil;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;

@RestController
public class ClientRegistrationController {

  private static final Logger logger = LoggerFactory.getLogger(ClientRegistrationController.class);
  @Autowired private ClientsService registerService;

  @Autowired private CurrentUserDetails currentUserDetails;

  /**
   * This method registers the client
   *
   * @param client
   * @return This method returns registered client
   */
  @RequestMapping(value = "/api/client/", method = RequestMethod.POST)
  @ResponseBody
//  @TrackRequest
  public Clients registerClient(@RequestBody Clients client, HttpServletRequest request) {
    logger.debug("Request received to register client");
    client.setUserId(currentUserDetails.getUserId());
    client.setUserEmail(currentUserDetails.getEmail());
    client.setName(currentUserDetails.getName());
    return registerService.registerClient(client);	
  }

  /**
   * This method updates the client
   *
   * @param client
   * @return This method returns registered client
   */
  @RequestMapping(value = "/api/client/", method = RequestMethod.PUT)
  @ResponseBody
//  @TrackRequest
  public Clients updateClient(@RequestBody Clients client) {
    logger.debug("Request received to update client.");
    return registerService.updateClientNew(client);	
  }

  /**
   * This method used to get the client details with clientId and register token
   *
   * @param clientId
   * @param regtoken
   * @return This method returns client, if not exist returns null
   * @throws IOException
   */
  @RequestMapping(value = "/api/client/details", method = RequestMethod.GET)
  @ResponseBody
  public Clients getClientByDetails(
      @RequestParam("clientId") String clientId,
      @RequestParam("regtoken") String regtoken,
      HttpServletResponse response)
      throws IOException {
    logger.debug(
        "Request received to retrive client by details with parameters clientId="
            + clientId
            + "and regtoken="
            + regtoken);
    Clients client = registerService.getClientByDetails(clientId, regtoken);
    if (client == null) {
      logger.error("Error code 401 : Invalid Client id or Registration Token.");
      response.sendError(
          HttpServletResponse.SC_NOT_FOUND, "Invalid Client id or Registration Token.");
    }

    return client;
  }

  /**
   * This method is used to get the client with client id and client secret
   *
   * @param clientId
   * @param clientSecret
   * @return This method returns client, if not exist returns null
   */
  @RequestMapping(value = "/api/client/credentials", method = RequestMethod.GET)
  @ResponseBody
  public Clients getClientByCredentials(
      @RequestParam("clientId") String clientId,
      @RequestParam("clientSecret") String clientSecret) {
    logger.debug(
        "Request received to retrive client details by credentials with parameters clientId="
            + clientId
            + "and clientSecret="
            + clientSecret);
    return registerService.getClientByCredentials(clientId, clientSecret);
  }

  /**
   * This method is used to get the clients with user id
   *
   * @param userId
   * @return This method returns list of clients, if not exist returns null
   */
  @RequestMapping(
      value = {"/api/client/list/{userId}", "/api/client/list/{userId}/{approvedStatus}"},
      method = RequestMethod.GET)
  @ResponseBody
  public List<Clients> getClientsByUserId(
      @PathVariable String userId, @PathVariable(required = false) ApprovedStatus approvedStatus) {
    logger.debug("Request received to retrive client by user id : " + approvedStatus);
    if(validateUserIdByToken(userId.toString())) {
        if (approvedStatus == null) {
            return registerService.getClientsByUserId(userId);
          } else {
            return registerService.getClientsByUserIdAndStatus(userId, approvedStatus);
          }
    }
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you are not authorized to get clients of user id " + userId);
  }

  /**
   * This method is used to get the clients by approved status
   *
   * @param userId
   * @return This method returns list of clients, if not exist returns null
   */
  @RequestMapping(value = "/api/client/listbystatus/{approvedStatus}", method = RequestMethod.GET)
  @ResponseBody
  public List<Clients> getClientsByApprovedStatus(@PathVariable ApprovedStatus approvedStatus) {
    logger.debug("Request received to retrive client by user id : " + approvedStatus);
    if(validateUserRole("Admin")) {
    	return registerService.getClientsByApprovedStatus(approvedStatus);	
    }
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only Admin user is allowed to perform this operation");
  }

  /**
   * This method is used to get the clients by approved status
   *
   * @param userId
   * @return This method returns list of clients, if not exist returns null
   */
  @RequestMapping(value = "/api/clients", method = RequestMethod.GET)
  @ResponseBody
  public List<Clients> getAllClients() {
    logger.debug("Request received to retrive All client");
    if(validateUserRole("Admin")) {
    	return registerService.getAllClients();	
    }
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only Admin user is allowed to perform this operation");
  }

  /**
   * This method registers the backend client
   *
   * @param client
   * @return This method returns registered backend client
   * @throws OAuthSystemException
   * @throws FHIRHapiException
   */
  @RequestMapping(value = "/api/client/backendclient/", method = RequestMethod.POST)
  @ResponseBody
//  @TrackRequest
  public Clients registerBackendClient(
      @RequestBody HashMap<String, String> params, HttpServletRequest request) {
	  logger.debug("Request received to register backend client");
	  params.put("userId", currentUserDetails.getUserId());
	  params.put("user_email", currentUserDetails.getEmail());
	  params.put("user_name", currentUserDetails.getName());
	  return registerService.registerBackendClient(params, request);
  }

  /**
   * This method update the backend client
   *
   * @param client
   * @return This method returns updated backend client
   * @throws OAuthSystemException
   * @throws FHIRHapiException
   */
  @RequestMapping(value = "/api/client/backendclient/", method = RequestMethod.PUT)
  @ResponseBody
//  @TrackRequest
  public Clients updateBackendClient(@RequestBody Clients client, HttpServletRequest request) {
    logger.debug("Request received to update backend client");
    return registerService.updateBackendClient(client, request);	
  }

  @RequestMapping(value = "/api/client/model", method = RequestMethod.GET)
  @ResponseBody
  public Clients getModel() {
    logger.debug("Request received to retrive client model details");
    return new Clients();
  }

  /**
   * This method add patient id to client
   *
   * @param clientId
   * @param patientId
   * @return
   */
  @RequestMapping(value = "/api/client/{clientId}/{patientId}", method = RequestMethod.PUT)
  @ResponseBody
//  @TrackRequest
  public ResponseEntity<?> addPatientIdToClient(
      @PathVariable Integer clientId, @PathVariable String patientId) {
    logger.debug("Add Patient Id to client");
    Clients dafClientRegister = registerService.getClientById(clientId);
    if (ObjectUtils.isNotEmpty(dafClientRegister)) {
      dafClientRegister.setPatientId(patientId);
      registerService.updateClient(dafClientRegister);
      logger.debug("Success code 200: Patient Id updated successfully.");
      return new ResponseEntity<Clients>(dafClientRegister, HttpStatus.OK);
    }
    logger.error("Error code 400: Failed to update patient id");
    return new ResponseEntity<String>("Failed to update patient id", HttpStatus.BAD_REQUEST);
  }

  @RequestMapping(value = "/api/client/status", method = RequestMethod.PUT)
  @ResponseBody
//  @TrackRequest
  public ResponseEntity<?> updateApproveStatus(@RequestBody Clients client) {
    logger.debug("Start in updateApproveStatus() for {}", client.getClientId());
    if(validateUserRole("Admin")) {
        Clients dafClientRegister = registerService.getClientById(client.getId());
        if (ObjectUtils.isNotEmpty(dafClientRegister)) {
          dafClientRegister.setApprovedStatus(
              AuthUtil.getEnumValue(client.getApprovedStatus().toString()));
          dafClientRegister.setReviewComments(client.getReviewComments());
          registerService.updateClient(dafClientRegister);
          logger.debug("Success code 200: Status updated successfully for {}", client.getClientId());
          return new ResponseEntity<Clients>(dafClientRegister, HttpStatus.OK);
        }
        logger.error("Error code 400: Failed to update status for {}", client.getClientId());
        return new ResponseEntity<String>("Failed to update status", HttpStatus.BAD_REQUEST);    	
    }
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only Admin user is allowed to perform this operation");
  }
  
  @RequestMapping(value = "/api/user", method = RequestMethod.GET)
  @ResponseBody
  public CurrentUserDetails getauth0USerDetails() {
    logger.debug("Request received to retrive client model details");
//    Map<String, Object> responseObj = new HashMap<>();
//    responseObj.put("userId", currentUserDetails.getUserId());
//    responseObj.put("roles", currentUserDetails.getUserRole());
//    CurrentUserDetails currentUserDetails1 = new CurrentUserDetails();
//    currentUserDetails1 = currentUserDetails;
    return currentUserDetails;
  }
  
  public boolean validateUserIdByToken(String userId) {
	  boolean isValid = false;
	  if(userId.equals(currentUserDetails.getUserId())) {
		  isValid = true;
	  }
	  return isValid;
  }
  
  public boolean validateUserRole(String givenRole) {
	  boolean isValidRole = false;
	  List<String> roles = currentUserDetails.getUserRole();
	  for(String role : roles) {
		  if(role.equals(givenRole)) {
			  isValidRole = true;
			  break;
		  }
	  }
	  return isValidRole;
  }
  
}
