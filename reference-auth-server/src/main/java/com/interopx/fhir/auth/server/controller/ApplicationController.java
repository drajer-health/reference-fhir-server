package com.interopx.fhir.auth.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** ApplicationController */
@RestController
public class ApplicationController {
  /**
   * swaggerHomePage
   *
   * @return ModelAndView value
   */
  @RequestMapping(value = "/api/actuator/info", method = RequestMethod.POST)
  public ModelAndView swaggerHomePage() {
    return new ModelAndView("redirect:/swagger-ui.html");
  }
  /**
   * swaggerPage
   *
   * @return ModelAndView value
   */
  @RequestMapping(value = "/api/")
  public ModelAndView swaggerPage() {
    return new ModelAndView("redirect:/swagger-ui.html");
  }
}
