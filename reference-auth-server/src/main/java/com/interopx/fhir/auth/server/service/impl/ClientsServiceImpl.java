package com.interopx.fhir.auth.server.service.impl;

import ch.qos.logback.classic.Logger;
import com.interopx.fhir.auth.server.dao.ClientsDao;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.model.CurrentUserDetails;
import com.interopx.fhir.auth.server.model.Users;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.interopx.fhir.auth.server.service.UsersService;
import com.interopx.fhir.auth.server.util.CommonUtil;
import com.interopx.fhir.auth.server.util.AuthUtil;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service("clientRegistrationService")
@Transactional
public class ClientsServiceImpl implements ClientsService {
  private static final Logger logger = (Logger) LoggerFactory.getLogger(ClientsServiceImpl.class);

  @Autowired(required = true)
  private ClientsDao clientDao;

  @Autowired UsersService userRegistrationService;

  @Autowired CurrentUserDetails currentUserDetails;
  
  protected static final RestTemplate restTemplate = new RestTemplate();

  public static final String BACKEND_SERVICE_TOKEN_END_POINT = "/api/backendservice/token";

  @Override
  public Clients registerClient(Clients client) {
    logger.debug("Start in registerClient() of ClientRegistrationServiceImpl class ");
    try {
      String registerToken = CommonUtil.generateRandomString(250);
      client.setRegisterToken(registerToken);

      String client_id = CommonUtil.generateRandomString(30);
      client.setClientId(client_id);

      if (client.isConfidentialClient()) {
        String client_secret = CommonUtil.base64Encoder(CommonUtil.generateRandomString(50));
        client.setClientSecret(client_secret);
      }

      String clientLaunchId = RandomStringUtils.randomAlphanumeric(8);
      client.setLaunchId(clientLaunchId);

      client.setApprovedStatus(ApprovedStatus.PENDING);
      if (ObjectUtils.isNotEmpty(client)
          && ObjectUtils.isNotEmpty(client.isDynamicClient())
          && client.isDynamicClient()) {
        client.setApprovedStatus(ApprovedStatus.APPROVED);
      }

    } catch (Exception e) {
      logger.error("Exception in registerClient() of ClientRegistrationServiceImpl class ", e);
    }
    logger.debug("End in registerClient() of ClientRegistrationServiceImpl class ");
    return clientDao.registerClient(client);
  }
  
  public Clients updateClient(Clients client) {
	    if (client.isConfidentialClient()) {
	        if (client.getClientSecret() == null) {
	          String client_secret = CommonUtil.base64Encoder(CommonUtil.generateRandomString(50));
	          client.setClientSecret(client_secret);
	        } else {
	          client.setClientSecret(client.getClientSecret());
	        }

	      } else {
	        client.setClientSecret(null);
	      }
	    
	  return clientDao.updateClient(client);
  }

  public Clients updateClientNew(Clients client) {

    if (client.isConfidentialClient()) {
      if (client.getClientSecret() == null) {
        String client_secret = CommonUtil.base64Encoder(CommonUtil.generateRandomString(50));
        client.setClientSecret(client_secret);
      } else {
        client.setClientSecret(client.getClientSecret());
      }

    } else {
      client.setClientSecret(null);
    }
    
	  if(clientDao.verifyClientWithUser(client.getId(), client.getClientId(), currentUserDetails.getUserId())) {
		  client.setUserId(currentUserDetails.getUserId());
		  return clientDao.updateClient(client); 
	  }
	  throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Client does not belog to authorized user OR invalid clientId");
  }

  public Clients updateBackendClient(Clients client, HttpServletRequest request) {
	  
	  if(clientDao.verifyClientWithUser(client.getId(), client.getClientId(), currentUserDetails.getUserId())) {
		  Clients clientDetails = clientDao.getClient(client.getClientId());
		  clientDetails.setName(client.getName());
		  clientDetails.setOrgName(client.getOrgName());
		  clientDetails.setAlgorithmUsed(client.getAlgorithmUsed());
		  clientDetails.setCustomerId(client.getCustomerId());
		  clientDetails.setCenterId(client.getCenterId());
		  clientDetails.setScope(client.getScope());
		  clientDetails.setScopeType(client.getScopeType());
		  clientDetails.setUserId(currentUserDetails.getUserId());
		  return clientDao.updateClient(clientDetails);  
	  }
	  throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Client does not belog to authorized user OR invalid clientId");
  }

  @Override
  @Transactional
  public Clients getClientByDetails(String clientId, String regtoken) {
    return clientDao.getClientByDetails(clientId, regtoken);
  }

