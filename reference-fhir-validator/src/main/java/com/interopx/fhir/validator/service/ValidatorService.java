package com.interopx.fhir.validator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.interopx.fhir.validator.model.ValidationResultsDto;

import ca.uhn.fhir.context.FhirContext;

@Service
public interface ValidatorService {
	
	public List<ValidationResultsDto> validateResources(List<String> scenarioName,List<MultipartFile> sourceFiles,FhirContext r4Context,Integer dataSourceId,String filesLocation, String resourceName);

	public List<ValidationResultsDto> getReport(String name,String date);
}
