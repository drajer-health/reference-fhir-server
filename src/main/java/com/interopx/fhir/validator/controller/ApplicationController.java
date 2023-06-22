package com.interopx.fhir.validator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** The Class ApplicationController. */
@RestController
public class ApplicationController {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

  /**
   * Swagger home page.
   *
   * @return the model and view
   */
  @GetMapping(value = "/actuator/info")
  public ModelAndView swaggerHomePage() {
    LOGGER.info("Entered - swaggerHomePage Method in ApplicationController ");

    return new ModelAndView("redirect:/swagger-ui.html");
  }

  /**
   * Swagger page.
   *
   * @return the model and view
   */
  @GetMapping(value = "")
  public ModelAndView swaggerPage() {
    LOGGER.info("Entered - swaggerPage Method in ApplicationController ");

    return new ModelAndView("redirect:/swagger-ui.html");
  }
}
