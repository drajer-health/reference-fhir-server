package com.interopx.fhir.validator.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.interopx.fhir.validator.entity.FhirValidationSummaryReport;
import com.interopx.fhir.validator.model.ResultMetaData;
import com.interopx.fhir.validator.model.ValidationResultsDto;
import com.interopx.fhir.validator.model.ValidationResultsMetaData;
import com.interopx.fhir.validator.model.ReferenceError;

@Component
public class EntityToDtoMapper {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(EntityToDtoMapper.class);
	
	
	 public List<ValidationResultsDto> mapEntityWithDto(List<FhirValidationSummaryReport> results) {
		 logger.info("Entry in EntityToDtoMapper.mapEntityWithDto()");
		    List<ValidationResultsDto> validationResultsDtoList = new ArrayList<ValidationResultsDto>();
		    try {
		      for (FhirValidationSummaryReport fhirvalidationSummaryReport : results) {
		    	ValidationResultsMetaData validationResultsMetaData = new ValidationResultsMetaData();
		        ValidationResultsDto validationResultsDto = new ValidationResultsDto();
		        validationResultsMetaData.setFhirVersion(fhirvalidationSummaryReport.getFhirVersion());
		        validationResultsMetaData.setPatientName(fhirvalidationSummaryReport.getPatientName());
		        validationResultsMetaData.setGoldFileName(fhirvalidationSummaryReport.getGoldFileName());
		        validationResultsMetaData.setApuId(fhirvalidationSummaryReport.getApuId());
		        validationResultsMetaData.setTransactionId(fhirvalidationSummaryReport.getTransactionId());
		        validationResultsMetaData.setEmrVersion(fhirvalidationSummaryReport.getEmrVersion());
		        validationResultsMetaData.setAdaptorVersion(fhirvalidationSummaryReport.getAdaptorVersion());
		        validationResultsMetaData.setModality(fhirvalidationSummaryReport.getModality());
		        validationResultsMetaData.setError(fhirvalidationSummaryReport.getError());
		        validationResultsMetaData.setScores(fhirvalidationSummaryReport.getScores());
		        validationResultsMetaData.setValidatedOn(DateConversionUtils.convertDateIntoString(fhirvalidationSummaryReport.getValidatedOn()));
		        validationResultsMetaData.setCriterionName(fhirvalidationSummaryReport.getCriterionName());
		        validationResultsMetaData.setFhirValidationResults(fhirvalidationSummaryReport.getFhirValidationResults());

		        String validationResultsString =
		        		fhirvalidationSummaryReport.getFhirValidationResults().replace("[", "").replace("]", "");

		        List<String> categoryList =
		            new ArrayList<String>(
		                Arrays.asList(
		                		"IG Conformance & Vocabulary Error",
		                		"IG Conformance & Vocabulary Warning",
		                		"IG Conformance & Vocabulary Info",
		                		"Content Validation Error",
		                		"Content Validation Warning",
		                		"Content Validation Info"));
		        List<ResultMetaData> resultMetaDataList = new ArrayList<ResultMetaData>();

		        for (String categoryName : categoryList) {
		          ResultMetaData resultMetaData = new ResultMetaData();
		          switch (categoryName) {
		            case CategoryTypeConstants.FHIR_IG_VOCABULARY_ERROR:
		              resultMetaData.setType(categoryName);
		              resultMetaData.setCount(fhirvalidationSummaryReport.getFhirIgVacabularyError());
		              break;
		            case CategoryTypeConstants.FHIR_IG_VOCABULARY_WARNING:
		              resultMetaData.setType(categoryName);
		              resultMetaData.setCount(fhirvalidationSummaryReport.getFhirIgVacabularyWarning());
		              break;
		            case CategoryTypeConstants.FHIR_IG_VOCABULARY_INFO:
		              resultMetaData.setType(categoryName);
		              resultMetaData.setCount(fhirvalidationSummaryReport.getFhirIgVacabularyInfo());
		              break;
		            case CategoryTypeConstants.FHIR_REFERENCE_ERROR:
		              resultMetaData.setType(categoryName);
		              resultMetaData.setCount(fhirvalidationSummaryReport.getFhirReferenceError());
		              break;
		            case CategoryTypeConstants.FHIR_REFERENCE_WARNING:
		              resultMetaData.setType(categoryName);
		              resultMetaData.setCount(fhirvalidationSummaryReport.getFhirReferenceWarning());
		              break;
		            case CategoryTypeConstants.FHIR_REFERENCE_INFO:
		              resultMetaData.setType(categoryName);
		              resultMetaData.setCount(fhirvalidationSummaryReport.getFhiraReferenceInfo());
		              break;
		          }
		          resultMetaDataList.add(resultMetaData);
		          validationResultsMetaData.setResultMetaData(resultMetaDataList);
		        }

		        validationResultsDto.setFhirValidationResults(
		        		addFhirValidationResults(validationResultsString));
		        validationResultsDto.setResultsMetaData(validationResultsMetaData);
		        validationResultsDtoList.add(validationResultsDto);
		      }
		    } catch (Exception e) {
		      logger.error("Error while mapping Entity With Dto ", e);
		    }

		    logger.info("Exit from EntityToDtoMapper.mapEntityWithDto()");
		    return validationResultsDtoList;
		 		 
		 
	 }
	 
	 List<ReferenceError> addFhirValidationResults(String validationResultsString) {
		    logger.error("Entry in EntityToDtoMapper.addFhirValidationResults()");
		    List<ReferenceError> fhirValidationResults = new ArrayList<>();
		    List<String> list = Stream.of(validationResultsString.split("},")).collect(Collectors.toList());
		    try {
		      for (String validationResultsJson : list) {
		        String newResultsString =
		            validationResultsJson.substring(0, validationResultsJson.length()) + "}";
		        Gson gson = new GsonBuilder().setLenient().create();
		        ReferenceError referenceError = gson.fromJson(newResultsString, ReferenceError.class);
		        fhirValidationResults.add(referenceError);
		      }
		    } catch (Exception e) {
		      logger.error("Error while compiling fhirValidationResults ", e);
		    }
		    logger.error("Exit From EntityToDtoMapper.addFhirValidationResults()");
		    return fhirValidationResults;
		  }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
