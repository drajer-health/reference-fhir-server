package com.interopx.fhir.auth.server.dao.impl;

import com.interopx.fhir.auth.server.dao.AbstractDao;
import com.interopx.fhir.auth.server.dao.ResourceListDao;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceListDaoImpl extends AbstractDao implements ResourceListDao {

  Session session = null;
  final Logger log = (Logger) LoggerFactory.getLogger(ResourceListDaoImpl.class);

  @Override
  public List<String> getResourcesByGroup(Integer groupId) {
    log.debug("Start in getResourcesByGroup() of ResourceListDaoImpl class ");
    session = getSession();
    List<String> resourceList = null;
    try {
      String sql = "";
      if (groupId == null) {
        sql = "select resource_name from resource_list";
      } else {
        sql = "select resource_name from resource_list where resource_group_id=" + groupId;
      }
      Query query = getSession().createNativeQuery(sql);
      resourceList = (query).getResultList();
    } catch (Exception e) {
      log.error("Exception in getResourcesByGroup() of ResourceListDaoImpl class ");
    }
    log.info("End in getResourcesByGroup() of ResourceListDaoImpl class ");
    return resourceList;
  }
}
