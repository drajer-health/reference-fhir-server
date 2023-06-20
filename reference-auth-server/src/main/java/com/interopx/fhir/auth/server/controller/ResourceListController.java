package com.interopx.fhir.auth.server.controller;

import com.interopx.fhir.auth.server.service.ResourceListService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceListController {
  private static final Logger logger = LoggerFactory.getLogger(ResourceListController.class);

  @Autowired ResourceListService resourceListService;

  /**
   * get the resources by type
   *
   * @param type
   * @param request
   * @return
   */
  @RequestMapping(value = "/api/getresources", method = RequestMethod.GET)
  @ResponseBody
  public List<String> registerClient(@QueryParam("type") String type, HttpServletRequest request) {
    logger.debug("Request received to get resource list");
    List<String> resourcesTypeList = new ArrayList<String>();
    if (type != null && type.equalsIgnoreCase("clinical")) {
      resourcesTypeList = resourceListService.getResourcesByGroup(3);
    } else if (type != null && type.equalsIgnoreCase("financial")) {
      resourcesTypeList = resourceListService.getResourcesByGroup(2);
    } else if (type != null && type.equalsIgnoreCase("administrative")) {
      resourcesTypeList = resourceListService.getResourcesByGroup(1);
    } else if (type != null && type.equalsIgnoreCase("security")) {
      resourcesTypeList = resourceListService.getResourcesByGroup(4);
    } else if (type != null && type.equalsIgnoreCase("all")) {
      resourcesTypeList = resourceListService.getResourcesByGroup(null);
    }
    return resourcesTypeList;
  }
}
