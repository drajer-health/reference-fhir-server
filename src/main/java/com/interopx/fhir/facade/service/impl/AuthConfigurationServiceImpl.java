package com.interopx.fhir.facade.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interopx.fhir.facade.dao.AuthConfigurationDao;
import com.interopx.fhir.facade.model.AuthConfiguration;
import com.interopx.fhir.facade.service.AuthConfigurationService;

@Service
@Transactional
public class AuthConfigurationServiceImpl implements AuthConfigurationService {

	@Autowired
	AuthConfigurationDao authConfigurationDao;
	@Override
	public List<AuthConfiguration> getAuthConfiguration() {
		return authConfigurationDao.getAuthConfiguration();
	}
	@Override
	public  String getBaseURL(String str) {
		String baseUrlTrimmed = null;
		int index = getIndex(str);
		if(index == 0) {
			String str1 = str.substring(0,str.lastIndexOf("/"));
			baseUrlTrimmed = str1.substring(0,str1.lastIndexOf("/"));
		}else {
			String baseUrl = str.substring(0,index);		
			baseUrlTrimmed = baseUrl.substring(0,baseUrl.lastIndexOf("/"));
		}
		return baseUrlTrimmed;
		
	}
	
	private  int getIndex(String str) {
		int position = 0 ;
		for (int i = 0; i < str.length(); i++) {
		    if (str.charAt(i) == '?') {
		    	position = i;
		        break;
		    }
		}
		
		return position;	
		
	}
	
}
