package com.interopx.fhir.auth.server.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.interopx.fhir.auth.server.dao.ResourceListDao;

@SpringBootTest
class ResourceListServiceImplTest {

	@MockBean
	ResourceListDao resourceListDao;
	
	@Autowired
	ResourceListServiceImpl resourceListServiceImpl;
	
	@Test
	void getResourcesByGroup() {
		String financial = "{Claim, ClaimResponse, Coverage, Coverage, InsurancePlan}";
		List<String> resourceList = new ArrayList<String>();
		resourceList.add(financial);
		when(resourceListDao.getResourcesByGroup(2)).thenReturn(resourceList);
		
		assertEquals(resourceList, resourceListServiceImpl.getResourcesByGroup(2));
	}

}
