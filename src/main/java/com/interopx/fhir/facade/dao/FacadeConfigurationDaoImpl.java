package com.interopx.fhir.facade.dao;

import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interopx.fhir.facade.configuration.HibernateConfiguration;
import com.interopx.fhir.facade.model.FacadeConfiguration;
import com.interopx.fhir.facade.service.AbstractDao;

@Repository
public class FacadeConfigurationDaoImpl extends AbstractDao implements FacadeConfigurationDao {
	private final Logger logger = LoggerFactory.getLogger(FacadeConfigurationDaoImpl.class);
	@Autowired
	HibernateConfiguration hibernateConfig;
	@Override
	public String getFhirValidatorUrl() {
		try (Connection connection = hibernateConfig.getConnection()) {
			String query =
			          "select * from facade_config where facade_config_key = 'fhir_validator_url'";
			logger.info("Query is {}", query);
			FacadeConfiguration facadeConfiguration =
			         (FacadeConfiguration) getSession()
			          .createNativeQuery(query)
			          .addEntity(FacadeConfiguration.class)
			          .getSingleResult();
			return facadeConfiguration.getFacadeConfigValue();
		} catch (Exception e) {
			logger.error("Exception in FacadeConfigurationDaoImpl for getFhirValidatorUrl() ", e);
			throw new RuntimeException(e);
		}
	}
	

	@Override
	public Boolean getFhirValidationStatus() {
		try (Connection connection = hibernateConfig.getConnection()) {
			String query =
			          "select * from facade_config where facade_config_key = 'fhir_validation_enabled'";
			logger.info("Query is {}", query);
			FacadeConfiguration facadeConfiguration =
			         (FacadeConfiguration) getSession()
			          .createNativeQuery(query)
			          .addEntity(FacadeConfiguration.class)
			          .getSingleResult();
			String status = facadeConfiguration.getFacadeConfigValue();
			if ("true".equals(status)) {
			   return Boolean.parseBoolean(status);
			}
			else  {
				return false;
			}
		} catch (Exception e) {
			logger.error("Exception in FacadeConfigurationDaoImpl for getFhirValidationStatus() ", e);
			throw new RuntimeException(e);
		}
	}
}
