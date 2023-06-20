package com.interopx.fhir.auth.server.service;

import java.util.List;

public interface ResourceListService {

  List<String> getResourcesByGroup(Integer groupId);
}
