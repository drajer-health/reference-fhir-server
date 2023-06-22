package com.interopx.fhir.validator.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.interopx.fhir.validator.model.ValidationResultsDto;
import com.interopx.fhir.validator.service.ResourceContentValidationService;
import com.interopx.fhir.validator.service.ResourceValidationService;
import com.interopx.fhir.validator.service.ValidatorService;
import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.JSONUtil;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

@RestController
public class ResourceValidationController {
	private static final Logger logger = LoggerFactory.getLogger(ResourceValidationController.class);

	@Autowired
	@Qualifier("r4FhirContext")
	private FhirContext r4Context;

	@Autowired
	@Qualifier("dstu3FhirContext")
	private FhirContext dstu3Context;

	@Autowired
	@Qualifier("dstu2FhirContext")
	private FhirContext dstu2Context;

	@Autowired
	@Qualifier("dstu2HL7FhirContext")
	private FhirContext dstu2HL7FhirContext;

	@Autowired
	@Qualifier("r5FhirContext")
	private FhirContext r5Context;

	@Autowired
	private ResourceValidationService validationService;

	@Autowired
	private ResourceContentValidationService resourceContentValidationService;

	@Autowired
	private ValidatorService validatorService;

	@Value("${scenario.base.directory}")
	private String baseLocation;

