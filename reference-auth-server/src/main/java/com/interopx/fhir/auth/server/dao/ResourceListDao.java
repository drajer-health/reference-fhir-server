package com.interopx.fhir.auth.server.dao;

import java.util.List;

public interface ResourceListDao {

  List<String> getResourcesByGroup(Integer groupId);
}