  @Override
  @Transactional
  public Clients getClientByCredentials(String clientId, String clientSecret) {
    return clientDao.getClientByCredentials(clientId, clientSecret);
  }

  @Override
  public Clients getClient(String clientId) {
    return clientDao.getClient(clientId);
  }

  @Override
  public List<Clients> getClientsByUserId(String userId) {
	  return clientDao.getClientsByUserId(userId);
//    return setUserDetails(clientDao.getClientsByUserId(userId));
  }

  @Override
  public Clients registerBackendClient(HashMap<String, String> params, HttpServletRequest request) {
    logger.debug("Start in registerBackendClient() of ClientRegistrationServiceImpl class ");
    Clients backendClient = new Clients();
    try {
    	backendClient.setUserEmail(params.get("user_email"));
    	backendClient.setUserName(params.get("user_name"));
      String publicKey = readPublicKeyFromUrl(params.get("jku"));
      backendClient.setPublicKey(publicKey);
      backendClient.setAlgorithmUsed(AuthUtil.getAlgorithmUsedEnum(params.get("algorithmUsed")));
      backendClient.setApprovedStatus(ApprovedStatus.PENDING);
      backendClient.setJku(params.get("jku"));
      backendClient.setIsBackendClient(true);
      backendClient.setName(params.get("name"));
      backendClient.setOrgName(params.get("org_name"));
      backendClient.setScopeType(params.get("scopeType"));
      backendClient.setCustomerId(params.get("customerId"));
      backendClient.setCenterId(params.get("centerId"));
      if (params.get("userId") != null) {
        backendClient.setUserId(params.get("userId"));
      }
      backendClient.setIssuer(params.get("issuer"));
      backendClient.setScope(params.get("scope"));
      String client_id = CommonUtil.generateRandomString(25);
      backendClient.setClientId(client_id);
      String tokenEndpoint =
              request.getScheme()
                  + "://"
                  + request.getServerName()
                  + ("http".equals(request.getScheme()) && request.getServerPort() == 80
                          || "https".equals(request.getScheme()) && request.getServerPort() == 443
                      ? ""
                      : ":" + request.getServerPort())
                  + request.getContextPath() + BACKEND_SERVICE_TOKEN_END_POINT;
      backendClient.setTokenEndPoint(tokenEndpoint);

    } catch (ResponseStatusException e) {
    	throw e;
//        logger.error(
//                "Exception in registerBackendClient() of ClientRegistrationServiceImpl class ", e);
    } catch (Exception e) {
      logger.error(
          "Exception in registerBackendClient() of ClientRegistrationServiceImpl class ", e);
    } 
    logger.debug("End in registerBackendClient() of ClientRegistrationServiceImpl class ");
    return clientDao.registerClient(backendClient);
  }

  @Override
  public List<Clients> getClientsByUserIdAndStatus(String userId, ApprovedStatus status) {
	  return clientDao.getClientsByUserIdAndStatus(userId, status);
//    return setUserDetails(clientDao.getClientsByUserIdAndStatus(userId, status));
  }

  public String readPublicKeyFromUrl(String url) {
    logger.debug("End in readvalueFromApi() of ClientRegistrationServiceImpl class ");
    try {
      MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
      headers.add("Accept", "application/json");
      headers.add("Content-Type", "application/json");
      HttpEntity<Object> request = new HttpEntity<>(headers);
      ResponseEntity<String> exchange =
          this.restTemplate.exchange(url, HttpMethod.GET, request, String.class);
      String body = exchange.getBody();
      logger.info("!!!!!!!!!!api response body !!!!!!!" + body);
      return body;
    } catch (Exception e) {
      logger.debug(
          "End in readvalueFromApi() of ClientRegistrationServiceImpl class error while getting public key");
      return null;
    }
  }

  @Override
  public List<Clients> getClientsByApprovedStatus(ApprovedStatus status) {
	  return clientDao.getClientsByApprovedStatus(status);	  
//    return setUserDetails(clientDao.getClientsByApprovedStatus(status));
  }

  @Override
  public List<Clients> getAllClients() {
	  return clientDao.getAllClients();
//    return setUserDetails(clientDao.getAllClients());
  }

//  public List<Clients> setUserDetails(List<Clients> clientList) {
//    clientList
//        .stream()
//        .forEach(
//            obj -> {
//              Users dafUserRegister = userRegistrationService.getUserById(obj.getUserId());
//              if(dafUserRegister != null) {
//                  obj.setUserName(dafUserRegister.getFirstName());
//                  obj.setUserEmail(dafUserRegister.getUser_email());            	  
//              }
//            });
//    return clientList;
//  }

  @Override
  public Clients getClientById(Integer id) {
    return clientDao.getClientById(id);
  }
}
