package com.interopx.fhir.auth.server.service.impl;

import com.interopx.fhir.auth.server.dao.ResourceListDao;
import com.interopx.fhir.auth.server.service.ResourceListService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("resourceListServiceImpl")
@Transactional
public class ResourceListServiceImpl implements ResourceListService {

  @Autowired ResourceListDao resourceListDao;

  @Override
  public List<String> getResourcesByGroup(Integer groupId) {
    return resourceListDao.getResourcesByGroup(groupId);
  }
}