	@PostMapping(value = "/dstu2/resource/validate")
	public ResponseEntity<String> validateDSTU2Resource(@RequestBody String bodyStr) {
		String output = null;
		FhirValidator validator = dstu2HL7FhirContext.newValidator();
		try {
			ValidationResult results = validationService.validateDSTU2Resource(dstu2HL7FhirContext, validator, bodyStr);
			if (results instanceof ValidationResult && results.isSuccessful()) {
				logger.info("validateDSTU2Resource - Validation passed");
				output = dstu2operationOutcome(results);
				return new ResponseEntity<>(output, HttpStatus.OK);
			} else {
				logger.error("Failed to validateDSTU2Resource.");
				output = dstu2operationOutcome(results);
				return new ResponseEntity<>(output, HttpStatus.BAD_REQUEST);
			}

		} catch (DataFormatException e) {
			logger.error("Exception in validateDSTU2Resource.");
			org.hl7.fhir.dstu2.model.OperationOutcome outcomes = new org.hl7.fhir.dstu2.model.OperationOutcome();
			outcomes.addIssue().setSeverity(org.hl7.fhir.dstu2.model.OperationOutcome.IssueSeverity.ERROR)
					.setDiagnostics("validateDSTU2Resource - Failed to parse request body as JSON resource. Error was: "
							+ e.getMessage());
			output = dstu2HL7FhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcomes);
		}
		return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Validates STU3 resources
	 * 
	 * @param bodyStr
	 * @return
	 */
	@PostMapping(value = "/stu3/resource/validate")
	public ResponseEntity<String> validateSTU3Resource(@RequestBody String bodyStr) {
		String output = null;
		FhirValidator validator = dstu3Context.newValidator();
		try {
			ValidationResult results = validationService.validateSTU3Resource(dstu3Context, validator, bodyStr);
			if (results instanceof ValidationResult && results.isSuccessful()) {
				logger.info("validateSTU3Resource - Validation passed");
				output = dstu3operationOutcome(results);
				return new ResponseEntity<>(output, HttpStatus.OK);
			} else {
				logger.error("Failed to validateSTU3Resource.");
				output = dstu3operationOutcome(results);
				return new ResponseEntity<>(output, HttpStatus.BAD_REQUEST);
			}

		} catch (DataFormatException e) {
			logger.error("Exception in validateSTU3Resource");
			org.hl7.fhir.dstu3.model.OperationOutcome outcomes = new org.hl7.fhir.dstu3.model.OperationOutcome();
			outcomes.addIssue().setSeverity(org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity.ERROR)
					.setDiagnostics("validateSTU3Resource - Failed to parse request body as JSON resource. Error was: "
							+ e.getMessage());
			output = dstu3Context.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcomes);
		}
		return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/**
	 * Validates r4 resources
	 * 
	 * @param bodyStr
	 * @return
	 */
	@PostMapping(value = "/r4/resource/validate")
	public ResponseEntity<String> validateR4Resource(@RequestBody(required = true) String bodyStr,
			@RequestParam(value = "profile", required = true) String profile,
			@RequestParam(value = "scenarioName", required = false) String scenarioName) throws Exception {
		String output = null;
		FhirValidator validator = r4Context.newValidator();
		if ((profile != null && scenarioName == null)) {
			try {
				String results;
				logger.info("validating us-core-R4Resource ");
				OperationOutcome oo = validationService.validate(bodyStr.getBytes(), profile);
				results = r5Context.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
				return new ResponseEntity<>(results, HttpStatus.OK);
			} catch (NullPointerException e) {
				logger.error("Exception in validateR4Resource");
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		if (profile != null && scenarioName != null) {
			try {
				String results;
				logger.info("validating us-core-R4Resource with scenarioName ");
				CompletableFuture<OperationOutcome> profileValidationResults = CompletableFuture.supplyAsync(() -> {
					try {
						OperationOutcome oo = validationService.validate(bodyStr.getBytes(), profile);
						OperationOutcome oo1 = validationService.validateID(bodyStr, profile, 0);
						List<OperationOutcomeIssueComponent> issuesList = oo1.getIssue();
						if (issuesList != null) {
							for (OperationOutcomeIssueComponent issue : issuesList) {
								oo.addIssue(issue);
							}
						}
						return oo;
					} catch (Exception e) {
						logger.error("Exception in validateR4Resource with profile", e);
					}
					return null;
				});

				CompletableFuture<org.hl7.fhir.r4.model.OperationOutcome> contentValidationResults = CompletableFuture
						.supplyAsync(() -> resourceContentValidationService.validateResourceContent(
								JSONUtil.stringToFhirResource(r4Context, bodyStr), scenarioName));
				if (contentValidationResults.get().getIssue().size() == 0) {

					contentValidationResults.get().addIssue(ComparatorUtils
							.createScenarioResultsWithAllOk(IssueType.INFORMATIONAL, "", "AllOk", scenarioName));
				}
				String profileValidationresults = r5Context.newJsonParser().setPrettyPrint(true)
						.encodeResourceToString(profileValidationResults.get());
				results = r4Context.newJsonParser().setPrettyPrint(true)
						.encodeResourceToString(contentValidationResults.get());
				results = "[" + profileValidationresults + "," + results + "]";
				return new ResponseEntity<>(results, HttpStatus.OK);
			} catch (NullPointerException e) {
				logger.error("Exception in validateR4Resource");
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			Resource resource = (Resource) r4Context.newJsonParser().parseResource(bodyStr);
			String resProfile = null;
			if (resource.hasMeta()) {
				Meta resMeta = resource.getMeta();
				if (resMeta.hasProfile()) {
					CanonicalType canonicalProfileType = resMeta.getProfile().get(0);
					resProfile = canonicalProfileType.asStringValue();
				}
			}
			if (resProfile != null) {
				try {
					String results;
					logger.info("Validating with Profile " + resProfile);
					OperationOutcome oo = validationService.validate(bodyStr.getBytes(), resProfile);
					results = r5Context.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
					return new ResponseEntity<>(results, HttpStatus.OK);
				} catch (Exception e) {
					logger.error("Exception in validateR4Resource");
					org.hl7.fhir.r4.model.OperationOutcome outcomes = new org.hl7.fhir.r4.model.OperationOutcome();
					outcomes.addIssue().setSeverity(org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity.ERROR)
							.setDiagnostics(
									"Failed to parse request body as JSON resource. Error was: " + e.getMessage());
					output = r4Context.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcomes);
					logger.error(output);
				}
			} else {
				try {
					ValidationResult results = validationService.validateR4Resource(r4Context, validator, bodyStr);

					if (results instanceof ValidationResult && results.isSuccessful()) {
						logger.info("Validation passed");
						org.hl7.fhir.r4.model.OperationOutcome oo = (org.hl7.fhir.r4.model.OperationOutcome) results
								.toOperationOutcome();
						output = r4Context.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
						return new ResponseEntity<>(output, HttpStatus.OK);
					} else {
						logger.error("Failed to validateR4Resource.");
						org.hl7.fhir.r4.model.OperationOutcome oo = (org.hl7.fhir.r4.model.OperationOutcome) results
								.toOperationOutcome();
						output = r4Context.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
						return new ResponseEntity<>(output, HttpStatus.OK);
					}
				} catch (DataFormatException e) {
					logger.error("Exception in validateR4Resource");
					org.hl7.fhir.r4.model.OperationOutcome outcomes = new org.hl7.fhir.r4.model.OperationOutcome();
					outcomes.addIssue().setSeverity(org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity.ERROR)
							.setDiagnostics(
									" validateR4Resource - Failed to parse request body as JSON resource. Error was: "
											+ e.getMessage());
					output = r4Context.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcomes);
				}
			}
		}
		return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(value = "/r4/validate")
	public ResponseEntity<List<ValidationResultsDto>> validateResource(
			@RequestParam(value = "scenarioName", required = true) List<String> scenarioName,
			@RequestParam(value = "dataSourceId", required = false) Integer dataSourceId,
			@RequestParam(value = "resourceName", required = false) String resourceName,
			@RequestParam(value = "filesLocation", required = false) String filesLocation,
			@RequestParam(value = "sourceFiles", required = false) List<MultipartFile> sourceFiles) throws Exception {

		List<ValidationResultsDto> validationResults = validatorService.validateResources(scenarioName, sourceFiles,
				r4Context, dataSourceId, filesLocation, resourceName);

		return new ResponseEntity<>(validationResults, HttpStatus.OK);
	}

	/*
	 * gives dstu2 operationOutcome type result
	 */
	private String dstu2operationOutcome(ValidationResult results) {
		org.hl7.fhir.dstu2.model.OperationOutcome oo = (org.hl7.fhir.dstu2.model.OperationOutcome) results
				.toOperationOutcome();
		return dstu2HL7FhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
	}

	/*
	 * gives dstu3 operationOutcome type result
	 */
	private String dstu3operationOutcome(ValidationResult results) {
		org.hl7.fhir.dstu3.model.OperationOutcome oo = (org.hl7.fhir.dstu3.model.OperationOutcome) results
				.toOperationOutcome();
		return dstu3Context.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
	}

	/*
	 * generates report based on ResourceName and validatedDate
	 */

	@GetMapping(value = "/generateReport")
	public ResponseEntity<List<ValidationResultsDto>> getReport(@RequestParam(required = false) String ResourceName,
			@RequestParam(required = false) String ValidatedDate) {
		List<ValidationResultsDto> reportResults = new ArrayList<ValidationResultsDto>();
		try {
			reportResults = validatorService.getReport(ResourceName, ValidatedDate);
		} catch (Exception e) {
			logger.info("Error while getting report ", e);
			return new ResponseEntity<List<ValidationResultsDto>>(reportResults, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ValidationResultsDto>>(reportResults, HttpStatus.OK);

	}

	@GetMapping(value = "/generateReportOnDate")
	public ResponseEntity<List<ValidationResultsDto>> getReport(@RequestParam(required = false) String ValidatedDate) {
		List<ValidationResultsDto> reportResults = new ArrayList<ValidationResultsDto>();
		try {
			reportResults = validatorService.getReport(null, ValidatedDate);
		} catch (Exception e) {
			logger.info("Error while getting report ", e);
			return new ResponseEntity<List<ValidationResultsDto>>(reportResults, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ValidationResultsDto>>(reportResults, HttpStatus.OK);

	}

	@GetMapping(value = "/getScenarioNames")
	public ResponseEntity<List<Map<String, Object>>> getScenarioNames() {
		List<Map<String, Object>> scenarioNames = new ArrayList<Map<String, Object>>();
		try {

			scenarioNames = getFileNamesFromLocation(baseLocation);

		} catch (Exception e) {
			logger.info("Error while getting report ", e);
			return new ResponseEntity<List<Map<String, Object>>>(scenarioNames, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Map<String, Object>>>(scenarioNames, HttpStatus.OK);

	}

	public List<Map<String, Object>> getFileNamesFromLocation(String baseLocation) {
		File directory = new File(baseLocation);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File file : fList) {
			if (file.isDirectory()) {
				System.out.println(file.getName());
				resultList.add(readAllFilesFromLocation(file.getAbsolutePath(), file.getName()));
			}
		}
		return resultList;
	}

	private Map<String, Object> readAllFilesFromLocation(String absolutePath, String resourceName) {
		File directory = new File(absolutePath);
		Map<String, Object> object = new HashMap<String, Object>();
		List<String> resultList = new ArrayList<String>();
		object.put("resourceName", resourceName);

		File[] fList = directory.listFiles();

		for (File file : fList) {
			if (file.isFile()) {
				System.out.println(file.getAbsolutePath());
				resultList.add(file.getName().replace(".json", ""));
			}
		}
		object.put("files", resultList);

		return object;
	}
}