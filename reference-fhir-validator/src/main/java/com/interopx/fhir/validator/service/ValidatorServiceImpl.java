package com.interopx.fhir.validator.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.CaseUtils;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r5.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.interopx.fhir.validator.client.ValidatorClient;
import com.interopx.fhir.validator.entity.FhirValidationSummaryReport;
import com.interopx.fhir.validator.model.DataSource;
import com.interopx.fhir.validator.model.ReferenceError;
import com.interopx.fhir.validator.model.ResultMetaData;
import com.interopx.fhir.validator.model.ValidationResultsDto;
import com.interopx.fhir.validator.model.ValidationResultsMetaData;
import com.interopx.fhir.validator.repositories.FhirValidationSummaryRepository;
import com.interopx.fhir.validator.util.CategoryTypeConstants;
import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.DateConversionUtils;
import com.interopx.fhir.validator.util.EntityToDtoMapper;
import com.interopx.fhir.validator.util.FhirConstants;
import com.interopx.fhir.validator.util.JSONUtil;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;

import ca.uhn.fhir.context.FhirContext;

@Service
@Transactional
public class ValidatorServiceImpl implements ValidatorService {

	private static final Logger logger = LoggerFactory.getLogger(ValidatorServiceImpl.class);

	private static final String ERROR = "error";
	private static final String WARNING = "warning";
	private static final String INFO = "information";
	private static final String PROFILE = "IG Conformance & Vocabulary ";
	private static final String CONTENT = "Content Validation ";
	private static final String SEPARATOR = " ";
	private static final String YES = "YES";
	private static final String NO = "NO";
	private static final String BUNDLE = "Bundle";

	@Autowired
	private ResourceValidationService validationService;
	
	@Autowired
	private ResourceContentValidationService resourceContentValidationService;

	@Autowired
	private FhirValidationSummaryRepository fhirValidationSummaryRepository;

	@Autowired
	ValidatorClient validatorClient;
	
	@Autowired
	EntityToDtoMapper entityToDtoMap;
	
