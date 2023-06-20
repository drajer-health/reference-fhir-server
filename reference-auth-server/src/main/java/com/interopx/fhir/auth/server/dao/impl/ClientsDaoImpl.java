package com.interopx.fhir.auth.server.dao.impl;

import com.interopx.fhir.auth.server.dao.AbstractDao;
import com.interopx.fhir.auth.server.dao.ClientsDao;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.util.AuthUtil;
import com.interopx.fhir.auth.server.util.AuthUtil.ApprovedStatus;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ClientsDaoImpl extends AbstractDao implements ClientsDao {
  Session session = null;
  final Logger log = (Logger) LoggerFactory.getLogger(ClientsDaoImpl.class);

  @SuppressWarnings("deprecation")
  @Override
  public Clients registerClient(Clients client) {
    log.debug("Start in registerClient() of ClientRegistrationDaoImpl class ");
    session = getSession();

    Criteria criteria =
        getSession()
            .createCriteria(Clients.class)
            .add(Restrictions.eq("name", client.getName()).ignoreCase())
            .add(Restrictions.eq("userId", client.getUserId()));

    @SuppressWarnings("unchecked")
    List<Clients> existedClient = criteria.list();
    if (existedClient != null && existedClient.size() > 0) {
      log.error("Client Name is already existed. Please use a different Client Name.");
      throw new Error("Client Name is already existed. Please use a different Client Name.");
    } else {
      session.saveOrUpdate(client);
      log.debug(
          "Client saved successfully in registerClient() of ClientRegistrationDaoImpl class ");
      return client;
    }
  }

  public Clients updateClient(Clients client) {
    log.debug("Start in updateClient() of ClientRegistrationDaoImpl class ");

    try {
      if (client.getLaunchId() == null) {
        String clientLaunchId = RandomStringUtils.randomAlphanumeric(8);
        client.setLaunchId(clientLaunchId);
      }
      session = getSession();
      session.update(client);

    } catch (Exception e) {
      log.error("Exception in updateClient() of ClientRegistrationDaoImpl class ");
    }
    log.debug("End in updateClient() of ClientRegistrationDaoImpl class ");
    return client;
    // }
  }

  @SuppressWarnings("deprecation")
  @Override
  public Clients getClientByDetails(String clientId, String regtoken) {
    log.debug("Start in getClientByDetails() of ClientRegistrationDaoImpl class ");
    Clients client = null;
    try {
      Criteria crit =
          getSession()
              .createCriteria(Clients.class)
              .add(Restrictions.eq("clientId", clientId))
              .add(Restrictions.eq("registerToken", regtoken));
      client = new Clients();
      client = (Clients) crit.uniqueResult();
    } catch (Exception e) {
      log.error("Exception in getClientByDsetails() of ClientRegistrationDaoImpl class ");
    }
    log.debug("End in getClientByDetails() of ClientRegistrationDaoImpl class ");
    return client;
  }

  @SuppressWarnings("deprecation")
  @Override
  public Clients getClientByCredentials(String clientId, String clientSecret) {
    log.debug("Start in getClientByDetails() of ClientRegistrationDaoImpl class ");
    Clients client = null;
    try {
      Criteria crit =
          getSession()
              .createCriteria(Clients.class)
              .add(Restrictions.eq("clientId", clientId))
              .add(Restrictions.eq("clientSecret", clientSecret));
      client = new Clients();
      client = (Clients) crit.uniqueResult();
    } catch (Exception e) {
      log.error("Exception in getClientByCredentials() of ClientRegistrationDaoImpl class ");
    }
    log.debug("End in getClientByCredentials() of ClientRegistrationDaoImpl class ");
    return client;
  }

  @SuppressWarnings("deprecation")
  @Override
  public Clients getClient(String clientId) {
    log.debug("Start in getClient() of ClientRegistrationDaoImpl class ");
    Clients client = null;
    try {
      Criteria crit =
          getSession().createCriteria(Clients.class).add(Restrictions.eq("clientId", clientId));
      client = new Clients();
      client = (Clients) crit.uniqueResult();
    } catch (Exception e) {
    	e.printStackTrace();
      log.error("Exception in getClient() of ClientRegistrationDaoImpl class ");
    }
    log.debug("End in getClient() of ClientRegistrationDaoImpl class ");
    return client;
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Override
  public List<Clients> getClientsByUserId(String userId) {
    log.debug("Start in getClientsByUserId() of ClientRegistrationDaoImpl class ");
    List<Clients> clients = null;
    try {
      Criteria crit =
          getSession().createCriteria(Clients.class).add(Restrictions.eq("userId", userId));
      clients = new ArrayList<>();
      clients = crit.list();
    } catch (Exception e) {
      log.error("Exception in getClientsByUserId() of ClientRegistrationDaoImpl class ");
    }
    log.debug("End in getClientsByUserId() of ClientRegistrationDaoImpl class ");
    return clients;
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Override
  public List<Clients> getClientsByUserIdAndStatus(String userId, ApprovedStatus status) {
    log.debug("Start in getClientsByUserIdAndStatus() of ClientRegistrationDaoImpl class ");
    List<Clients> clients = null;
    try {
      Criteria crit =
          getSession()
              .createCriteria(Clients.class)
              .add(Restrictions.eq("userId", userId))
              .add(Restrictions.eq("approvedStatus", AuthUtil.getEnumValue(status.toString())));
      clients = new ArrayList<>();
      clients = crit.list();
    } catch (Exception e) {
      log.error("Exception in getClientsByUserIdAndStatus() of ClientRegistrationDaoImpl class ");
    }
    log.debug("End in getClientsByUserIdAndStatus() of ClientRegistrationDaoImpl class ");
    return clients;
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Override
  public List<Clients> getClientsByApprovedStatus(ApprovedStatus status) {
    log.debug("Start in getClientsByApprovedStatus() of ClientRegistrationDaoImpl class ");
    List<Clients> clients = null;
    try {
      Criteria crit =
          getSession()
              .createCriteria(Clients.class)
              .add(Restrictions.eq("approvedStatus", AuthUtil.getEnumValue(status.toString())));
      clients = new ArrayList<>();
      clients = crit.list();
    } catch (Exception e) {
      log.error(
          "Exception in getClientsByApprovedStatus() of ClientRegistrationDaoImpl class " + e);
    }
    log.debug("End in getClientsByApprovedStatus() of ClientRegistrationDaoImpl class ");
    return clients;
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Override
  public List<Clients> getAllClients() {
    log.debug("Start in getAllClients() of ClientRegistrationDaoImpl class ");
    List<Clients> clients = null;
    try {
      Criteria crit = getSession().createCriteria(Clients.class);
      clients = new ArrayList<>();
      clients = crit.list();
    } catch (Exception e) {
      log.error("Exception in getAllClients() of ClientRegistrationDaoImpl class ");
    }
    log.debug("End in getAllClients() of ClientRegistrationDaoImpl class ");
    return clients;
  }

  @Override
  public Clients getClientById(Integer id) {
    log.debug("Start in getClientById() of ClientRegistrationDaoImpl class ");
    Clients client = null;
    try {
      Criteria crit = getSession().createCriteria(Clients.class).add(Restrictions.eq("id", id));
      client = new Clients();
      client = (Clients) crit.uniqueResult();

    } catch (Exception e) {
      log.error("Exception in getClientById() of ClientRegistrationDaoImpl class ");
    }
    log.debug("End in getClientById() of ClientRegistrationDaoImpl class ");
    return client;
  }
  
  @Override
  public boolean verifyClientWithUser(Integer id, String clientId, String userId) {
    log.debug("Start in verifyClientWithUser()");
    String query = "select id from clients where id=" + id + " and client_id='" + clientId + "' and user_id='" + userId + "'";
    try {
    	Session session = getSession();
    	List queryResult = session.createNativeQuery(query).list();
    	
    	return queryResult.size() > 0;

    } catch (Exception e) {
      log.error("Exception in verifyClientWithUser()");
    }
    log.debug("End in verifyClientWithUser()");
    return false;
  }
}
