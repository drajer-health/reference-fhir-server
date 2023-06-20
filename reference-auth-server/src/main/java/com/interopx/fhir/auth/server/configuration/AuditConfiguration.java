//package com.interopx.fhir.auth.server.configuration;
//
//import com.ix.audit.log.AuditInterceptor;
//import com.ix.audit.log.advice.AuditTrackerAdvice;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Component
//@Configuration
//public class AuditConfiguration implements WebMvcConfigurer {
//  @Autowired AuditTrackerAdvice advice;
//
//  @Value("${audit.enable}")
//  private boolean isAuditEnable;
//
//  @Autowired private AuditInterceptor auditInterceptor;
//
//  static final Logger logger = LoggerFactory.getLogger(AuditConfiguration.class);
//
//  @Override
//  public void addInterceptors(InterceptorRegistry registry) {
//
//    logger.info("Audit Check :{} ", isAuditEnable);
//    if (isAuditEnable) {
//      registry.addInterceptor(auditInterceptor);
//      advice.setEnabled(true);
//      logger.info("Advice enabled:{} ", advice.isEnabled());
//      logger.info("Registered Interceptor successfuly...");
//    } else {
//      advice.setEnabled(false);
//      logger.info("Advice enabled:{} ", advice.isEnabled());
//      logger.info("Skipped Interceptor/Advice ..");
//    }
//  }
//}
