package com.interopx.fhir.auth.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("schedular")
public class SchedularProperties {

  private Long timePeriod;

  public Long getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(Long timePeriod) {
    this.timePeriod = timePeriod;
  }
}