	@Override
	public List<ValidationResultsDto> validateResources(List<String> scenarioNames, List<MultipartFile> sourceFiles,
			FhirContext r4Context, Integer dataSourceId, String filesLocation,String reqResourceName) {
		logger.info(" Entry - ValidatorServiceImpl.validateResources()");
		List<ResultMetaData> resultMetaDataList = new ArrayList<>();
		List<ReferenceError> fhirProfileValidationResults = new ArrayList<>();
		List<ReferenceError> fhirContentValidationResults = new ArrayList<>();
		List<ValidationResultsDto> results = new ArrayList<>();
		DataSource dataSource = null;
		try {
			if (dataSourceId != null) {
				dataSource = validatorClient.getDataSourceObjectById(dataSourceId);
			}

			if (filesLocation != null && sourceFiles == null) {
				sourceFiles = readFilesFromExtractedLocation(filesLocation, sourceFiles);
			}
			for (MultipartFile sourceFile : sourceFiles) {
				byte[] resourceBytes = sourceFile.getBytes();
				String resourceString = new String(resourceBytes, Charset.defaultCharset());
				List<Resource> resources = JSONUtil.getResources(r4Context, resourceString);
				logger.info("No. of resources in Bundle:::::{}",resources.size());
				for (Resource resource : resources) {
					String resourceName = resource.getResourceType().name();
					logger.info("ResourceId:::::::::{}",resource.getIdElement().getIdPart().toString());
					int resourceIndex=resources.indexOf(resource);
					for (String scenarioName : scenarioNames) {
						if (!resourceName.equals(BUNDLE) && resourceName.equals(reqResourceName)) {
							ValidationResultsDto validationResultsDto = new ValidationResultsDto();
							ValidationResultsMetaData validationResultsMetaData = new ValidationResultsMetaData();
							
							String profile = getResourceProfile(resource);
							logger.info("validating resource against to profile and scenario " + scenarioName + " and "
									+ resourceName);
							String patientName = resourceName.equalsIgnoreCase("Patient")
									? getPatientData(r4Context, resource)
									: resourceName;
							String resourceContent = JSONUtil.fhirResourceToString(r4Context, resource);
							String emrVersion = dataSource == null ? "EMR" : dataSource.getEmrVersion();

							String adaptorVersion = dataSource == null ? "Adaptor" : dataSource.getAdaptorVersion();

							String modality = dataSource == null ? "Modality" : dataSource.getModality();

							validationResultsMetaData.setEmrVersion(emrVersion);
							validationResultsMetaData.setAdaptorVersion(adaptorVersion);
							validationResultsMetaData.setModality(modality);
							validationResultsMetaData
									.setValidatedOn(DateConversionUtils.convertDateIntoString(new Date()));
							validationResultsMetaData.setPatientName(patientName);
							validationResultsMetaData.setCriterionName(scenarioName);
							validationResultsMetaData.setResourceContents(resourceContent);
							validationResultsMetaData.setResourceName(resourceName);
							validationResultsMetaData.setTransactionId(UUID.randomUUID().toString());
							validationResultsMetaData.setGoldFileName(scenarioName + ".json");
							validationResultsMetaData.setFhirVersion(FhirConstants.TARGET_VERSION);
							validationResultsMetaData
									.setFhirValidationResults(getValidationResults(resultMetaDataList));

							CompletableFuture<OperationOutcome> profileValidationResults = CompletableFuture
									.supplyAsync(() -> {
										try {
											 OperationOutcome oo1 = validationService.validateID(resourceContent,profile,resourceIndex);
											 OperationOutcome oo= validationService.validate(resourceContent.getBytes(), profile);
											 List<OperationOutcomeIssueComponent> issuesList = oo1.getIssue();			
											 if(issuesList!=null) {
											 for( OperationOutcomeIssueComponent issue: issuesList) {
												 oo.addIssue(issue);
											 	}
											 }
				
											 return oo;
										} catch (Exception e) {
											logger.error("Exception in validateR4Resource with profile", e);
										}
										return null;
									});
							
							// logger.info("ProfileValidationResults::::::::{}",r4Context.newJsonParser().encodeResourceToString(profileValidationResults.get()));
							List<OperationOutcomeIssueComponent> confirmanceAndVacabularyIssuesList = profileValidationResults
									.get().getIssue();

							CompletableFuture<org.hl7.fhir.r4.model.OperationOutcome> contentValidationResults = CompletableFuture
									.supplyAsync(() -> resourceContentValidationService
											.validateResourceContent(resource, scenarioName));
							if (contentValidationResults.get().getIssue().size() == 0) {

								contentValidationResults.get().addIssue(ComparatorUtils.createScenarioResultsWithAllOk(
										IssueType.INFORMATIONAL, "", "AllOk", scenarioName));
							}

							List<org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent> referenceValidatorIssuesList = contentValidationResults
									.get().getIssue();

							List<ResultMetaData> resultMetaDataProfileList = getIgAndVacabularyResultMetaData(
									confirmanceAndVacabularyIssuesList, validationResultsDto, validationResultsMetaData,
									fhirProfileValidationResults);

							List<ResultMetaData> resultMetaDataContentList = getContentValidationResultMetaData(
									referenceValidatorIssuesList, validationResultsDto, validationResultsMetaData,
									fhirContentValidationResults);

							List<ResultMetaData> allResultMetaDataList = new ArrayList<>();

							List<ReferenceError> fhirValidationResults = new ArrayList<>();

							allResultMetaDataList.addAll(resultMetaDataProfileList);
							allResultMetaDataList.addAll(resultMetaDataContentList);
							validationResultsMetaData.setResultMetaData(allResultMetaDataList);

							List<ReferenceError> profileValidationErrorList = getProfileValidationReferenceError(
									confirmanceAndVacabularyIssuesList);
							List<ReferenceError> contentValidationErrorList = getContentValidationReferenceError(
									referenceValidatorIssuesList);
							fhirValidationResults.addAll(profileValidationErrorList);
							fhirValidationResults.addAll(contentValidationErrorList);

							List<String> errors = getError(validationResultsMetaData);
							if (errors.contains(YES)) {
								validationResultsMetaData.setError(YES);
							} else {
								validationResultsMetaData.setError(NO);
							}
							validationResultsDto.setFhirValidationResults(fhirValidationResults);
							validationResultsDto.setResultsMetaData(validationResultsMetaData);
							validationResultsMetaData.setFhirValidationResults(getFHIRValidationResults(fhirValidationResults));
							saveValidationResults(validationResultsMetaData);
							results.add(validationResultsDto);
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error(" Error - ValidatorServiceImpl.validateResources()", e);
		}
		logger.info(" Exit - ValidatorServiceImpl.validateResources()");
		return results;
	}

	private List<ResultMetaData> getIgAndVacabularyResultMetaData(
			List<OperationOutcomeIssueComponent> confirmanceAndVacabularyIssuesList,
			ValidationResultsDto validationResultsDto, ValidationResultsMetaData validationResultsMetaData,
			List<ReferenceError> fhirProfileValidationResults) {
		
		List<ResultMetaData> resultMetaDataList = new ArrayList<>();
		List<ReferenceError> fhirProfileValidationList = new ArrayList<>();
		try {
			Map<IssueSeverity, Long> confirmanceAndVacabularyIssues = confirmanceAndVacabularyIssuesList.stream()
					.collect(Collectors.groupingBy(OperationOutcomeIssueComponent::getSeverity, Collectors.counting()));
			
			Set<Entry<IssueSeverity, Long>> entrySet = confirmanceAndVacabularyIssues.entrySet();
			ResultMetaData resultMetaDataError = new ResultMetaData(CategoryTypeConstants.FHIR_IG_VOCABULARY_ERROR);
			
			ResultMetaData resultMetaDataWarning = new ResultMetaData(CategoryTypeConstants.FHIR_IG_VOCABULARY_WARNING);
			
			ResultMetaData resultMetaDataInfo = new ResultMetaData(CategoryTypeConstants.FHIR_IG_VOCABULARY_INFO);
			for (Entry<IssueSeverity, Long> entry : entrySet) {

				String categoryType = entry.getKey().getDisplay().toLowerCase();

				switch (categoryType) {
				case ERROR:
					resultMetaDataError.setCount(entry.getValue().intValue());
					break;
				case WARNING:
					resultMetaDataWarning.setCount(entry.getValue().intValue());
					break;
				case INFO:
					resultMetaDataInfo.setCount(entry.getValue().intValue());
					break;
				}
			}
			resultMetaDataList.add(resultMetaDataError);
			resultMetaDataList.add(resultMetaDataWarning);
			resultMetaDataList.add(resultMetaDataInfo);
			validationResultsMetaData.setResultMetaData(resultMetaDataList);
			fhirProfileValidationResults.addAll(fhirProfileValidationList);
			validationResultsDto.setFhirValidationResults(fhirProfileValidationResults);
			
		} catch (Exception e2) {
			logger.info("Error - ValidationServiceImpl.getIgAndVacabularyResultMetaData ",e2);
		}
		return resultMetaDataList;
	}

	private List<ResultMetaData> getContentValidationResultMetaData(
			List<org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent> referenceValidatorIssuesList,
			ValidationResultsDto validationResultsDto, ValidationResultsMetaData validationResultsMetaData,
			List<ReferenceError> fhirContentValidationResults) {

		List<ResultMetaData> resultMetaDataList = new ArrayList<>();
		List<ReferenceError> fhirContentValidationList = new ArrayList<>();
		try {

			Map<org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity, Long> referenceIssues = referenceValidatorIssuesList
					.stream()
					.collect(Collectors.groupingBy(
							org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent::getSeverity,
							Collectors.counting()));
			Set<Entry<org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity, Long>> entrySet = referenceIssues
					.entrySet();

			for (Entry<org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity, Long> entry : entrySet) {
				ResultMetaData resultMetaDataError = new ResultMetaData(CategoryTypeConstants.FHIR_REFERENCE_ERROR);
				ResultMetaData resultMetaDataWarning = new ResultMetaData(CategoryTypeConstants.FHIR_REFERENCE_WARNING);
				ResultMetaData resultMetaDataInfo = new ResultMetaData(CategoryTypeConstants.FHIR_REFERENCE_INFO);
				String categoryType = entry.getKey().getDisplay().toLowerCase();

				switch (categoryType) {
				case ERROR:
					resultMetaDataError.setCount(entry.getValue().intValue());
					break;
				case WARNING:
					resultMetaDataWarning.setCount(entry.getValue().intValue());
					break;
				case INFO:
					resultMetaDataInfo.setCount(entry.getValue().intValue());
					break;

				}
				resultMetaDataList.add(resultMetaDataError);
				resultMetaDataList.add(resultMetaDataWarning);
				resultMetaDataList.add(resultMetaDataInfo);
				validationResultsMetaData.setResultMetaData(resultMetaDataList);
			}
			fhirContentValidationResults.addAll(fhirContentValidationList);
			validationResultsDto.setFhirValidationResults(fhirContentValidationResults);

		} catch (Exception e) {

		}
		return resultMetaDataList;

	}

	private List<ReferenceError> getProfileValidationReferenceError(
			List<OperationOutcomeIssueComponent> profileValidatorIssuesList) {
		logger.info("Entry in ValidationServiceImpl.getProfileValidationReferenceError");
		List<ReferenceError> fhirProfileValidationList = new ArrayList<>();
		try {
			
			for (OperationOutcomeIssueComponent operationOutcomeIssueComponent : profileValidatorIssuesList) {
				ReferenceError referenceError = new ReferenceError();
				
				referenceError.setCode(operationOutcomeIssueComponent.getCode().getDisplay());
				
				referenceError.setDetails(operationOutcomeIssueComponent.getDetails().getText());
				
				referenceError.setType(PROFILE + operationOutcomeIssueComponent.getSeverity().getDisplay());
				
				referenceError.setExpression(
						operationOutcomeIssueComponent.getExpression().iterator().next().asStringValue());		
				
				fhirProfileValidationList.add(referenceError);
			}
		} catch (Exception e) {
			logger.info("Exeption in getProfileValidationReferenceError"+e);
		}
		logger.info("Exit in ValidationServiceImpl.getProfileValidationReferenceError");
		return fhirProfileValidationList;

	}

	private List<ReferenceError> getContentValidationReferenceError(
			List<org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent> referenceValidatorIssuesList) {
		logger.info("Entry in ValidationServiceImpl.getContentValidationReferenceError");
		List<ReferenceError> fhirContentValidationList = new ArrayList<>();
		try {

			for (org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent operationOutcomeIssueComponent : referenceValidatorIssuesList) {
				ReferenceError referenceError = new ReferenceError();
				referenceError.setDetails(operationOutcomeIssueComponent.getDetails().getText());
				referenceError.setType(CONTENT + operationOutcomeIssueComponent.getSeverity().getDisplay());
				fhirContentValidationList.add(referenceError);
			}
		} catch (Exception e) {

		}
		logger.info("Exit in ValidationServiceImpl.getContentValidationReferenceError");
		return fhirContentValidationList;

	}

	private List<String> getError(ValidationResultsMetaData validationResultsMetaData) {

		logger.info("Entry in ValidationServiceImpl.getError");
		List<String> errors = new ArrayList<>();
		List<ResultMetaData> errorList = validationResultsMetaData.getResultMetaData();
		try {
			for (ResultMetaData resultMetaData : errorList) {
				if (resultMetaData.getType().contains("Error") && resultMetaData.getCount() != 0) {
					errors.add(YES);
				} else {
					errors.add(NO);
				}
			}
		} catch (Exception e) {
			logger.error("ERROR while finding error  in validation process", e);
		}
		logger.info("Exit in ValidationServiceImpl.getError");
		return errors;
	}

	private String getValidationResults(List<ResultMetaData> resultMetaDataList) {

		String results = null;
		try {
			Gson gson = new Gson();
			results = gson.toJson(resultMetaDataList);
			return results;
		} catch (Exception e) {
			logger.error("Error While retriving validation results !!! ", e);
		}
		return results;
	}
	
	private String getFHIRValidationResults(List<ReferenceError> fhirValidationResults) {

		String results = null;
		try {
			Gson gson = new Gson();
			results = gson.toJson(fhirValidationResults);
			return results;
		} catch (Exception e) {
			logger.error("Error While retriving validation results !!! ", e);
		}
		return results;
	}

	private void saveValidationResults(ValidationResultsMetaData validationResultsMetaData) {

		FhirValidationSummaryReport fhirValidationSummaryReport = new FhirValidationSummaryReport();
		BeanUtils.copyProperties(validationResultsMetaData, fhirValidationSummaryReport);
		fhirValidationSummaryReport
		.setValidatedOn(new java.sql.Timestamp(DateConversionUtils.convertStringIntoDate(validationResultsMetaData.getValidatedOn()).getTime()));
		fhirValidationSummaryRepository.save(fhirValidationSummaryReport);
	}

	private String getPatientData(FhirContext r4Context, Resource resource) {
		Patient sourceResource = (Patient) resource;
		List<HumanName> names = sourceResource.getName();
		String lastName = null;
		String firstName = null;
		StringBuilder givenNames = new StringBuilder();
		for (HumanName humanName : names) {
			if (humanName.hasFamily()) {
				lastName = humanName.getFamily();
			}
			if (humanName.hasGiven()) {
				humanName.getGiven().stream().filter(human -> human != null).collect(Collectors.toList())
						.forEach(name -> givenNames.append(name + SEPARATOR));
				;
				firstName = givenNames.toString();
			}
		}
		return firstName + lastName;
	}

	public List<MultipartFile> readFilesFromExtractedLocation(String filesLocation,
			List<MultipartFile> multipartFiles) {
		List<MultipartFile> ccdaFiles = new ArrayList<MultipartFile>();
		List<File> filesInFolder = new ArrayList<File>();

		if (filesLocation != null && multipartFiles == null) {
			logger.info("Validation started from Extracted Location:::: " + filesLocation);
			try (Stream<Path> stream = Files.walk(Paths.get(filesLocation)).filter(path -> path.toFile().isFile())) {
				filesInFolder = stream.map(Path::toFile).collect(Collectors.toList());
				for (File file : filesInFolder) {
					try (FileInputStream input = new FileInputStream(file)) {
						MultipartFile ccdaFile = new MockMultipartFile("file", file.getName(), "xml",
								IOUtils.toByteArray(input));
						ccdaFiles.add(ccdaFile);
					} catch (Exception e) {
						logger.error("Error while converting file into MultipartFile ", e);
					}
				}
			} catch (Exception e) {
				logger.error("Error while readFilesFromFS", e);
			}

		}
		return ccdaFiles;
	}
	
	@Override
	public List<ValidationResultsDto> getReport(String name,String date) {
		logger.info("Entry in ValidationServiceImpl.getReport");
		List<FhirValidationSummaryReport> results = new ArrayList<>();
		List<ValidationResultsDto> validationResultsDtoList = new ArrayList<ValidationResultsDto>();
		String resourceName = null;
		Timestamp validatedOn = null;
		try {
			if(name!=null) {
				resourceName=CaseUtils.toCamelCase(name, true, ' ');
			}
			
			if(date!=null) {
				  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				    Date parsedDate = dateFormat.parse(date);
				    validatedOn = new java.sql.Timestamp(parsedDate.getTime());
			}
			
			if(name !=null && date==null) {
				logger.info("Resource Name is not Empty,But Date is Empty :",resourceName);
				results=fhirValidationSummaryRepository.getReportByResourceName(resourceName);
				}
			else if(name==null && date!=null) {
				logger.info("Resource Name is Empty,But Date is not Empty :",date);
				results=fhirValidationSummaryRepository.getReportByDate(validatedOn);
			}
			else if(name!=null && date!=null) {
				logger.info("Resource Name and date Both are not empty: "+resourceName+" & "+validatedOn);
				results=fhirValidationSummaryRepository.getReportByNameAndDate(resourceName,validatedOn);
			}

			return  entityToDtoMap.mapEntityWithDto(results);
				
		}
		catch(Exception ex){
			logger.info("Error - in ValidationServiceImpl.getReport ",ex);
			
		}
		logger.info("Exit in ValidationServiceImpl.getReport ");
		return validationResultsDtoList;
	}
	
	private String getResourceProfile(Resource resource) {
		String profile=null;
		String resourceName = resource.getResourceType().name();
		if(resourceName.equalsIgnoreCase(ResourceNames.OBSERVATION)) {
		 profile = resource.getMeta().getProfile().stream().filter(prof -> (!prof.getValue().isEmpty()))
				.findFirst().get().getValue();
		}else if(FhirConstants.getProfile().containsKey(resourceName) && !resourceName.equalsIgnoreCase(ResourceNames.OBSERVATION)){
			profile=FhirConstants.getProfile().get(resourceName);
		}else {
			profile="No matching resource found";
		}
		
		return profile;

	}
}
