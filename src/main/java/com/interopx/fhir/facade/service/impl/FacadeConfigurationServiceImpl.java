package com.interopx.fhir.facade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interopx.fhir.facade.dao.FacadeConfigurationDao;
import com.interopx.fhir.facade.service.FacadeConfigurationService;

@Service
@Transactional
public class FacadeConfigurationServiceImpl implements FacadeConfigurationService {

	@Autowired
	FacadeConfigurationDao facadeConfigurationDao;
	@Override
	public String getFhirValidatorUrl() {
		return facadeConfigurationDao.getFhirValidatorUrl();
	}
	@Override
	public Boolean getFhirValidationStatus() {
		return facadeConfigurationDao.getFhirValidationStatus();
	}

}
