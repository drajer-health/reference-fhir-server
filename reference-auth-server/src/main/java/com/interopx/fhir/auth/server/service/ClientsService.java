package com.interopx.fhir.auth.server.service;

import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface ClientsService {

  public Clients registerClient(Clients client);
  
  public Clients updateClient(Clients client);

  public Clients updateClientNew(Clients client);

  public Clients updateBackendClient(Clients client, HttpServletRequest request);

  public Clients getClientByDetails(String clientId, String regtoken);

  public Clients getClientByCredentials(String clientId, String clientSecret);

  public Clients getClient(String clientId);

  public List<Clients> getClientsByUserId(String userId);

  public Clients registerBackendClient(HashMap<String, String> params, HttpServletRequest request);

  public List<Clients> getClientsByUserIdAndStatus(String userId, ApprovedStatus status);

  public List<Clients> getClientsByApprovedStatus(ApprovedStatus status);

  public List<Clients> getAllClients();

  public Clients getClientById(Integer id);
}
