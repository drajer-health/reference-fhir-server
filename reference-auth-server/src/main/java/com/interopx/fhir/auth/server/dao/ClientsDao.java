package com.interopx.fhir.auth.server.dao;

import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;
import java.util.List;

public interface ClientsDao {

  public Clients registerClient(Clients client);

  public Clients updateClient(Clients client);

  public Clients getClientByDetails(String clientId, String regtoken);

  public Clients getClientByCredentials(String clientId, String clientSecret);

  public Clients getClient(String clientId);

  public List<Clients> getClientsByUserId(String userId);

  public List<Clients> getClientsByUserIdAndStatus(String userId, ApprovedStatus status);

  public List<Clients> getClientsByApprovedStatus(ApprovedStatus status);

  public List<Clients> getAllClients();

  public Clients getClientById(Integer id);

boolean verifyClientWithUser(Integer id, String clientId, String userId);
}
