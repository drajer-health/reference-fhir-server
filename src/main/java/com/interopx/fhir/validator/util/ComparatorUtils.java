package com.interopx.fhir.validator.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.TimeType;
import org.hl7.fhir.r4.model.Timing;

import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceReactionComponent;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition.ConditionEvidenceComponent;
import org.hl7.fhir.r4.model.Condition.ConditionStageComponent;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Age;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CarePlan.CarePlanActivityComponent;
import org.hl7.fhir.r4.model.CarePlan.CarePlanActivityDetailComponent;
import org.hl7.fhir.r4.model.CareTeam.CareTeamParticipantComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Device.DeviceDeviceNameComponent;
import org.hl7.fhir.r4.model.Device.DevicePropertyComponent;
import org.hl7.fhir.r4.model.Device.DeviceSpecializationComponent;
import org.hl7.fhir.r4.model.Device.DeviceUdiCarrierComponent;
import org.hl7.fhir.r4.model.Device.DeviceVersionComponent;
import org.hl7.fhir.r4.model.DiagnosticReport.DiagnosticReportMediaComponent;
import org.hl7.fhir.r4.model.DocumentReference.DocumentReferenceContentComponent;
import org.hl7.fhir.r4.model.DocumentReference.DocumentReferenceContextComponent;
import org.hl7.fhir.r4.model.DocumentReference.DocumentReferenceRelatesToComponent;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.Dosage.DosageDoseAndRateComponent;
import org.hl7.fhir.r4.model.Encounter.ClassHistoryComponent;
import org.hl7.fhir.r4.model.Encounter.DiagnosisComponent;
import org.hl7.fhir.r4.model.Encounter.EncounterHospitalizationComponent;
import org.hl7.fhir.r4.model.Encounter.EncounterLocationComponent;
import org.hl7.fhir.r4.model.Encounter.EncounterParticipantComponent;
import org.hl7.fhir.r4.model.Encounter.StatusHistoryComponent;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Goal.GoalTargetComponent;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Immunization.ImmunizationEducationComponent;
import org.hl7.fhir.r4.model.Immunization.ImmunizationPerformerComponent;
import org.hl7.fhir.r4.model.Immunization.ImmunizationProtocolAppliedComponent;
import org.hl7.fhir.r4.model.Immunization.ImmunizationReactionComponent;
import org.hl7.fhir.r4.model.Immunization.ImmunizationStatus;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestDispenseRequestComponent;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestSubstitutionComponent;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.r4.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Procedure.ProcedureFocalDeviceComponent;
import org.hl7.fhir.r4.model.Procedure.ProcedurePerformerComponent;
import org.hl7.fhir.r4.model.Provenance.ProvenanceAgentComponent;
import org.hl7.fhir.r4.model.Provenance.ProvenanceEntityComponent;
import org.hl7.fhir.r4.model.PositiveIntType;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Range;
import org.hl7.fhir.r4.model.Ratio;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.model.Patient.ContactComponent;
import org.hl7.fhir.r4.model.Patient.PatientCommunicationComponent;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Signature;
import org.hl7.fhir.r4.utils.ToolingExtensions;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

/**
 * This utility compares source/input Resource content with goldJson
 * 
 * @author Swarna Nelaturi
 *
 */

public class ComparatorUtils {

	private static final Logger logger = LoggerFactory.getLogger(ComparatorUtils.class);

	static Map<String, String> scenarioNames = ScenarioConstants.getScenarioName();

	public static OperationOutcomeIssueComponent createScenarioResults(IssueType type, String path, String message) {
		try {

			OperationOutcome operationOutcome = new OperationOutcome();
			ValidationMessage validationMessage = new ValidationMessage(Source.ResourceValidator, type, path, message,
					IssueSeverity.ERROR);
			OperationOutcomeIssueComponent operationOutcomeIssueComponent = convertToIssue(validationMessage,
					operationOutcome);
			return operationOutcomeIssueComponent;
		} catch (Exception ex) {
			logger.error("\n Exception in createScenarioResults of ComparatorUtils :: ", ex.getMessage());
		}
		return null;

	}

	public static OperationOutcomeIssueComponent createScenarioResultsWithAllOk(IssueType type, String path,
			String message, String ScenarioName) {
		try {
			OperationOutcome operationOutcome = new OperationOutcome();
			ValidationMessage validationMessage = new ValidationMessage(Source.ResourceValidator, type, path, message,
					IssueSeverity.INFORMATION);
			OperationOutcomeIssueComponent operationOutcomeIssueComponent = convertToIssue(validationMessage,
					operationOutcome);
			return operationOutcomeIssueComponent;
		} catch (Exception ex) {
			logger.error("\n Exception in createScenarioResultsWithAllOk of ComparatorUtils :: ", ex.getMessage());
		}
		return null;

	}

	private static OperationOutcomeIssueComponent convertToIssue(ValidationMessage message, OperationOutcome op) {
		try {
			OperationOutcomeIssueComponent issue = new OperationOutcome.OperationOutcomeIssueComponent();
			issue.setSeverity(convert(message.getLevel()));
			issue.setCode(issue.getCode());
			if (message.getLocation() != null) {
				StringType s = new StringType();
				s.setValue(message.getLocation() + (message.getLine() >= 0 && message.getCol() >= 0 ? " (line "
						+ Integer.toString(message.getLine()) + ", col" + Integer.toString(message.getCol()) + ")"
						: ""));
				issue.getLocation();
			}
			issue.setSeverity(convert(message.getLevel()));
			CodeableConcept c = new CodeableConcept();
			c.setText(message.getMessage());
			issue.setDetails(c);
			if (message.getSource() != null) {
				issue.getExtension().add(ToolingExtensions.makeIssueSource(message.getSource()));
			}
			return issue;
		} catch (Exception ex) {
			logger.error("\n Exception in convertToIssue of ComparatorUtils :: ", ex.getMessage());
		}
		return null;

	}

	private static OperationOutcome.IssueSeverity convert(IssueSeverity issueSeverity) {
		return OperationOutcome.IssueSeverity.valueOf(issueSeverity.name());

	}

	public static void compareListOfHumanName(String fieldName, List<HumanName> humanNameOneList,
			List<HumanName> humanNameTwoList, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality("address", humanNameOneList, humanNameTwoList, operationOutcome,
					scenarioName)) {
				logger.info("Entry-compareListOfHumanName");
				for (int i = 0; i < humanNameOneList.size();) {
					for (int j = 0; j < humanNameTwoList.size(); j++) {
						compareHumanName(fieldName, humanNameOneList.get(i), humanNameTwoList.get(j), operationOutcome,
								scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(humanNameOneList) && ObjectUtils.isEmpty(humanNameTwoList)) {
				String errorMessage = "The scenario does not require name, but submitted file does have name";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(humanNameOneList) && ObjectUtils.isNotEmpty(humanNameTwoList)) {
				String errorMessage = "The scenario requires name, but submitted file does not have name";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

			logger.info("Exit-compareListOfHumanName");

		} catch (Exception ex) {
			logger.error("\n Exception in convertToIssue of ComparatorUtils :: ", ex.getMessage());
		}

	}

	/**
	 * Compare two HumanName
	 * 
	 * @param nameOne
	 * @param nameTwo
	 * @return
	 */
	public static void compareHumanName(String fieldName, HumanName nameOne, HumanName nameTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		logger.info("Entry-compareHumanName");
		try {
			if ((nameOne != null && !nameOne.isEmpty()) && (nameTwo != null && !nameTwo.isEmpty())) {

				// family
				if (nameOne.hasFamily() && nameTwo.hasFamily()
						&& (!(nameOne.getFamily().equalsIgnoreCase(nameTwo.getFamily())))) {
					String errorMessage = fieldName + ".name.family Expected = " + nameTwo.getFamily()
							+ " but, submitted file contains name.family of " + nameOne.getFamily();
					operationOutcome.addIssue(
							createScenarioResults(IssueType.INCOMPLETE, fieldName + ".name.family", errorMessage));
				} else if (nameOne.hasFamily() && !nameTwo.hasFamily()) {
					String errorMessage = "The scenario does not require name.family, but submitted file does have name.family";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".name.family", errorMessage));
				} else if (!nameOne.hasFamily() && nameTwo.hasFamily()) {
					String errorMessage = "The scenario requires family name, but submitted file does not have patient family name";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".name.family", errorMessage));
				} else {
					logger.info("name.family information is null OR equal in both inputJson and goldJson");
				}

				// given
				if (nameOne.hasGiven() && nameTwo.hasGiven()) {
					List<StringType> nameOneGiven = nameOne.getGiven();
					List<StringType> nameTwoGiven = nameTwo.getGiven();

					if (compareListForSizeEquality("name.given", nameOneGiven, nameTwoGiven, operationOutcome,
							scenarioName)) {
						StringType firstNameOne = null;
						StringType middleNameOne = null;
						StringType firstNameTwo = null;
						StringType middleNameTwo = null;

						if (nameOneGiven.size() > 0) {
							firstNameOne = nameOneGiven.get(0);
						}
						if (nameOneGiven.size() >= 2) {
							middleNameOne = nameOneGiven.get(1);
						}

						if (nameTwoGiven.size() > 0) {
							firstNameTwo = nameTwoGiven.get(0);
						}

						if (nameTwoGiven.size() >= 2) {
							middleNameTwo = nameTwoGiven.get(1);
						}

						// firstName
						if (ObjectUtils.isNotEmpty(firstNameOne) && ObjectUtils.isNotEmpty(firstNameTwo)
								&& (!(firstNameOne.getValue().equalsIgnoreCase(firstNameTwo.getValue())))) {
							String errorMessage = fieldName + ".name.given Expected = " + firstNameTwo.getValue()
									+ " but, submitted file contains .name.given of " + firstNameOne.getValue();
							operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE,
									fieldName + ".name.firstName", errorMessage));
						} else if (firstNameOne == null && firstNameTwo != null) {
							String errorMessage = "The scenario requires name.given, but submitted file does not have patient family name";
							operationOutcome.addIssue(
									createScenarioResults(IssueType.CONFLICT, fieldName + ".name.given", errorMessage));
						} else if (firstNameOne != null && firstNameTwo == null) {
							String errorMessage = "The scenario does not require name.given, but submitted file does have patient firstName";
							operationOutcome.addIssue(
									createScenarioResults(IssueType.CONFLICT, fieldName + ".name.given", errorMessage));
						} else {
							logger.info("name.given information is null OR equal in both inputJson and goldJson");
						}

						// middleName
						if (ObjectUtils.isNotEmpty(middleNameOne) && ObjectUtils.isNotEmpty(middleNameTwo)
								&& (!(middleNameOne.getValue().equalsIgnoreCase(middleNameTwo.getValue())))) {
							String errorMessage = fieldName + ".name.given Expected = " + middleNameTwo.getValue()
									+ " but, submitted file contains .name.given of " + middleNameOne.getValue();
							operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE,
									fieldName + ".name.firstName", errorMessage));
						} else if (ObjectUtils.isEmpty(middleNameOne) && ObjectUtils.isNotEmpty(middleNameTwo)) {
							String errorMessage = "The scenario requires patient middleName, but submitted file does not have patient middle name";
							operationOutcome.addIssue(
									createScenarioResults(IssueType.CONFLICT, fieldName + ".name.given", errorMessage));
						} else if (ObjectUtils.isNotEmpty(middleNameOne) && ObjectUtils.isEmpty(middleNameTwo)) {
							String errorMessage = "The scenario does not require patient middleName, but submitted file does have patient middle name";
							operationOutcome.addIssue(
									createScenarioResults(IssueType.CONFLICT, fieldName + ".name.given", errorMessage));
						} else {
							logger.info("name.given information is null OR equal in both inputJson and goldJson");
						}

					}
				} else if (nameOne.hasGiven() && !nameTwo.hasGiven()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".given, but submitted file does have .given";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".prefix", errorMessage));
				} else if (!nameOne.hasGiven() && nameTwo.hasGiven()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".given, but submitted file does not have .given";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".given", errorMessage));
				} else {
					logger.info(fieldName + ".given information is null OR equal in both inputJson and goldJson");
				}

				if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_PATIENT)) {

					if (nameOne.hasPrefix() && nameTwo.hasPrefix()) {
						List<StringType> nameOnePrefix = nameOne.getPrefix();
						List<StringType> nameTwoPrefix = nameTwo.getPrefix();
						if (!compareListForSizeEquality("name.prefix", nameOnePrefix, nameTwoPrefix, operationOutcome,
								scenarioName)) {
							String errorMessage = "Data size mismatch";
							operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
									fieldName + ".name.prefix", errorMessage));
						} else {
							for (StringType prefixOne : nameOnePrefix) {
								for (StringType prefixTwo : nameTwoPrefix) {
									if (!(prefixOne.getValue().equalsIgnoreCase(prefixTwo.getValue()))) {
										String errorMessage = fieldName + ".name.prefix Expected = "
												+ prefixTwo.getValue() + " but, submitted file contains prefix of "
												+ prefixOne.getValue();
										operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE,
												fieldName + ".name.prefix", errorMessage));
									} else if (StringUtils.isEmpty(prefixOne.getValue())
											&& StringUtils.isNotEmpty(prefixTwo.getValue())) {
										String errorMessage = "The scenario requires name.prefix, but submitted file does not have patient name.prefix";
										operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
												fieldName + ".name.prefix", errorMessage));
									} else if (StringUtils.isNotEmpty(prefixOne.getValue())
											&& StringUtils.isEmpty(prefixTwo.getValue())) {
										String errorMessage = "The scenario does not require name.prefix, but submitted file does have patient name.prefix";
										operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
												fieldName + ".name.prefix", errorMessage));
									} else {
										logger.info(
												"name.prefix information is null OR equal in both inputJson and goldJson ");
									}
								}
							}

						}

					} else if (nameOne.hasPrefix() && !nameTwo.hasPrefix()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".prefix, but submitted file does have .prefix";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".prefix", errorMessage));
					} else if (!nameOne.hasPrefix() && nameTwo.hasPrefix()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".prefix, but submitted file does not have .prefix";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".prefix", errorMessage));
					} else {
						logger.info(fieldName + ".prefix information is null OR equal in both inputJson and goldJson");
					}

					// suffix
					logger.info("op " + nameOne.hasSuffix());
					logger.info("op " + nameTwo.hasSuffix());
					if (nameOne.hasSuffix() && nameTwo.hasSuffix()) {
						List<StringType> nameOneSuffix = nameOne.getSuffix();
						List<StringType> nameTwoSuffix = nameTwo.getSuffix();
						if (!compareListForSizeEquality("name.suffix", nameOneSuffix, nameTwoSuffix, operationOutcome,
								scenarioName)) {
							String errorMessage = "Data size mismatch";
							operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
									fieldName + ".name.suffix", errorMessage));
						} else {
							for (StringType suffixOne : nameOneSuffix) {
								for (StringType suffixTwo : nameTwoSuffix) {
									if (!(suffixOne.getValue().equalsIgnoreCase(suffixTwo.getValue()))) {
										String errorMessage = fieldName + ".name.suffix Expected = "
												+ suffixTwo.getValue() + " but, submitted file contains suffix of "
												+ suffixOne.getValue();
										operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE,
												fieldName + ".name.suffix", errorMessage));
									} else if (StringUtils.isEmpty(suffixOne.getValue())
											&& StringUtils.isNotEmpty(suffixTwo.getValue())) {
										String errorMessage = "The scenario requires name.suffix, but submitted file does not have patient name.suffix";
										operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
												fieldName + ".name.suffix", errorMessage));
									} else if (StringUtils.isNotEmpty(suffixOne.getValue())
											&& StringUtils.isEmpty(suffixTwo.getValue())) {
										String errorMessage = "The scenario does not require name.suffix, but submitted file does have patient name.suffix";
										operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
												fieldName + ".name.suffix", errorMessage));
									} else {
										logger.info(
												"name.suffix information is null OR equal in both inputJson and goldJson ");
									}
								}
							}

						}

					} else if (nameOne.hasSuffix() && !nameTwo.hasSuffix()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".suffix, but submitted file does have .suffix";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".suffix", errorMessage));
					} else if (!nameOne.hasSuffix() && nameTwo.hasSuffix()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".suffix, but submitted file does not have .suffix";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".suffix", errorMessage));
					} else {
						logger.info(fieldName + ".suffix information is null OR equal in both inputJson and goldJson");
					}

					// text
					if (nameOne.hasText() && nameTwo.hasText()
							&& (!(nameOne.getText().equalsIgnoreCase(nameTwo.getText())))) {
						compareString(fieldName + ".text", nameOne.getText(), nameTwo.getText(), operationOutcome,
								scenarioName);
					} else if (nameOne.hasText() && !nameTwo.hasText()) {
						String errorMessage = "The scenario does not require patient name.use, but submitted file does have patient name.use";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else if (!nameOne.hasText() && nameTwo.hasText()) {
						String errorMessage = "The scenario does not require patient name.text, but submitted file does have patient name.use";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else {
						logger.info("name.text information is null OR equal in both inputJson and goldJson ");
					}

					// period
					if (nameOne.hasPeriod() && nameTwo.hasPeriod()) {
						comparePeriod(fieldName + ".text", nameOne.getPeriod(), nameTwo.getPeriod(), operationOutcome,
								scenarioName);
					} else if (nameOne.hasPeriod() && !nameTwo.hasPeriod()) {
						String errorMessage = "The scenario does not require Patient.name.period, but submitted file does have patient.name.period";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else if (!nameOne.hasPeriod() && nameTwo.hasPeriod()) {
						String errorMessage = "The scenario require Patient.name.period, but submitted file does have Patient.name.period";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else {
						logger.info("name.period information is null OR equal in both inputJson and goldJson ");
					}

					// use
					if (nameOne.hasUse() && nameTwo.hasUse()
							&& (!(nameOne.getUse().getDisplay().equalsIgnoreCase(nameTwo.getUse().getDisplay())))) {
						String useOneDisplay = nameOne.getUse().getDisplay();
						String useTwoDisplay = nameTwo.getUse().getDisplay();
						String errorMessage = fieldName + ".use Expected = " + useTwoDisplay
								+ " but, submitted file contains  " + fieldName + ".use of " + useOneDisplay;
						operationOutcome.addIssue(
								createScenarioResults(IssueType.INCOMPLETE, fieldName + ".name.use", errorMessage));
					} else if (nameOne.hasUse() && !nameTwo.hasUse()) {
						String errorMessage = "The scenario does not require Patient.name.use, but submitted file does have Patient.name.use";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".name.use", errorMessage));
					} else if (!nameOne.hasUse() && nameTwo.hasUse()) {
						String errorMessage = "The scenario require Patient.name.use, but submitted file does not have Patient.name.use";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".name.use", errorMessage));
					} else {
						logger.info("name.use information is null OR equal in both inputJson and goldJson ");
					}
				}
			} else if (nameOne != null && nameTwo == null) {

				String errorMessage = "The scenario does not require name, but submitted file does have name";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (nameOne == null && ObjectUtils.isNotEmpty(nameTwo)) {

				String errorMessage = "The scenario requires name, but location file does not have name";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
			logger.info("Try -Exit-compareHumanName");
		} catch (Exception ex) {
			logger.error("\n Exception while comparing two HumanName in ComparatorUtils class", ex);
		}
	}

	public static void compareGender(String resourceName, Enumeration<AdministrativeGender> genderOne,
			Enumeration<AdministrativeGender> genderTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (StringUtils.isNotEmpty(genderOne.getValueAsString())
					&& StringUtils.isNotEmpty(genderTwo.getValueAsString())
					&& (!(genderOne.getValueAsString().equalsIgnoreCase(genderTwo.getValueAsString())))) {
				String errorMessage = resourceName + " Expected = " + genderTwo.getValueAsString()
						+ " but, submitted file contains gender of " + genderOne.getValueAsString();
				operationOutcome
						.addIssue(createScenarioResults(IssueType.INCOMPLETE, resourceName + ".gender", errorMessage));
			} else if (StringUtils.isEmpty(genderOne.getValueAsString())
					&& StringUtils.isNotEmpty(genderTwo.getValueAsString())
					|| (genderOne == null && genderTwo != null)) {
				String errorMessage = "The scenario requires gender, but submitted file does not have gender";
				operationOutcome
						.addIssue(createScenarioResults(IssueType.CONFLICT, resourceName + ".gender", errorMessage));
			} else if (StringUtils.isNotEmpty(genderOne.getValueAsString())
					&& StringUtils.isEmpty(genderTwo.getValueAsString()) || (genderOne != null && genderTwo == null)) {
				String errorMessage = "The scenario does not require gender, but submitted file does have gender";
				operationOutcome
						.addIssue(createScenarioResults(IssueType.CONFLICT, resourceName + ".gender", errorMessage));
			} else {
				logger.info("gender information is null OR equal in both inputJson and goldJson ");
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareGender of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareDate(String fieldName, Date birthDateOne, Date birthDateTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(birthDateOne) && ObjectUtils.isNotEmpty(birthDateTwo)
					&& (!(birthDateOne.equals(birthDateTwo)))) {
				String errorMessage = fieldName + " Expected = " + birthDateTwo + " but, submitted file contains of "
						+ birthDateOne;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(birthDateOne) && ObjectUtils.isNotEmpty(birthDateTwo)
					|| (birthDateOne == null && birthDateTwo != null)) {
				String errorMessage = "The scenario require " + fieldName + ", but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isNotEmpty(birthDateOne) && ObjectUtils.isEmpty(birthDateTwo)
					|| (birthDateOne != null && birthDateTwo == null)) {
				String errorMessage = "The scenario does not requires " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else {
				logger.info(fieldName + " information is null OR equal in both inputJson and goldJson ");
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDate of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareListOfAddress(String fieldName, List<Address> addressListOne,
			List<Address> addressListTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, addressListOne, addressListTwo, operationOutcome, scenarioName)) {
				for (Address addressOne : addressListOne) {
					for (Address addressTwo : addressListTwo) {
						compareAddress(fieldName, addressOne, addressTwo, operationOutcome, scenarioName);
					}
				}
			} else if (ObjectUtils.isNotEmpty(addressListOne) && ObjectUtils.isEmpty(addressListTwo)) {
				String errorMessage = "The scenario does not require address, but submitted file does have address";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(addressListOne) && ObjectUtils.isNotEmpty(addressListTwo)) {
				String errorMessage = "The scenario requires address, but submitted file does not have address";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfAddress of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareAddress(String fieldName, Address addressOne, Address addressTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(addressOne) && ObjectUtils.isNotEmpty(addressTwo)) {
				// city
				if (addressOne.hasCity() && addressTwo.hasCity()
						&& (!(addressOne.getCity().equalsIgnoreCase(addressTwo.getCity())))) {
					String errorMessage = fieldName + ".city Expected = " + addressTwo.getCity()
							+ " but, submitted file contains city of " + addressOne.getCity();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".city", errorMessage));
				} else if (addressOne.hasCity() && !addressTwo.hasCity()) {
					String errorMessage = "The scenario does not require city, but submitted file does have city";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".city", errorMessage));
				} else if (!addressOne.hasCity() && addressTwo.hasCity()) {
					String errorMessage = "The scenario requires city, but submitted file does not have city";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".city", errorMessage));
				} else {
					logger.info("address.city information is null OR equal in both inputJson and goldJson");
				}
				// line
				if (addressOne.hasLine() && addressTwo.hasLine()) {
					compareString(fieldName + ".line", addressOne.getLine().toString(), addressTwo.getLine().toString(),
							operationOutcome, scenarioName);
				} else if (addressOne.hasLine() && !addressTwo.hasLine()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".line but submitted file does have .line";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".line", errorMessage));
				} else if (!addressOne.hasLine() && addressTwo.hasLine()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".line, but submitted file does not have .line";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".line", errorMessage));
				} else {
					logger.info("address.line information is null OR equal in both inputJson and goldJson");
				}
				// state
				if (addressOne.hasState() && addressTwo.hasState()
						&& (!(addressOne.getState().equalsIgnoreCase(addressTwo.getState())))) {
					String errorMessage = fieldName + ".state Expected = " + addressTwo.getState()
							+ " but, submitted file contains state of " + addressOne.getState();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".state", errorMessage));
				} else if (addressOne.hasState() && !addressTwo.hasState()) {
					String errorMessage = "The scenario does not require state, but submitted file does have state";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".state", errorMessage));
				} else if (!addressOne.hasState() && addressTwo.hasState()) {
					String errorMessage = "The scenario requires state, but submitted file does not have state";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".state", errorMessage));
				} else {
					logger.info("address.state information is null OR equal in both inputJson and goldJson");
				}

				// postalCode
				if (addressOne.hasPostalCode() && addressTwo.hasPostalCode()
						&& (!(addressOne.getPostalCode().equalsIgnoreCase(addressTwo.getPostalCode())))) {
					String errorMessage = fieldName + ".postalCode Expected = " + addressTwo.getPostalCode()
							+ " but, submitted file contains postalCode of " + addressOne.getPostalCode();
					operationOutcome.addIssue(
							createScenarioResults(IssueType.INCOMPLETE, fieldName + ".postalCode", errorMessage));
				} else if (addressOne.hasPostalCode() && !addressTwo.hasPostalCode()) {
					String errorMessage = "The scenario does not require postalCode, but submitted file does have postalCode";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".postalCode", errorMessage));
				} else if (!addressOne.hasPostalCode() && addressTwo.hasPostalCode()) {
					String errorMessage = "The scenario requires postalCode, but submitted file does not have postalCode";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".postalCode", errorMessage));
				} else {
					logger.info("address.postalCode information is null OR equal in both inputJson and goldJson");
				}
				// period
				if (addressOne.hasPeriod() && addressTwo.hasPeriod()) {
					comparePeriod(fieldName + ".period", addressOne.getPeriod(), addressTwo.getPeriod(),
							operationOutcome, scenarioName);
				} else if (addressOne.hasPeriod() && !addressTwo.hasPeriod()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".period but submitted file does have .period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".line", errorMessage));
				} else if (!addressOne.hasPeriod() && addressTwo.hasPeriod()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".period, but submitted file does not have .period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".line", errorMessage));
				} else {
					logger.info("address.period information is null OR equal in both inputJson and goldJson");
				}

				// country
				if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_PATIENT)) {
					if (addressOne.hasCountry() && addressTwo.hasCountry()
							&& (!(addressOne.getCountry().equalsIgnoreCase(addressTwo.getCountry())))) {
						String errorMessage = fieldName + ".country Expected = " + addressTwo.getCountry()
								+ " but, submitted file contains country of " + addressOne.getCountry();
						operationOutcome.addIssue(
								createScenarioResults(IssueType.INCOMPLETE, fieldName + ".country", errorMessage));
					} else if (addressOne.hasCountry() && !addressTwo.hasCountry()) {
						String errorMessage = "The scenario does not require country, but submitted file does have country";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".country", errorMessage));
					} else if (!addressOne.hasCountry() && addressTwo.hasCountry()) {
						String errorMessage = "The scenario requires country, but submitted file does not have country";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".country", errorMessage));
					} else {
						logger.info("address.country information is null OR equal in both inputJson and goldJson");
					}

					// use
					if (addressOne.hasUse() && addressTwo.hasUse()) {
						compareString(fieldName + ".use", addressOne.getUse().getDisplay(),
								addressTwo.getUse().getDisplay(), operationOutcome, scenarioName);
					} else if (addressOne.hasUse() && !addressTwo.hasUse()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".use but submitted file does have .use";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".country", errorMessage));
					} else if (!addressOne.hasUse() && addressTwo.hasUse()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".use, but submitted file does not have .use";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".country", errorMessage));
					} else {
						logger.info("address.use information is null OR equal in both inputJson and goldJson");
					}

					// type
					if (addressOne.hasType() && addressTwo.hasType()) {
						compareString(fieldName + ".type", addressOne.getType().getDisplay(),
								addressTwo.getType().getDisplay(), operationOutcome, scenarioName);
					} else if (addressOne.hasType() && !addressTwo.hasType()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".type but submitted file does have .type";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
					} else if (!addressOne.hasType() && addressTwo.hasType()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".type, but submitted file does not have .type";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
					} else {
						logger.info("address.type information is null OR equal in both inputJson and goldJson");
					}

					// text
					if (addressOne.hasText() && addressTwo.hasText()) {
						compareString(fieldName + ".text", addressOne.getText(), addressTwo.getText(), operationOutcome,
								scenarioName);
					} else if (addressOne.hasText() && !addressTwo.hasText()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".text but submitted file does have .text";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else if (!addressOne.hasText() && addressTwo.hasText()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".text, but submitted file does not have .text";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else {
						logger.info("address.text information is null OR equal in both inputJson and goldJson");
					}
				}
			} else if (addressOne != null && addressTwo == null) {

				String errorMessage = "The scenario does not require address, but submitted file does have address";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (addressOne == null && ObjectUtils.isNotEmpty(addressTwo)) {

				String errorMessage = "The scenario requires address, but location file does not have address";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareAddress of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareListOfContactPoint(String fieldName, List<ContactPoint> telecomListOne,
			List<ContactPoint> telecomListTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, telecomListOne, telecomListTwo, operationOutcome, scenarioName)) {

				for (int i = 0; i < telecomListOne.size();) {
					for (int j = 0; j < telecomListTwo.size(); j++) {
						compareContactPoint(fieldName, telecomListOne.get(i), telecomListTwo.get(j), operationOutcome,
								scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(telecomListOne) && ObjectUtils.isEmpty(telecomListTwo)) {
				String errorMessage = "The scenario does not require telecom, but submitted file does have telecom";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(telecomListOne) && ObjectUtils.isNotEmpty(telecomListTwo)) {
				String errorMessage = "The scenario requires telecom, but submitted file does not have telecom";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfContactPoint of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareContactPoint(String fieldName, ContactPoint contactPointOne,
			ContactPoint contactPointTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(contactPointOne) && ObjectUtils.isNotEmpty(contactPointTwo)) {
				// compare system
				if (contactPointOne.hasSystem() && contactPointTwo.hasSystem() && !(contactPointOne.getSystem()
						.toString().equalsIgnoreCase(contactPointTwo.getSystem().toString()))) {
					String errorMessage = fieldName + ".system Expected = " + contactPointTwo.getSystem().getDisplay()
							+ " but, submitted file contains system of " + contactPointOne.getSystem().getDisplay();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".system", errorMessage));
				} else if (contactPointOne.hasSystem() && !contactPointTwo.hasSystem()) {
					String errorMessage = "The scenario does not require system, but submitted file does have system";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".system", errorMessage));
				} else if (!contactPointOne.hasSystem() && contactPointTwo.hasSystem()) {
					String errorMessage = "The scenario requires system, but submitted file does not have system";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".system", errorMessage));
				} else {
					logger.info(fieldName + ".system information is null OR equal in both inputJson and goldJson");
				}
				// compare value
				if (contactPointOne.hasValue() && contactPointTwo.hasValue()
						&& !(contactPointOne.getValue().equalsIgnoreCase(contactPointTwo.getValue()))) {
					String errorMessage = fieldName + ".value Expected = " + contactPointTwo.getValue()
							+ " but, submitted file contains value of " + contactPointOne.getValue();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".value", errorMessage));
				} else if (contactPointOne.hasValue() && !contactPointTwo.hasValue()) {
					String errorMessage = "The scenario does not require value, but submitted file does have value";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".value", errorMessage));
				} else if (!contactPointOne.hasValue() && contactPointTwo.hasValue()) {
					String errorMessage = "The scenario requires value, but submitted file does not have value";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".value", errorMessage));
				} else {
					logger.info(fieldName + ".value information is null OR equal in both inputJson and goldJson");
				}
				// compare use
				if (contactPointOne.hasUse() && contactPointTwo.hasUse() && !(contactPointOne.getUse().toString()
						.equalsIgnoreCase(contactPointTwo.getUse().toString()))) {
					String errorMessage = fieldName + ".use Expected = " + contactPointTwo.getUse().getDisplay()
							+ " but, submitted file contains use of " + contactPointOne.getUse().getDisplay();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".use", errorMessage));
				} else if (contactPointOne.hasUse() && !contactPointTwo.hasUse()) {
					String errorMessage = "The scenario does not require use, but submitted file does have use";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".use", errorMessage));
				} else if (!contactPointOne.hasUse() && contactPointTwo.hasUse()) {
					String errorMessage = "The scenario requires use, but submitted file does not have use";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".use", errorMessage));
				} else {
					logger.info("telecome.use information is null OR equal in both inputJson and goldJson");
				}

				// period
				if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_PATIENT)) {

					if (contactPointOne.hasPeriod() && contactPointTwo.hasPeriod()) {
						comparePeriod(fieldName + ".period", contactPointOne.getPeriod(), contactPointTwo.getPeriod(),
								operationOutcome, scenarioName);
					} else if (contactPointOne.hasPeriod() && !contactPointTwo.hasPeriod()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".period, but submitted file does have " + fieldName + ".period";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
					} else if (!contactPointOne.hasPeriod() && contactPointTwo.hasPeriod()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".period, but submitted file does not have " + fieldName + ".period";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
					} else {
						logger.info("telecome.period information is null OR equal in both inputJson and goldJson");
					}
				}
			} else if (contactPointOne != null && contactPointTwo == null) {

				String errorMessage = "The scenario does not require telecom, but submitted file does have telecom";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (contactPointOne == null && ObjectUtils.isNotEmpty(contactPointTwo)) {

				String errorMessage = "The scenario requires telecom, but location file does not have telecom";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareContactPoint of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareLanguage(String fieldName, String languageOne, String languageTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (StringUtils.isNotEmpty(languageOne) && StringUtils.isNotEmpty(languageTwo)
					&& !(languageOne.equalsIgnoreCase(languageTwo))) {
				String errorMessage = fieldName + " Expected = " + languageTwo
						+ " but, submitted file contains language of " + languageOne;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (StringUtils.isNotEmpty(languageOne) && StringUtils.isEmpty(languageTwo)) {
				String errorMessage = "The scenario does not require language, but submitted file does have language";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (StringUtils.isEmpty(languageOne) && StringUtils.isNotEmpty(languageTwo)) {
				String errorMessage = "The scenario requires language, but submitted file does not have language";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else {
				logger.info(fieldName + " information is null OR equal in both inputJson and goldJson");
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareLanguage of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareBooleanType(String fieldName, BooleanType booleanOne, BooleanType booleanTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(booleanOne) && ObjectUtils.isNotEmpty(booleanTwo)) {
				if (booleanOne.hasValue() && booleanTwo.hasValue()
						&& (!(booleanOne.getValue().equals(booleanTwo.getValue())))) {
					String errorMessage = fieldName + "Expected = " + booleanTwo + " but, submitted file contains "
							+ booleanOne;
					operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, "", errorMessage));
				} else if (booleanOne.hasValue() && !booleanTwo.hasValue()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ", but submitted file does have" + fieldName;
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!booleanOne.hasValue() && booleanTwo.hasValue()) {
					String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have  "
							+ fieldName;
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + " information is null OR equal in both inputJson and goldJson");
				}

			} else if (booleanOne != null && booleanTwo == null) {

				String errorMessage = "The scenario does not require" + fieldName + ", but submitted file does have"
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (booleanOne == null && ObjectUtils.isNotEmpty(booleanTwo)) {

				String errorMessage = "The scenario requires" + fieldName + ", but location file does not have"
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareBoolean of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareListOfCommunication(String fieldName,
			List<PatientCommunicationComponent> communicationListOne,
			List<PatientCommunicationComponent> communicationListTwo, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, communicationListOne, communicationListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < communicationListOne.size();) {
					for (int j = 0; j < communicationListTwo.size(); j++) {
						compareCommunication(fieldName, communicationListOne.get(i), communicationListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (!communicationListOne.isEmpty() && communicationListTwo.isEmpty()) {
				String errorMessage = "The scenario does not require communication, but submitted file does have communication";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (communicationListOne.isEmpty() && !communicationListTwo.isEmpty()) {
				String errorMessage = "The scenario requires communication, but submitted file does not have communication";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfCommunication of ComparatorUtils :: ", ex.getMessage());
		}
	}

	private static void compareCommunication(String fieldName, PatientCommunicationComponent communicationComponentOne,
			PatientCommunicationComponent communicationComponentTwo, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(communicationComponentOne)
					&& ObjectUtils.isNotEmpty(communicationComponentTwo)) {

				// preferred
				if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_PATIENT)) {
					if (communicationComponentOne.hasPreferred() && communicationComponentTwo.hasPreferred()) {
						compareBoolean(fieldName + ".preferred", communicationComponentOne.getPreferred(),
								communicationComponentTwo.getPreferred(), operationOutcome, scenarioName);

					} else if (communicationComponentOne.hasPreferred() && !communicationComponentTwo.hasPreferred()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".preferred, but submitted file does have " + fieldName + ".preferred";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".preferred", errorMessage));
					} else if (!communicationComponentOne.hasPreferred() && communicationComponentTwo.hasPreferred()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".preferred, but submitted file does not have " + fieldName + ".preferred";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".preferred", errorMessage));
					} else {
						logger.info(
								fieldName + ".preferred information is null OR equal in both inputJson and goldJson");
					}
				}
				// language
				if (communicationComponentOne.hasLanguage() && communicationComponentTwo.hasLanguage()) {
					compareCodeableConcept(fieldName + ".language", communicationComponentOne.getLanguage(),
							communicationComponentTwo.getLanguage(), operationOutcome, scenarioName);

				} else if (communicationComponentOne.hasLanguage() && !communicationComponentTwo.hasLanguage()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".language, but submitted file does have " + fieldName + ".language";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".language", errorMessage));
				} else if (!communicationComponentOne.hasLanguage() && communicationComponentTwo.hasLanguage()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".language, but submitted file does not have " + fieldName + ".language";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".language", errorMessage));
				} else {
					logger.info(fieldName + ".language information is null OR equal in both inputJson and goldJson");
				}

			} else if (communicationComponentOne != null && communicationComponentTwo == null) {

				String errorMessage = "The scenario does not require communication, but submitted file does have communication";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (communicationComponentOne == null && ObjectUtils.isNotEmpty(communicationComponentTwo)) {

				String errorMessage = "The scenario requires communication, but submitted file does not have communication";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareCommunication of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareCodeableConcept(String fieldName, CodeableConcept codeableConceptOne,
			CodeableConcept codeableConceptTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			logger.info("compareCodeableConcept started");
			if (ObjectUtils.isNotEmpty(codeableConceptOne) && ObjectUtils.isNotEmpty(codeableConceptTwo)) {

				// coding
				if (codeableConceptOne.hasCoding() && codeableConceptTwo.hasCoding()) {
					List<Coding> codingListOne = codeableConceptOne.getCoding();
					List<Coding> codingListTwo = codeableConceptTwo.getCoding();
					compareListOfCoding(fieldName + ".coding", codingListOne, codingListTwo, operationOutcome,
							scenarioName);
				} else if (codeableConceptOne.hasCoding() && !codeableConceptTwo.hasCoding()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".coding, but submitted file does have " + fieldName + ".coding";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".coding", errorMessage));
				} else if (!codeableConceptOne.hasCoding() && codeableConceptTwo.hasCoding()) {
					String errorMessage = "The scenario requires " + fieldName + " OR/ANd " + fieldName
							+ ".coding, but submitted file does not have " + fieldName + " OR/ANd " + fieldName
							+ ".coding";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".coding", errorMessage));
				} else {
					logger.info(fieldName + ".coding information is null OR equal in both inputJson and goldJson");
				}

				// text
				if (scenarioNames.containsKey(scenarioName)) {
					if (codeableConceptOne.hasText() && codeableConceptTwo.hasText()) {
						compareString(fieldName + ".text", codeableConceptOne.getText(), codeableConceptTwo.getText(),
								operationOutcome, scenarioName);

					} else if (codeableConceptOne.hasText() && !codeableConceptTwo.hasText()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".text, but submitted file does have " + fieldName + ".text";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else if (!codeableConceptOne.hasText() && codeableConceptTwo.hasText()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".text, but submitted file does not have " + fieldName + ".text";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else {
						logger.info(fieldName + ".text information is null OR equal in both inputJson and goldJson");
					}
				}
			} else if (ObjectUtils.isNotEmpty(codeableConceptOne) && ObjectUtils.isEmpty(codeableConceptTwo)) {

				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(codeableConceptOne) && ObjectUtils.isNotEmpty(codeableConceptTwo)) {

				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else {
				logger.info("compareCodeableConcept End");
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareCodeableConcept of ComparatorUtils :: ", ex.getMessage());
		}
	}

	private static void compareListOfCoding(String fieldName, List<Coding> codingListOne, List<Coding> codingListTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		logger.info("compareListOfCoding started");
		try {
			if (compareListForSizeEquality(fieldName, codingListOne, codingListTwo, operationOutcome, scenarioName)) {
				for (int i = 0; i < codingListOne.size();) {
					for (int j = 0; j < codingListTwo.size(); j++) {
						compareCoding(fieldName, codingListOne.get(i), codingListTwo.get(j), operationOutcome,
								scenarioName);
						i++;
					}
				}

			}
			if (ObjectUtils.isNotEmpty(codingListOne) && ObjectUtils.isEmpty(codingListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName + " OR Submited size is greater than the expected size";
				operationOutcome
						.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".coding", errorMessage));
			} else if (ObjectUtils.isEmpty(codingListOne) && ObjectUtils.isNotEmpty(codingListTwo)) {
				String errorMessage = "The scenario requires " + fieldName + " , but submitted file does not have "
						+ fieldName + " OR Submited size is less than the expected size";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
			logger.info("compareListOfCoding End");
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfCoding of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareCoding(String fieldName, Coding codingOne, Coding codingTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(codingOne) && (ObjectUtils.isNotEmpty(codingTwo))) {
				// code
				if (codingOne.hasCode() && codingTwo.hasCode()
						&& !(codingOne.getCode().equalsIgnoreCase(codingTwo.getCode()))) {
					String errorMessage = fieldName + ".code Expected = " + codingTwo.getCode()
							+ ", but submitted file contains " + fieldName + ".code of " + codingOne.getCode();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".code", errorMessage));
				} else if (codingOne.hasCode() && !codingTwo.hasCode()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".code, but submitted file does have " + fieldName + ".code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else if (!codingOne.hasCode() && codingTwo.hasCode()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".code, but submitted file does not have " + fieldName + ".code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else {
					logger.info(fieldName + ".code information is null OR equal in both inputJson and goldJson");
				}

				// system
				if (codingOne.hasSystem() && codingTwo.hasSystem()
						&& !(codingOne.getSystem().equalsIgnoreCase(codingTwo.getSystem()))) {
					String errorMessage = fieldName + ".system Expected = " + codingTwo.getSystem()
							+ " but, submitted file contains " + fieldName + ".system of " + codingOne.getSystem();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".system", errorMessage));
				} else if (codingOne.hasSystem() && !codingTwo.hasSystem()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".system, but submitted file does have " + fieldName + ".system";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".system", errorMessage));
				} else if (!codingOne.hasSystem() && codingTwo.hasSystem()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".system, but submitted file does not have " + fieldName + ".system";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".system", errorMessage));
				} else {
					logger.info(fieldName + ".system information is null OR equal in both inputJson and goldJson");
				}

				// display
				if (scenarioNames.containsKey(scenarioName)) {

					if (codingOne.hasDisplay() && codingTwo.hasDisplay()
							&& !(codingOne.getDisplay().equalsIgnoreCase(codingTwo.getDisplay()))) {
						String errorMessage = fieldName + ".display Expected = " + codingTwo.getDisplay()
								+ " but, submitted file contains " + fieldName + ". display of "
								+ codingOne.getDisplay();
						operationOutcome.addIssue(
								createScenarioResults(IssueType.INCOMPLETE, fieldName + ".display", errorMessage));
					} else if (codingOne.hasDisplay() && !codingTwo.hasDisplay()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".display, but submitted file does have " + fieldName + ".display";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".display", errorMessage));
					} else if (!codingOne.hasDisplay() && codingTwo.hasDisplay()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".display, but submitted file does not have " + fieldName + ".display";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".display", errorMessage));
					} else {
						logger.info(
								fieldName + ".display  information is null OR equal in both inputJson and goldJson");
					}
					// version
					if (codingOne.hasVersion() && codingTwo.hasVersion()
							&& !(codingOne.getVersion().equalsIgnoreCase(codingTwo.getVersion()))) {
						String errorMessage = fieldName + ".version Expected = " + codingTwo.getVersion()
								+ " but, submitted file contains " + fieldName + ". version of "
								+ codingOne.getVersion();
						operationOutcome.addIssue(
								createScenarioResults(IssueType.INCOMPLETE, fieldName + ".version", errorMessage));
					} else if (codingOne.hasVersion() && !codingTwo.hasVersion()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".version, but submitted file does have " + fieldName + ".version";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".display", errorMessage));
					} else if (!codingOne.hasVersion() && codingTwo.hasVersion()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".version, but submitted file does not have " + fieldName + " version";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".version", errorMessage));
					} else {
						logger.info(
								fieldName + ".version  information is null OR equal in both inputJson and goldJson");
					}
				}
			} else if (codingOne != null && codingTwo == null) {

				String errorMessage = "The scenario does not require" + fieldName + ", but submitted file does have"
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (codingOne == null && ObjectUtils.isNotEmpty(codingTwo)) {

				String errorMessage = "The scenario requires" + fieldName + ", but submitted file does not have"
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareCoding of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareListOfReference(String fieldName, List<Reference> generalPractitionerOne,
			List<Reference> generalPractitionerTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, generalPractitionerOne, generalPractitionerTwo, operationOutcome,
					scenarioName)) {

				for (int i = 0; i < generalPractitionerOne.size();) {
					for (int j = 0; j < generalPractitionerTwo.size(); j++) {
						compareReference(fieldName, generalPractitionerOne.get(i), generalPractitionerTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}

			} else if (ObjectUtils.isNotEmpty(generalPractitionerOne) && ObjectUtils.isEmpty(generalPractitionerTwo)) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(generalPractitionerOne) && ObjectUtils.isNotEmpty(generalPractitionerTwo)) {
				String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfReference of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareReference(String fieldName, Reference managingOrganizationOne,
			Reference managingOrganizationTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(managingOrganizationOne) && ObjectUtils.isNotEmpty(managingOrganizationTwo)) {
				// reference
				if (managingOrganizationOne.hasReference() && managingOrganizationTwo.hasReference()) {
					compareString(fieldName + ".reference", managingOrganizationOne.getReference(),
							managingOrganizationTwo.getReference(), operationOutcome, scenarioName);
				} else if (managingOrganizationOne.hasReference() && !managingOrganizationTwo.hasReference()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".reference, but submitted file does have " + fieldName + ".reference";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".reference", errorMessage));
				} else if (!managingOrganizationOne.hasReference() && managingOrganizationTwo.hasReference()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".reference, but submitted file does not have " + fieldName + ".reference";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".reference", errorMessage));
				} else {
					logger.info("reference information is null OR equal in both inputJson and goldJson");
				}
				// display
				if (managingOrganizationOne.hasDisplay() && managingOrganizationTwo.hasDisplay()) {
					compareString(fieldName + ".display", managingOrganizationOne.getDisplay(),
							managingOrganizationTwo.getDisplay(), operationOutcome, scenarioName);

				} else if (managingOrganizationOne.hasDisplay() && !managingOrganizationTwo.hasDisplay()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".display, but submitted file does have " + fieldName + ".display";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".display", errorMessage));
				} else if (!managingOrganizationOne.hasDisplay() && managingOrganizationTwo.hasDisplay()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".display, but submitted file does not have " + fieldName + ".display";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".display", errorMessage));
				} else {
					logger.info("display information is null OR equal in both inputJson and goldJson");
				}

				// type
				if (managingOrganizationOne.hasType() && managingOrganizationTwo.hasType()) {
					compareString(fieldName + ".type", managingOrganizationOne.getType(),
							managingOrganizationTwo.getType(), operationOutcome, scenarioName);
				} else if (managingOrganizationOne.hasType() && !managingOrganizationTwo.hasType()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".type, but submitted file does have " + fieldName + ".type";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".type", errorMessage));
				} else if (!managingOrganizationOne.hasType() && managingOrganizationTwo.hasType()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".type, but submitted file does not have " + fieldName + ".type";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".type", errorMessage));
				} else {
					logger.info("type information is null OR equal in both inputJson and goldJson");
				}
			} else if (!managingOrganizationOne.isEmpty() && managingOrganizationTwo.isEmpty()) {

				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (managingOrganizationOne.isEmpty() && !managingOrganizationTwo.isEmpty()) {

				String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareReference of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareListOfIdentifier(String fieldName, List<Identifier> identifierListOne,
			List<Identifier> identifierListTwo, OperationOutcome operationOutcome, String scenarioName) {
		logger.info("Entry - compareListOfIdentifier");
		if (compareListForSizeEquality(fieldName, identifierListOne, identifierListTwo, operationOutcome,
				scenarioName)) {

			for (int i = 0; i < identifierListOne.size();) {
				for (int j = 0; j < identifierListTwo.size(); j++) {
					compareIdentifier(fieldName, identifierListOne.get(i), identifierListTwo.get(j), operationOutcome,
							scenarioName);
					i++;
				}
			}
		} else if (ObjectUtils.isNotEmpty(identifierListOne) && ObjectUtils.isEmpty(identifierListTwo)) {
			String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
					+ fieldName;
			operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
		} else if (ObjectUtils.isEmpty(identifierListOne) && ObjectUtils.isNotEmpty(identifierListTwo)) {
			String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
					+ fieldName;
			operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
		}

		logger.info("Exit - compareListOfIdentifier");
	}

	public static void compareIdentifier(String fieldName, Identifier identifierOne, Identifier identifierTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (!identifierOne.isEmpty() && !identifierTwo.isEmpty()) {
				// compare system
				if (identifierOne.hasSystem() && identifierTwo.hasSystem()) {
					compareString(fieldName + ".system", identifierOne.getSystem(), identifierTwo.getSystem(),
							operationOutcome, scenarioName);
				} else if (identifierOne.hasSystem() && !identifierTwo.hasSystem()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".system, but submitted file does have " + fieldName + ".system";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".system", errorMessage));
				} else if (!identifierOne.hasSystem() && identifierTwo.hasSystem()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".system, but submitted file does not have " + fieldName + ".system";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".system", errorMessage));
				} else {
					logger.info(fieldName + ".system information is null OR equal in both inputJson and goldJson");
				}

				// compare value
				if (identifierOne.hasValue() && identifierTwo.hasValue()) {
					compareString(fieldName + ".value", identifierOne.getValue(), identifierTwo.getValue(),
							operationOutcome, scenarioName);
				} else if (identifierOne.hasValue() && !identifierTwo.hasValue()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".value, but submitted file does have " + fieldName + ".value";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".value", errorMessage));
				} else if (!identifierOne.hasValue() && identifierTwo.hasValue()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".value, but submitted file does not have " + fieldName + ".value";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".value", errorMessage));
				} else {
					logger.info(fieldName + ".value information is null OR equal in both inputJson and goldJson");
				}

				// assigner
				if (scenarioNames.containsKey(scenarioName)) {
					if (identifierOne.hasAssigner() && identifierTwo.hasAssigner()) {
						compareReference(fieldName + ".assigner", identifierOne.getAssigner(),
								identifierTwo.getAssigner(), operationOutcome, scenarioName);
					} else if (identifierOne.hasAssigner() && !identifierTwo.hasAssigner()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".assigner, but submitted file does have " + fieldName + ".assigner";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".assigner", errorMessage));
					} else if (!identifierOne.hasAssigner() && identifierTwo.hasAssigner()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".assigner, but submitted file does not have " + fieldName + ".assigner";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".assigner", errorMessage));
					} else {
						logger.info(
								fieldName + ".assigner information is null OR equal in both inputJson and goldJson");
					}

					// period
					if (identifierOne.hasPeriod() && identifierTwo.hasPeriod()) {
						comparePeriod(fieldName + ".period", identifierOne.getPeriod(), identifierTwo.getPeriod(),
								operationOutcome, scenarioName);
					} else if (identifierOne.hasPeriod() && !identifierTwo.hasPeriod()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".period, but submitted file does have " + fieldName + ".period";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
					} else if (!identifierOne.hasPeriod() && identifierTwo.hasPeriod()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".period, but submitted file does not have " + fieldName + ".period";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
					} else {
						logger.info(fieldName + ".period information is null OR equal in both inputJson and goldJson");
					}

					// compare use
					if (identifierOne.hasUse() && identifierTwo.hasUse()) {
						compareString(fieldName + ".use", identifierOne.getUse().getDisplay(),
								identifierTwo.getUse().getDisplay(), operationOutcome, scenarioName);
					} else if (identifierOne.hasUse() && !identifierTwo.hasUse()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".use, but submitted file does have " + fieldName + ".use";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".use", errorMessage));
					} else if (!identifierOne.hasUse() && identifierTwo.hasUse()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".use, but submitted file does not have " + fieldName + ".use";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".use", errorMessage));
					} else {
						logger.info(fieldName + ".use information is null OR equal in both inputJson and goldJson");
					}

					// compare type
					if (identifierOne.hasType() && identifierTwo.hasType()) {
						logger.info(" Compare identifier Type started");
						CodeableConcept codeableConceptOne = identifierOne.getType();
						CodeableConcept codeableConceptTwo = identifierTwo.getType();
						compareCodeableConcept(fieldName + ".type", codeableConceptOne, codeableConceptTwo,
								operationOutcome, scenarioName);
						logger.info(" Compare identifier Type End");
					} else if (identifierOne.hasType() && !identifierTwo.hasType()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".type, but submitted file does have " + fieldName + ".type";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
					} else if (!identifierOne.hasType() && identifierTwo.hasType()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".type, but submitted file does not have " + fieldName + ".type";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
					} else {
						logger.info(fieldName + ".type information is null OR equal in both inputJson and goldJson");
					}
				}
			} else if (!identifierOne.isEmpty() && identifierTwo.isEmpty()) {

				String errorMessage = "The scenario does not require identifier, but submitted file does have identifier";
				operationOutcome
						.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".identifier", errorMessage));
			} else if (identifierOne.isEmpty() && !identifierTwo.isEmpty()) {

				String errorMessage = "The scenario requires identifier, but submitted file does not have identifier";
				operationOutcome
						.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".identifier", errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareIdentifier of ComparatorUtils :: ", ex.getMessage());

		}
	}

	public static void compareListOfExtension(String fieldName, List<Extension> extListOne, List<Extension> extListTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, extListOne, extListTwo, operationOutcome, scenarioName)) {

				for (int i = 0; i < extListOne.size();) {
					for (int j = 0; j < extListTwo.size(); j++) {
						compareExtension(fieldName, extListOne.get(i), extListTwo.get(j), operationOutcome,
								scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(extListOne) && ObjectUtils.isEmpty(extListTwo)) {
				String errorMessage = "The scenario does not require extension, but submitted file does have extension";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(extListOne) && ObjectUtils.isNotEmpty(extListTwo)) {
				String errorMessage = "The scenario requires extension, but submitted file does not have extension";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfExtension of ComparatorUtils :: ", ex.getMessage());
		}
	}

	private static void compareExtension(String fieldName, Extension extensionOne, Extension extensionTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		if (ObjectUtils.isNotEmpty(extensionOne) && ObjectUtils.isNotEmpty(extensionTwo)) {
			// compare url
			if (extensionOne.hasUrl() && extensionTwo.hasUrl()
					&& (!(extensionOne.getUrl().equalsIgnoreCase(extensionTwo.getUrl())))) {
				String errorMessage = fieldName + ".url Expected = " + extensionTwo.getUrl()
						+ " but, submitted file contains url of " + extensionOne.getUrl();
				operationOutcome
						.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".url", errorMessage));
			} else if (extensionOne.hasUrl() && !extensionTwo.hasUrl()) {
				String errorMessage = "The scenario does not require " + fieldName
						+ ".url, but submitted file does have " + fieldName + ".url";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (!extensionOne.hasUrl() && extensionTwo.hasUrl()) {
				String errorMessage = "The scenario requires " + fieldName + ".url, but submitted file does not have "
						+ fieldName + ".url";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else {
				logger.info(fieldName + ".url information is null OR equal in both inputJson and goldJson");
			}
			// compare value
			if (extensionOne.hasValue() && extensionTwo.hasValue()) {
				// compare StringType value
				if (extensionOne.getValue() instanceof StringType && extensionTwo.getValue() instanceof StringType
						&& (!extensionOne.getValue().toString().equalsIgnoreCase(extensionTwo.getValue().toString()))) {
					String errorMessage = fieldName + ".valueStringType Expected = " + extensionTwo.getValue()
							+ " but, submitted file contains .valueStringType of " + extensionOne.getValue();
					operationOutcome.addIssue(
							createScenarioResults(IssueType.INCOMPLETE, fieldName + ".valueStringType", errorMessage));
				} else if (extensionOne.getValue() instanceof Coding && extensionTwo.getValue() instanceof Coding) {
					Coding codingOne = (Coding) extensionOne.getValue();
					Coding codingTwo = (Coding) extensionTwo.getValue();
					compareCoding(fieldName, codingOne, codingTwo, operationOutcome, scenarioName);
				}
			} else if (!extensionOne.hasValue() && extensionTwo.hasValue()) {
				String errorMessage = "The scenario requires " + fieldName + ".value, but submitted file does not have "
						+ fieldName + ".value";
				operationOutcome
						.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".value", errorMessage));
			} else {
				logger.info("Extension information is null OR equal in both inputJson and goldJson");
			}

		} else if (extensionOne != null && extensionTwo == null) {

			String errorMessage = "The scenario does not require extension, but submitted file does have extension";
			operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
		} else if (extensionOne == null && ObjectUtils.isNotEmpty(extensionTwo)) {

			String errorMessage = "The scenario requires extension, but submitted file does not have extension";
			operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
		}

	}

	/**
	 * Compare List For Size Equality
	 * 
	 * @param listOfObjectOne
	 * @param listOfObjectTwo
	 * @return false if both list size are not same, return true if same size
	 */
	private static boolean compareListForSizeEquality(String elementName, List<?> listOfObjectOne,
			List<?> listOfObjectTwo, OperationOutcome operationOutcome, String scenarioName) {
		boolean sizeOfBothObjectSame = false;
		try {
			sizeOfBothObjectSame = !listOfObjectOne.isEmpty() && !listOfObjectTwo.isEmpty()
					&& listOfObjectOne.size() == listOfObjectTwo.size();
			if (ObjectUtils.isNotEmpty(listOfObjectOne) && ObjectUtils.isNotEmpty(listOfObjectTwo)
					&& listOfObjectOne.size() > listOfObjectTwo.size()) {

				String errorMessage = "The size of the " + elementName + " is greater than the size of the Expected";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, elementName, errorMessage));
			} else if (ObjectUtils.isNotEmpty(listOfObjectOne) && ObjectUtils.isNotEmpty(listOfObjectTwo)
					&& listOfObjectOne.size() < listOfObjectTwo.size()) {

				String errorMessage = "The size of the " + elementName + " is less than the size of the Expected";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, elementName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListForSizeEquality of FhirComparatorUtils :: ", ex);
		}
		return sizeOfBothObjectSame;
	}

	public static void compareListOfCodeableConcept(String resourceName, List<CodeableConcept> codeableConceptListOne,
			List<CodeableConcept> codeableConceptListTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(resourceName, codeableConceptListOne, codeableConceptListTwo,
					operationOutcome, scenarioName)) {
				for (int i = 0; i < codeableConceptListOne.size();) {
					for (int j = 0; j < codeableConceptListTwo.size(); j++) {
						compareCodeableConcept(resourceName, codeableConceptListOne.get(i),
								codeableConceptListTwo.get(j), operationOutcome, scenarioName);
						i++;
					}
				}

			} else if (ObjectUtils.isNotEmpty(codeableConceptListOne) && ObjectUtils.isEmpty(codeableConceptListTwo)) {
				String errorMessage = "The scenario does not require " + resourceName + " but submitted file does have "
						+ resourceName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, resourceName, errorMessage));
			} else if (ObjectUtils.isEmpty(codeableConceptListOne) && ObjectUtils.isNotEmpty(codeableConceptListTwo)) {
				String errorMessage = "The scenario requires " + resourceName + " but submitted file does not have "
						+ resourceName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, resourceName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfCodeableConcept of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareAnnotation(String fieldName, Annotation annotationOne, Annotation annotationTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(annotationOne) && ObjectUtils.isNotEmpty(annotationTwo)
					&& annotationOne.hasAuthor() && annotationTwo.hasAuthor()) {
				// compare author stringType
				if (annotationOne.hasAuthorStringType() && annotationTwo.hasAuthorStringType()) {
					compareString(fieldName + ".authorStringType", annotationOne.getAuthorStringType().toString(),
							annotationTwo.getAuthorStringType().toString(), operationOutcome, scenarioName);
				} else if (annotationOne.hasAuthorStringType() && !annotationTwo.hasAuthorStringType()) {
					String errorMessage = "The scenario does not require" + fieldName
							+ ".authorStringType, but submitted file does have authorStringType";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".authorStringType", errorMessage));
				} else if (!annotationOne.hasAuthorStringType() && annotationTwo.hasAuthorStringType()) {
					String errorMessage = "The scenario requires" + fieldName
							+ ".authorStringType, but submitted file does not have authorStringType";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".authorStringType", errorMessage));
				} else {
					logger.info(fieldName
							+ ".authorStringType information is null OR equal in both inputJson and goldJson");
				}
				// compare author reference
				if (annotationOne.hasAuthorReference() && annotationTwo.hasAuthorReference()) {
					compareReference(fieldName + ".authorReference", annotationOne.getAuthorReference(),
							annotationTwo.getAuthorReference(), operationOutcome, scenarioName);

				} else if (annotationOne.hasAuthorReference() && !annotationTwo.hasAuthorReference()) {
					String errorMessage = "The scenario does not require" + fieldName
							+ ".authorReference, but submitted file does have authorReference";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".authorReference", errorMessage));
				} else if (!annotationOne.hasAuthorReference() && annotationTwo.hasAuthorReference()) {
					String errorMessage = "The scenario requires" + fieldName
							+ ".authorReference, but submitted file does not have authorReference";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".authorReference", errorMessage));
				} else {
					logger.info(
							fieldName + ".authorReference information is null OR equal in both inputJson and goldJson");
				}
			}
			// compare time
			if (ObjectUtils.isNotEmpty(annotationOne) && ObjectUtils.isNotEmpty(annotationTwo)) {
				if (annotationOne.hasTime() && annotationTwo.hasTime()) {
					compareDate(fieldName + ".time", annotationOne.getTime(), annotationTwo.getTime(), operationOutcome,
							scenarioName);
				} else if (annotationOne.hasTime() && !annotationTwo.hasTime()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".time, but submitted file does have " + fieldName + ".time";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".time", errorMessage));
				} else if (!annotationOne.hasTime() && annotationTwo.hasTime()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".time, but submitted file does not have " + fieldName + ".time";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".time", errorMessage));
				} else {
					logger.info(fieldName + ".time information is null in both inputJson and goldJson");
				}

				// compare text
				if (annotationOne.hasText() && annotationTwo.hasText()) {
					compareString(fieldName, annotationOne.getText(), annotationTwo.getText(), operationOutcome,
							scenarioName);

				} else if (annotationOne.hasText() && !annotationTwo.hasText()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".text, but submitted file does have text";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
				} else if (!annotationOne.hasText() && annotationTwo.hasText()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".text, but submitted file does not have text";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
				} else {
					logger.info(fieldName + ".text information is null OR equal in both inputJson and goldJson");
				}

			} else if (annotationOne != null && annotationTwo == null) {

				String errorMessage = "The scenario does not require note, but submitted file does have note";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (annotationOne == null && ObjectUtils.isNotEmpty(annotationTwo)) {

				String errorMessage = "The scenario requires note, but submitted file does not have note";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception e) {

		}

	}

	public static void compareListOfAnnotation(String fieldName, List<Annotation> annotationOneList,
			List<Annotation> annotationTwoList, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, annotationOneList, annotationTwoList, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < annotationOneList.size();) {
					for (int j = 0; j < annotationTwoList.size(); j++) {
						compareAnnotation(fieldName, annotationOneList.get(i), annotationTwoList.get(j),
								operationOutcome, scenarioName);

						i++;
					}
				}
			} else if (!annotationOneList.isEmpty() && annotationTwoList.isEmpty()) {
				String errorMessage = "The scenario does not require note, but submitted file does have note";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (annotationOneList.isEmpty() && !annotationTwoList.isEmpty()) {
				String errorMessage = "The scenario requires note, but submitted file does not have note";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfAnnotation of ComparatorUtils :: ", ex.getMessage());
		}

	}

	/**
	 * Compare two String
	 * 
	 * @param textOne
	 * @param textTwo
	 * @return
	 */
	public static void compareString(String fieldName, String textOne, String textTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (StringUtils.isNotEmpty(textOne) && StringUtils.isNotEmpty(textTwo)) {
				if ((!(textOne.equals(textTwo)))) {
					String errorMessage = fieldName + " Expected = " + textTwo + " but, submitted file contains "
							+ fieldName + " of " + textOne;
					operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
				} else if (StringUtils.isEmpty(textOne) && StringUtils.isNotEmpty(textTwo)) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ", but submitted file does have " + fieldName;
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (StringUtils.isNotEmpty(textOne) && StringUtils.isEmpty(textTwo)) {
					String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have "
							+ fieldName;
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + "information is null OR equal in both inputJson and goldJson");
				}
			} else if (ObjectUtils.isNotEmpty(textOne) && textTwo == null) {

				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (textOne == null && ObjectUtils.isNotEmpty(textTwo)) {

				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception while comparing two strings in ComparatorUtils class", ex);
		}

	}

	/**
	 * Compare two UriType
	 * 
	 * @param textOne
	 * @param textTwo
	 * @return
	 */
	public static void compareUriType(String resourceName, UriType textOne, UriType textTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(textOne) && ObjectUtils.isNotEmpty(textTwo)) {
				if ((!(textOne.equals(textTwo)))) {
					String errorMessage = "implicitRules Expected = " + textTwo.getValue()
							+ " but, submitted file contains implicitRules of " + textOne.getValue();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, ".implicitRules", errorMessage));
				} else if (ObjectUtils.isNotEmpty(textOne) && ObjectUtils.isEmpty(textTwo)) {
					String errorMessage = "The scenario does not require Impicit Rules, but submitted file does have implicitRules";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, ".implicitRules", errorMessage));
				} else if (ObjectUtils.isEmpty(textOne) && ObjectUtils.isNotEmpty(textTwo)) {
					String errorMessage = "The scenario requires Impicit Rules, but submitted file does not have implicitRules";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, ".implicitRules", errorMessage));
				} else {
					logger.info("implicitRules is null in both inputJson and goldJson");
				}
			}
		} catch (Exception e) {
			logger.error("\n Exception while comparing two ImplicitRules in ComparatorUtils class", e);
		}
	}

	/**
	 * Compare two AllergyIntoleranceCategory
	 * 
	 * @param CategoryOne
	 * @param CategoryTwo
	 * @return
	 */
	public static void compareAllergyIntoleranceCategory(String resourceName,
			Enumeration<AllergyIntoleranceCategory> CategoryOne, Enumeration<AllergyIntoleranceCategory> CategoryTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(CategoryOne) && ObjectUtils.isNotEmpty(CategoryTwo)) {
				if (CategoryOne.hasCode() && CategoryTwo.hasCode()
						&& (!(CategoryOne.getCode().equalsIgnoreCase(CategoryTwo.getCode())))) {
					String errorMessage = "AllergyIntoleranceCategory Expected = " + CategoryTwo.getCode()
							+ " but, submitted file contains AllergyIntoleranceCategory of " + CategoryOne.getCode();
					operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, ".code", errorMessage));
				} else if (CategoryOne.hasCode() && !CategoryTwo.hasCode()) {
					String errorMessage = "The scenario does not require AllergyIntoleranceCategory, but submitted file does have AllergyIntoleranceCategory";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".code", errorMessage));
				} else if (!CategoryOne.hasCode() && CategoryTwo.hasCode()) {
					String errorMessage = "The scenario requires AllergyIntoleranceCategory, but submitted file does not have AllergyIntoleranceCategory";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".code", errorMessage));
				} else {
					logger.info("reference information is null in both inputJson and goldJson");
				}
			}

		} catch (Exception e) {
			logger.error("\n Exception while comparing  AllergyIntoleranceCategory in ComparatorUtils class", e);
		}
	}

	public static void compareListOfAllergyIntoleranceCategory(String resourceName,
			List<Enumeration<AllergyIntoleranceCategory>> CategoryOneList,
			List<Enumeration<AllergyIntoleranceCategory>> CategoryTwoList, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (compareListForSizeEquality("category", CategoryOneList, CategoryTwoList, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < CategoryOneList.size();) {
					for (int j = 0; j < CategoryTwoList.size(); j++) {
						compareAllergyIntoleranceCategory(resourceName, CategoryOneList.get(i), CategoryTwoList.get(j),
								operationOutcome, scenarioName);

						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(CategoryOneList) && ObjectUtils.isEmpty(CategoryTwoList)) {
				String errorMessage = "The scenario does not require AllergyIntolerance.Category, but submitted file does have AllergyIntolerance.Category";
				operationOutcome
						.addIssue(createScenarioResults(IssueType.CONFLICT, resourceName + ".category", errorMessage));
			} else if (ObjectUtils.isEmpty(CategoryOneList) && ObjectUtils.isNotEmpty(CategoryTwoList)) {
				String errorMessage = "The scenario requires AllergyIntolerance.Category, but submitted file does not have AllergyIntolerance.Category";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".category", errorMessage));
			}
		} catch (Exception e) {
			logger.error(
					"\n Exception while comparing  compareListOfAllergyIntoleranceCategory in ComparatorUtils class",
					e.getMessage());
		}
	}

	/**
	 * Compare two AllergyIntoleranceReaction
	 * 
	 * @param textOne
	 * @param textTwo
	 * @return
	 */

	public static void compareAllergyIntoleranceReactionComponent(String resourceName,
			AllergyIntoleranceReactionComponent ractionOne, AllergyIntoleranceReactionComponent reactionTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(ractionOne) && ObjectUtils.isNotEmpty(reactionTwo)) {

				// compare manifestation
				if (ractionOne.hasManifestation() && reactionTwo.hasManifestation()) {
					compareListOfCodeableConcept(resourceName + ".manifestation", ractionOne.getManifestation(),
							reactionTwo.getManifestation(), operationOutcome, scenarioName);
				} else if (ractionOne.hasManifestation() && !reactionTwo.hasManifestation()) {
					String errorMessage = "The scenario does not require " + resourceName
							+ ".manifestation, but submitted file does have manifestation";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, resourceName + ".manifestation", errorMessage));
				} else if (!ractionOne.hasManifestation() && reactionTwo.hasManifestation()) {
					String errorMessage = "The scenario requires " + resourceName
							+ ".manifestation, but submitted file does not have manifestation";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, resourceName + ".manifestation", errorMessage));
				} else {
					logger.info("reaction.manifestation information is null in both inputJson and goldJson");
				}

				if (scenarioName
						.equalsIgnoreCase(ScenarioConstants.ScenarioNameConstants.USCDI_FULL_ALLERGY_INTOLERANCE)) {
					// compare substance
					if (ractionOne.hasSubstance() && reactionTwo.hasSubstance()) {
						compareCodeableConcept(resourceName + ".substance", ractionOne.getSubstance(),
								reactionTwo.getSubstance(), operationOutcome, scenarioName);
					} else if (ractionOne.hasSubstance() && !reactionTwo.hasSubstance()) {
						String errorMessage = "The scenario does not require " + resourceName
								+ ".substance, but submitted file does have substance";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, resourceName + "substance", errorMessage));
					} else if (!ractionOne.hasSubstance() && reactionTwo.hasSubstance()) {
						String errorMessage = "The scenario requires " + resourceName
								+ ".substance, but submitted file does not have substance";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, resourceName + ".substance", errorMessage));
					} else {
						logger.info("reaction.substance information is null in both inputJson and goldJson");
					}

					// compare description
					if (ractionOne.hasDescription() && reactionTwo.hasDescription()) {
						compareString("description", ractionOne.getDescription(), reactionTwo.getDescription(),
								operationOutcome, scenarioName);
					} else if (ractionOne.hasDescription() && !reactionTwo.hasDescription()) {
						String errorMessage = "The scenario does not require " + resourceName
								+ ".description, but submitted file does have description";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, resourceName + ".description", errorMessage));
					} else if (!ractionOne.hasDescription() && reactionTwo.hasDescription()) {
						String errorMessage = "The scenario requires " + resourceName
								+ ".description, but submitted file does not have description";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, resourceName + ".description", errorMessage));
					} else {
						logger.info("reaction.description information is null in both inputJson and goldJson");
					}

					// compare severity
					if (ractionOne.hasSeverity() && reactionTwo.hasSeverity()
							&& (!(ractionOne.getSeverity().equals(reactionTwo.getSeverity())))) {
						String errorMessage = resourceName + " severity Expected = " + reactionTwo.getSeverity()
								+ " but, submitted file contains severity of " + ractionOne.getSeverity();
						operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE,
								resourceName + ".reaction.severity", errorMessage));
					} else if (ractionOne.hasSeverity() && !reactionTwo.hasSeverity()) {
						String errorMessage = "The scenario does not require " + resourceName
								+ ".severity, but submitted file does have severity";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, resourceName + ".severity", errorMessage));
					} else if (!ractionOne.hasSeverity() && reactionTwo.hasSeverity()) {
						String errorMessage = "The scenario requires " + resourceName
								+ ".severity, but submitted file does not have severity";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, resourceName + ".severity", errorMessage));
					} else {
						logger.info("reaction.severity information is null in both inputJson and goldJson");
					}

					// compare onset
					if (ractionOne.hasOnset() && reactionTwo.hasOnset()
							&& (!(ractionOne.getOnset().equals(reactionTwo.getOnset())))) {
						String errorMessage = resourceName + " onset Expected = " + reactionTwo.getOnset()
								+ " but, submitted file contains onset of " + ractionOne.getOnset();
						operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE,
								resourceName + ".reaction.onset", errorMessage));
					} else if (ractionOne.hasOnset() && !reactionTwo.hasOnset()) {
						String errorMessage = "The scenario does not require " + resourceName
								+ ".onset, but submitted file does have onset";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, resourceName + ".onset", errorMessage));
					} else if (!ractionOne.hasOnset() && reactionTwo.hasOnset()) {
						String errorMessage = "The scenario requires " + resourceName
								+ ".onset, but submitted file does not have onset";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, resourceName + ".onset", errorMessage));
					} else {
						logger.info("reaction.onset information is null in both inputJson and goldJson");
					}

					// compare exposureRoute
					if (ractionOne.hasExposureRoute() && reactionTwo.hasExposureRoute()) {
						compareCodeableConcept("exposureRoute", ractionOne.getExposureRoute(),
								reactionTwo.getExposureRoute(), operationOutcome, scenarioName);
					} else if (ractionOne.hasExposureRoute() && !reactionTwo.hasExposureRoute()) {
						String errorMessage = "The scenario does not require " + resourceName
								+ ".reaction.exposureRoute, but submitted file does have exposureRoute";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								resourceName + ".reaction.exposureRoute", errorMessage));
					} else if (!ractionOne.hasExposureRoute() && reactionTwo.hasExposureRoute()) {
						String errorMessage = "The scenario requires " + resourceName
								+ ".reaction.exposureRoute, but submitted file does not have exposureRoute";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								resourceName + ".reaction.exposureRoute", errorMessage));
					} else {
						logger.info("reaction.exposureRoute information is null in both inputJson and goldJson");
					}

					// compare note
					if (ractionOne.hasNote() && reactionTwo.hasNote()) {
						compareListOfAnnotation("note", ractionOne.getNote(), reactionTwo.getNote(), operationOutcome,
								scenarioName);
					} else if (ractionOne.hasNote() && !reactionTwo.hasNote()) {
						String errorMessage = "The scenario does not require " + resourceName
								+ ".reaction.note, but submitted file does have note";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								resourceName + ".reaction.note", errorMessage));
					} else if (!ractionOne.hasNote() && reactionTwo.hasNote()) {
						String errorMessage = "The scenario requires " + resourceName
								+ ".reaction.note, but submitted file does not have note";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								resourceName + ".reaction.note", errorMessage));
					} else {
						logger.info("reaction.note information is null in both inputJson and goldJson");
					}

					// compare extension
					if (ractionOne.hasExtension() && reactionTwo.hasExtension()
							&& (!(ractionOne.getExtension().equals(reactionTwo.getExtension())))) {
						String errorMessage = resourceName + " extension Expected = " + reactionTwo.getExtension()
								+ " but, submitted file contains extension of " + ractionOne.getExtension();
						operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE,
								resourceName + ".reaction.extension", errorMessage));
					} else if (ractionOne.hasExtension() && !reactionTwo.hasExtension()) {
						String errorMessage = "The scenario does not require " + resourceName
								+ ".reaction.extension, but submitted file does have extension";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								resourceName + ".reaction.extension", errorMessage));
					} else if (!ractionOne.hasExtension() && reactionTwo.hasExtension()) {
						String errorMessage = "The scenario requires " + resourceName
								+ ".reaction.extension, but submitted file does not have extension";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								resourceName + ".reaction.extension", errorMessage));
					} else {
						logger.info("reaction.extension information is null in both inputJson and goldJson");
					}

					// compare id
					if (ractionOne.hasId() && reactionTwo.hasId()
							&& (!(ractionOne.getId().equals(reactionTwo.getId())))) {
						String errorMessage = resourceName + " id Expected = " + reactionTwo.getId()
								+ " but, submitted file contains id of " + ractionOne.getId();
						operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE,
								resourceName + ".reaction.id", errorMessage));
					} else if (ractionOne.hasId() && !reactionTwo.hasId()) {
						String errorMessage = "The scenario does not require " + resourceName
								+ ".reaction.id, but submitted file does have id";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, resourceName + ".reaction.id", errorMessage));
					} else if (!ractionOne.hasId() && reactionTwo.hasId()) {
						String errorMessage = "The scenario requires " + resourceName
								+ ".reaction.id, but submitted file does not have id";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, resourceName + ".reaction.id", errorMessage));
					} else {
						logger.info("reaction.id information is null in both inputJson and goldJson");
					}
				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareAllergyIntoleranceReactionComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareListOfAllergyIntoleranceReactionComponent(String resourceName,
			List<AllergyIntoleranceReactionComponent> ReactionOneList,
			List<AllergyIntoleranceReactionComponent> ReactionTwoList, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (compareListForSizeEquality("reaction", ReactionOneList, ReactionTwoList, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < ReactionOneList.size();) {
					for (int j = 0; j < ReactionTwoList.size(); j++) {
						compareAllergyIntoleranceReactionComponent(resourceName, ReactionOneList.get(i),
								ReactionTwoList.get(j), operationOutcome, scenarioName);

						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(ReactionOneList) && ObjectUtils.isEmpty(ReactionTwoList)) {
				String errorMessage = "The scenario does not require reaction, but submitted file does have reaction";
				operationOutcome
						.addIssue(createScenarioResults(IssueType.CONFLICT, resourceName + ".reaction", errorMessage));
			} else if (ObjectUtils.isEmpty(ReactionOneList) && ObjectUtils.isNotEmpty(ReactionTwoList)) {
				String errorMessage = "The scenario requires reaction, but submitted file does not have reaction";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".reaction", errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfAllergyIntoleranceReactionComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	/**
	 * Compare two Resource
	 * 
	 * @param ResourceOne
	 * @param ResourceTwo
	 * @return
	 */
	public static void compareResource(String resourceName, Resource ResourceOne, Resource ResourceTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(ResourceOne) && ObjectUtils.isNotEmpty(ResourceTwo)) {

				// compare implicitRules
				if (ResourceOne.hasImplicitRules() && ResourceTwo.hasImplicitRules()
						&& (!(ResourceOne.getImplicitRules().equalsIgnoreCase(ResourceTwo.getImplicitRules())))) {
					String errorMessage = resourceName + " implicitRules Expected = " + ResourceTwo.getImplicitRules()
							+ " but, submitted file contains implicitRules of " + ResourceOne.getImplicitRules();
					operationOutcome.addIssue(
							createScenarioResults(IssueType.INCOMPLETE, resourceName + ".implicitRules", errorMessage));
				} else if (ResourceOne.hasImplicitRules() && !ResourceTwo.hasImplicitRules()) {
					String errorMessage = "The scenario does not require " + resourceName
							+ ".implicitRules, but submitted file does have implicitRules";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, resourceName + ".implicitRules", errorMessage));
				} else if (!ResourceOne.hasImplicitRules() && ResourceTwo.hasImplicitRules()) {
					String errorMessage = "The scenario requires " + resourceName
							+ ".implicitRules, but submitted file does not have implicitRules";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, resourceName + ".implicitRules", errorMessage));
				} else {
					logger.info(".implicitRules information is null in both inputJson and goldJson");
				}

				// compare language
				if (ResourceOne.hasLanguage() && ResourceTwo.hasLanguage()
						&& (!(ResourceOne.getLanguage().equalsIgnoreCase(ResourceTwo.getLanguage())))) {
					String errorMessage = resourceName + " language Expected = " + ResourceTwo.getLanguage()
							+ " but, submitted file contains language of " + ResourceOne.getLanguage();
					operationOutcome.addIssue(
							createScenarioResults(IssueType.INCOMPLETE, resourceName + ".language", errorMessage));
				} else if (ResourceOne.hasLanguage() && !ResourceTwo.hasLanguage()) {
					String errorMessage = "The scenario does not require " + resourceName
							+ ".language, but submitted file does have language";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, resourceName + ".language", errorMessage));
				} else if (!ResourceOne.hasLanguage() && ResourceTwo.hasLanguage()) {
					String errorMessage = "The scenario requires " + resourceName
							+ ".language, but submitted file does not have language";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, resourceName + ".language", errorMessage));
				} else {
					logger.info(".language information is null in both inputJson and goldJson");
				}

				// compare id
				if (ResourceOne.hasId() && ResourceTwo.hasId()
						&& (!(ResourceOne.getId().equalsIgnoreCase(ResourceTwo.getId())))) {
					String errorMessage = resourceName + " id Expected = " + ResourceTwo.getId()
							+ " but, submitted file contains id of " + ResourceOne.getId();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, resourceName + ".id", errorMessage));
				} else if (ResourceOne.hasId() && !ResourceTwo.hasId()) {
					String errorMessage = "The scenario does not require " + resourceName
							+ ".id, but submitted file does have id";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, resourceName + ".id", errorMessage));
				} else if (!ResourceOne.hasId() && ResourceTwo.hasId()) {
					String errorMessage = "The scenario requires " + resourceName
							+ ".id, but submitted file does not have id";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, resourceName + ".id", errorMessage));
				} else {
					logger.info(".id information is null in both inputJson and goldJson");
				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareResource of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareListOfResource(String resourceName, List<Resource> ResourceOneList,
			List<Resource> ResourceTwoList, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(resourceName, ResourceOneList, ResourceTwoList, operationOutcome,
					scenarioName)) {
				for (Resource resourceOne : ResourceOneList) {
					for (Resource resourceTwo : ResourceTwoList) {
						compareResource(resourceName, resourceOne, resourceTwo, operationOutcome, scenarioName);

					}
				}
			} else if (ObjectUtils.isNotEmpty(ResourceOneList) && ObjectUtils.isEmpty(ResourceTwoList)) {
				String errorMessage = "The scenario does not require contained, but submitted file does have contained";
				operationOutcome
						.addIssue(createScenarioResults(IssueType.CONFLICT, resourceName + ".contained", errorMessage));
			} else if (ObjectUtils.isEmpty(ResourceOneList) && ObjectUtils.isNotEmpty(ResourceTwoList)) {
				String errorMessage = "The scenario requires contained, but submitted file does not have contained";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, ".contained", errorMessage));
			}
		} catch (Exception e) {
			logger.error("\n Exception in compareListOfResource of ComparatorUtils :: ", e.getMessage());
		}
	}

	public static void compareListOfStatusHistory(String fieldName, List<StatusHistoryComponent> statusHistoryListOne,
			List<StatusHistoryComponent> statusHistoryListTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, statusHistoryListOne, statusHistoryListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < statusHistoryListOne.size();) {
					for (int j = 0; j < statusHistoryListTwo.size(); j++) {
						compareStatusHistory(fieldName, statusHistoryListOne.get(i), statusHistoryListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(statusHistoryListOne) && ObjectUtils.isEmpty(statusHistoryListTwo)) {
				String errorMessage = "The scenario does not require statusHistory, but submitted file does have statusHistory";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(statusHistoryListOne) && ObjectUtils.isNotEmpty(statusHistoryListTwo)) {
				String errorMessage = "The scenario requires statusHistory, but submitted file does not have statusHistory";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfStatusHistory of ComparatorUtils :: ", ex.getMessage());
		}

	}

	/**
	 * Compare two Period
	 * 
	 * @param periodOne
	 * @param periodTwo
	 * @return
	 */
	public static void comparePeriod(String fieldName, Period periodOne, Period periodTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(periodOne) && ObjectUtils.isNotEmpty(periodTwo)) {
				// compare start
				if (periodOne.hasStart() && periodTwo.hasStart()) {
					compareDate(fieldName + ".start", periodOne.getStart(), periodTwo.getStart(), operationOutcome,
							scenarioName);
				} else if (periodOne.hasStart() && !periodTwo.hasStart()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".start, but submitted file does have " + fieldName + ".start";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".start", errorMessage));
				} else if (!periodOne.hasStart() && periodTwo.hasStart()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".start, but submitted file does not have " + fieldName + ".start";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".start", errorMessage));
				} else {
					logger.info(fieldName + ".start information is null in both inputJson and goldJson");
				}
				// compare end
				if (periodOne.hasEnd() && periodTwo.hasEnd()) {
					compareDate(fieldName + ".end", periodOne.getEnd(), periodTwo.getEnd(), operationOutcome,
							scenarioName);
				} else if (periodOne.hasEnd() && !periodTwo.hasEnd()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".end, but submitted file does have .end";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".end", errorMessage));
				} else if (!periodOne.hasEnd() && periodTwo.hasEnd()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".end, but submitted file does not have " + fieldName + ".end";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".end", errorMessage));
				} else {
					logger.info(fieldName + ".end date information is null OR equal in both inputJson and goldJson");
				}
			}
		} catch (Exception ex) {

			logger.error("\n Exception while comparing two Period in ComparatorUtils class", ex);
		}

	}

	public static void compareStatusHistory(String fieldName, StatusHistoryComponent statusHistoryOne,
			StatusHistoryComponent statusHistoryTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(statusHistoryOne) && ObjectUtils.isNotEmpty(statusHistoryTwo)) {
				// compare status
				if (statusHistoryOne.hasStatus() && statusHistoryTwo.hasStatus()
						&& !(statusHistoryOne.getStatus().equals(statusHistoryTwo.getStatus()))) {
					String errorMessage = fieldName + ".status Expected = " + statusHistoryTwo.getStatus()
							+ " but, submitted file contains " + fieldName + ".status of "
							+ statusHistoryOne.getStatus();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".status", errorMessage));
				} else if (statusHistoryOne.hasStatus() && !statusHistoryTwo.hasStatus()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".status, but submitted file does have " + fieldName + ".status";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".status", errorMessage));
				} else if (!statusHistoryOne.hasStatus() && statusHistoryTwo.hasStatus()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".status, but submitted file does not have " + fieldName + ".status";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".status", errorMessage));
				} else {
					logger.info("statusHistory.status information is null in both inputJson and goldJson");
				}

				// compare period
				if (statusHistoryOne.hasPeriod() && statusHistoryTwo.hasPeriod()) {
					comparePeriod(fieldName, statusHistoryOne.getPeriod(), statusHistoryTwo.getPeriod(),
							operationOutcome, scenarioName);
				} else if (statusHistoryOne.hasPeriod() && !statusHistoryTwo.hasPeriod()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".period, but submitted file does have " + fieldName + ".period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
				} else if (!statusHistoryOne.hasPeriod() && statusHistoryTwo.hasPeriod()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".period, but submitted file does not have " + fieldName + ".period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
				} else {
					logger.info("statusHistory.period information is null OR equal in both inputJson and goldJson");
				}
			} else if (statusHistoryOne != null && statusHistoryTwo == null) {

				String errorMessage = "The scenario does not require statusHistory, but submitted file does have statusHistory";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (statusHistoryOne == null && ObjectUtils.isNotEmpty(statusHistoryTwo)) {

				String errorMessage = "The scenario requires statusHistory, but submitted file does not have statusHistory";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception while comparing StatusHistory in ComparatorUtils class", ex);
		}
	}

	public static void compareListOfEncounterLocationComponent(String fieldName,
			List<EncounterLocationComponent> locationListOne, List<EncounterLocationComponent> locationListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, locationListOne, locationListTwo, operationOutcome,
					scenarioName)) {

				for (int i = 0; i < locationListOne.size();) {
					for (int j = 0; j < locationListTwo.size(); j++) {
						compareEncounterLocationComponent(fieldName, locationListOne.get(i), locationListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(locationListOne) && ObjectUtils.isEmpty(locationListTwo)) {
				String errorMessage = "The scenario does not require location, but submitted file does have location";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(locationListOne) && ObjectUtils.isNotEmpty(locationListTwo)) {
				String errorMessage = "The scenario requires location, but submitted file does not have location";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfEncounterLocationComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareEncounterLocationComponent(String fieldName, EncounterLocationComponent locationOne,
			EncounterLocationComponent locationTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(locationOne) && ObjectUtils.isNotEmpty(locationTwo)) {

				// compare location
				if (locationOne.hasLocation() && locationTwo.hasLocation()) {
					compareReference(fieldName + ".location", locationOne.getLocation(), locationTwo.getLocation(),
							operationOutcome, scenarioName);
				} else if (locationOne.hasLocation() && !locationTwo.hasLocation()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".location, but submitted file does have .location";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".location", errorMessage));
				} else if (!locationOne.hasLocation() && locationTwo.hasLocation()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".location, but submitted file does not have .location";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".location", errorMessage));
				} else {
					logger.info("location.location information is null in both inputJson and goldJson");
				}
				// compare status
				if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_ENCOUNTER)) {
					if (locationOne.hasStatus() && locationTwo.hasStatus()) {
						compareString(fieldName + ".status", locationOne.getStatus().getDisplay(),
								locationTwo.getStatus().getDisplay(), operationOutcome, scenarioName);

					} else if (locationOne.hasStatus() && !locationTwo.hasStatus()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".status, but submitted file does have .status";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".status", errorMessage));
					} else if (!locationOne.hasStatus() && locationTwo.hasStatus()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".location.status, but submitted file does not have " + fieldName
								+ ".location.status";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".status", errorMessage));
					} else {
						logger.info("location.status information is null in both inputJson and goldJson");
					}
					// compare physicalType
					if (locationOne.hasPhysicalType() && locationTwo.hasPhysicalType()) {
						compareCodeableConcept(fieldName, locationOne.getPhysicalType(), locationTwo.getPhysicalType(),
								operationOutcome, scenarioName);
					} else if (locationOne.hasPhysicalType() && !locationTwo.hasPhysicalType()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".physicalType, but submitted file does have .physicalType";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".physicalType", errorMessage));
					} else if (!locationOne.hasPhysicalType() && locationTwo.hasPhysicalType()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".physicalType, but submitted file does not have .physicalType";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".physicalType", errorMessage));
					} else {
						logger.info("location.physicalType information is null in both inputJson and goldJson");
					}

					// compare period
					if (locationOne.hasPeriod() && locationTwo.hasPeriod()) {
						comparePeriod(fieldName + ".period", locationOne.getPeriod(), locationTwo.getPeriod(),
								operationOutcome, scenarioName);
					} else if (locationOne.hasPeriod() && !locationTwo.hasPeriod()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".period, but submitted file does have .period";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
					} else if (!locationOne.hasPeriod() && locationTwo.hasPeriod()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".location.period, but submitted file does not have .period";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
					} else {
						logger.info("location.period information is null OR equal in both inputJson and goldJson");
					}
				}
			} else if (locationOne != null && locationTwo == null) {

				String errorMessage = "The scenario does not require location, but submitted file does have location";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (locationOne == null && ObjectUtils.isNotEmpty(locationTwo)) {

				String errorMessage = "The scenario requires location, but location file does not have location";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareEncounterLocationComponent of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareListOfEncounterParticipantComponent(String fieldName,
			List<EncounterParticipantComponent> participantListOne,
			List<EncounterParticipantComponent> participantListTwo, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, participantListOne, participantListTwo, operationOutcome,
					scenarioName)) {

				for (int i = 0; i < participantListOne.size();) {
					for (int j = 0; j < participantListTwo.size(); j++) {
						compareEncounterParticipantComponent(fieldName, participantListOne.get(i),
								participantListTwo.get(j), operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(participantListOne) && ObjectUtils.isEmpty(participantListTwo)) {
				String errorMessage = "The scenario does not require participant, but submitted file does have participant";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(participantListOne) && ObjectUtils.isNotEmpty(participantListTwo)) {
				String errorMessage = "The scenario requires participant, but submitted file does not have participant";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfEncounterParticipantComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareEncounterParticipantComponent(String fieldName,
			EncounterParticipantComponent participantOne, EncounterParticipantComponent participantTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(participantOne) && ObjectUtils.isNotEmpty(participantTwo)) {
				// compare type
				if (participantOne.hasType() && participantTwo.hasType()) {
					compareListOfCodeableConcept(fieldName + ".type", participantOne.getType(),
							participantTwo.getType(), operationOutcome, scenarioName);
				} else if (participantOne.hasType() && !participantTwo.hasType()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".type, but submitted file does have .type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else if (!participantOne.hasType() && participantTwo.hasType()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".type, but submitted file does not have type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else {
					logger.info("participant.type information is null OR equal in both inputJson and goldJson");
				}
				// compare period
				if (participantOne.hasPeriod() && participantTwo.hasPeriod()) {
					comparePeriod(fieldName + ".period", participantOne.getPeriod(), participantTwo.getPeriod(),
							operationOutcome, scenarioName);
				} else if (participantOne.hasPeriod() && !participantTwo.hasPeriod()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".period, but submitted file does have " + fieldName + ".period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
				} else if (!participantOne.hasPeriod() && participantTwo.hasPeriod()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".period, but submitted file does not have " + fieldName + "period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
				} else {
					logger.info("participant.period information is null in both inputJson and goldJson");
				}
				// compare individual
				if (participantOne.hasIndividual() && participantTwo.hasIndividual()) {
					compareReference(fieldName + ".individual", participantOne.getIndividual(),
							participantTwo.getIndividual(), operationOutcome, scenarioName);
				} else if (participantOne.hasIndividual() && !participantTwo.hasIndividual()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".individual, but submitted file does have " + fieldName + ".individual";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".individual", errorMessage));
				} else if (!participantOne.hasIndividual() && participantTwo.hasIndividual()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".individual, but submitted file does not have " + fieldName + ".individual";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".individual", errorMessage));
				} else {
					logger.info("participant.individual information is null OR equal in both inputJson and goldJson");
				}

				// extension
				if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_ENCOUNTER)) {

					if (participantOne.hasExtension() && participantTwo.hasExtension()) {
						compareListOfExtension(fieldName + ".extension", participantOne.getExtension(),
								participantTwo.getExtension(), operationOutcome, scenarioName);
					} else if (participantOne.hasExtension() && !participantTwo.hasExtension()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".extension, but submitted file does have " + fieldName + ".extension";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
					} else if (!participantOne.hasExtension() && participantTwo.hasExtension()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".extension, but submitted file does not have " + fieldName + ".extension";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
					} else {
						logger.info(
								"participant.extension information is null OR equal in both inputJson and goldJson");
					}
				}
			} else if (participantOne != null && participantTwo == null) {

				String errorMessage = "The scenario does not require participant, but submitted file does have participant";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (participantOne == null && ObjectUtils.isNotEmpty(participantTwo)) {

				String errorMessage = "The scenario requires participant, but location file does not have participant";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareEncounterParticipantComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	public static void compareListOfDiagnosisComponent(String fieldName, List<DiagnosisComponent> diagnosisListOne,
			List<DiagnosisComponent> diagnosisListTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, diagnosisListOne, diagnosisListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < diagnosisListOne.size();) {
					for (int j = 0; j < diagnosisListTwo.size(); j++) {
						compareDiagnosisComponent(fieldName, diagnosisListOne.get(i), diagnosisListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(diagnosisListOne) && ObjectUtils.isEmpty(diagnosisListTwo)) {
				String errorMessage = "The scenario does not require diagnosis, but submitted file does have diagnosis";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(diagnosisListOne) && ObjectUtils.isNotEmpty(diagnosisListTwo)) {
				String errorMessage = "The scenario requires diagnosis, but submitted file does not have diagnosis";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfDiagnosisComponent of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareDiagnosisComponent(String fieldName, DiagnosisComponent diagnosisOne,
			DiagnosisComponent diagnosisTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(diagnosisOne) && ObjectUtils.isNotEmpty(diagnosisTwo)) {

				// compare condition
				if (diagnosisOne.hasCondition() && diagnosisTwo.hasCondition()) {
					compareReference(fieldName, diagnosisOne.getCondition(), diagnosisTwo.getCondition(),
							operationOutcome, scenarioName);
				} else if (diagnosisOne.hasCondition() && !diagnosisTwo.hasCondition()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".condition, but submitted file does have .diagnosis.condition";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".condition", errorMessage));
				} else if (!diagnosisOne.hasCondition() && diagnosisTwo.hasCondition()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".condition, but submitted file does not have .diagnosis.condition";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".condition", errorMessage));
				} else {
					logger.info("diagnosis.condition information is null OR equal in both inputJson and goldJson");
				}

				// compare use
				if (diagnosisOne.hasUse() && diagnosisTwo.hasUse()) {
					compareCodeableConcept(fieldName, diagnosisOne.getUse(), diagnosisTwo.getUse(), operationOutcome,
							scenarioName);
				} else if (diagnosisOne.hasUse() && !diagnosisTwo.hasUse()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".use, but submitted file does have .diagnosis.use";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".diagnosis.use", errorMessage));
				} else if (!diagnosisOne.hasUse() && diagnosisTwo.hasUse()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".use, but submitted file does not have .diagnosis.use";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".use", errorMessage));
				} else {
					logger.info("diagnosis.use information is null OR equal in both inputJson and goldJson");
				}
			} else if (diagnosisOne != null && diagnosisTwo == null) {

				String errorMessage = "The scenario does not require diagnosis, but submitted file does have diagnosis";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (diagnosisOne == null && ObjectUtils.isNotEmpty(diagnosisTwo)) {

				String errorMessage = "The scenario requires diagnosis, but location file does not have diagnosis";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDiagnosisComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareEncounterHospitalizationComponent(String fieldName,
			EncounterHospitalizationComponent hospitalizationOne, EncounterHospitalizationComponent hospitalizationTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(hospitalizationOne) && ObjectUtils.isNotEmpty(hospitalizationTwo)) {

				// compare dischargeDisposition
				if (hospitalizationOne.hasDischargeDisposition() && hospitalizationTwo.hasDischargeDisposition()) {
					compareCodeableConcept(fieldName + ".dischargeDisposition",
							hospitalizationOne.getDischargeDisposition(), hospitalizationTwo.getDischargeDisposition(),
							operationOutcome, scenarioName);
				} else if (hospitalizationOne.hasDischargeDisposition()
						&& !hospitalizationTwo.hasDischargeDisposition()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".dischargeDisposition, but submitted file does have dischargeDisposition";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							fieldName + ".dischargeDisposition", errorMessage));
				} else if (!hospitalizationOne.hasDischargeDisposition()
						&& hospitalizationTwo.hasDischargeDisposition()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".dischargeDisposition, but submitted file does not have dischargeDisposition";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".specialCourtesy", errorMessage));
				} else {
					logger.info(
							"hospitalization.dischargeDisposition information is null OR equal in both inputJson and goldJson");
				}

				// compare origin
				if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_ENCOUNTER)) {

					if (hospitalizationOne.hasOrigin() && hospitalizationTwo.hasOrigin()) {
						compareReference(fieldName + ".origin", hospitalizationOne.getOrigin(),
								hospitalizationTwo.getOrigin(), operationOutcome, scenarioName);
					} else if (hospitalizationOne.hasOrigin() && !hospitalizationTwo.hasOrigin()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".origin, but submitted file does have .origin";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + "origin", errorMessage));
					} else if (!hospitalizationOne.hasOrigin() && hospitalizationTwo.hasOrigin()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".origin, but submitted file does not have origin";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".origin", errorMessage));
					} else {
						logger.info(
								"hospitalization.origin information is null OR equal in both inputJson and goldJson");
					}

					// compare preAdmissionIdentifier
					if (hospitalizationOne.hasPreAdmissionIdentifier()
							&& hospitalizationTwo.hasPreAdmissionIdentifier()) {
						compareIdentifier(fieldName + ".preAdmissionIdentifier",
								hospitalizationOne.getPreAdmissionIdentifier(),
								hospitalizationTwo.getPreAdmissionIdentifier(), operationOutcome, scenarioName);
					} else if (hospitalizationOne.hasPreAdmissionIdentifier()
							&& !hospitalizationTwo.hasPreAdmissionIdentifier()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".preAdmissionIdentifier, but submitted file does have .preAdmissionIdentifier";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".preAdmissionIdentifier", errorMessage));
					} else if (!hospitalizationOne.hasPreAdmissionIdentifier()
							&& hospitalizationTwo.hasPreAdmissionIdentifier()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".preAdmissionIdentifier, but submitted file does not have .preAdmissionIdentifier";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".preAdmissionIdentifier", errorMessage));
					} else {
						logger.info(
								"hospitalization.preAdmissionIdentifier information is null OR equal in both inputJson and goldJson");
					}

					// compare admitSource
					if (hospitalizationOne.hasAdmitSource() && hospitalizationTwo.hasAdmitSource()) {
						compareCodeableConcept(fieldName + ".admitSource", hospitalizationOne.getAdmitSource(),
								hospitalizationTwo.getAdmitSource(), operationOutcome, scenarioName);
					} else if (hospitalizationOne.hasAdmitSource() && !hospitalizationTwo.hasAdmitSource()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".admitSource, but submitted file does have admitSource";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".admitSource", errorMessage));
					} else if (!hospitalizationOne.hasAdmitSource() && hospitalizationTwo.hasAdmitSource()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".admitSource, but submitted file does not have admitSource";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".admitSource", errorMessage));
					} else {
						logger.info(
								"hospitalization.admitSource information is null OR equal in both inputJson and goldJson");
					}

					// compare reAdmission
					if (hospitalizationOne.hasReAdmission() && hospitalizationTwo.hasReAdmission()) {
						compareCodeableConcept(fieldName + ".reAdmission", hospitalizationOne.getReAdmission(),
								hospitalizationTwo.getReAdmission(), operationOutcome, scenarioName);
					} else if (hospitalizationOne.hasReAdmission() && !hospitalizationTwo.hasReAdmission()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".reAdmission, but submitted file does have reAdmission";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".reAdmission", errorMessage));
					} else if (!hospitalizationOne.hasReAdmission() && hospitalizationTwo.hasReAdmission()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".reAdmission, but submitted file does not have reAdmission";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".reAdmission", errorMessage));
					} else {
						logger.info(
								"hospitalization.reAdmission information is null OR equal in both inputJson and goldJson");
					}
					// compare dietPreference
					if (hospitalizationOne.hasDietPreference() && hospitalizationTwo.hasDietPreference()) {
						compareListOfCodeableConcept(fieldName + ".dietPreference",
								hospitalizationOne.getDietPreference(), hospitalizationTwo.getDietPreference(),
								operationOutcome, scenarioName);
					} else if (hospitalizationOne.hasDietPreference() && !hospitalizationTwo.hasDietPreference()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".dietPreference, but submitted file does have dietPreference";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".dietPreference", errorMessage));
					} else if (!hospitalizationOne.hasDietPreference() && hospitalizationTwo.hasDietPreference()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".dietPreference, but submitted file does not have dietPreference";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".dietPreference", errorMessage));
					} else {
						logger.info(
								"hospitalization.dietPreference information is null OR equal in both inputJson and goldJson");
					}
					// compare specialCourtesy
					if (hospitalizationOne.hasSpecialCourtesy() && hospitalizationTwo.hasSpecialCourtesy()) {
						compareListOfCodeableConcept(fieldName + ".specialCourtesy",
								hospitalizationOne.getSpecialCourtesy(), hospitalizationTwo.getSpecialCourtesy(),
								operationOutcome, scenarioName);
					} else if (hospitalizationOne.hasSpecialCourtesy() && !hospitalizationTwo.hasSpecialCourtesy()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".specialCourtesy, but submitted file does have specialCourtesy";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".specialCourtesy", errorMessage));
					} else if (!hospitalizationOne.hasSpecialCourtesy() && hospitalizationTwo.hasSpecialCourtesy()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".specialCourtesy, but submitted file does not have specialCourtesy";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".specialCourtesy", errorMessage));
					} else {
						logger.info(
								"hospitalization.specialCourtesy information is null OR equal in both inputJson and goldJson");
					}
					// compare specialArrangement
					if (hospitalizationOne.hasSpecialArrangement() && hospitalizationTwo.hasSpecialArrangement()) {
						compareListOfCodeableConcept(fieldName + ".specialArrangement",
								hospitalizationOne.getSpecialArrangement(), hospitalizationTwo.getSpecialArrangement(),
								operationOutcome, scenarioName);
					} else if (hospitalizationOne.hasSpecialArrangement()
							&& !hospitalizationTwo.hasSpecialArrangement()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".specialArrangement, but submitted file does have specialArrangement";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".specialArrangement", errorMessage));
					} else if (!hospitalizationOne.hasSpecialArrangement()
							&& hospitalizationTwo.hasSpecialArrangement()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".specialArrangement, but submitted file does not have specialArrangement";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".specialCourtesy", errorMessage));
					} else {
						logger.info(
								"hospitalization.specialArrangement information is null OR equal in both inputJson and goldJson");
					}

					// compare destination
					if (hospitalizationOne.hasDestination() && hospitalizationTwo.hasDestination()) {
						compareReference(fieldName + ".destination", hospitalizationOne.getDestination(),
								hospitalizationTwo.getDestination(), operationOutcome, scenarioName);
					} else if (hospitalizationOne.hasDestination() && !hospitalizationTwo.hasDestination()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".destination, but submitted file does have " + fieldName + ".destination";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".destination", errorMessage));
					} else if (!hospitalizationOne.hasDestination() && hospitalizationTwo.hasDestination()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".destination, but submitted file does not have " + fieldName + ".destination";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".destination", errorMessage));
					} else {
						logger.info(
								"hospitalization.destination information is null OR equal in both inputJson and goldJson");
					}

				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareEncounterHospitalizationComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	public static void compareListOfClassHistoryComponent(String fieldName,
			List<ClassHistoryComponent> classHistoryListOne, List<ClassHistoryComponent> classHistoryListTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, classHistoryListOne, classHistoryListTwo, operationOutcome,
					scenarioName)) {

				for (int i = 0; i < classHistoryListOne.size();) {
					for (int j = 0; j < classHistoryListTwo.size(); j++) {
						compareClassHistoryComponent(fieldName, classHistoryListOne.get(i), classHistoryListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(classHistoryListOne) && ObjectUtils.isEmpty(classHistoryListTwo)) {
				String errorMessage = "The scenario does not require classHistory, but submitted file does have classHistory";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(classHistoryListOne) && ObjectUtils.isNotEmpty(classHistoryListTwo)) {
				String errorMessage = "The scenario requires classHistory, but submitted file does not have classHistory";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfClassHistoryComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareClassHistoryComponent(String fieldName, ClassHistoryComponent classHistoryOne,
			ClassHistoryComponent classHistoryTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(classHistoryOne) && ObjectUtils.isNotEmpty(classHistoryTwo)) {

				// compare class
				if (classHistoryOne.hasClass_() && classHistoryTwo.hasClass_()) {
					compareCoding(fieldName, classHistoryOne.getClass_(), classHistoryTwo.getClass_(), operationOutcome,
							scenarioName);
				} else if (classHistoryOne.hasClass_() && !classHistoryTwo.hasClass_()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".class, but submitted file does have class";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".class", errorMessage));
				} else if (!classHistoryOne.hasClass_() && classHistoryTwo.hasClass_()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".class, but submitted file does not have class";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".class", errorMessage));
				} else {
					logger.info("classHistory.class information is null OR equal in both inputJson and goldJson");
				}

				// compare period
				if (classHistoryOne.hasPeriod() && classHistoryTwo.hasPeriod()) {
					comparePeriod(fieldName, classHistoryOne.getPeriod(), classHistoryTwo.getPeriod(), operationOutcome,
							scenarioName);
				} else if (classHistoryOne.hasPeriod() && !classHistoryTwo.hasPeriod()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".period, but submitted file does have period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
				} else if (!classHistoryOne.hasPeriod() && classHistoryTwo.hasPeriod()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".period, but submitted file does not have period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
				} else {
					logger.info("classHistory.period information is null OR equal in both inputJson and goldJson");
				}
			} else if (classHistoryOne != null && classHistoryTwo == null) {

				String errorMessage = "The scenario does not require classHistory, but submitted file does have classHistory";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (classHistoryOne == null && ObjectUtils.isNotEmpty(classHistoryTwo)) {

				String errorMessage = "The scenario requires classHistory, but location file does not have classHistory";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareClassHistoryComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareListOfProcedurePerformerComponent(String performer,
			List<ProcedurePerformerComponent> performerListone, List<ProcedurePerformerComponent> performerListtwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(performer, performerListone, performerListtwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < performerListone.size();) {
					for (int j = 0; j < performerListtwo.size(); j++) {
						compareProcedurePerformerComponent(performer, performerListone.get(i), performerListtwo.get(j),
								operationOutcome, scenarioName);

						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(performerListone) && ObjectUtils.isEmpty(performerListtwo)) {
				String errorMessage = "The scenario does not require Performer, but submitted file does have Performer";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, performer, errorMessage));
			} else if (ObjectUtils.isEmpty(performerListone) && ObjectUtils.isNotEmpty(performerListtwo)) {
				String errorMessage = "The scenario requires Performer, but submitted file does not have Performer";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, performer, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfProcedurePerformerComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static String convertImmunizationStatus(String sourceStatus, OperationOutcome operationOutcome) {
		logger.info("Entry in convertImmunizationStatus " + sourceStatus);
		ImmunizationStatus status = ImmunizationStatus.NULL;
		try {
			if (StringUtils.isNotEmpty(sourceStatus)) {
				if (sourceStatus.equalsIgnoreCase(ResourceNames.COMPLETED)) {
					status = ImmunizationStatus.COMPLETED;
				} else if (sourceStatus.equalsIgnoreCase(ResourceNames.NOTDONE)) {
					status = ImmunizationStatus.NOTDONE;
				} else if (sourceStatus.equalsIgnoreCase(ResourceNames.ENTEREDINERROR)) {
					status = ImmunizationStatus.ENTEREDINERROR;
				}
				return status.name();
			} else {

				String errorMessage = "The scenario will accept these code completed | entered-in-error | not-done only please correct the status value";

				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, sourceStatus, errorMessage));
			}
			logger.info("Exit in convertImmunizationStatus " + status);
		} catch (Exception ex) {
			logger.error("\n Exception in convertImmunizationStatus of ComparatorUtils :: ", ex.getMessage());
		}
		return status.name();
	}

	public static void compareBoolean(String fieldName, Boolean booleanOne, Boolean booleanTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {

			if (ObjectUtils.isNotEmpty(booleanOne) && ObjectUtils.isNotEmpty(booleanTwo)
					&& (!(booleanOne.equals(booleanTwo)))) {
				String errorMessage = fieldName + " Expected = " + booleanTwo + " but, submitted file contains "
						+ fieldName + " of " + booleanOne;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (ObjectUtils.isNotEmpty(booleanOne) && ObjectUtils.isEmpty(booleanTwo)) {
				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(booleanOne) && ObjectUtils.isNotEmpty(booleanTwo)) {
				String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (booleanOne != null && booleanTwo == null) {

				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (booleanOne == null && ObjectUtils.isNotEmpty(booleanTwo)) {

				String errorMessage = "The scenario requires " + fieldName + ", but location file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else {
				logger.info(fieldName + " information is null OR equal in both inputJson and goldJson");
			}
		} catch (Exception ex) {
			logger.error("\n Exception in convertImmunizationStatus of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareQuantity(String fieldName, Quantity quantityOne, Quantity quantityTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(quantityOne) && ObjectUtils.isNotEmpty(quantityTwo)) {
				// value
				if (quantityOne.hasValue() && quantityTwo.hasValue()) {
					compareString(fieldName + ".value", quantityOne.getValue().toString(),
							quantityTwo.getValue().toString(), operationOutcome, scenarioName);
				} else if (quantityOne.hasValue() && !quantityTwo.hasValue()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".value, but submitted file does have " + fieldName + ".value";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!quantityOne.hasValue() && quantityTwo.hasValue()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".value, but submitted file does not have " + fieldName + ".value";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".value information is null OR equal in both inputJson and goldJson");
				}

				// unit
				if (quantityOne.hasUnit() && quantityTwo.hasUnit()) {
					compareString(fieldName + ".unit", quantityOne.getUnit().toString(),
							quantityTwo.getUnit().toString(), operationOutcome, scenarioName);
				} else if (quantityOne.hasUnit() && !quantityTwo.hasUnit()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".unit, but submitted file does have " + fieldName + ".unit";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!quantityOne.hasUnit() && quantityTwo.hasUnit()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".unit, but submitted file does not have " + fieldName + ".unit";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".value information is null OR equal in both inputJson and goldJson");
				}

				// system
				if (quantityOne.hasSystem() && quantityTwo.hasSystem()) {
					compareString(fieldName + "system", quantityOne.getSystem().toString(),
							quantityTwo.getSystem().toString(), operationOutcome, scenarioName);

				} else if (quantityOne.hasSystem() && !quantityTwo.hasSystem()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".system, but submitted file does have " + fieldName + ".system";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!quantityOne.hasSystem() && quantityTwo.hasSystem()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".system, but submitted file does not have " + fieldName + ".system";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".system information is null OR equal in both inputJson and goldJson");
				}

				// currency
				if (quantityOne.hasCode() && quantityTwo.hasCode()) {
					compareString(fieldName + "code/currency", quantityOne.getCode(), quantityTwo.getCode(),
							operationOutcome, scenarioName);

				} else if (quantityOne.hasCode() && !quantityTwo.hasCode()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".code/currency, but submitted file does have " + fieldName + ".code/currency";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!quantityOne.hasCode() && quantityTwo.hasCode()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".code/currency, but submitted file does not have " + fieldName + ".code/currency";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(
							fieldName + ".code/currency information is null OR equal in both inputJson and goldJson");
				}
			} else if (quantityOne != null && quantityTwo == null) {

				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have"
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (quantityOne == null && ObjectUtils.isNotEmpty(quantityTwo)) {

				String errorMessage = "The scenario requires " + fieldName + ", but location file does not have"
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareQuantity of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareDateTimeType(String fieldName, DateTimeType occurrenceDateTimeTypeOne,
			DateTimeType occurrenceDateTimeTypeTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(occurrenceDateTimeTypeOne)
					&& ObjectUtils.isNotEmpty(occurrenceDateTimeTypeTwo)) {
				// value
				if (occurrenceDateTimeTypeOne.hasValue() && occurrenceDateTimeTypeTwo.hasValue()
						&& (!(occurrenceDateTimeTypeOne.getValue().equals(occurrenceDateTimeTypeTwo.getValue())))) {
					String errorMessage = fieldName + ".value Expected = " + occurrenceDateTimeTypeTwo.getValue()
							+ " but, submitted file contains " + fieldName + ".value of "
							+ occurrenceDateTimeTypeOne.getValue();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".value", errorMessage));
				} else if (occurrenceDateTimeTypeOne.hasValue() && !occurrenceDateTimeTypeTwo.hasValue()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ", but submitted file does have" + fieldName + ".value";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!occurrenceDateTimeTypeOne.hasValue() && occurrenceDateTimeTypeTwo.hasValue()) {
					String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have "
							+ fieldName;
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + " information is null OR equal in both inputJson and goldJson");
				}

			} else if (occurrenceDateTimeTypeOne != null && occurrenceDateTimeTypeTwo == null) {

				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (occurrenceDateTimeTypeOne == null && ObjectUtils.isNotEmpty(occurrenceDateTimeTypeTwo)) {

				String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDateTimeType of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareListOfImmunizationPerformerComponent(String fieldName,
			List<ImmunizationPerformerComponent> performerListOne,
			List<ImmunizationPerformerComponent> performerListTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, performerListOne, performerListTwo, operationOutcome,
					scenarioName)) {

				for (int i = 0; i < performerListOne.size();) {
					for (int j = 0; j < performerListTwo.size(); j++) {
						compareImmunizationPerformerComponent(fieldName, performerListOne.get(i),
								performerListTwo.get(j), operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(performerListOne) && ObjectUtils.isEmpty(performerListTwo)) {
				String errorMessage = "The scenario does not require performer, but submitted file does have performer";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(performerListOne) && ObjectUtils.isNotEmpty(performerListTwo)) {
				String errorMessage = "The scenario requires performer, but submitted file does not have performer";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfImmunizationPerformerComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	public static void compareImmunizationPerformerComponent(String fieldName,
			ImmunizationPerformerComponent performerOne, ImmunizationPerformerComponent performerTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(performerOne) && ObjectUtils.isNotEmpty(performerTwo)) {
				// function
				if (performerOne.hasFunction() && performerTwo.hasFunction()) {
					compareCodeableConcept(fieldName + ".function", performerOne.getFunction(),
							performerTwo.getFunction(), operationOutcome, scenarioName);
				} else if (performerOne.hasFunction() && !performerTwo.hasFunction()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".function, but submitted file does have " + fieldName + ".function";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!performerOne.hasFunction() && performerTwo.hasFunction()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".function, but submitted file does not have " + fieldName + ".function";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".function information is null OR equal in both inputJson and goldJson");
				}

				// actor
				if (performerOne.hasActor() && performerTwo.hasActor()) {
					compareReference(fieldName + ".actor", performerOne.getActor(), performerTwo.getActor(),
							operationOutcome, scenarioName);
				} else if (performerOne.hasActor() && !performerTwo.hasActor()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".actor, but submitted file does have " + fieldName + ".actor";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!performerOne.hasActor() && performerTwo.hasActor()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".actor, but submitted file does not have " + fieldName + ".actor";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".actor information is null OR equal in both inputJson and goldJson");
				}
			} else if (performerOne != null && performerTwo == null) {

				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (performerOne == null && ObjectUtils.isNotEmpty(performerTwo)) {

				String errorMessage = "The scenario requires " + fieldName + ", but location file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareImmunizationPerformerComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	public static void compareListOfImmunizationEducationComponent(String fieldName,
			List<ImmunizationEducationComponent> educationListOne,
			List<ImmunizationEducationComponent> educationListTwo, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, educationListOne, educationListTwo, operationOutcome,
					scenarioName)) {

				for (int i = 0; i < educationListOne.size();) {
					for (int j = 0; j < educationListTwo.size(); j++) {
						compareImmunizationEducationComponent(fieldName, educationListOne.get(i),
								educationListTwo.get(j), operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(educationListOne) && ObjectUtils.isEmpty(educationListTwo)) {
				String errorMessage = "The scenario does not require education, but submitted file does have education";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(educationListOne) && ObjectUtils.isNotEmpty(educationListTwo)) {
				String errorMessage = "The scenario requires education, but submitted file does not have education";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfImmunizationEducationComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareImmunizationEducationComponent(String fieldName,
			ImmunizationEducationComponent educationOne, ImmunizationEducationComponent educationTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(educationOne) && ObjectUtils.isNotEmpty(educationTwo)) {
				// documentType
				if (educationOne.hasDocumentType() && educationTwo.hasDocumentType()) {
					compareString(fieldName + ".documentType", educationOne.getDocumentType(),
							educationTwo.getDocumentType(), operationOutcome, scenarioName);
				} else if (educationOne.hasDocumentType() && !educationTwo.hasDocumentType()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".documentType, but submitted file does have " + fieldName + ".documentType";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!educationOne.hasDocumentType() && educationTwo.hasDocumentType()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".documentType, but submitted file does not have " + fieldName + ".documentType";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".value information is null OR equal in both inputJson and goldJson");
				}

				// reference
				if (educationOne.hasReference() && educationTwo.hasReference()) {
					compareString(fieldName + ".reference", educationOne.getReference(), educationTwo.getReference(),
							operationOutcome, scenarioName);

				} else if (educationOne.hasReference() && !educationTwo.hasReference()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".reference, but submitted file does have " + fieldName + ".reference";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!educationOne.hasReference() && educationTwo.hasReference()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".reference, but submitted file does not have " + fieldName + ".reference";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".reference information is null OR equal in both inputJson and goldJson");
				}

				// publicationDate
				if (educationOne.hasPublicationDate() && educationTwo.hasPublicationDate()) {
					compareDate(fieldName + ".publicationDate", educationOne.getPublicationDate(),
							educationTwo.getPublicationDate(), operationOutcome, scenarioName);
				} else if (educationOne.hasPublicationDate() && !educationTwo.hasPublicationDate()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".publicationDate, but submitted file does have " + fieldName + ".publicationDate";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!educationOne.hasPublicationDate() && educationTwo.hasPublicationDate()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".publicationDate, but submitted file does not have " + fieldName + ".publicationDate";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(
							fieldName + ".publicationDate information is null OR equal in both inputJson and goldJson");
				}
				// presentationDate
				if (educationOne.hasPresentationDate() && educationTwo.hasPresentationDate()) {
					compareDate(fieldName + ".presentationDate", educationOne.getPresentationDate(),
							educationTwo.getPresentationDate(), operationOutcome, scenarioName);
				} else if (educationOne.hasPresentationDate() && !educationTwo.hasPresentationDate()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".presentationDate, but submitted file does have " + fieldName + ".presentationDate";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!educationOne.hasPresentationDate() && educationTwo.hasPresentationDate()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".presentationDate, but submitted file does not have " + fieldName + ".presentationDate";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName
							+ ".presentationDate information is null OR equal in both inputJson and goldJson");
				}

			} else if (educationOne != null && educationTwo == null) {

				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (educationOne == null && ObjectUtils.isNotEmpty(educationTwo)) {

				String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareImmunizationEducationComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	public static void compareListOfImmunizationReactionComponent(String fieldName,
			List<ImmunizationReactionComponent> reactionListOne, List<ImmunizationReactionComponent> reactionListTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, reactionListOne, reactionListTwo, operationOutcome,
					scenarioName)) {

				for (int i = 0; i < reactionListOne.size();) {
					for (int j = 0; j < reactionListTwo.size(); j++) {
						compareImmunizationReactionComponent(fieldName, reactionListOne.get(i), reactionListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (!reactionListOne.isEmpty() && reactionListTwo.isEmpty()) {
				String errorMessage = "The scenario does not require reaction, but submitted file does have reaction";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (reactionListOne.isEmpty() && !reactionListTwo.isEmpty()) {
				String errorMessage = "The scenario requires reaction, but submitted file does not have reaction";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfImmunizationEducationComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareImmunizationReactionComponent(String fieldName, ImmunizationReactionComponent reactionOne,
			ImmunizationReactionComponent reactionTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (!reactionOne.isEmpty() && !reactionTwo.isEmpty()) {
				// date
				if (reactionOne.hasDate() && reactionTwo.hasDate()) {
					compareDate(fieldName + ".date", reactionOne.getDate(), reactionTwo.getDate(), operationOutcome,
							scenarioName);
				} else if (reactionOne.hasDate() && !reactionTwo.hasDate()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".date, but submitted file does have " + fieldName + ".date";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!reactionOne.hasDate() && reactionTwo.hasDate()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".date, but submitted file does not have " + fieldName + ".date";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".date information is null OR equal in both inputJson and goldJson");
				}
				// detail
				if (reactionOne.hasDetail() && reactionTwo.hasDetail()) {
					compareReference(fieldName + ".detail", reactionOne.getDetail(), reactionTwo.getDetail(),
							operationOutcome, scenarioName);
				} else if (reactionOne.hasDetail() && !reactionTwo.hasDetail()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".detail, but submitted file does have " + fieldName + ".detail";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!reactionOne.hasDetail() && reactionTwo.hasDetail()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".detail, but submitted file does not have " + fieldName + ".detail";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".detail information is null OR equal in both inputJson and goldJson");
				}
				// reported
				if (reactionOne.hasReported() && reactionTwo.hasReported()) {
					compareBoolean(fieldName + ".reported", reactionOne.getReported(), reactionTwo.getReported(),
							operationOutcome, scenarioName);
				} else if (reactionOne.hasReported() && !reactionTwo.hasReported()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".reported, but submitted file does have " + fieldName + ".reported";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!reactionOne.hasReported() && reactionTwo.hasReported()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".reported, but submitted file does not have " + fieldName + ".reported";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".reported information is null OR equal in both inputJson and goldJson");
				}

			} else if (!reactionOne.isEmpty() && reactionTwo.isEmpty()) {

				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (reactionOne.isEmpty() && !reactionTwo.isEmpty()) {

				String errorMessage = "The scenario requires " + fieldName + ", but location file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareImmunizationReactionComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	public static void compareListOfImmunizationProtocolAppliedComponent(String fieldName,
			List<ImmunizationProtocolAppliedComponent> protocolAppliedListOne,
			List<ImmunizationProtocolAppliedComponent> protocolAppliedListTwo, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, protocolAppliedListOne, protocolAppliedListTwo, operationOutcome,
					scenarioName)) {

				for (int i = 0; i < protocolAppliedListOne.size();) {
					for (int j = 0; j < protocolAppliedListTwo.size(); j++) {
						compareImmunizationProtocolAppliedComponent(fieldName, protocolAppliedListOne.get(i),
								protocolAppliedListTwo.get(j), operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(protocolAppliedListOne) && ObjectUtils.isEmpty(protocolAppliedListTwo)) {
				String errorMessage = "The scenario does not require protocolApplied, but submitted file does have protocolApplied";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(protocolAppliedListOne) && ObjectUtils.isNotEmpty(protocolAppliedListTwo)) {
				String errorMessage = "The scenario requires protocolApplied, but submitted file does not have protocolApplied";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfImmunizationEducationComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	public static void compareImmunizationProtocolAppliedComponent(String fieldName,
			ImmunizationProtocolAppliedComponent protocolAppliedOne,
			ImmunizationProtocolAppliedComponent protocolAppliedTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(protocolAppliedOne) && ObjectUtils.isNotEmpty(protocolAppliedTwo)) {
				// series
				if (protocolAppliedOne.hasSeries() && protocolAppliedTwo.hasSeries()) {
					compareString(fieldName + ".series", protocolAppliedOne.getSeries(), protocolAppliedTwo.getSeries(),
							operationOutcome, scenarioName);

				} else if (protocolAppliedOne.hasSeries() && !protocolAppliedTwo.hasSeries()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".series, but submitted file does have " + fieldName + ".series";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!protocolAppliedOne.hasSeries() && protocolAppliedTwo.hasSeries()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".series, but submitted file does not have " + fieldName + ".series";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".series information is null OR equal in both inputJson and goldJson");
				}

				// authority
				if (protocolAppliedOne.hasAuthority() && protocolAppliedTwo.hasAuthority()) {
					compareReference(fieldName + ".authority", protocolAppliedOne.getAuthority(),
							protocolAppliedTwo.getAuthority(), operationOutcome, scenarioName);
				} else if (protocolAppliedOne.hasAuthority() && !protocolAppliedTwo.hasAuthority()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".authority, but submitted file does have " + fieldName + ".authority";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!protocolAppliedOne.hasAuthority() && protocolAppliedTwo.hasAuthority()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".authority, but submitted file does not have " + fieldName + ".authority";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".authority information is null OR equal in both inputJson and goldJson");
				}
				// targetDisease
				if (protocolAppliedOne.hasTargetDisease() && protocolAppliedTwo.hasTargetDisease()) {
					compareListOfCodeableConcept(fieldName + ".targetDisease", protocolAppliedOne.getTargetDisease(),
							protocolAppliedTwo.getTargetDisease(), operationOutcome, scenarioName);
				} else if (protocolAppliedOne.hasTargetDisease() && !protocolAppliedTwo.hasTargetDisease()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".targetDisease, but submitted file does have " + fieldName + ".targetDisease";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!protocolAppliedOne.hasTargetDisease() && protocolAppliedTwo.hasTargetDisease()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".targetDisease, but submitted file does not have " + fieldName + ".targetDisease";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(
							fieldName + ".targetDisease information is null OR equal in both inputJson and goldJson");
				}

				// doseNumber[x]
				if (protocolAppliedOne.hasDoseNumber() && protocolAppliedTwo.hasDoseNumber()) {
					if (protocolAppliedOne.hasDoseNumberPositiveIntType()
							&& protocolAppliedTwo.hasDoseNumberPositiveIntType()
							&& protocolAppliedOne.getDoseNumberPositiveIntType() instanceof PositiveIntType
							&& protocolAppliedTwo.getDoseNumberPositiveIntType() instanceof PositiveIntType) {
						comparePositiveIntType(fieldName + ".doseNumberPositiveInt",
								protocolAppliedOne.getDoseNumberPositiveIntType(),
								protocolAppliedTwo.getDoseNumberPositiveIntType(), operationOutcome, scenarioName);
					} else if (protocolAppliedOne.hasDoseNumberPositiveIntType()
							&& !protocolAppliedTwo.hasDoseNumberPositiveIntType()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".doseNumberPositiveInt, but submitted file does have " + fieldName
								+ ".doseNumberPositiveInt";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
					} else if (!protocolAppliedOne.hasDoseNumberPositiveIntType()
							&& protocolAppliedTwo.hasDoseNumberPositiveIntType()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".doseNumberPositiveInt, but submitted file does not have " + fieldName
								+ ".doseNumberPositiveInt";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
					} else {
						logger.info(fieldName
								+ ".doseNumberPositiveInt information is null OR equal in both inputJson and goldJson");
					}

					if (protocolAppliedOne.hasDoseNumberStringType() && protocolAppliedTwo.hasDoseNumberStringType()
							&& protocolAppliedOne.getDoseNumberStringType() instanceof StringType
							&& protocolAppliedTwo.getDoseNumberStringType() instanceof StringType) {
						compareString(fieldName + ".doseNumberString",
								protocolAppliedOne.getDoseNumberStringType().getValueAsString(),
								protocolAppliedTwo.getDoseNumberStringType().getValueAsString(), operationOutcome,
								scenarioName);
					} else if (protocolAppliedOne.hasDoseNumberStringType()
							&& !protocolAppliedTwo.hasDoseNumberStringType()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".doseNumberString, but submitted file does have " + fieldName + ".doseNumberString";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
					} else if (!protocolAppliedOne.hasDoseNumberStringType()
							&& protocolAppliedTwo.hasDoseNumberStringType()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".doseNumberString, but submitted file does not have " + fieldName
								+ ".doseNumberString";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
					} else {
						logger.info(fieldName
								+ ".doseNumberString information is null OR equal in both inputJson and goldJson");
					}
				} else if (protocolAppliedOne.hasDoseNumber() && !protocolAppliedTwo.hasDoseNumber()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".doseNumber, but submitted file does have " + fieldName + ".doseNumber";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!protocolAppliedOne.hasDoseNumber() && protocolAppliedTwo.hasDoseNumber()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".doseNumber, but submitted file does not have " + fieldName + ".doseNumber";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".doseNumber information is null OR equal in both inputJson and goldJson");
				}

				// seriesDoses[x]
				if (protocolAppliedOne.hasSeriesDoses() && protocolAppliedTwo.hasSeriesDoses()) {
					if (protocolAppliedOne.hasSeriesDosesPositiveIntType()
							&& protocolAppliedTwo.hasSeriesDosesPositiveIntType()
							&& protocolAppliedOne.getSeriesDosesPositiveIntType() instanceof PositiveIntType
							&& protocolAppliedTwo.getSeriesDosesPositiveIntType() instanceof PositiveIntType) {
						comparePositiveIntType(fieldName + ".seriesDosesPositiveInt",
								protocolAppliedOne.getSeriesDosesPositiveIntType(),
								protocolAppliedTwo.getSeriesDosesPositiveIntType(), operationOutcome, scenarioName);
					} else if (protocolAppliedOne.hasSeriesDosesPositiveIntType()
							&& !protocolAppliedTwo.hasSeriesDosesPositiveIntType()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".seriesDosesPositiveInt, but submitted file does have " + fieldName
								+ ".seriesDosesPositiveInt";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
					} else if (!protocolAppliedOne.hasSeriesDosesPositiveIntType()
							&& protocolAppliedTwo.hasSeriesDosesPositiveIntType()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".seriesDosesPositiveInt, but submitted file does not have " + fieldName
								+ ".seriesDosesPositiveInt";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
					} else {
						logger.info(fieldName
								+ ".seriesDosesPositiveInt information is null OR equal in both inputJson and goldJson");
					}
					if (protocolAppliedOne.hasSeriesDosesStringType() && protocolAppliedTwo.hasSeriesDosesStringType()
							&& protocolAppliedOne.getSeriesDosesStringType() instanceof StringType
							&& protocolAppliedTwo.getSeriesDosesStringType() instanceof StringType) {
						compareString(fieldName + ".seriesDosesString",
								protocolAppliedOne.getSeriesDosesStringType().getValueAsString(),
								protocolAppliedTwo.getSeriesDosesStringType().getValueAsString(), operationOutcome,
								scenarioName);
					} else if (protocolAppliedOne.hasSeriesDosesStringType()
							&& !protocolAppliedTwo.hasSeriesDosesStringType()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".seriesDosesString, but submitted file does have " + fieldName
								+ ".seriesDosesString";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
					} else if (!protocolAppliedOne.hasSeriesDosesStringType()
							&& protocolAppliedTwo.hasSeriesDosesStringType()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".seriesDosesString, but submitted file does not have " + fieldName
								+ ".seriesDosesString";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
					} else {
						logger.info(fieldName
								+ ".seriesDosesString information is null OR equal in both inputJson and goldJson");
					}
				} else if (protocolAppliedOne.hasSeriesDoses() && !protocolAppliedTwo.hasSeriesDoses()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".seriesDoses, but submitted file does have " + fieldName + ".seriesDoses";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!protocolAppliedOne.hasSeriesDoses() && protocolAppliedTwo.hasSeriesDoses()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".seriesDoses, but submitted file does not have " + fieldName + ".seriesDoses";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".seriesDoses information is null OR equal in both inputJson and goldJson");
				}

			} else if (protocolAppliedOne != null && protocolAppliedTwo == null) {

				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have"
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (protocolAppliedOne == null && ObjectUtils.isNotEmpty(protocolAppliedTwo)) {

				String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have"
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareImmunizationProtocolAppliedComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	private static void comparePositiveIntType(String fieldName, PositiveIntType doseNumberPositiveIntTypeOne,
			PositiveIntType doseNumberPositiveIntTypeTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(doseNumberPositiveIntTypeOne)
					&& ObjectUtils.isNotEmpty(doseNumberPositiveIntTypeTwo)) {
				// value
				if (doseNumberPositiveIntTypeOne.hasValue() && doseNumberPositiveIntTypeTwo.hasValue()
						&& (!(doseNumberPositiveIntTypeOne.getValue()
								.equals(doseNumberPositiveIntTypeTwo.getValue())))) {
					String errorMessage = fieldName + ".value Expected = " + doseNumberPositiveIntTypeTwo.getValue()
							+ " but, submitted file contains" + fieldName + ".value of "
							+ doseNumberPositiveIntTypeOne.getValue();
					operationOutcome
							.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName + ".value", errorMessage));
				} else if (doseNumberPositiveIntTypeOne.hasValue() && !doseNumberPositiveIntTypeTwo.hasValue()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".value, but submitted file does have " + fieldName + ".value";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!doseNumberPositiveIntTypeOne.hasValue() && doseNumberPositiveIntTypeTwo.hasValue()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".value, but submitted file does not have " + fieldName + ".value";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".value information is null OR equal in both inputJson and goldJson");
				}

			} else if (doseNumberPositiveIntTypeOne != null && doseNumberPositiveIntTypeTwo == null) {

				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (doseNumberPositiveIntTypeOne == null && ObjectUtils.isNotEmpty(doseNumberPositiveIntTypeTwo)) {

				String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in comparePositiveIntType of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareListOfDiagnosticReportMediaComponent(String fieldName,
			List<DiagnosticReportMediaComponent> mediaListOne, List<DiagnosticReportMediaComponent> mediaListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, mediaListOne, mediaListTwo, operationOutcome, scenarioName)) {

				for (int i = 0; i < mediaListOne.size();) {
					for (int j = 0; j < mediaListTwo.size(); j++) {
						compareDiagnosticReportMediaComponent(fieldName, mediaListOne.get(i), mediaListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(mediaListOne) && ObjectUtils.isEmpty(mediaListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(mediaListOne) && ObjectUtils.isNotEmpty(mediaListTwo)) {
				String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfDiagnosticReportMediaComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	public static void compareDiagnosticReportMediaComponent(String fieldName, DiagnosticReportMediaComponent mediaOne,
			DiagnosticReportMediaComponent mediaTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(mediaOne) && ObjectUtils.isNotEmpty(mediaTwo)) {
				// comment
				if (mediaOne.hasComment() && mediaTwo.hasComment()
						&& (!(mediaOne.getComment().equals(mediaTwo.getComment())))) {
					String errorMessage = fieldName + ".comment Expected = " + mediaTwo
							+ " but, submitted file contains" + fieldName + ".comment of " + mediaOne;
					operationOutcome.addIssue(
							createScenarioResults(IssueType.INCOMPLETE, fieldName + ".comment", errorMessage));
				} else if (mediaOne.hasComment() && !mediaTwo.hasComment()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".comment, but submitted file does have " + fieldName + ".comment";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else if (!mediaOne.hasComment() && mediaTwo.hasComment()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".comment, but submitted file does not have " + fieldName + ".comment";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
				} else {
					logger.info(fieldName + ".comment information is null OR equal in both inputJson and goldJson");
				}

				// link
				if (mediaOne.hasLink() && mediaTwo.hasLink())
					compareReference(fieldName + ".link", mediaOne.getLink(), mediaTwo.getLink(), operationOutcome,
							scenarioName);
			} else if (mediaOne != null && mediaTwo == null) {

				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (mediaOne == null && ObjectUtils.isNotEmpty(mediaTwo)) {

				String errorMessage = "The scenario requires " + fieldName + ", but location file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDateTimeType of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareListOfAttachment(String fieldName, List<Attachment> presentedFormListOne,
			List<Attachment> presentedFormListTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, presentedFormListOne, presentedFormListTwo, operationOutcome,
					scenarioName)) {

				for (int i = 0; i < presentedFormListOne.size();) {
					for (int j = 0; j < presentedFormListTwo.size(); j++) {
						compareAttachment(fieldName, presentedFormListOne.get(i), presentedFormListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(presentedFormListOne) && ObjectUtils.isEmpty(presentedFormListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName + ", but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(presentedFormListOne) && ObjectUtils.isNotEmpty(presentedFormListTwo)) {
				String errorMessage = "The scenario requires " + fieldName + ", but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfAttachment of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareAttachment(String fieldName, Attachment attachment, Attachment attachment2,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(attachment) && ObjectUtils.isNotEmpty(attachment2)) {

				// contentType
				if (attachment.hasContentType() && attachment2.hasContentType()) {
					compareString(fieldName + ".contentType", attachment.getContentType(), attachment2.getContentType(),
							operationOutcome, scenarioName);
				} else if (attachment.hasContentType() && !attachment2.hasContentType()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".contentType, but submitted file does have .contentType";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".contentType", errorMessage));
				} else if (!attachment.hasContentType() && attachment2.hasContentType()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".contentType, but submitted file does not have .contentType";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".contentType", errorMessage));
				} else {
					logger.info(fieldName + ".contentType information is null OR equal in both inputJson and goldJson");
				}

				// language
				if (attachment.hasLanguage() && attachment2.hasLanguage()) {
					compareString(fieldName + ".language", attachment.getLanguage(), attachment2.getLanguage(),
							operationOutcome, scenarioName);
				} else if (attachment.hasLanguage() && !attachment2.hasLanguage()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".language, but submitted file does have .language";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".language", errorMessage));
				} else if (!attachment.hasLanguage() && attachment2.hasLanguage()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".language, but submitted file does not have .language";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".language", errorMessage));
				} else {
					logger.info(fieldName + ".language information is null OR equal in both inputJson and goldJson");
				}

				// title
				if (attachment.hasTitle() && attachment2.hasTitle()) {
					compareString(fieldName + ".title", attachment.getTitle(), attachment2.getTitle(), operationOutcome,
							scenarioName);
				} else if (attachment.hasTitle() && !attachment2.hasTitle()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".title, but submitted file does have .title";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".title", errorMessage));
				} else if (!attachment.hasTitle() && attachment2.hasTitle()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".title, but submitted file does not have .title";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".title", errorMessage));
				} else {
					logger.info(fieldName + ".title information is null OR equal in both inputJson and goldJson");
				}

				// creation
				if (attachment.hasCreation() && attachment2.hasCreation()) {
					compareDate(fieldName + ".creation", attachment.getCreation(), attachment2.getCreation(),
							operationOutcome, scenarioName);
				} else if (attachment.hasCreation() && !attachment2.hasCreation()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".creation, but submitted file does have .creation";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".creation", errorMessage));
				} else if (!attachment.hasCreation() && attachment2.hasCreation()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".creation, but submitted file does not have .creation";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".creation", errorMessage));
				} else {
					logger.info(fieldName + ".creation information is null OR equal in both inputJson and goldJson");
				}

				// url
				if (attachment.hasUrl() && attachment2.hasUrl()) {
					compareString(fieldName + ".url", attachment.getUrl(), attachment2.getUrl(), operationOutcome,
							scenarioName);
				} else if (attachment.hasUrl() && !attachment2.hasUrl()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".url, but submitted file does have .url";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".url", errorMessage));
				} else if (!attachment.hasUrl() && attachment2.hasUrl()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".url, but submitted file does not have .url";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".url", errorMessage));
				} else {
					logger.info(fieldName + ".url information is null OR equal in both inputJson and goldJson");
				}

				// data
//				if (attachment.hasData() && attachment2.hasData()) {
//					comparebyte(fieldName+".data", attachment.getData(), attachment2.getData(),
//							operationOutcome);
//				} else if (attachment.hasData() && !attachment2.hasData()) {
//					String errorMessage = "The scenario does not require " + fieldName
//							+ ".data, but submitted file does have .data";
//					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
//							fieldName + ".data", errorMessage));
//				} else if (!attachment.hasData() && attachment2.hasData()) {
//					String errorMessage = "The scenario requires " + fieldName
//							+ ".data, but submitted file does not have .data";
//					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
//							fieldName + ".data", errorMessage));
//				} else {
//					logger.info(fieldName + ".data information is null OR equal in both inputJson and goldJson");
//				}

			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareAttachment of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareProcedurePerformerComponent(String performer, ProcedurePerformerComponent performerOne,
			ProcedurePerformerComponent performertwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(performerOne) && ObjectUtils.isNotEmpty(performertwo)) {

				// function
				if (performerOne.hasFunction() && performertwo.hasFunction()) {
					compareCodeableConcept(performer, performerOne.getFunction(), performertwo.getFunction(),
							operationOutcome, scenarioName);
				} else if (performerOne.hasFunction() && !performertwo.hasFunction()) {
					String errorMessage = "The scenario does not require " + performer
							+ ".function, but submitted file does have function";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, performer + ".function", errorMessage));
				} else if (!performerOne.hasFunction() && performertwo.hasFunction()) {
					String errorMessage = "The scenario requires " + performer
							+ ".class, but submitted file does not have function";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, performer + ".function", errorMessage));
				} else {
					logger.info(performer + ".function information is null OR equal in both inputJson and goldJson");
				}

				// actor
				if (performerOne.hasActor() && performertwo.hasActor()) {
					compareReference(performer, performerOne.getActor(), performertwo.getActor(), operationOutcome,
							scenarioName);
				} else if (performerOne.hasActor() && !performertwo.hasActor()) {
					String errorMessage = "The scenario does not require " + performer
							+ ".actor, but submitted file does have actor";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, performer + ".actor", errorMessage));
				} else if (!performerOne.hasActor() && performertwo.hasActor()) {
					String errorMessage = "The scenario requires " + performer
							+ ".actor, but submitted file does not have actor";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, performer + ".actor", errorMessage));
				} else {

					logger.info(performer + ".actor information is null OR equal in both inputJson and goldJson");
				}
				// onBehalfOf
				if (performerOne.hasOnBehalfOf() && performertwo.hasOnBehalfOf()) {
					compareReference(performer, performerOne.getOnBehalfOf(), performertwo.getOnBehalfOf(),
							operationOutcome, scenarioName);
				} else if (performerOne.hasOnBehalfOf() && !performertwo.hasOnBehalfOf()) {
					String errorMessage = "The scenario does not require " + performer
							+ ".onBehalfOf, but submitted file does have onBehalfOf";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, performer + ".onBehalfOf", errorMessage));
				} else if (!performerOne.hasOnBehalfOf() && performertwo.hasOnBehalfOf()) {
					String errorMessage = "The scenario requires " + performer
							+ ".onBehalfOf, but submitted file does not have onBehalfOf";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, performer + ".onBehalfOf", errorMessage));
				} else {
					logger.info(performer + ".onBehalfOf information is null OR equal in both inputJson and goldJson");
				}

			}
		} catch (Exception ex) {

			logger.info(performer + ".actor information is null OR equal in both inputJson and goldJson");
		}

	}

	public static void compareListOfCareTeamParticipantComponent(String fieldName,
			List<CareTeamParticipantComponent> participantListOne,
			List<CareTeamParticipantComponent> participantListTwo, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, participantListOne, participantListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < participantListOne.size();) {
					for (int j = 0; j < participantListTwo.size(); j++) {
						compareCareTeamParticipantComponent(fieldName, participantListOne.get(i),
								participantListTwo.get(j), operationOutcome, scenarioName);

						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(participantListOne) && ObjectUtils.isEmpty(participantListTwo)) {
				String errorMessage = "The scenario does not require participant, but submitted file does have participant";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(participantListOne) && ObjectUtils.isNotEmpty(participantListTwo)) {
				String errorMessage = "The scenario requires participant, but submitted file does not have participant";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfCareTeamParticipantComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	private static void compareCareTeamParticipantComponent(String fieldName,
			CareTeamParticipantComponent careTeamParticipantComponentOne,
			CareTeamParticipantComponent careTeamParticipantComponentTwo, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(careTeamParticipantComponentOne)
					&& ObjectUtils.isNotEmpty(careTeamParticipantComponentTwo)) {

				// role
				if (careTeamParticipantComponentOne.hasRole() && careTeamParticipantComponentTwo.hasRole()) {
					compareListOfCodeableConcept(fieldName + ".role", careTeamParticipantComponentOne.getRole(),
							careTeamParticipantComponentTwo.getRole(), operationOutcome, scenarioName);
				} else if (careTeamParticipantComponentOne.hasRole() && !careTeamParticipantComponentTwo.hasRole()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".role, but submitted file does have role";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".role", errorMessage));
				} else if (!careTeamParticipantComponentOne.hasRole() && careTeamParticipantComponentTwo.hasRole()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".role, but submitted file does not have role";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".role", errorMessage));
				} else {
					logger.info(fieldName + ".role information is null OR equal in both inputJson and goldJson");
				}

				// member
				if (careTeamParticipantComponentOne.hasMember() && careTeamParticipantComponentTwo.hasMember()) {
					compareReference(fieldName + ".member", careTeamParticipantComponentOne.getMember(),
							careTeamParticipantComponentTwo.getMember(), operationOutcome, scenarioName);
				} else if (careTeamParticipantComponentOne.hasMember()
						&& !careTeamParticipantComponentTwo.hasMember()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".member, but submitted file does have member";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".member", errorMessage));
				} else if (!careTeamParticipantComponentOne.hasMember()
						&& careTeamParticipantComponentTwo.hasMember()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".member, but submitted file does not have member";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".member", errorMessage));
				} else {
					logger.info(fieldName + ".member information is null OR equal in both inputJson and goldJson");
				}

				// onBehalfOf
				if (scenarioName.equalsIgnoreCase(ScenarioConstants.ScenarioNameConstants.USCDI_FULL_CARETEAM)) {
					if (careTeamParticipantComponentOne.hasOnBehalfOf()
							&& careTeamParticipantComponentTwo.hasOnBehalfOf()) {
						compareReference(fieldName + ".onBehalfOf", careTeamParticipantComponentOne.getOnBehalfOf(),
								careTeamParticipantComponentTwo.getOnBehalfOf(), operationOutcome, scenarioName);
					} else if (careTeamParticipantComponentOne.hasOnBehalfOf()
							&& !careTeamParticipantComponentTwo.hasOnBehalfOf()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".OnBehalfOf, but submitted file does have OnBehalfOf";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".OnBehalfOf", errorMessage));
					} else if (!careTeamParticipantComponentOne.hasOnBehalfOf()
							&& careTeamParticipantComponentTwo.hasOnBehalfOf()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".OnBehalfOf, but submitted file does not have OnBehalfOf";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".OnBehalfOf", errorMessage));
					} else {
						logger.info(
								fieldName + ".OnBehalfOf information is null OR equal in both inputJson and goldJson");
					}

					// period
					if (careTeamParticipantComponentOne.hasPeriod() && careTeamParticipantComponentTwo.hasPeriod()) {
						comparePeriod(fieldName + ".period", careTeamParticipantComponentOne.getPeriod(),
								careTeamParticipantComponentTwo.getPeriod(), operationOutcome, scenarioName);
					} else if (careTeamParticipantComponentOne.hasPeriod()
							&& !careTeamParticipantComponentTwo.hasPeriod()) {
						String errorMessage = "The scenario does not require " + fieldName
								+ ".period, but submitted file does have period";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
					} else if (!careTeamParticipantComponentOne.hasPeriod()
							&& careTeamParticipantComponentTwo.hasPeriod()) {
						String errorMessage = "The scenario requires " + fieldName
								+ ".period, but submitted file does not have period";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
					} else {
						logger.info(fieldName + ".period information is null OR equal in both inputJson and goldJson");
					}
				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfCareTeamParticipantComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void comparePerformedDate(String performed, DateTimeType type, DateTimeType type2,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(type) && ObjectUtils.isNotEmpty(type2)) {
				Date date1 = type.getValue();
				Date date2 = type2.getValue();
				compareDate(performed, date1, date2, operationOutcome, scenarioName);
			} else if (ObjectUtils.isNotEmpty(type) && ObjectUtils.isEmpty(type2)) {
				String errorMessage = "The scenario does not require " + performed
						+ ".performedDateTime, but submitted file does have performedDateTime";
				operationOutcome.addIssue(
						createScenarioResults(IssueType.CONFLICT, performed + ".performedDateTime", errorMessage));
			} else if (ObjectUtils.isEmpty(type) && ObjectUtils.isNotEmpty(type2)) {
				String errorMessage = "The scenario requires " + performed
						+ ".performedDateTime, but submitted file does not have performedDateTime";
				operationOutcome.addIssue(
						createScenarioResults(IssueType.CONFLICT, performed + ".performedDateTime", errorMessage));
			} else {
				logger.info(
						performed + " performedDateTime information is null OR equal in both inputJson and goldJson");
			}
		} catch (Exception ex) {
			logger.error("\n Exception in comparePerformedDate of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareListOfCarePlanActivityComponent(String activity,
			List<CarePlanActivityComponent> activitylistone, List<CarePlanActivityComponent> activitylisttwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(activity, activitylistone, activitylisttwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < activitylistone.size();) {
					for (int j = 0; j < activitylisttwo.size(); j++) {
						compareCarePlanActivityComponent(activity, activitylistone.get(i), activitylisttwo.get(j),
								operationOutcome, scenarioName);

						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(activitylistone) && ObjectUtils.isEmpty(activitylisttwo)) {
				String errorMessage = "The scenario does not require Activity, but submitted file does have Activity";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, activity, errorMessage));
			} else if (ObjectUtils.isEmpty(activitylistone) && ObjectUtils.isNotEmpty(activitylisttwo)) {
				String errorMessage = "The scenario requires Activity, but submitted file does not have Activity";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, activity, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfCarePlanActivityComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareCarePlanActivityComponent(String activity, CarePlanActivityComponent activityone,
			CarePlanActivityComponent activitytwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(activityone) && ObjectUtils.isNotEmpty(activitytwo)) {

				// outcomeCodeableConcept
				if (activityone.hasOutcomeCodeableConcept() && activitytwo.hasOutcomeCodeableConcept()) {
					compareListOfCodeableConcept(activity + ".outcomeCodeableConcept",
							activityone.getOutcomeCodeableConcept(), activitytwo.getOutcomeCodeableConcept(),
							operationOutcome, scenarioName);
				} else if (activityone.hasOutcomeCodeableConcept() && !activitytwo.hasOutcomeCodeableConcept()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".outcomeCodeableConcept, but submitted file does have outcomeCodeableConcept";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							activity + ".outcomeCodeableConcept", errorMessage));
				} else if (!activityone.hasOutcomeCodeableConcept() && activitytwo.hasOutcomeCodeableConcept()) {
					String errorMessage = "The scenario requires " + activity
							+ ".outcomeCodeableConcept, but submitted file does not have outcomeCodeableConcept";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							activity + ".outcomeCodeableConcept", errorMessage));
				} else {
					logger.info(activity
							+ ".outcomeCodeableConcept information is null OR equal in both inputJson and goldJson");
				}

				// outcomeReference
				if (activityone.hasOutcomeReference() && activitytwo.hasOutcomeReference()) {
					compareListOfReference(activity + ".outcomeReference", activityone.getOutcomeReference(),
							activitytwo.getOutcomeReference(), operationOutcome, scenarioName);
				} else if (activityone.hasOutcomeReference() && !activitytwo.hasOutcomeReference()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".outcomeReference, but submitted file does have outcomeReference";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".outcomeReference", errorMessage));
				} else if (!activityone.hasOutcomeReference() && activitytwo.hasOutcomeReference()) {
					String errorMessage = "The scenario requires " + activity
							+ ".outcomeReference, but submitted file does not have outcomeReference";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".outcomeReference", errorMessage));
				} else {
					logger.info(
							activity + ".outcomeReference information is null OR equal in both inputJson and goldJson");
				}

				// progress
				if (activityone.hasProgress() && activitytwo.hasProgress()) {
					compareListOfAnnotation(activity + ".progress", activityone.getProgress(),
							activitytwo.getProgress(), operationOutcome, scenarioName);
				} else if (activityone.hasProgress() && !activitytwo.hasProgress()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".progress, but submitted file does have progress";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, activity + ".progress", errorMessage));
				} else if (!activityone.hasProgress() && activitytwo.hasProgress()) {
					String errorMessage = "The scenario requires " + activity
							+ ".progress, but submitted file does not have progress";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, activity + ".progress", errorMessage));
				} else {
					logger.info(activity + ".progress information is null OR equal in both inputJson and goldJson");
				}

				// reference
				if (activityone.hasReference() && activitytwo.hasReference()) {
					compareReference(activity + ".reference", activityone.getReference(), activitytwo.getReference(),
							operationOutcome, scenarioName);
				} else if (activityone.hasReference() && !activitytwo.hasReference()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".reference, but submitted file does have reference";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, activity + ".reference", errorMessage));
				} else if (!activityone.hasReference() && activitytwo.hasReference()) {
					String errorMessage = "The scenario requires " + activity
							+ ".reference, but submitted file does not have reference";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, activity + ".reference", errorMessage));
				} else {
					logger.info(activity + ".reference information is null OR equal in both inputJson and goldJson");
				}

				// detail
				if (activityone.hasDetail() && activitytwo.hasDetail()) {
					compareCarePlanActivityDetailComponent(activity + ".detail", activityone.getDetail(),
							activitytwo.getDetail(), operationOutcome, scenarioName);
				} else if (activityone.hasDetail() && !activitytwo.hasDetail()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail, but submitted file does have detail";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, activity + ".detail", errorMessage));
				} else if (!activityone.hasDetail() && activitytwo.hasDetail()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail, but submitted file does not have detail";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, activity + ".detail", errorMessage));
				} else {
					logger.info(activity + ".detail information is null OR equal in both inputJson and goldJson");
				}

			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareCarePlanActivityComponent of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareCarePlanActivityDetailComponent(String activity,
			CarePlanActivityDetailComponent detailone, CarePlanActivityDetailComponent detailtwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(detailone) && ObjectUtils.isNotEmpty(detailtwo)) {
				// kind
				if (detailone.hasKind() && detailtwo.hasKind()) {
					compareString(activity + ".detail.kind", detailone.getKind().getDisplay(),
							detailtwo.getKind().getDisplay(), operationOutcome, scenarioName);
				} else if (detailone.hasKind() && !detailtwo.hasKind()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.kind, but submitted file does have .detail.kind";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.kind", errorMessage));
				} else if (!detailone.hasKind() && detailtwo.hasKind()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.kind, but submitted file does not have .detail.kind";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.kind", errorMessage));
				} else {
					logger.info(activity + ".detail.kind information is null OR equal in both inputJson and goldJson");
				}

				// extension
				if (detailone.hasExtension() && detailtwo.hasExtension()) {
					compareListOfExtension(activity + ".detail.extension", detailone.getExtension(),
							detailtwo.getExtension(), operationOutcome, scenarioName);
				} else if (detailone.hasExtension() && !detailtwo.hasExtension()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.extension, but submitted file does have .detail.extension";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.extension", errorMessage));
				} else if (!detailone.hasExtension() && detailtwo.hasExtension()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.extension, but submitted file does not have .detail.extension";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, activity + ".extension", errorMessage));
				} else {
					logger.info(
							activity + ".detail.extension information is null OR equal in both inputJson and goldJson");
				}

				// modifierExtension
				if (detailone.hasModifierExtension() && detailtwo.hasModifierExtension()) {
					compareListOfExtension(activity + ".detail.modifierExtension", detailone.getModifierExtension(),
							detailtwo.getModifierExtension(), operationOutcome, scenarioName);
				} else if (detailone.hasModifierExtension() && !detailtwo.hasModifierExtension()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.modifierExtension, but submitted file does have .detail.modifierExtension";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							activity + ".detail.modifierExtension", errorMessage));
				} else if (!detailone.hasModifierExtension() && detailtwo.hasModifierExtension()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.modifierExtension, but submitted file does not have .detail.modifierExtension";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							activity + ".detail.modifierExtension", errorMessage));
				} else {
					logger.info(activity
							+ ".detail.modifierExtension information is null OR equal in both inputJson and goldJson");
				}

				// id
				if (detailone.hasId() && detailtwo.hasId()) {
					compareString(activity + ".detail.id", detailone.getId(), detailtwo.getId(), operationOutcome,
							scenarioName);
				} else if (detailone.hasId() && !detailtwo.hasId()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.id, but submitted file does have .detail.id";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, activity + ".detail.id", errorMessage));
				} else if (!detailone.hasId() && detailtwo.hasId()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.id, but submitted file does not have .detail.id";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, activity + ".detail.id", errorMessage));
				} else {
					logger.info(activity + ".detail.id information is null OR equal in both inputJson and goldJson");
				}

				// code
				if (detailone.hasCode() && detailtwo.hasCode()) {
					compareCodeableConcept(activity + ".detail.code", detailone.getCode(), detailtwo.getCode(),
							operationOutcome, scenarioName);
				} else if (detailone.hasCode() && !detailtwo.hasCode()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.code, but submitted file does have code";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.code", errorMessage));
				} else if (!detailone.hasCode() && detailtwo.hasCode()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.code, but submitted file does not have code";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.code", errorMessage));
				} else {
					logger.info(activity + ".detail.code information is null OR equal in both inputJson and goldJson");
				}

				// reasonCode
				if (detailone.hasReasonCode() && detailtwo.hasReasonCode()) {
					compareListOfCodeableConcept(activity + ".detail.reasonCode", detailone.getReasonCode(),
							detailtwo.getReasonCode(), operationOutcome, scenarioName);
				} else if (detailone.hasReasonCode() && !detailtwo.hasReasonCode()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.reasonCode, but submitted file does have .detail.reasonCode";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.reasonCode", errorMessage));
				} else if (!detailone.hasReasonCode() && detailtwo.hasReasonCode()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.reasonCode, but submitted file does not have .detail.reasonCode";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.reasonCode", errorMessage));
				} else {
					logger.info(activity
							+ ".detail.reasonCode information is null OR equal in both inputJson and goldJson");
				}

				// reasonReference
				if (detailone.hasReasonReference() && detailtwo.hasReasonReference()) {
					compareListOfReference(activity + ".detail.reasonReference", detailone.getReasonReference(),
							detailtwo.getReasonReference(), operationOutcome, scenarioName);
				} else if (detailone.hasReasonReference() && !detailtwo.hasReasonReference()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.reasonReference, but submitted file does have .detail.reasonReference";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							activity + ".detail.reasonReference", errorMessage));
				} else if (!detailone.hasReasonReference() && detailtwo.hasReasonReference()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.reasonReference, but submitted file does not have .detail.reasonReference";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							activity + ".detail.reasonReference", errorMessage));
				} else {
					logger.info(activity
							+ ".detail.reasonReference information is null OR equal in both inputJson and goldJson");
				}

				// goal
				if (detailone.hasGoal() && detailtwo.hasGoal()) {
					compareListOfReference(activity + ".detail.goal", detailone.getGoal(), detailtwo.getGoal(),
							operationOutcome, scenarioName);
				} else if (detailone.hasGoal() && !detailtwo.hasGoal()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.goal, but submitted file does have .detail.goal";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.goal", errorMessage));
				} else if (!detailone.hasGoal() && detailtwo.hasGoal()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.goal, but submitted file does not have .detail.goal";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.goal", errorMessage));
				} else {
					logger.info(activity + ".goal information is null OR equal in both inputJson and goldJson");
				}

				// status
				if (detailone.hasStatus() && detailtwo.hasStatus()) {
					compareString(activity + ".detail.status", detailone.getStatus().getDisplay(),
							detailtwo.getStatus().getDisplay(), operationOutcome, scenarioName);
				} else if (detailone.hasStatus() && !detailtwo.hasStatus()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.status, but submitted file does have .detail.status";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.status", errorMessage));
				} else if (!detailone.hasStatus() && detailtwo.hasStatus()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.status, but submitted file does not have .detail.status";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.status", errorMessage));
				} else {
					logger.info(
							activity + ".detail.status information is null OR equal in both inputJson and goldJson");
				}

				// statusReason
				if (detailone.hasStatusReason() && detailtwo.hasStatusReason()) {
					compareCodeableConcept(activity + ".detail.statusReason", detailone.getStatusReason(),
							detailtwo.getStatusReason(), operationOutcome, scenarioName);
				} else if (detailone.hasStatusReason() && !detailtwo.hasStatusReason()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.statusReason, but submitted file does have .detail.statusReason";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.statusReason", errorMessage));
				} else if (!detailone.hasStatusReason() && detailtwo.hasStatusReason()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.statusReason, but submitted file does not have .detail.statusReason";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.statusReason", errorMessage));
				} else {
					logger.info(activity
							+ ".detail.statusReason information is null OR equal in both inputJson and goldJson");
				}

				// location
				if (detailone.hasLocation() && detailtwo.hasLocation()) {
					compareReference(activity + ".detail.location", detailone.getLocation(), detailtwo.getLocation(),
							operationOutcome, scenarioName);
				} else if (detailone.hasLocation() && !detailtwo.hasLocation()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.location, but submitted file does have .detail.location";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.location", errorMessage));
				} else if (!detailone.hasLocation() && detailtwo.hasLocation()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.location, but submitted file does not have .detail.location";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.location", errorMessage));
				} else {
					logger.info(
							activity + ".detail.location information is null OR equal in both inputJson and goldJson");
				}

				// performer
				if (detailone.hasPerformer() && detailtwo.hasPerformer()) {
					compareListOfReference(activity + ".detail.performer", detailone.getPerformer(),
							detailtwo.getPerformer(), operationOutcome, scenarioName);
				} else if (detailone.hasPerformer() && !detailtwo.hasPerformer()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.performer, but submitted file does have .detail.performer";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.performer", errorMessage));
				} else if (!detailone.hasPerformer() && detailtwo.hasPerformer()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.performer, but submitted file does not have .detail.performer";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.performer", errorMessage));
				} else {
					logger.info(
							activity + ".detail.performer information is null OR equal in both inputJson and goldJson");
				}

				// description
				if (detailone.hasDescription() && detailtwo.hasDescription()) {
					compareString(activity + ".detail.description", detailone.getDescription(),
							detailtwo.getDescription(), operationOutcome, scenarioName);
				} else if (detailone.hasDescription() && !detailtwo.hasDescription()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.description, but submitted file does have .detail.description";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.description", errorMessage));
				} else if (!detailone.hasDescription() && detailtwo.hasDescription()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.description, but submitted file does not have .detail.description";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.description", errorMessage));
				} else {
					logger.info(activity
							+ ".detail.description information is null OR equal in both inputJson and goldJson");
				}
				// doNotPerform
				if (detailone.hasDoNotPerform() && detailtwo.hasDoNotPerform()) {
					compareBoolean("activity.detail.doNotPerform", detailone.getDoNotPerform(),
							detailtwo.getDoNotPerform(), operationOutcome, scenarioName);
				} else if (detailone.hasDoNotPerform() && !detailtwo.hasDoNotPerform()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.doNotPerform, but submitted file does have .detail.doNotPerform";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.doNotPerform", errorMessage));
				} else if (!detailone.hasDoNotPerform() && detailtwo.hasDoNotPerform()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.doNotPerform, but submitted file does not have .detail.doNotPerform";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.doNotPerform", errorMessage));
				} else {
					logger.info(activity + ".doNotPerform information is null OR equal in both inputJson and goldJson");
				}

				// quantity
				if (detailone.hasQuantity() && detailtwo.hasQuantity()) {
					compareQuantity("activity.detail.quantity", detailone.getQuantity(), detailtwo.getQuantity(),
							operationOutcome, scenarioName);
				} else if (detailone.hasQuantity() && !detailtwo.hasQuantity()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.quantity, but submitted file does have .detail.quantity";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.quantity", errorMessage));
				} else if (!detailone.hasQuantity() && detailtwo.hasQuantity()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.quantity, but submitted file does not have .detail.quantity";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.quantity", errorMessage));
				} else {
					logger.info(
							activity + ".detail.quantity information is null OR equal in both inputJson and goldJson");
				}

				// dailyAmount
				if (detailone.hasDailyAmount() && detailtwo.hasDailyAmount()) {
					compareQuantity("activity.detail.dailyAmount", detailone.getDailyAmount(),
							detailtwo.getDailyAmount(), operationOutcome, scenarioName);
				} else if (detailone.hasDailyAmount() && !detailtwo.hasDailyAmount()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".detail.dailyAmount, but submitted file does have .detail.dailyAmount";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.dailyAmount", errorMessage));
				} else if (!detailone.hasDailyAmount() && detailtwo.hasDailyAmount()) {
					String errorMessage = "The scenario requires " + activity
							+ ".detail.dailyAmount, but submitted file does not have .detail.dailyAmount";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".detail.dailyAmount", errorMessage));
				} else {
					logger.info(activity
							+ ".detail.dailyAmount information is null OR equal in both inputJson and goldJson");
				}

				// scheduledString
				if (detailone.hasScheduledStringType() && detailtwo.hasScheduledStringType()
						&& !(detailone.getScheduledStringType().equals(detailtwo.getScheduledStringType()))) {

					String errorMessage = activity + ".scheduledString Expected = " + detailtwo.getScheduledStringType()
							+ " but, submitted file contains .scheduledString of " + detailone.getScheduledStringType();
					operationOutcome.addIssue(
							createScenarioResults(IssueType.INCOMPLETE, activity + ".scheduledString", errorMessage));
				} else if (detailone.hasScheduledStringType() && !detailtwo.hasScheduledStringType()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".scheduledString, but submitted file does have .scheduledString";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".scheduledString", errorMessage));
				} else if (!detailone.hasScheduledStringType() && detailtwo.hasScheduledStringType()) {
					String errorMessage = "The scenario requires " + activity
							+ ".scheduledString, but submitted file does not have .scheduledString";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".scheduledString", errorMessage));
				} else {
					logger.info(
							activity + ".scheduledString information is null OR equal in both inputJson and goldJson");
				}

				// productCodeableConcept
				if (detailone.hasProductCodeableConcept() && detailtwo.hasProductCodeableConcept()) {
					compareCodeableConcept(activity + ".productCodeableConcept", detailone.getProductCodeableConcept(),
							detailtwo.getProductCodeableConcept(), operationOutcome, scenarioName);
				} else if (detailone.hasProductCodeableConcept() && !detailtwo.hasProductCodeableConcept()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".productCodeableConcept	, but submitted file does have .productCodeableConcept";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							activity + ".productCodeableConcept", errorMessage));
				} else if (!detailone.hasProductCodeableConcept() && detailtwo.hasProductCodeableConcept()) {
					String errorMessage = "The scenario requires " + activity
							+ ".productCodeableConcept, but submitted file does not have .productCodeableConcept";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							activity + ".productCodeableConcept	", errorMessage));
				} else {
					logger.info(activity
							+ ".productCodeableConcept information is null OR equal in both inputJson and goldJson");
				}

				// productReference
				if (detailone.hasProductReference() && detailtwo.hasProductReference()) {
					compareReference(activity + ".productReference", detailone.getProductReference(),
							detailtwo.getProductReference(), operationOutcome, scenarioName);
				} else if (detailone.hasProductReference() && !detailtwo.hasProductReference()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".productReference, but submitted file does have .productReference";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".productReference", errorMessage));
				} else if (!detailone.hasProductReference() && detailtwo.hasProductReference()) {
					String errorMessage = "The scenario requires " + activity
							+ ".productReference, but submitted file does not have .productReference";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".productReference", errorMessage));
				} else {
					logger.info(
							activity + ".productReference information is null OR equal in both inputJson and goldJson");
				}

				// scheduledPeriod
				if (detailone.hasScheduledPeriod() && detailtwo.hasScheduledPeriod()) {
					comparePeriod(activity + ".period", detailone.getScheduledPeriod(), detailtwo.getScheduledPeriod(),
							operationOutcome, scenarioName);
				} else if (detailone.hasScheduledPeriod() && !detailtwo.hasScheduledPeriod()) {
					String errorMessage = "The scenario does not require " + activity
							+ ".scheduledPeriod, but submitted file does have .scheduledPeriod";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".scheduledPeriod", errorMessage));
				} else if (!detailone.hasScheduledPeriod() && detailtwo.hasScheduledPeriod()) {
					String errorMessage = "The scenario requires " + activity
							+ ".scheduledPeriod, but submitted file does not have .scheduledPeriod";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".scheduledPeriod", errorMessage));
				} else {
					logger.info(
							activity + ".scheduledPeriod information is null OR equal in both inputJson and goldJson");
				}

				// scheduledTiming
				if (detailone.hasScheduledTiming() && detailtwo.hasScheduledTiming()) {
					if (detailone.getScheduledTiming() instanceof Timing
							&& detailtwo.getScheduledTiming() instanceof Timing && (!detailone.getScheduledTiming()
									.toString().equalsIgnoreCase(detailtwo.getScheduledTiming().toString()))) {
						String errorMessage = activity + ".value Expected = " + detailtwo.getScheduledTiming()
								+ " but, submitted file contains url of " + detailone.getScheduledTiming();
						operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE,
								activity + ".scheduledTiming", errorMessage));
					}
				} else if (!detailone.hasScheduledTiming() && detailtwo.hasScheduledTiming()) {
					String errorMessage = "The scenario requires " + activity
							+ ".scheduledTiming, but submitted file does not have " + activity + ".scheduledTiming";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, activity + ".value", errorMessage));
				} else if (detailone.hasScheduledTiming() && !detailtwo.hasScheduledTiming()) {
					String errorMessage = "The scenario does not requires " + activity
							+ ".scheduledTiming, but submitted file have " + activity + ".scheduledTiming";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, activity + ".scheduledTiming", errorMessage));
				} else {
					logger.info(
							activity + ".scheduledTiming information is null OR equal in both inputJson and goldJson");
				}
			} else if (detailone != null && detailtwo == null) {

				String errorMessage = "The scenario does not require " + activity + ", but submitted file does have "
						+ activity;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, activity, errorMessage));
			} else if (detailone == null && ObjectUtils.isNotEmpty(detailtwo)) {

				String errorMessage = "The scenario requires " + activity + ", but location file does not have "
						+ activity;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, activity, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareCarePlanActivityDetailComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareNarrative(String text, Narrative text1, Narrative text2,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(text1) && ObjectUtils.isNotEmpty(text2)) {

				// status
				if (text1.hasStatus() && text2.hasStatus()) {
					compareString("Text.status", text1.getStatus().getDisplay(), text2.getStatus().getDisplay(),
							operationOutcome, scenarioName);
				} else if (text1.hasStatus() && !text2.hasStatus()) {
					String errorMessage = "The scenario does not require " + text
							+ " but submitted file does have status";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, text + "", errorMessage));
				} else if (!text1.hasStatus() && text2.hasStatus()) {
					String errorMessage = "The scenario requires " + text + " but submitted file does not have status";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, text + "", errorMessage));
				} else {
					logger.info(text + " information is null OR equal in both inputJson and goldJson");
				}

				// div
				if (scenarioNames.containsKey(scenarioName)) {

					if (text1.hasDiv() && text2.hasDiv()) {
						compareString("Text.div", text1.getDiv().toString(), text2.getDiv().toString(),
								operationOutcome, scenarioName);
					} else if (text1.hasDiv() && !text2.hasDiv()) {
						String errorMessage = "The scenario does not require " + text
								+ " but submitted file does have div";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, text + "", errorMessage));
					} else if (!text1.hasDiv() && text2.hasDiv()) {
						String errorMessage = "The scenario requires " + text + " but submitted file does not have div";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, text + "", errorMessage));
					} else {
						logger.info(text + " information is null OR equal in both inputJson and goldJson");
					}
				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareNarrative of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareListOfProcedureFocalDeviceComponent(String fieldName,
			List<ProcedureFocalDeviceComponent> focalDevice1, List<ProcedureFocalDeviceComponent> focalDevice2,
			OperationOutcome operationOutcome, String scenarioName) {
		try {

			if (compareListForSizeEquality(fieldName, focalDevice1, focalDevice2, operationOutcome, scenarioName)) {
				for (int i = 0; i < focalDevice1.size();) {
					for (int j = 0; j < focalDevice2.size(); j++) {
						compareProcedureFocalDeviceComponent(fieldName, focalDevice1.get(i), focalDevice2.get(j),
								operationOutcome, scenarioName);

						i++;
					}
				}
			} else if (!focalDevice1.isEmpty() && focalDevice2.isEmpty()) {
				String errorMessage = "The scenario does not require focalDevice, but submitted file does have focalDevice";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (focalDevice1.isEmpty() && !focalDevice2.isEmpty()) {
				String errorMessage = "The scenario requires focalDevice, but submitted file does not have focalDevice";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfProcedureFocalDeviceComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	private static void compareProcedureFocalDeviceComponent(String fieldName,
			ProcedureFocalDeviceComponent procedureFocal, ProcedureFocalDeviceComponent procedureFocal2,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (ObjectUtils.isNotEmpty(procedureFocal) && ObjectUtils.isNotEmpty(procedureFocal2)) {
				// manipulated
				if (procedureFocal.hasManipulated() && procedureFocal2.hasManipulated()) {
					compareReference(fieldName + ".manipulated", procedureFocal.getManipulated(),
							procedureFocal2.getManipulated(), operationOutcome, scenarioName);
				} else if (procedureFocal.hasManipulated() && !procedureFocal2.hasManipulated()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".manipulated but submitted file does have .manipulated";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".manipulated", errorMessage));
				} else if (!procedureFocal.hasManipulated() && procedureFocal2.hasManipulated()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".manipulated but submitted file does not have .manipulated";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".manipulated", errorMessage));
				} else {
					logger.info(fieldName + ".manipulated information is null OR equal in both inputJson and goldJson");
				}

				if (procedureFocal.hasAction() && procedureFocal2.hasAction()) {
					compareCodeableConcept(fieldName + ".action", procedureFocal.getAction(),
							procedureFocal2.getAction(), operationOutcome, scenarioName);
				} else if (procedureFocal.hasAction() && !procedureFocal2.hasAction()) {
					String errorMessage = "The scenario does not require " + fieldName
							+ ".action but submitted file does have .action";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".action", errorMessage));
				} else if (!procedureFocal.hasAction() && procedureFocal2.hasAction()) {
					String errorMessage = "The scenario requires " + fieldName
							+ ".action but submitted file does not have .action";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".action", errorMessage));
				} else {
					logger.info(fieldName + ".action information is null OR equal in both inputJson and goldJson");
				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareProcedureFocalDeviceComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareListOfDeviceUdiCarrierComponent(String fieldName,
			List<DeviceUdiCarrierComponent> udiCarrierListOne, List<DeviceUdiCarrierComponent> udiCarrierListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {

			if (compareListForSizeEquality(fieldName, udiCarrierListOne, udiCarrierListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < udiCarrierListOne.size();) {
					for (int j = 0; j < udiCarrierListTwo.size(); j++) {
						compareDeviceUdiCarrierComponent(fieldName, udiCarrierListOne.get(i), udiCarrierListTwo.get(j),
								operationOutcome, scenarioName);

						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(udiCarrierListOne) && ObjectUtils.isEmpty(udiCarrierListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ ", but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(udiCarrierListOne) && ObjectUtils.isNotEmpty(udiCarrierListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ ", but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfDeviceUdiCarrierComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	private static void compareDeviceUdiCarrierComponent(String fieldName,
			DeviceUdiCarrierComponent deviceUdiCarrierComponentOne,
			DeviceUdiCarrierComponent deviceUdiCarrierComponentTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {

			if (deviceUdiCarrierComponentOne != null && deviceUdiCarrierComponentTwo != null) {
				// deviceIdentifier
				if (deviceUdiCarrierComponentOne.hasDeviceIdentifier()
						&& deviceUdiCarrierComponentTwo.hasDeviceIdentifier()) {
					compareString(fieldName + ".deviceIdentifier", deviceUdiCarrierComponentOne.getDeviceIdentifier(),
							deviceUdiCarrierComponentTwo.getDeviceIdentifier(), operationOutcome, scenarioName);
				} else if (deviceUdiCarrierComponentOne.hasDeviceIdentifier()
						&& !deviceUdiCarrierComponentTwo.hasDeviceIdentifier()) {
					String errorMessage = "The scenario does not require " + fieldName + ".deviceIdentifier"
							+ " but submitted file does have " + fieldName + ".deviceIdentifier";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							fieldName + ".deviceIdentifier" + "", errorMessage));
				} else if (!deviceUdiCarrierComponentOne.hasDeviceIdentifier()
						&& deviceUdiCarrierComponentTwo.hasDeviceIdentifier()) {
					String errorMessage = "The scenario requires " + fieldName + ".deviceIdentifier"
							+ " but submitted file does not have " + fieldName + ".deviceIdentifier";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							fieldName + ".deviceIdentifier" + "", errorMessage));
				} else {
					logger.info(fieldName
							+ ".deviceIdentifier information is null OR equal in both inputJson and goldJson");
				}

				// carrierAIDC
				if (deviceUdiCarrierComponentOne.hasCarrierAIDC() && deviceUdiCarrierComponentTwo.hasCarrierAIDC()) {
//					compareByte(fieldName + ".carrierAIDC", deviceUdiCarrierComponentOne.getCarrierAIDC(),
//							deviceUdiCarrierComponentTwo.getCarrierAIDC(), operationOutcome);
				} else if (deviceUdiCarrierComponentOne.hasCarrierAIDC()
						&& !deviceUdiCarrierComponentTwo.hasCarrierAIDC()) {
					String errorMessage = "The scenario does not require " + fieldName + ".carrierAIDC"
							+ " but submitted file does have " + fieldName + ".carrierAIDC";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".carrierAIDC", errorMessage));
				} else if (!deviceUdiCarrierComponentOne.hasCarrierAIDC()
						&& deviceUdiCarrierComponentTwo.hasCarrierAIDC()) {
					String errorMessage = "The scenario requires " + fieldName + ".carrierAIDC"
							+ " but submitted file does not have " + fieldName + ".carrierAIDC";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".carrierAIDC", errorMessage));
				} else {
					logger.info(fieldName + ".carrierAIDC information is null OR equal in both inputJson and goldJson");
				}

				// issuer
				if (scenarioName.equalsIgnoreCase(ScenarioConstants.ScenarioNameConstants.USCDI_FULL_DEVICE)) {
					if (deviceUdiCarrierComponentOne.hasIssuer() && deviceUdiCarrierComponentTwo.hasIssuer()) {
						compareString(fieldName + ".issuer", deviceUdiCarrierComponentOne.getIssuer(),
								deviceUdiCarrierComponentTwo.getIssuer(), operationOutcome, scenarioName);
					} else if (deviceUdiCarrierComponentOne.hasIssuer() && !deviceUdiCarrierComponentTwo.hasIssuer()) {
						String errorMessage = "The scenario does not require " + fieldName + ".issuer"
								+ " but submitted file does have " + fieldName + ".issuer";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".issuer", errorMessage));
					} else if (!deviceUdiCarrierComponentOne.hasIssuer() && deviceUdiCarrierComponentTwo.hasIssuer()) {
						String errorMessage = "The scenario requires " + fieldName + ".issuer"
								+ " but submitted file does not have " + fieldName + ".issuer";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".issuer", errorMessage));
					} else {
						logger.info(fieldName + ".issuer information is null OR equal in both inputJson and goldJson");
					}
				}
				// jurisdiction
				if (scenarioName.equalsIgnoreCase(ScenarioConstants.ScenarioNameConstants.USCDI_FULL_DEVICE)) {
					if (deviceUdiCarrierComponentOne.hasJurisdiction()
							&& deviceUdiCarrierComponentTwo.hasJurisdiction()) {
						compareString(fieldName + ".jurisdiction", deviceUdiCarrierComponentOne.getJurisdiction(),
								deviceUdiCarrierComponentTwo.getJurisdiction(), operationOutcome, scenarioName);
					} else if (deviceUdiCarrierComponentOne.hasJurisdiction()
							&& !deviceUdiCarrierComponentTwo.hasJurisdiction()) {
						String errorMessage = "The scenario does not require " + fieldName + ".jurisdiction"
								+ " but submitted file does have " + fieldName + ".jurisdiction";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".jurisdiction", errorMessage));
					} else if (!deviceUdiCarrierComponentOne.hasJurisdiction()
							&& deviceUdiCarrierComponentTwo.hasJurisdiction()) {
						String errorMessage = "The scenario requires " + fieldName + ".jurisdiction"
								+ " but submitted file does not have " + fieldName + ".jurisdiction";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".jurisdiction", errorMessage));
					} else {
						logger.info(fieldName
								+ ".jurisdiction information is null OR equal in both inputJson and goldJson");
					}
				}

				// carrierHRF
				if (deviceUdiCarrierComponentOne.hasCarrierHRF() && deviceUdiCarrierComponentTwo.hasCarrierHRF()) {
					compareString(fieldName + ".carrierHRF", deviceUdiCarrierComponentOne.getCarrierHRF(),
							deviceUdiCarrierComponentTwo.getCarrierHRF(), operationOutcome, scenarioName);
				} else if (deviceUdiCarrierComponentOne.hasCarrierHRF()
						&& !deviceUdiCarrierComponentTwo.hasCarrierHRF()) {
					String errorMessage = "The scenario does not require " + fieldName + ".carrierHRF"
							+ " but submitted file does have " + fieldName + ".carrierHRF";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".carrierHRF", errorMessage));
				} else if (!deviceUdiCarrierComponentOne.hasCarrierHRF()
						&& deviceUdiCarrierComponentTwo.hasCarrierHRF()) {
					String errorMessage = "The scenario requires " + fieldName + ".carrierHRF"
							+ " but submitted file does not have " + fieldName + ".carrierHRF";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".carrierHRF", errorMessage));
				} else {
					logger.info(fieldName + ".carrierHRF information is null OR equal in both inputJson and goldJson");
				}

				// entryType
				if (scenarioName.equalsIgnoreCase(ScenarioConstants.ScenarioNameConstants.USCDI_FULL_DEVICE)) {
					if (deviceUdiCarrierComponentOne.hasEntryType() && deviceUdiCarrierComponentTwo.hasEntryType()) {
						compareString(fieldName + ".entryType",
								deviceUdiCarrierComponentOne.getEntryType().getDisplay(),
								deviceUdiCarrierComponentTwo.getEntryType().getDisplay(), operationOutcome,
								scenarioName);
					} else if (deviceUdiCarrierComponentOne.hasEntryType()
							&& !deviceUdiCarrierComponentTwo.hasEntryType()) {
						String errorMessage = "The scenario does not require " + fieldName + ".entryType"
								+ " but submitted file does have " + fieldName + ".entryType";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".entryType", errorMessage));
					} else if (!deviceUdiCarrierComponentOne.hasEntryType()
							&& deviceUdiCarrierComponentTwo.hasEntryType()) {
						String errorMessage = "The scenario requires " + fieldName + ".entryType"
								+ " but submitted file does not have " + fieldName + ".entryType";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".entryType", errorMessage));
					} else {
						logger.info(
								fieldName + ".entryType information is null OR equal in both inputJson and goldJson");
					}
				}

			} else if (deviceUdiCarrierComponentOne != null && deviceUdiCarrierComponentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (deviceUdiCarrierComponentOne == null && deviceUdiCarrierComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + "", errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDeviceUdiCarrierComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

//	private static void compareByte(String fieldName, byte[] carrierAIDC, byte[] carrierAIDC2,
//			OperationOutcome operationOutcome) {
//		try {
//			if(carrierAIDC)
//			String errorMessage = fieldName +" Expected = " + carrierAIDC
//						+ " but, submitted file contains " + carrierAIDC2;
//				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE,
//						fieldName , errorMessage));
//			}
//		}catch(Exception ex) {
//			logger.error("\n Exception in compareByte of ComparatorUtils :: ", ex.getMessage());
//
//		}
//	}

	public static void compareListOfDeviceDeviceNameComponent(String fieldName,
			List<DeviceDeviceNameComponent> deviceNameListOne, List<DeviceDeviceNameComponent> deviceNameListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, deviceNameListOne, deviceNameListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < deviceNameListOne.size();) {
					for (int j = 0; j < deviceNameListTwo.size(); j++) {
						compareDeviceDeviceNameComponent(fieldName, deviceNameListOne.get(i), deviceNameListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(deviceNameListOne) && ObjectUtils.isEmpty(deviceNameListTwo)) {
				String errorMessage = "The scenario does not required " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(deviceNameListOne) && ObjectUtils.isNotEmpty(deviceNameListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfDeviceDeviceNameComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareListOfContactComponent(String contact, List<ContactComponent> contactone,
			List<ContactComponent> contacttwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(contact, contactone, contacttwo, operationOutcome, scenarioName)) {
				for (int i = 0; i < contactone.size();) {
					for (int j = 0; j < contacttwo.size(); j++) {
						compareContactComponent(contact, contactone.get(i), contacttwo.get(j), operationOutcome,
								scenarioName);

						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(contactone) && ObjectUtils.isEmpty(contacttwo)) {
				String errorMessage = "The scenario does not require Contact, but submitted file does have Contact";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, contact, errorMessage));
			} else if (ObjectUtils.isEmpty(contactone) && ObjectUtils.isNotEmpty(contacttwo)) {
				String errorMessage = "The scenario requires Contact, but submitted file does not have Contact";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, contact, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfContactComponent of ComparatorUtils :: ", ex.getMessage());
		}
	}

	private static void compareContactComponent(String contact, ContactComponent contactComponentone,
			ContactComponent contactComponenttwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (ObjectUtils.isNotEmpty(contactComponentone) && ObjectUtils.isNotEmpty(contactComponenttwo)) {

				// relationship
				if (contactComponentone.hasRelationship() && contactComponenttwo.hasRelationship()) {
					compareListOfCodeableConcept(contact + ".relationship", contactComponentone.getRelationship(),
							contactComponenttwo.getRelationship(), operationOutcome, scenarioName);
				} else if (contactComponentone.hasRelationship() && !contactComponenttwo.hasRelationship()) {
					String errorMessage = "The scenario does not require " + contact
							+ ".relationship but submitted file does have .relationship";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, contact + ".relationship", errorMessage));
				} else if (!contactComponentone.hasRelationship() && contactComponenttwo.hasRelationship()) {
					String errorMessage = "The scenario requires " + contact
							+ ".relationship but submitted file does not have .relationship";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, contact + ".relationship", errorMessage));
				} else {
					logger.info(contact + ".relationship information is null OR equal in both inputJson and goldJson");
				}

				// name
				if (contactComponentone.hasName() && contactComponenttwo.hasName()) {
					compareHumanName(contact + ".name", contactComponentone.getName(), contactComponenttwo.getName(),
							operationOutcome, scenarioName);
				} else if (contactComponentone.hasName() && !contactComponenttwo.hasName()) {
					String errorMessage = "The scenario does not require " + contact
							+ ".name but submitted file does have .name";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, contact + ".name", errorMessage));
				} else if (!contactComponentone.hasName() && contactComponenttwo.hasName()) {
					String errorMessage = "The scenario requires " + contact
							+ ".name but submitted file does not have .name";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, contact + ".name", errorMessage));
				} else {
					logger.info(contact + ".name information is null OR equal in both inputJson and goldJson");
				}

				// telecom
				if (contactComponentone.hasTelecom() && contactComponenttwo.hasTelecom()) {
					compareListOfContactPoint(contact + ".telecom", contactComponentone.getTelecom(),
							contactComponenttwo.getTelecom(), operationOutcome, scenarioName);
				} else if (contactComponentone.hasTelecom() && !contactComponenttwo.hasTelecom()) {
					String errorMessage = "The scenario does not require " + contact
							+ ".telecom but submitted file does have .telecom";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, contact + ".telecom", errorMessage));
				} else if (!contactComponentone.hasTelecom() && contactComponenttwo.hasTelecom()) {
					String errorMessage = "The scenario requires " + contact
							+ ".telecom but submitted file does not have .telecom";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, contact + ".telecom", errorMessage));
				} else {
					logger.info(contact + ".telecom information is null OR equal in both inputJson and goldJson");
				}

				// address
				if (contactComponentone.hasAddress() && contactComponenttwo.hasAddress()) {
					compareAddress(contact + ".address", contactComponentone.getAddress(),
							contactComponenttwo.getAddress(), operationOutcome, scenarioName);
				} else if (contactComponentone.hasAddress() && !contactComponenttwo.hasAddress()) {
					String errorMessage = "The scenario does not require " + contact
							+ ".address but submitted file does have .address";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, contact + ".address", errorMessage));
				} else if (!contactComponentone.hasAddress() && contactComponenttwo.hasAddress()) {
					String errorMessage = "The scenario requires " + contact
							+ ".address but submitted file does not have .address";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, contact + ".address", errorMessage));
				} else {
					logger.info(contact + ".address information is null OR equal in both inputJson and goldJson");
				}

				// gender
				if (contactComponentone.hasGender() && contactComponenttwo.hasGender()) {
					compareString(contact + ".gender", contactComponentone.getGender().getDisplay(),
							contactComponenttwo.getGender().getDisplay(), operationOutcome, scenarioName);
				} else if (contactComponentone.hasGender() && !contactComponenttwo.hasGender()) {
					String errorMessage = "The scenario does not require " + contact
							+ ".gender but submitted file does have .gender";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, contact + ".gender", errorMessage));
				} else if (!contactComponentone.hasGender() && contactComponenttwo.hasGender()) {
					String errorMessage = "The scenario requires " + contact
							+ ".gender but submitted file does not have .gender";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, contact + ".gender", errorMessage));
				} else {
					logger.info(contact + ".gender information is null OR equal in both inputJson and goldJson");
				}

				// organization
				if (contactComponentone.hasOrganization() && contactComponenttwo.hasOrganization()) {
					compareReference(contact + ".organization", contactComponentone.getOrganization(),
							contactComponenttwo.getOrganization(), operationOutcome, scenarioName);
				} else if (contactComponentone.hasOrganization() && !contactComponenttwo.hasOrganization()) {
					String errorMessage = "The scenario does not require " + contact
							+ ".organization but submitted file does have .organization";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, contact + ".organization", errorMessage));
				} else if (!contactComponentone.hasOrganization() && contactComponenttwo.hasOrganization()) {
					String errorMessage = "The scenario requires " + contact
							+ ".organization but submitted file does not have .organization";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, contact + ".organization", errorMessage));
				} else {
					logger.info(contact + ".organization information is null OR equal in both inputJson and goldJson");
				}

				// period
				if (contactComponentone.hasPeriod() && contactComponenttwo.hasPeriod()) {
					comparePeriod(contact + ".period", contactComponentone.getPeriod(), contactComponenttwo.getPeriod(),
							operationOutcome, scenarioName);
				} else if (contactComponentone.hasPeriod() && !contactComponenttwo.hasPeriod()) {
					String errorMessage = "The scenario does not require " + contact
							+ ".period but submitted file does have .period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, contact + ".period", errorMessage));
				} else if (!contactComponentone.hasPeriod() && contactComponenttwo.hasPeriod()) {
					String errorMessage = "The scenario requires " + contact
							+ ".period but submitted file does not have .period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, contact + ".period", errorMessage));
				} else {
					logger.info(contact + ".period information is null OR equal in both inputJson and goldJson");
				}
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareContactComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareDeviceDeviceNameComponent(String fieldName,
			DeviceDeviceNameComponent deviceDeviceNameComponentOne,
			DeviceDeviceNameComponent deviceDeviceNameComponentTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (deviceDeviceNameComponentOne != null && deviceDeviceNameComponentTwo != null) {

				// name
				if (deviceDeviceNameComponentOne.hasName() && deviceDeviceNameComponentTwo.hasName()) {
					compareString(fieldName + ".name", deviceDeviceNameComponentOne.getName(),
							deviceDeviceNameComponentTwo.getName(), operationOutcome, scenarioName);
				} else if (deviceDeviceNameComponentOne.hasName() && !deviceDeviceNameComponentTwo.hasName()) {
					String errorMessage = "The scenario does not require " + fieldName + ".name"
							+ " but submitted file does have " + fieldName + ".name";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".name", errorMessage));
				} else if (!deviceDeviceNameComponentOne.hasName() && deviceDeviceNameComponentTwo.hasName()) {
					String errorMessage = "The scenario requires " + fieldName + ".name"
							+ " but submitted file does not have" + fieldName + ".name";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".name", errorMessage));
				} else {
					logger.info(fieldName + ".name information is null OR equal in both inputJson and goldJson");
				}

				// type
				if (deviceDeviceNameComponentOne.hasType() && deviceDeviceNameComponentTwo.hasType()) {
					compareString(fieldName + ".type", deviceDeviceNameComponentOne.getType().getDisplay(),
							deviceDeviceNameComponentTwo.getType().getDisplay(), operationOutcome, scenarioName);
				} else if (deviceDeviceNameComponentOne.hasType() && !deviceDeviceNameComponentTwo.hasType()) {
					String errorMessage = "The scenario does not require " + fieldName + ".type"
							+ " but submitted file does have " + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else if (!deviceDeviceNameComponentOne.hasType() && deviceDeviceNameComponentTwo.hasType()) {
					String errorMessage = "The scenario requires " + fieldName + ".type"
							+ " but submitted file does not have" + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else {
					logger.info(fieldName + ".type information is null OR equal in both inputJson and goldJson");
				}

			} else if (deviceDeviceNameComponentOne != null && deviceDeviceNameComponentTwo == null) {
				String errorMessage = "The scenario does not required " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (deviceDeviceNameComponentOne == null && deviceDeviceNameComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + "", errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDeviceDeviceNameComponent of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareListOfDeviceSpecializationComponent(String fieldName,
			List<DeviceSpecializationComponent> specializationListOne,
			List<DeviceSpecializationComponent> specializationListTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, specializationListOne, specializationListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < specializationListOne.size();) {
					for (int j = 0; j < specializationListTwo.size(); j++) {
						compareDeviceSpecializationComponent(fieldName, specializationListOne.get(i),
								specializationListTwo.get(j), operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(specializationListOne) && ObjectUtils.isEmpty(specializationListTwo)) {
				String errorMessage = "The scenario does not required " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(specializationListOne) && ObjectUtils.isNotEmpty(specializationListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not haveOR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfDeviceDeviceNameComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	private static void compareDeviceSpecializationComponent(String fieldName,
			DeviceSpecializationComponent deviceSpecializationComponentOne,
			DeviceSpecializationComponent deviceSpecializationComponentTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (deviceSpecializationComponentOne != null && deviceSpecializationComponentTwo != null) {

				// systemType
				if (deviceSpecializationComponentOne.hasSystemType()
						&& deviceSpecializationComponentTwo.hasSystemType()) {
					compareCodeableConcept(fieldName + ".systemType", deviceSpecializationComponentOne.getSystemType(),
							deviceSpecializationComponentTwo.getSystemType(), operationOutcome, scenarioName);
				} else if (deviceSpecializationComponentOne.hasSystemType()
						&& !deviceSpecializationComponentTwo.hasSystemType()) {
					String errorMessage = "The scenario does not require " + fieldName + ".systemType"
							+ " but submitted file does have " + fieldName + ".systemType";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".systemType", errorMessage));
				} else if (!deviceSpecializationComponentOne.hasSystemType()
						&& deviceSpecializationComponentTwo.hasSystemType()) {
					String errorMessage = "The scenario requires " + fieldName + ".systemType"
							+ " but submitted file does not have" + fieldName + ".systemType";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".systemType", errorMessage));
				} else {
					logger.info(fieldName + ".systemType information is null OR equal in both inputJson and goldJson");
				}

				// version
				if (deviceSpecializationComponentOne.hasVersion() && deviceSpecializationComponentTwo.hasVersion()) {
					compareString(fieldName + ".version", deviceSpecializationComponentOne.getVersion(),
							deviceSpecializationComponentTwo.getVersion(), operationOutcome, scenarioName);
				} else if (deviceSpecializationComponentOne.hasVersion()
						&& !deviceSpecializationComponentTwo.hasVersion()) {
					String errorMessage = "The scenario does not require " + fieldName + ".version"
							+ " but submitted file does have " + fieldName + ".version";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".version", errorMessage));
				} else if (!deviceSpecializationComponentOne.hasVersion()
						&& deviceSpecializationComponentTwo.hasVersion()) {
					String errorMessage = "The scenario requires " + fieldName + ".version"
							+ " but submitted file does not have" + fieldName + ".version";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".version", errorMessage));
				} else {
					logger.info(fieldName + ".version information is null OR equal in both inputJson and goldJson");
				}

			} else if (deviceSpecializationComponentOne != null && deviceSpecializationComponentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (deviceSpecializationComponentOne == null && deviceSpecializationComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDeviceSpecializationComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	public static void compareListOfDeviceVersionComponent(String fieldName,
			List<DeviceVersionComponent> versionListOne, List<DeviceVersionComponent> versionListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, versionListOne, versionListTwo, operationOutcome, scenarioName)) {
				for (int i = 0; i < versionListOne.size();) {
					for (int j = 0; j < versionListTwo.size(); j++) {
						compareDeviceVersionComponent(fieldName, versionListOne.get(i), versionListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(versionListOne) && ObjectUtils.isEmpty(versionListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(versionListOne) && ObjectUtils.isNotEmpty(versionListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfDeviceVersionComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareDeviceVersionComponent(String fieldName,
			DeviceVersionComponent deviceVersionComponentOne, DeviceVersionComponent deviceVersionComponentTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (deviceVersionComponentOne != null && deviceVersionComponentTwo != null) {

				// type
				if (deviceVersionComponentOne.hasType() && deviceVersionComponentTwo.hasType()) {
					compareCodeableConcept(fieldName + ".type", deviceVersionComponentOne.getType(),
							deviceVersionComponentTwo.getType(), operationOutcome, scenarioName);
				} else if (deviceVersionComponentOne.hasType() && !deviceVersionComponentTwo.hasType()) {
					String errorMessage = "The scenario does not require " + fieldName + ".type"
							+ " but submitted file does have " + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else if (!deviceVersionComponentOne.hasType() && deviceVersionComponentTwo.hasType()) {
					String errorMessage = "The scenario requires " + fieldName + ".type"
							+ " but submitted file does not have " + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else {
					logger.info(fieldName + ".type information is null OR equal in both inputJson and goldJson");
				}

				// component
				logger.info(deviceVersionComponentOne.hasComponent() + "op");
				logger.info(deviceVersionComponentTwo.hasComponent() + "op");
				if (deviceVersionComponentOne.hasComponent() && deviceVersionComponentTwo.hasComponent()) {
					compareIdentifier(fieldName + ".component", deviceVersionComponentOne.getComponent(),
							deviceVersionComponentTwo.getComponent(), operationOutcome, scenarioName);
				} else if (deviceVersionComponentOne.hasComponent() && !deviceVersionComponentTwo.hasComponent()) {
					String errorMessage = "The scenario does not require " + fieldName + ".component"
							+ " but submitted file does have " + fieldName + ".component";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".component", errorMessage));
				} else if (!deviceVersionComponentOne.hasComponent() && deviceVersionComponentTwo.hasComponent()) {
					String errorMessage = "The scenario requires " + fieldName + ".component"
							+ " but submitted file does not have " + fieldName + ".component";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".component", errorMessage));
				} else {
					logger.info(fieldName + ".component information is null OR equal in both inputJson and goldJson");
				}
				// value
				if (deviceVersionComponentOne.hasValue() && deviceVersionComponentTwo.hasValue()) {
					compareString(fieldName + ". value", deviceVersionComponentOne.getValue(),
							deviceVersionComponentTwo.getValue(), operationOutcome, scenarioName);
				} else if (deviceVersionComponentOne.hasValue() && !deviceVersionComponentTwo.hasValue()) {
					String errorMessage = "The scenario does not require " + fieldName + ".value"
							+ " but submitted file does have " + fieldName + ". value";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". value", errorMessage));
				} else if (!deviceVersionComponentOne.hasValue() && deviceVersionComponentTwo.hasValue()) {
					String errorMessage = "The scenario requires " + fieldName + ". value"
							+ " but submitted file does not have " + fieldName + ". value";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".value", errorMessage));
				} else {
					logger.info(fieldName + ". value information is null OR equal in both inputJson and goldJson");
				}

			} else if (deviceVersionComponentOne != null && deviceVersionComponentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (deviceVersionComponentOne == null && deviceVersionComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDeviceVersionComponent of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareListOfDevicePropertyComponent(String fieldName,
			List<DevicePropertyComponent> propertyListOne, List<DevicePropertyComponent> propertyListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, propertyListOne, propertyListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < propertyListOne.size();) {
					for (int j = 0; j < propertyListTwo.size(); j++) {
						compareDevicePropertyComponent(fieldName, propertyListOne.get(i), propertyListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(propertyListOne) && ObjectUtils.isEmpty(propertyListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(propertyListOne) && ObjectUtils.isNotEmpty(propertyListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfDeviceVersionComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareDevicePropertyComponent(String fieldName,
			DevicePropertyComponent devicePropertyComponentOne, DevicePropertyComponent devicePropertyComponentTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (devicePropertyComponentOne != null && devicePropertyComponentTwo != null) {

				// type
				if (devicePropertyComponentOne.hasType() && devicePropertyComponentTwo.hasType()) {
					compareCodeableConcept(fieldName + ".type ", devicePropertyComponentOne.getType(),
							devicePropertyComponentTwo.getType(), operationOutcome, scenarioName);
				} else if (devicePropertyComponentOne.hasType() && !devicePropertyComponentTwo.hasType()) {
					String errorMessage = "The scenario does not require " + fieldName + ".type "
							+ " but submitted file does have " + fieldName + ".type ";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type ", errorMessage));
				} else if (!devicePropertyComponentOne.hasType() && devicePropertyComponentTwo.hasType()) {
					String errorMessage = "The scenario requires " + fieldName + ".type "
							+ " but submitted file does not have" + fieldName + ".type ";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type ", errorMessage));
				} else {
					logger.info(fieldName + ".type information is null OR equal in both inputJson and goldJson");
				}

				// valueQuantity
				if (devicePropertyComponentOne.hasValueQuantity() && devicePropertyComponentTwo.hasValueQuantity()) {
					compareListOfQuantity(fieldName + ".valueQuantity", devicePropertyComponentOne.getValueQuantity(),
							devicePropertyComponentTwo.getValueQuantity(), operationOutcome, scenarioName);
				} else if (devicePropertyComponentOne.hasValueQuantity()
						&& !devicePropertyComponentTwo.hasValueQuantity()) {
					String errorMessage = "The scenario does not require " + fieldName + " .valueQuantity"
							+ " but submitted file does have " + fieldName + ".valueQuantity";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".valueQuantity", errorMessage));
				} else if (!devicePropertyComponentOne.hasValueQuantity()
						&& devicePropertyComponentTwo.hasValueQuantity()) {
					String errorMessage = "The scenario requires " + fieldName + ".valueQuantity"
							+ " but submitted file does not have " + fieldName + ".valueQuantity";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".valueQuantity", errorMessage));
				} else {
					logger.info(
							fieldName + ".valueQuantity information is null OR equal in both inputJson and goldJson");
				}

				// valueCode
				if (devicePropertyComponentOne.hasValueCode() && devicePropertyComponentTwo.hasValueCode()) {
					compareListOfCodeableConcept(fieldName + ".valueCode", devicePropertyComponentOne.getValueCode(),
							devicePropertyComponentTwo.getValueCode(), operationOutcome, scenarioName);
				} else if (devicePropertyComponentOne.hasValueCode() && !devicePropertyComponentTwo.hasValueCode()) {
					String errorMessage = "The scenario does not require " + fieldName + ".valueCode"
							+ " but submitted file does have " + fieldName + ".valueCode";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".valueCode", errorMessage));
				} else if (!devicePropertyComponentOne.hasValueCode() && devicePropertyComponentTwo.hasValueCode()) {
					String errorMessage = "The scenario requires " + fieldName + ".valueCode"
							+ " but submitted file does not have" + fieldName + ".valueCode";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".valueCode", errorMessage));
				} else {
					logger.info(fieldName + ".valueCode information is null OR equal in both inputJson and goldJson");
				}

			} else if (devicePropertyComponentOne != null && devicePropertyComponentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + "", errorMessage));
			} else if (devicePropertyComponentOne == null && devicePropertyComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + "", errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDevicePropertyComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareListOfQuantity(String fieldName, List<Quantity> valueQuantityListOne,
			List<Quantity> valueQuantityListTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, valueQuantityListOne, valueQuantityListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < valueQuantityListOne.size();) {
					for (int j = 0; j < valueQuantityListTwo.size(); j++) {
						compareQuantityComponent(fieldName, valueQuantityListOne.get(i), valueQuantityListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(valueQuantityListOne) && ObjectUtils.isEmpty(valueQuantityListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(valueQuantityListOne) && ObjectUtils.isNotEmpty(valueQuantityListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfQuantity of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareQuantityComponent(String fieldName, Quantity quantityOne, Quantity quantityTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (quantityOne != null && quantityTwo != null) {

				// value
				if (quantityOne.hasValue() && quantityTwo.hasValue()) {
					compareInt(fieldName + ".value", quantityOne.getValue().intValue(),
							quantityTwo.getValue().intValue(), operationOutcome, scenarioName);
				} else if (quantityOne.hasValue() && !quantityTwo.hasValue()) {
					String errorMessage = "The scenario does not require " + fieldName + ".value"
							+ " but submitted file does have " + fieldName + ".value";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".value", errorMessage));
				} else if (!quantityOne.hasValue() && quantityTwo.hasValue()) {
					String errorMessage = "The scenario requires " + fieldName + ".value"
							+ " but submitted file does not have " + fieldName + ".value";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".value", errorMessage));
				} else {
					logger.info(fieldName + ".value information is null OR equal in both inputJson and goldJson");
				}

				// comparator
				if (quantityOne.hasComparator() && quantityTwo.hasComparator()) {
					compareString(fieldName + ".comparator", quantityOne.getComparator().getDisplay(),
							quantityTwo.getComparator().getDisplay(), operationOutcome, scenarioName);
				} else if (quantityOne.hasComparator() && !quantityTwo.hasComparator()) {
					String errorMessage = "The scenario does not require " + fieldName + "comparator"
							+ " but submitted file does have " + fieldName + ".comparator";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".comparator", errorMessage));
				} else if (!quantityOne.hasComparator() && quantityTwo.hasComparator()) {
					String errorMessage = "The scenario requires " + fieldName + ".comparator"
							+ " but submitted file does not have " + fieldName + ".comparator";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".comparator", errorMessage));
				} else {
					logger.info(fieldName + ".comparator information is null OR equal in both inputJson and goldJson");
				}
				// unit
				if (quantityOne.hasUnit() && quantityTwo.hasUnit()) {
					compareString(fieldName + ". unit", quantityOne.getUnit(), quantityTwo.getUnit(), operationOutcome,
							scenarioName);
				} else if (quantityOne.hasUnit() && !quantityTwo.hasUnit()) {
					String errorMessage = "The scenario does not require " + fieldName + " unit"
							+ " but submitted file does have " + fieldName + ". unit";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". unit", errorMessage));
				} else if (!quantityOne.hasUnit() && quantityTwo.hasUnit()) {
					String errorMessage = "The scenario requires " + fieldName + ". unit"
							+ " but submitted file does not have " + fieldName + ". unit";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". unit", errorMessage));
				} else {
					logger.info(fieldName + ". unit information is null OR equal in both inputJson and goldJson");
				}
				// system
				if (quantityOne.hasSystem() && quantityTwo.hasSystem()) {
					compareString(fieldName + ". system", quantityOne.getSystem(), quantityTwo.getSystem(),
							operationOutcome, scenarioName);
				} else if (quantityOne.hasSystem() && !quantityTwo.hasSystem()) {
					String errorMessage = "The scenario does not require " + fieldName + " system"
							+ " but submitted file does have " + fieldName + ". system";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". system", errorMessage));
				} else if (!quantityOne.hasSystem() && quantityTwo.hasSystem()) {
					String errorMessage = "The scenario requires " + fieldName + ". system"
							+ " but submitted file does not have " + fieldName + ". system";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". system", errorMessage));
				} else {
					logger.info(fieldName + ". system information is null OR equal in both inputJson and goldJson");
				}

				// code
				if (quantityOne.hasCode() && quantityTwo.hasCode()) {
					compareString(fieldName + ". code", quantityOne.getCode(), quantityTwo.getCode(), operationOutcome,
							scenarioName);
				} else if (quantityOne.hasCode() && !quantityTwo.hasCode()) {
					String errorMessage = "The scenario does not require " + fieldName + ".code"
							+ " but submitted file does have " + fieldName + ".code";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".code" + "", errorMessage));
				} else if (!quantityOne.hasCode() && quantityTwo.hasCode()) {
					String errorMessage = "The scenario requires " + fieldName + ". code"
							+ " but submitted file does not have " + fieldName + ". code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". code", errorMessage));
				} else {
					logger.info(fieldName + ". code information is null OR equal in both inputJson and goldJson");
				}

			} else if (quantityOne != null && quantityTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (quantityOne == null && quantityTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareQuantityComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareInt(String fieldName, Integer intValueOne, Integer intValueTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (!intValueOne.equals(intValueTwo)) {
				String errorMessage = fieldName + " Expected = " + intValueTwo + " but, submitted file contains "
						+ intValueOne;
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareInt of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareListOfDosage(String fieldName, List<Dosage> dosageInstructionOne,
			List<Dosage> dosageInstructionTwo, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, dosageInstructionOne, dosageInstructionTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < dosageInstructionOne.size();) {
					for (int j = 0; j < dosageInstructionTwo.size(); j++) {
						compareDosage(fieldName, dosageInstructionOne.get(i), dosageInstructionTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(dosageInstructionOne) && ObjectUtils.isEmpty(dosageInstructionTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(dosageInstructionOne) && ObjectUtils.isNotEmpty(dosageInstructionTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfQuantity of ComparatorUtils :: ", ex.getMessage());
		}
	}

	private static void compareDosage(String fieldName, Dosage dosageOne, Dosage dosageTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (dosageOne != null && dosageTwo != null) {

				// text
				if (dosageOne.hasText() && dosageTwo.hasText()) {
					compareString(fieldName + ".text", dosageOne.getText(), dosageTwo.getText(), operationOutcome,
							scenarioName);
				} else if (dosageOne.hasText() && !dosageTwo.hasText()) {
					String errorMessage = "The scenario does not require" + fieldName + ".text"
							+ " but submitted file does have " + fieldName + ".text";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
				} else if (!dosageOne.hasText() && dosageTwo.hasText()) {
					String errorMessage = "The scenario requires " + fieldName + ".text"
							+ " but submitted file does not have " + fieldName + ".text";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
				} else {
					logger.info(fieldName + ".text information is null OR equal in both inputJson and goldJson");
				}

				// additionalInstruction
				if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_MEDICATION_REQUEST)) {
					if (dosageOne.hasAdditionalInstruction() && dosageTwo.hasAdditionalInstruction()) {
						compareListOfCodeableConcept(fieldName + ".additionalInstruction",
								dosageOne.getAdditionalInstruction(), dosageTwo.getAdditionalInstruction(),
								operationOutcome, scenarioName);
					} else if (dosageOne.hasAdditionalInstruction() && !dosageTwo.hasAdditionalInstruction()) {
						String errorMessage = "The scenario does not require " + fieldName + ".additionalInstruction"
								+ " but submitted file does have " + fieldName + ".additionalInstruction";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".additionalInstruction", errorMessage));
					} else if (!dosageOne.hasAdditionalInstruction() && dosageTwo.hasAdditionalInstruction()) {
						String errorMessage = "The scenario requires " + fieldName + ".additionalInstruction"
								+ " but submitted file does not have " + fieldName + ".additionalInstruction";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else {
						logger.info(fieldName
								+ ".additionalInstruction information is null OR equal in both inputJson and goldJson");
					}
					// sequence
					if (dosageOne.hasSequence() && dosageTwo.hasSequence()) {
						compareInt(fieldName + ".sequence", dosageOne.getSequence(), dosageTwo.getSequence(),
								operationOutcome, scenarioName);
					} else if (dosageOne.hasSequence() && !dosageTwo.hasSequence()) {
						String errorMessage = "The scenario does not require " + fieldName + ".sequence"
								+ " but submitted file does have " + fieldName + ".sequence";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".sequence", errorMessage));
					} else if (!dosageOne.hasSequence() && dosageTwo.hasSequence()) {
						String errorMessage = "The scenario requires " + fieldName + ".sequence"
								+ " but submitted file does not have " + fieldName + ".sequence";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".sequence", errorMessage));
					} else {
						logger.info(
								fieldName + ".sequence information is null OR equal in both inputJson and goldJson");
					}
					// patientInstruction
					if (dosageOne.hasPatientInstruction() && dosageTwo.hasPatientInstruction()) {
						compareString(fieldName + ".additionalInstruction", dosageOne.getPatientInstruction(),
								dosageTwo.getPatientInstruction(), operationOutcome, scenarioName);
					} else if (dosageOne.hasPatientInstruction() && !dosageTwo.hasPatientInstruction()) {
						String errorMessage = "The scenario does not require " + fieldName + ".patientInstruction"
								+ " but submitted file does have " + fieldName + ".patientInstruction";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".patientInstruction", errorMessage));
					} else if (!dosageOne.hasPatientInstruction() && dosageTwo.hasPatientInstruction()) {
						String errorMessage = "The scenario requires " + fieldName + ".patientInstruction"
								+ " but submitted file does not have " + fieldName + ".patientInstruction";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else {
						logger.info(fieldName
								+ ".patientInstruction information is null OR equal in both inputJson and goldJson");
					}

					// timing
					if (dosageOne.hasTiming() && dosageTwo.hasTiming()) {
						compareCodeableConcept(fieldName + ".timing", dosageOne.getTiming().getCode(),
								dosageTwo.getTiming().getCode(), operationOutcome, scenarioName);
					} else if (dosageOne.hasTiming() && !dosageTwo.hasTiming()) {
						String errorMessage = "The scenario does not require " + fieldName + ".timing"
								+ " but submitted file does have " + fieldName + ".timing";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".timing", errorMessage));
					} else if (!dosageOne.hasTiming() && dosageTwo.hasTiming()) {
						String errorMessage = "The scenario requires " + fieldName + ".timing"
								+ " but submitted file does not have " + fieldName + ".timing";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".text", errorMessage));
					} else {
						logger.info(fieldName + ".timing information is null OR equal in both inputJson and goldJson");
					}

					// site
					if (dosageOne.hasSite() && dosageTwo.hasSite()) {
						compareCodeableConcept(fieldName + ".site", dosageOne.getSite(), dosageTwo.getSite(),
								operationOutcome, scenarioName);
					} else if (dosageOne.hasSite() && !dosageTwo.hasSite()) {
						String errorMessage = "The scenario does not require " + fieldName + ".site"
								+ " but submitted file does have " + fieldName + ".site";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".site", errorMessage));
					} else if (!dosageOne.hasSite() && dosageTwo.hasSite()) {
						String errorMessage = "The scenario requires " + fieldName + ".site"
								+ " but submitted file does not have " + fieldName + ".site";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".site", errorMessage));
					} else {
						logger.info(fieldName + ".site information is null OR equal in both inputJson and goldJson");
					}

					// route
					if (dosageOne.hasRoute() && dosageTwo.hasRoute()) {
						compareCodeableConcept(fieldName + ".route", dosageOne.getRoute(), dosageTwo.getRoute(),
								operationOutcome, scenarioName);
					} else if (dosageOne.hasRoute() && !dosageTwo.hasRoute()) {
						String errorMessage = "The scenario does not require " + fieldName + ".route"
								+ " but submitted file does have " + fieldName + ".site";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".route", errorMessage));
					} else if (!dosageOne.hasRoute() && dosageTwo.hasRoute()) {
						String errorMessage = "The scenario requires " + fieldName + ".route"
								+ " but submitted file does not have " + fieldName + ".route";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".route", errorMessage));
					} else {
						logger.info(fieldName + ".route information is null OR equal in both inputJson and goldJson");
					}

					// method
					if (dosageOne.hasMethod() && dosageTwo.hasMethod()) {
						compareCodeableConcept(fieldName + ".method", dosageOne.getMethod(), dosageTwo.getMethod(),
								operationOutcome, scenarioName);
					} else if (dosageOne.hasMethod() && !dosageTwo.hasMethod()) {
						String errorMessage = "The scenario does not require " + fieldName + ".method"
								+ " but submitted file does have " + fieldName + ".method";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".method", errorMessage));
					} else if (!dosageOne.hasMethod() && dosageTwo.hasMethod()) {
						String errorMessage = "The scenario requires " + fieldName + ".method"
								+ " but submitted file does not have " + fieldName + ".method";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".method", errorMessage));
					} else {
						logger.info(fieldName + ".method information is null OR equal in both inputJson and goldJson");
					}

					// maxDosePerAdministration
					if (dosageOne.hasMaxDosePerAdministration() && dosageTwo.hasMaxDosePerAdministration()) {
						compareQuantity(fieldName + ".maxDosePerAdministration",
								dosageOne.getMaxDosePerAdministration(), dosageTwo.getMaxDosePerAdministration(),
								operationOutcome, scenarioName);
					} else if (dosageOne.hasMaxDosePerAdministration() && !dosageTwo.hasMaxDosePerAdministration()) {
						String errorMessage = "The scenario does not require " + fieldName + ".maxDosePerAdministration"
								+ " but submitted file does have " + fieldName + ".maxDosePerAdministration";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".maxDosePerAdministration", errorMessage));
					} else if (!dosageOne.hasMaxDosePerAdministration() && dosageTwo.hasMaxDosePerAdministration()) {
						String errorMessage = "The scenario requires " + fieldName + ".maxDosePerAdministration"
								+ " but submitted file does not have " + fieldName + ".maxDosePerAdministration";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".maxDosePerAdministration", errorMessage));
					} else {
						logger.info(fieldName
								+ ".maxDosePerAdministration information is null OR equal in both inputJson and goldJson");
					}

					// maxDosePerLifetime
					if (dosageOne.hasMaxDosePerLifetime() && dosageTwo.hasMaxDosePerLifetime()) {
						compareQuantity(fieldName + ".maxDosePerLifetime", dosageOne.getMaxDosePerLifetime(),
								dosageTwo.getMaxDosePerLifetime(), operationOutcome, scenarioName);
					} else if (dosageOne.hasMaxDosePerLifetime() && !dosageTwo.hasMaxDosePerLifetime()) {
						String errorMessage = "The scenario does not require " + fieldName + ".maxDosePerLifetime"
								+ " but submitted file does have " + fieldName + ".maxDosePerLifetime";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".maxDosePerLifetime", errorMessage));
					} else if (!dosageOne.hasMaxDosePerLifetime() && dosageTwo.hasMaxDosePerLifetime()) {
						String errorMessage = "The scenario requires " + fieldName + ".maxDosePerLifetime"
								+ " but submitted file does not have " + fieldName + ".maxDosePerLifetime";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".maxDosePerLifetime", errorMessage));
					} else {
						logger.info(fieldName
								+ ".maxDosePerLifetime information is null OR equal in both inputJson and goldJson");
					}

					// asNeeded
					if (dosageOne.hasAsNeeded() && dosageTwo.hasAsNeeded()) {
						if (dosageOne.hasAsNeededBooleanType() && dosageTwo.hasAsNeededBooleanType()
								&& dosageOne.getAsNeededBooleanType() instanceof BooleanType
								&& dosageTwo.getAsNeededBooleanType() instanceof BooleanType) {
							compareBooleanType(fieldName + ".asNeededBoolean", dosageOne.getAsNeededBooleanType(),
									dosageTwo.getAsNeededBooleanType(), operationOutcome, scenarioName);
						} else if (dosageOne.hasAsNeededBooleanType() && !dosageTwo.hasAsNeededBooleanType()) {
							String errorMessage = "The scenario does not require " + fieldName + ".asNeededBoolean"
									+ " but submitted file does have " + fieldName + ".asNeededBoolean";
							operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
									fieldName + ".asNeededBoolean", errorMessage));
						} else if (!dosageOne.hasAsNeededBooleanType() && dosageTwo.hasAsNeededBooleanType()) {
							String errorMessage = "The scenario requires " + fieldName + ".asNeededBoolean"
									+ " but submitted file does not have " + fieldName + ".asNeededBoolean";
							operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
									fieldName + ".asNeededBoolean", errorMessage));
						} else {
							logger.info(fieldName
									+ ".asNeededBoolean information is null OR equal in both inputJson and goldJson");
						}

						if (dosageOne.hasAsNeededCodeableConcept() && dosageTwo.hasAsNeededCodeableConcept()
								&& dosageOne.getAsNeededCodeableConcept() instanceof CodeableConcept
								&& dosageTwo.getAsNeededCodeableConcept() instanceof CodeableConcept) {
							compareBooleanType(fieldName + ".asNeededCodeableConcept",
									dosageOne.getAsNeededBooleanType(), dosageTwo.getAsNeededBooleanType(),
									operationOutcome, scenarioName);
						} else if (dosageOne.hasAsNeededCodeableConcept() && !dosageTwo.hasAsNeededCodeableConcept()) {
							String errorMessage = "The scenario does not require " + fieldName
									+ ".asNeededCodeableConcept" + " but submitted file does have " + fieldName
									+ ".asNeededdCodeableConcept";
							operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
									fieldName + ".asNeededCodeableConcept", errorMessage));
						} else if (!dosageOne.hasAsNeededCodeableConcept() && dosageTwo.hasAsNeededCodeableConcept()) {
							String errorMessage = "The scenario requires " + fieldName + ".asNeededCodeableConcept"
									+ " but submitted file does not have " + fieldName + ".asNeededCodeableConcept";
							operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
									fieldName + ".asNeededCodeableConcept", errorMessage));
						} else {
							logger.info(fieldName
									+ ".asNeededCodeableConcept information is null OR equal in both inputJson and goldJson");
						}
					}

					// doseAndRate
					if (dosageOne.hasDoseAndRate() && dosageTwo.hasDoseAndRate()) {
						compareListOfDoseAndRate(fieldName + ".doseAndRate", dosageOne.getDoseAndRate(),
								dosageTwo.getDoseAndRate(), operationOutcome, scenarioName);
					} else if (dosageOne.hasDoseAndRate() && !dosageTwo.hasDoseAndRate()) {
						String errorMessage = "The scenario does not require " + fieldName + ".doseAndRate"
								+ " but submitted file does have " + fieldName + ".doseAndRate";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".doseAndRate", errorMessage));
					} else if (!dosageOne.hasDoseAndRate() && dosageTwo.hasDoseAndRate()) {
						String errorMessage = "The scenario requires " + fieldName + ".doseAndRate"
								+ " but submitted file does not have " + fieldName + ".doseAndRate";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".doseAndRate", errorMessage));
					} else {
						logger.info(
								fieldName + ".doseAndRate information is null OR equal in both inputJson and goldJson");
					}
				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDosage of ComparatorUtils :: ", ex.getMessage());
		}
	}

	private static void compareListOfDoseAndRate(String fieldName, List<DosageDoseAndRateComponent> doseAndRate1,
			List<DosageDoseAndRateComponent> doseAndRate2, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, doseAndRate1, doseAndRate2, operationOutcome, scenarioName)) {
				for (int i = 0; i < doseAndRate1.size();) {
					for (int j = 0; j < doseAndRate2.size(); j++) {
						compareDosage(fieldName, doseAndRate1.get(i), doseAndRate2.get(j), operationOutcome,
								scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(doseAndRate1) && ObjectUtils.isEmpty(doseAndRate2)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(doseAndRate1) && ObjectUtils.isNotEmpty(doseAndRate2)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfQuantity of ComparatorUtils :: ", ex.getMessage());
		}
	}

	private static void compareDosage(String fieldName, DosageDoseAndRateComponent dosageDoseAndRateComponent1,
			DosageDoseAndRateComponent dosageDoseAndRateComponent2, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (dosageDoseAndRateComponent1 != null && dosageDoseAndRateComponent2 != null) {
				// type
				if (dosageDoseAndRateComponent1.hasType() && dosageDoseAndRateComponent2.hasType()) {
					compareCodeableConcept(fieldName + ".type", dosageDoseAndRateComponent1.getType(),
							dosageDoseAndRateComponent2.getType(), operationOutcome, scenarioName);
				} else if (dosageDoseAndRateComponent1.hasType() && !dosageDoseAndRateComponent2.hasType()) {
					String errorMessage = "The scenario does not require " + fieldName + ".type"
							+ " but submitted file does have " + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else if (!dosageDoseAndRateComponent1.hasType() && dosageDoseAndRateComponent2.hasType()) {
					String errorMessage = "The scenario requires " + fieldName + ".type"
							+ " but submitted file does not have " + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else {
					logger.info(fieldName + ".type information is null OR equal in both inputJson and goldJson");
				}

				// extension
				if (dosageDoseAndRateComponent1.hasExtension() && dosageDoseAndRateComponent2.hasExtension()) {
					compareListOfExtension(fieldName + ".extension", dosageDoseAndRateComponent1.getExtension(),
							dosageDoseAndRateComponent2.getExtension(), operationOutcome, scenarioName);
				} else if (dosageDoseAndRateComponent1.hasExtension() && !dosageDoseAndRateComponent2.hasExtension()) {
					String errorMessage = "The scenario does not require " + fieldName + ".extension"
							+ " but submitted file does have " + fieldName + ".extension";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
				} else if (!dosageDoseAndRateComponent1.hasExtension() && dosageDoseAndRateComponent2.hasExtension()) {
					String errorMessage = "The scenario requires " + fieldName + ".extension"
							+ " but submitted file does not have " + fieldName + ".extension";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
				} else {
					logger.info(fieldName + ".extension information is null OR equal in both inputJson and goldJson");
				}

				// extension
				if (dosageDoseAndRateComponent1.hasExtension() && dosageDoseAndRateComponent2.hasExtension()) {
					compareListOfExtension(fieldName + ".extension", dosageDoseAndRateComponent1.getExtension(),
							dosageDoseAndRateComponent2.getExtension(), operationOutcome, scenarioName);
				} else if (dosageDoseAndRateComponent1.hasExtension() && !dosageDoseAndRateComponent2.hasExtension()) {
					String errorMessage = "The scenario does not require " + fieldName + ".extension"
							+ " but submitted file does have " + fieldName + ".extension";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
				} else if (!dosageDoseAndRateComponent1.hasExtension() && dosageDoseAndRateComponent2.hasExtension()) {
					String errorMessage = "The scenario requires " + fieldName + ".extension"
							+ " but submitted file does not have " + fieldName + ".extension";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
				} else {
					logger.info(fieldName + ".extension information is null OR equal in both inputJson and goldJson");
				}

				// dose
				if (dosageDoseAndRateComponent1.hasDose() && dosageDoseAndRateComponent2.hasDose()) {
					if (dosageDoseAndRateComponent1.hasDoseRange() && dosageDoseAndRateComponent2.hasDoseRange()) {
						compareString(fieldName + ".doseRange", dosageDoseAndRateComponent1.getDoseRange().toString(),
								dosageDoseAndRateComponent2.toString(), operationOutcome, scenarioName);
					} else if (dosageDoseAndRateComponent1.hasDoseRange()
							&& !dosageDoseAndRateComponent2.hasDoseRange()) {
						String errorMessage = "The scenario does not require " + fieldName + ".doseRange"
								+ " but submitted file does have " + fieldName + ".doseRange";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".doseRange", errorMessage));
					} else if (!dosageDoseAndRateComponent1.hasDoseRange()
							&& dosageDoseAndRateComponent2.hasDoseRange()) {
						String errorMessage = "The scenario requires " + fieldName + ".doseRange"
								+ " but submitted file does not have " + fieldName + ".doseRange";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".doseRange", errorMessage));
					} else {
						logger.info(
								fieldName + ".doseRange information is null OR equal in both inputJson and goldJson");
					}

					if (dosageDoseAndRateComponent1.hasDoseQuantity()
							&& dosageDoseAndRateComponent2.hasDoseQuantity()) {
						compareQuantity(fieldName + ".doseQuantity", dosageDoseAndRateComponent1.getDoseQuantity(),
								dosageDoseAndRateComponent2.getDoseQuantity(), operationOutcome, scenarioName);
					} else if (dosageDoseAndRateComponent1.hasDoseQuantity()
							&& !dosageDoseAndRateComponent2.hasDoseQuantity()) {
						String errorMessage = "The scenario does not require " + fieldName + ".doseQuantity"
								+ " but submitted file does have " + fieldName + ".doseQuantity";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".doseQuantity", errorMessage));
					} else if (!dosageDoseAndRateComponent1.hasDoseQuantity()
							&& dosageDoseAndRateComponent2.hasDoseQuantity()) {
						String errorMessage = "The scenario requires " + fieldName + ".doseQuantity"
								+ " but submitted file does not have " + fieldName + ".doseQuantity";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".doseQuantity", errorMessage));
					} else {
						logger.info(fieldName
								+ ".doseQuantity information is null OR equal in both inputJson and goldJson");
					}
				}

				// rate
				if (dosageDoseAndRateComponent1.hasRate() && dosageDoseAndRateComponent2.hasRate()) {
					if (dosageDoseAndRateComponent1.hasRateRatio() && dosageDoseAndRateComponent2.hasRateRatio()) {
						compareString(fieldName + ".rateRatio", dosageDoseAndRateComponent1.getRateRatio().toString(),
								dosageDoseAndRateComponent2.getRateRatio().toString(), operationOutcome, scenarioName);

					} else if (dosageDoseAndRateComponent1.hasRateRatio()
							&& !dosageDoseAndRateComponent2.hasRateRatio()) {
						String errorMessage = "The scenario does not require " + fieldName + ".rateRatio"
								+ " but submitted file does have " + fieldName + ".rateRatio";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".rateRatio", errorMessage));
					} else if (!dosageDoseAndRateComponent1.hasRateRatio()
							&& dosageDoseAndRateComponent2.hasRateRatio()) {
						String errorMessage = "The scenario requires " + fieldName + ".rateRatio"
								+ " but submitted file does not have " + fieldName + ".rateRatio";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".rateRatio", errorMessage));
					} else {
						logger.info(
								fieldName + ".rateRatio information is null OR equal in both inputJson and goldJson");
					}

					if (dosageDoseAndRateComponent1.hasRateQuantity()
							&& dosageDoseAndRateComponent2.hasRateQuantity()) {
						compareQuantity(fieldName + ".rateQuantity", dosageDoseAndRateComponent1.getRateQuantity(),
								dosageDoseAndRateComponent2.getRateQuantity(), operationOutcome, scenarioName);
					} else if (dosageDoseAndRateComponent1.hasRateQuantity()
							&& !dosageDoseAndRateComponent2.hasRateQuantity()) {
						String errorMessage = "The scenario does not require " + fieldName + ".rateQuantity"
								+ " but submitted file does have " + fieldName + ".rateQuantity";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".rateQuantity", errorMessage));
					} else if (!dosageDoseAndRateComponent1.hasRateQuantity()
							&& dosageDoseAndRateComponent2.hasRateQuantity()) {
						String errorMessage = "The scenario requires " + fieldName + ".rateQuantity"
								+ " but submitted file does not have " + fieldName + ".rateQuantity";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".rateQuantity", errorMessage));
					} else {
						logger.info(fieldName
								+ ".rateQuantity information is null OR equal in both inputJson and goldJson");
					}

					if (dosageDoseAndRateComponent1.hasRateRange() && dosageDoseAndRateComponent2.hasRateRange()) {
						compareRange(fieldName + ".rateRange", dosageDoseAndRateComponent1.getRateRange(),
								dosageDoseAndRateComponent2.getRateRange(), operationOutcome, scenarioName);
					} else if (dosageDoseAndRateComponent1.hasRateRange()
							&& !dosageDoseAndRateComponent2.hasRateRange()) {
						String errorMessage = "The scenario does not require " + fieldName + ".rateRange"
								+ " but submitted file does have " + fieldName + ".rateRange";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".rateRange", errorMessage));
					} else if (!dosageDoseAndRateComponent1.hasRateRange()
							&& dosageDoseAndRateComponent2.hasRateRange()) {
						String errorMessage = "The scenario requires " + fieldName + ".rateRange"
								+ " but submitted file does not have " + fieldName + ".rateRange";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".rateRange", errorMessage));
					} else {
						logger.info(
								fieldName + ".rateRange information is null OR equal in both inputJson and goldJson");
					}
				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareDosage of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareMedicationRequestDispenseRequestComponent(String fieldName,
			MedicationRequestDispenseRequestComponent dispenseRequest1,
			MedicationRequestDispenseRequestComponent dispenseRequest2, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (!dispenseRequest1.isEmpty() && !dispenseRequest2.isEmpty()) {
				// extension
				if (dispenseRequest1.hasExtension() && dispenseRequest2.hasExtension()) {
					compareListOfExtension(fieldName + ".extension", dispenseRequest1.getExtension(),
							dispenseRequest2.getExtension(), operationOutcome, scenarioName);
				} else if (dispenseRequest1.hasExtension() && !dispenseRequest2.hasExtension()) {
					String errorMessage = "The scenario does not require " + fieldName + ".extension"
							+ " but submitted file does have " + fieldName + ".extension";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
				} else if (!dispenseRequest1.hasExtension() && dispenseRequest2.hasExtension()) {
					String errorMessage = "The scenario requires " + fieldName + ".extension"
							+ " but submitted file does not have " + fieldName + ".extension";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".rateRange", errorMessage));
				} else {
					logger.info(fieldName + ".extension information is null OR equal in both inputJson and goldJson");
				}

				if (dispenseRequest1.hasInitialFill() && dispenseRequest2.hasInitialFill()) {

					if (dispenseRequest1.hasId() && dispenseRequest2.hasId()) {
						compareString(fieldName + ".id", dispenseRequest1.getId(), dispenseRequest2.getId(),
								operationOutcome, scenarioName);
					} else if (dispenseRequest1.hasInitialFill() && !dispenseRequest2.hasInitialFill()) {
						String errorMessage = "The scenario does not require " + fieldName + ".id"
								+ " but submitted file does have " + fieldName + ".id";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".id", errorMessage));
					} else if (!dispenseRequest1.hasInitialFill() && dispenseRequest2.hasInitialFill()) {
						String errorMessage = "The scenario requires " + fieldName + ".id"
								+ " but submitted file does not have " + fieldName + ".id";
						operationOutcome
								.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".id", errorMessage));
					} else {
						logger.info(fieldName + ".id information is null OR equal in both inputJson and goldJson");
					}

					if (dispenseRequest1.hasQuantity() && dispenseRequest2.hasQuantity()) {
						compareQuantity(fieldName + ".quantity", dispenseRequest1.getQuantity(),
								dispenseRequest2.getQuantity(), operationOutcome, scenarioName);
					} else if (dispenseRequest1.hasQuantity() && !dispenseRequest2.hasQuantity()) {
						String errorMessage = "The scenario does not require " + fieldName + ".quantity"
								+ " but submitted file does have " + fieldName + ".quantity";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".rateQuantity", errorMessage));
					} else if (!dispenseRequest1.hasQuantity() && dispenseRequest2.hasQuantity()) {
						String errorMessage = "The scenario requires " + fieldName + ".quantity"
								+ " but submitted file does not have " + fieldName + ".quantity";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".quantity", errorMessage));
					} else {
						logger.info(
								fieldName + ".quantity information is null OR equal in both inputJson and goldJson");
					}

					if (dispenseRequest1.hasExpectedSupplyDuration() && dispenseRequest2.hasExpectedSupplyDuration()) {
						compareQuantity(fieldName + ".duration", dispenseRequest1.getExpectedSupplyDuration(),
								dispenseRequest2.getExpectedSupplyDuration(), operationOutcome, scenarioName);
					} else if (dispenseRequest1.hasExpectedSupplyDuration()
							&& !dispenseRequest2.hasExpectedSupplyDuration()) {
						String errorMessage = "The scenario does not require " + fieldName + ".duration"
								+ " but submitted file does have " + fieldName + ".duration";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".duration", errorMessage));
					} else if (!dispenseRequest1.hasExpectedSupplyDuration()
							&& dispenseRequest2.hasExpectedSupplyDuration()) {
						String errorMessage = "The scenario requires " + fieldName + ".duration"
								+ " but submitted file does not have " + fieldName + ".duration";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".duration", errorMessage));
					} else {
						logger.info(
								fieldName + ".duration information is null OR equal in both inputJson and goldJson");
					}
				}

				if (dispenseRequest1.hasDispenseInterval() && dispenseRequest2.hasDispenseInterval()) {
					compareQuantity(fieldName + ".dispenseInterval", dispenseRequest1.getDispenseInterval(),
							dispenseRequest2.getDispenseInterval(), operationOutcome, scenarioName);
				} else if (dispenseRequest1.hasDispenseInterval() && !dispenseRequest2.hasDispenseInterval()) {
					String errorMessage = "The scenario does not require " + fieldName + ".dispenseInterval"
							+ " but submitted file does have " + fieldName + ".dispenseInterval";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".dispenseInterval", errorMessage));
				} else if (!dispenseRequest1.hasDispenseInterval() && dispenseRequest2.hasDispenseInterval()) {
					String errorMessage = "The scenario requires " + fieldName + ".dispenseInterval"
							+ " but submitted file does not have " + fieldName + ".dispenseInterval";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".dispenseInterval", errorMessage));
				} else {
					logger.info(fieldName
							+ ".dispenseInterval information is null OR equal in both inputJson and goldJson");
				}

				if (dispenseRequest1.hasValidityPeriod() && dispenseRequest2.hasValidityPeriod()) {
					comparePeriod(fieldName + ".validityPeriod", dispenseRequest1.getValidityPeriod(),
							dispenseRequest2.getValidityPeriod(), operationOutcome, scenarioName);
				} else if (dispenseRequest1.hasValidityPeriod() && !dispenseRequest2.hasValidityPeriod()) {
					String errorMessage = "The scenario does not require " + fieldName + ".validityPeriod"
							+ " but submitted file does have " + fieldName + ".validityPeriod";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".validityPeriod", errorMessage));
				} else if (!dispenseRequest1.hasValidityPeriod() && dispenseRequest2.hasValidityPeriod()) {
					String errorMessage = "The scenario requires " + fieldName + ".validityPeriod"
							+ " but submitted file does not have " + fieldName + ".validityPeriod";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".validityPeriod", errorMessage));
				} else {
					logger.info(
							fieldName + ".validityPeriod information is null OR equal in both inputJson and goldJson");
				}

				if (dispenseRequest1.hasQuantity() && dispenseRequest2.hasQuantity()) {
					compareQuantity(fieldName + ".quantity", dispenseRequest1.getQuantity(),
							dispenseRequest2.getQuantity(), operationOutcome, scenarioName);
				} else if (dispenseRequest1.hasQuantity() && !dispenseRequest2.hasQuantity()) {
					String errorMessage = "The scenario does not require " + fieldName + ".quantity"
							+ " but submitted file does have " + fieldName + ".quantity";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".quantity", errorMessage));
				} else if (!dispenseRequest1.hasQuantity() && dispenseRequest2.hasQuantity()) {
					String errorMessage = "The scenario requires " + fieldName + ".quantity"
							+ " but submitted file does not have " + fieldName + ".quantity";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".quantity", errorMessage));
				} else {
					logger.info(fieldName + ".quantity information is null OR equal in both inputJson and goldJson");
				}

				if (dispenseRequest1.hasExpectedSupplyDuration() && dispenseRequest2.hasExpectedSupplyDuration()) {
					compareQuantity(fieldName + ".expectedSupplyDuration", dispenseRequest1.getExpectedSupplyDuration(),
							dispenseRequest2.getExpectedSupplyDuration(), operationOutcome, scenarioName);
				} else if (dispenseRequest1.hasExpectedSupplyDuration()
						&& !dispenseRequest2.hasExpectedSupplyDuration()) {
					String errorMessage = "The scenario does not require " + fieldName + ".expectedSupplyDuration"
							+ " but submitted file does have " + fieldName + ".expectedSupplyDuration";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							fieldName + ".expectedSupplyDuration", errorMessage));
				} else if (!dispenseRequest1.hasExpectedSupplyDuration()
						&& dispenseRequest2.hasExpectedSupplyDuration()) {
					String errorMessage = "The scenario requires " + fieldName + ".expectedSupplyDuration"
							+ " but submitted file does not have " + fieldName + ".expectedSupplyDuration";
					operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
							fieldName + ".expectedSupplyDuration", errorMessage));
				} else {
					logger.info(fieldName
							+ ".expectedSupplyDuration information is null OR equal in both inputJson and goldJson");
				}

				if (dispenseRequest1.hasPerformer() && dispenseRequest2.hasPerformer()) {
					compareReference(fieldName + ".performer", dispenseRequest1.getPerformer(),
							dispenseRequest2.getPerformer(), operationOutcome, scenarioName);
				} else if (dispenseRequest1.hasPerformer() && !dispenseRequest2.hasPerformer()) {
					String errorMessage = "The scenario does not require " + fieldName + ".performer"
							+ " but submitted file does have " + fieldName + ".performer";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".performer", errorMessage));
				} else if (!dispenseRequest1.hasPerformer() && dispenseRequest2.hasPerformer()) {
					String errorMessage = "The scenario requires " + fieldName + ".performer"
							+ " but submitted file does not have " + fieldName + ".performer";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".performer", errorMessage));
				} else {
					logger.info(fieldName + ".performer information is null OR equal in both inputJson and goldJson");
				}
			} else if (!dispenseRequest1.isEmpty() && dispenseRequest2.isEmpty()) {

				String errorMessage = "The scenario does not require DispenseRequest, but submitted file does have DispenseRequest";
				operationOutcome.addIssue(createScenarioResults(IssueType.INCOMPLETE, fieldName, errorMessage));
			} else if (dispenseRequest1.isEmpty() && !dispenseRequest2.isEmpty()) {

				String errorMessage = "The scenario requires DispenseRequest, but submitted file does not have DispenseRequest";
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareMedicationRequestDispenseRequestComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareMedicationRequestSubstitutionComponent(String fieldName,
			MedicationRequestSubstitutionComponent substitution1, MedicationRequestSubstitutionComponent substitution2,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (substitution1 != null && substitution2 != null) {

				if (substitution1.hasExtension() && substitution2.hasExtension()) {
					compareListOfExtension(fieldName + ".extension", substitution1.getExtension(),
							substitution2.getExtension(), operationOutcome, scenarioName);
				} else if (substitution1.hasExtension() && !substitution2.hasExtension()) {
					String errorMessage = "The scenario does not require " + fieldName + ".extension"
							+ " but submitted file does have " + fieldName + ".extension";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
				} else if (!substitution1.hasExtension() && substitution2.hasExtension()) {
					String errorMessage = "The scenario requires " + fieldName + ".extension"
							+ " but submitted file does not have " + fieldName + ".extension";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
				} else {
					logger.info(fieldName + ".extension information is null OR equal in both inputJson and goldJson");
				}

				if (substitution1.hasReason() && substitution2.hasReason()) {
					compareCodeableConcept(fieldName + ".reason", substitution1.getReason(), substitution2.getReason(),
							operationOutcome, scenarioName);
				} else if (substitution1.hasReason() && !substitution2.hasReason()) {
					String errorMessage = "The scenario does not require " + fieldName + ".reason"
							+ " but submitted file does have " + fieldName + ".reason";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".reason", errorMessage));
				} else if (!substitution1.hasReason() && substitution2.hasReason()) {
					String errorMessage = "The scenario requires " + fieldName + ".reason"
							+ " but submitted file does not have " + fieldName + ".reason";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".reason", errorMessage));
				} else {
					logger.info(fieldName + ".reason information is null OR equal in both inputJson and goldJson");
				}

				if (substitution1.hasAllowed() && substitution2.hasAllowed()) {
					if (substitution1.hasAllowedBooleanType() && substitution2.hasAllowedBooleanType()) {
						compareBooleanType(fieldName + ".allowedBoolean", substitution1.getAllowedBooleanType(),
								substitution2.getAllowedBooleanType(), operationOutcome, scenarioName);

					} else if (substitution1.hasAllowedBooleanType() && !substitution2.hasAllowedBooleanType()) {
						String errorMessage = "The scenario does not require " + fieldName + ".allowedBoolean"
								+ " but submitted file does have " + fieldName + ".allowedBoolean";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".allowedBoolean", errorMessage));
					} else if (!substitution1.hasAllowedBooleanType() && substitution2.hasAllowedBooleanType()) {
						String errorMessage = "The scenario requires " + fieldName + ".allowedBoolean"
								+ " but submitted file does not have " + fieldName + ".allowedBoolean";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".allowedBoolean", errorMessage));
					} else {
						logger.info(fieldName
								+ ".allowedBoolean information is null OR equal in both inputJson and goldJson");
					}

					if (substitution1.hasAllowedCodeableConcept() && substitution2.hasAllowedCodeableConcept()) {
						compareCodeableConcept(fieldName + ".allowedCodeableConcept",
								substitution1.getAllowedCodeableConcept(), substitution2.getAllowedCodeableConcept(),
								operationOutcome, scenarioName);

					} else if (substitution1.hasAllowedCodeableConcept()
							&& !substitution2.hasAllowedCodeableConcept()) {
						String errorMessage = "The scenario does not require " + fieldName + ".allowedCodeableConcept"
								+ " but submitted file does have " + fieldName + ".allowedCodeableConcept";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".allowedBoolean", errorMessage));
					} else if (!substitution1.hasAllowedCodeableConcept()
							&& substitution2.hasAllowedCodeableConcept()) {
						String errorMessage = "The scenario requires " + fieldName + ".allowedCodeableConcept"
								+ " but submitted file does not have " + fieldName + ".allowedCodeableConcept";
						operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT,
								fieldName + ".allowedCodeableConcept", errorMessage));
					} else {
						logger.info(fieldName
								+ ".allowedCodeableConcept information is null OR equal in both inputJson and goldJson");
					}

				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareMedicationRequestDispenseRequestComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	public static void compareListOfGoalTargetComponent(String fieldName, List<GoalTargetComponent> target1,
			List<GoalTargetComponent> target2, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (compareListForSizeEquality(fieldName, target1, target2, operationOutcome, scenarioName)) {
				for (int i = 0; i < target1.size();) {
					for (int j = 0; j < target2.size(); j++) {
						compareGoalTargetComponent(fieldName, target1.get(i), target2.get(j), operationOutcome,
								scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(target1) && ObjectUtils.isEmpty(target2)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(target1) && ObjectUtils.isNotEmpty(target2)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfGoalTargetComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

	private static void compareGoalTargetComponent(String fieldName, GoalTargetComponent goalTargetComponent1,
			GoalTargetComponent goalTargetComponent2, OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (!goalTargetComponent1.isEmpty() && !goalTargetComponent2.isEmpty()) {

				if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_GOAL)) {
					if (goalTargetComponent1.hasExtension() && goalTargetComponent2.hasExtension()) {
						compareListOfExtension(fieldName + ".extension", goalTargetComponent1.getExtension(),
								goalTargetComponent2.getExtension(), operationOutcome, scenarioName);
					} else if (goalTargetComponent1.hasExtension() && !goalTargetComponent2.hasExtension()) {
						String errorMessage = "The scenario does not require " + fieldName + ".extension"
								+ " but submitted file does have " + fieldName + ".extension";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
					} else if (!goalTargetComponent1.hasExtension() && goalTargetComponent2.hasExtension()) {
						String errorMessage = "The scenario requires " + fieldName + ".extension"
								+ " but submitted file does not have " + fieldName + ".extension";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".extension", errorMessage));
					} else {
						logger.info(
								fieldName + ".extension information is null OR equal in both inputJson and goldJson");
					}

					if (goalTargetComponent1.hasMeasure() && goalTargetComponent2.hasMeasure()) {
						compareCodeableConcept(fieldName + ".measure", goalTargetComponent1.getMeasure(),
								goalTargetComponent2.getMeasure(), operationOutcome, scenarioName);
					} else if (goalTargetComponent1.hasMeasure() && !goalTargetComponent2.hasMeasure()) {
						String errorMessage = "The scenario does not require " + fieldName + ".measure"
								+ " but submitted file does have " + fieldName + ".measure";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".measure", errorMessage));
					} else if (!goalTargetComponent1.hasMeasure() && goalTargetComponent2.hasMeasure()) {
						String errorMessage = "The scenario requires " + fieldName + ".measure"
								+ " but submitted file does not have " + fieldName + ".measure";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".measure", errorMessage));
					} else {
						logger.info(fieldName + ".measure information is null OR equal in both inputJson and goldJson");
					}

					if (goalTargetComponent1.hasDetail() && goalTargetComponent2.hasDetail()) {
						if (goalTargetComponent1.getDetail() instanceof Quantity
								&& goalTargetComponent2.getDetail() instanceof Quantity) {
							compareQuantity(fieldName + "detailQuantity", goalTargetComponent1.getDetailQuantity(),
									goalTargetComponent2.getDetailQuantity(), operationOutcome, scenarioName);
						}
						if (goalTargetComponent1.getDetail() instanceof Range
								&& goalTargetComponent2.getDetail() instanceof Range) {
							compareRange(fieldName + "detailRange", goalTargetComponent1.getDetailRange(),
									goalTargetComponent2.getDetailRange(), operationOutcome, scenarioName);
						}

					} else if (goalTargetComponent1.hasDetail() && !goalTargetComponent2.hasDetail()) {
						String errorMessage = "The scenario does not require " + fieldName + ".detail"
								+ " but submitted file does have " + fieldName + ".detail";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".detail", errorMessage));
					} else if (!goalTargetComponent1.hasDetail() && goalTargetComponent2.hasDetail()) {
						String errorMessage = "The scenario requires " + fieldName + ".detail"
								+ " but submitted file does not have " + fieldName + ".detail";
						operationOutcome.addIssue(
								createScenarioResults(IssueType.CONFLICT, fieldName + ".detail", errorMessage));
					} else {
						logger.info(fieldName + ".detail information is null OR equal in both inputJson and goldJson");
					}
				}
				if (goalTargetComponent1.hasDue() && goalTargetComponent2.hasDue()) {
					if (goalTargetComponent1.getDue() instanceof DateType
							&& goalTargetComponent2.getDue() instanceof DateTimeType) {
						DateTimeType dateOne = (DateTimeType) goalTargetComponent1.getDue();
						DateTimeType dateTwo = (DateTimeType) goalTargetComponent2.getDue();
						compareDateTimeType(fieldName + "dueDate", dateOne, dateTwo, operationOutcome, scenarioName);
					}
				} else if (goalTargetComponent1.hasDue() && !goalTargetComponent2.hasDue()) {
					String errorMessage = "The scenario does not require " + fieldName + ".dueDate"
							+ " but submitted file does have " + fieldName + ".dueDate";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".dueDate", errorMessage));
				} else if (!goalTargetComponent1.hasDue() && goalTargetComponent2.hasDue()) {
					String errorMessage = "The scenario requires " + fieldName + ".dueDate"
							+ " but submitted file does not have " + fieldName + ".dueDate";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".dueDate", errorMessage));
				} else {
					logger.info(fieldName + ".dueDate information is null OR equal in both inputJson and goldJson");
				}
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfQuantity of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareListOfConditionStageComponent(String fieldName,
			List<ConditionStageComponent> stageListOne, List<ConditionStageComponent> stageListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, stageListOne, stageListTwo, operationOutcome, scenarioName)) {
				for (int i = 0; i < stageListOne.size();) {
					for (int j = 0; j < stageListTwo.size(); j++) {
						compareConditionStageComponent(fieldName, stageListOne.get(i), stageListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(stageListOne) && ObjectUtils.isEmpty(stageListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(stageListOne) && ObjectUtils.isNotEmpty(stageListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfConditionStageComponent of ComparatorUtils :: ",
					ex.getMessage());
		}

	}

	private static void compareConditionStageComponent(String fieldName,
			ConditionStageComponent conditionStageComponentOne, ConditionStageComponent conditionStageComponentTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (conditionStageComponentOne != null && conditionStageComponentTwo != null) {

				// summary
				if (conditionStageComponentOne.hasSummary() && conditionStageComponentTwo.hasSummary()) {
					compareCodeableConcept(fieldName + ".summary", conditionStageComponentOne.getSummary(),
							conditionStageComponentTwo.getSummary(), operationOutcome, scenarioName);
				} else if (conditionStageComponentOne.hasSummary() && !conditionStageComponentTwo.hasSummary()) {
					String errorMessage = "The scenario does not require " + fieldName + ".summary"
							+ " but submitted file does have " + fieldName + ".summary";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".summary", errorMessage));
				} else if (!conditionStageComponentOne.hasSummary() && conditionStageComponentTwo.hasSummary()) {
					String errorMessage = "The scenario requires " + fieldName + ".summary"
							+ " but submitted file does not have" + fieldName + ".summary";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".summary", errorMessage));
				} else {
					logger.info(fieldName + ".summary information is null OR equal in both inputJson and goldJson");
				}

				// assessment
				if (conditionStageComponentOne.hasAssessment() && conditionStageComponentTwo.hasAssessment()) {
					compareListOfReference(fieldName + ".assessment", conditionStageComponentOne.getAssessment(),
							conditionStageComponentTwo.getAssessment(), operationOutcome, scenarioName);
				} else if (conditionStageComponentOne.hasAssessment() && !conditionStageComponentTwo.hasAssessment()) {
					String errorMessage = "The scenario does not require " + fieldName + ".assessment"
							+ " but submitted file does have " + fieldName + ".assessment";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".assessment", errorMessage));
				} else if (!conditionStageComponentOne.hasAssessment() && conditionStageComponentTwo.hasAssessment()) {
					String errorMessage = "The scenario requires " + fieldName + ".assessment"
							+ " but submitted file does not have " + fieldName + ".assessment";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".assessment", errorMessage));
				} else {
					logger.info(fieldName + ".assessment information is null OR equal in both inputJson and goldJson");
				}
				// type
				if (conditionStageComponentOne.hasType() && conditionStageComponentTwo.hasType()) {
					compareCodeableConcept(fieldName + ". type", conditionStageComponentOne.getType(),
							conditionStageComponentTwo.getType(), operationOutcome, scenarioName);
				} else if (conditionStageComponentOne.hasType() && !conditionStageComponentTwo.hasType()) {
					String errorMessage = "The scenario does not require " + fieldName + ".type"
							+ " but submitted file does have " + fieldName + ". type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". type", errorMessage));
				} else if (!conditionStageComponentOne.hasType() && conditionStageComponentTwo.hasType()) {
					String errorMessage = "The scenario requires " + fieldName + ". type"
							+ " but submitted file does not have " + fieldName + ". type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else {
					logger.info(fieldName + ". type information is null OR equal in both inputJson and goldJson");
				}

			} else if (conditionStageComponentOne != null && conditionStageComponentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (conditionStageComponentOne == null && conditionStageComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareConditionStageComponent of ComparatorUtils :: ", ex.getMessage());
		}
	}

	public static void compareListOfConditionEvidenceComponent(String fieldName,
			List<ConditionEvidenceComponent> evidenceListOne, List<ConditionEvidenceComponent> evidenceListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, evidenceListOne, evidenceListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < evidenceListOne.size();) {
					for (int j = 0; j < evidenceListTwo.size(); j++) {
						compareConditionEvidenceComponent(fieldName, evidenceListOne.get(i), evidenceListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(evidenceListOne) && ObjectUtils.isEmpty(evidenceListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(evidenceListOne) && ObjectUtils.isNotEmpty(evidenceListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfConditionEvidenceComponent of ComparatorUtils :: ",
					ex.getMessage());
		}
	}

	private static void compareConditionEvidenceComponent(String fieldName,
			ConditionEvidenceComponent conditionEvidenceComponentOne,
			ConditionEvidenceComponent conditionEvidenceComponentTwo, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (conditionEvidenceComponentOne != null && conditionEvidenceComponentTwo != null) {

				// code
				if (conditionEvidenceComponentOne.hasCode() && conditionEvidenceComponentTwo.hasCode()) {
					compareListOfCodeableConcept(fieldName + ".code", conditionEvidenceComponentOne.getCode(),
							conditionEvidenceComponentTwo.getCode(), operationOutcome, scenarioName);
				} else if (conditionEvidenceComponentOne.hasCode() && !conditionEvidenceComponentTwo.hasCode()) {
					String errorMessage = "The scenario does not require " + fieldName + ".code"
							+ " but submitted file does have " + fieldName + ".code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else if (!conditionEvidenceComponentOne.hasCode() && conditionEvidenceComponentTwo.hasCode()) {
					String errorMessage = "The scenario requires " + fieldName + ".code"
							+ " but submitted file does not have " + fieldName + ".code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else {
					logger.info(fieldName + ".code information is null OR equal in both inputJson and goldJson");
				}

				// detail
				if (conditionEvidenceComponentOne.hasDetail() && conditionEvidenceComponentTwo.hasDetail()) {
					compareListOfReference(fieldName + ".detail", conditionEvidenceComponentOne.getDetail(),
							conditionEvidenceComponentTwo.getDetail(), operationOutcome, scenarioName);
				} else if (conditionEvidenceComponentOne.hasDetail() && !conditionEvidenceComponentTwo.hasDetail()) {
					String errorMessage = "The scenario does not require " + fieldName + ".detail"
							+ " but submitted file does have " + fieldName + ".detail";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".detail", errorMessage));
				} else if (!conditionEvidenceComponentOne.hasDetail() && conditionEvidenceComponentTwo.hasDetail()) {
					String errorMessage = "The scenario requires " + fieldName + ".detail"
							+ " but submitted file does not have " + fieldName + ".detail";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".detail", errorMessage));
				} else {
					logger.info(fieldName + ".detail information is null OR equal in both inputJson and goldJson");
				}

			} else if (conditionEvidenceComponentOne != null && conditionEvidenceComponentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (conditionEvidenceComponentOne == null && conditionEvidenceComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareConditionEvidenceComponent of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareAge(String fieldName, Age abatementAgeOne, Age abatementAgeTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (abatementAgeOne != null && abatementAgeTwo != null) {

				// code
				if (abatementAgeOne.hasCode() && abatementAgeTwo.hasCode()) {
					compareString(fieldName + ".code", abatementAgeOne.getCode(), abatementAgeTwo.getCode(),
							operationOutcome, scenarioName);
				} else if (abatementAgeOne.hasCode() && !abatementAgeTwo.hasCode()) {
					String errorMessage = "The scenario does not require " + fieldName + ".code"
							+ " but submitted file does have " + fieldName + ".code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else if (!abatementAgeOne.hasCode() && abatementAgeTwo.hasCode()) {
					String errorMessage = "The scenario requires " + fieldName + ".code"
							+ " but submitted file does not have" + fieldName + ".code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else {
					logger.info(fieldName + ".code information is null OR equal in both inputJson and goldJson");
				}

				// unit
				if (abatementAgeOne.hasUnit() && abatementAgeTwo.hasUnit()) {
					compareString(fieldName + ".detail", abatementAgeOne.getUnit(), abatementAgeTwo.getUnit(),
							operationOutcome, scenarioName);
				} else if (abatementAgeOne.hasUnit() && !abatementAgeTwo.hasUnit()) {
					String errorMessage = "The scenario does not require " + fieldName + ".detail"
							+ " but submitted file does have " + fieldName + ".detail";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".detail", errorMessage));
				} else if (!abatementAgeOne.hasUnit() && abatementAgeTwo.hasUnit()) {
					String errorMessage = "The scenario requires " + fieldName + ".detail"
							+ " but submitted file does not have" + fieldName + ".detail";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".detail", errorMessage));
				} else {
					logger.info(fieldName + ".detail information is null OR equal in both inputJson and goldJson");
				}

			} else if (abatementAgeOne != null && abatementAgeTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (abatementAgeOne == null && abatementAgeTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareAge of ComparatorUtils :: ", ex.getMessage());
		}

	}

	public static void compareRange(String fieldName, Range abatementRangeOne, Range abatementRangeTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (abatementRangeOne != null && abatementRangeTwo != null) {

				// low
				if (abatementRangeOne.hasLow() && abatementRangeTwo.hasLow()) {
					compareQuantity(fieldName + ".low", abatementRangeOne.getLow(), abatementRangeTwo.getLow(),
							operationOutcome, scenarioName);
				} else if (abatementRangeOne.hasLow() && !abatementRangeTwo.hasLow()) {
					String errorMessage = "The scenario does not require " + fieldName + ".low"
							+ " but submitted file does have " + fieldName + ".low";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".low", errorMessage));
				} else if (!abatementRangeOne.hasLow() && abatementRangeTwo.hasLow()) {
					String errorMessage = "The scenario requires " + fieldName + ".low"
							+ " but submitted file does not have" + fieldName + ".low";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".low", errorMessage));
				} else {
					logger.info(fieldName + ".low information is null OR equal in both inputJson and goldJson");
				}

				// high
				if (abatementRangeOne.hasHigh() && abatementRangeTwo.hasHigh()) {
					compareQuantity(fieldName + ".high", abatementRangeOne.getHigh(), abatementRangeTwo.getHigh(),
							operationOutcome, scenarioName);
				} else if (abatementRangeOne.hasHigh() && !abatementRangeTwo.hasHigh()) {
					String errorMessage = "The scenario does not require " + fieldName + ".high"
							+ " but submitted file does have " + fieldName + ".high";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".high", errorMessage));
				} else if (!abatementRangeOne.hasHigh() && abatementRangeTwo.hasHigh()) {
					String errorMessage = "The scenario requires " + fieldName + ".high"
							+ " but submitted file does not have" + fieldName + ".high";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".high", errorMessage));
				} else {
					logger.info(fieldName + ".high information is null OR equal in both inputJson and goldJson");
				}

			} else if (abatementRangeOne != null && abatementRangeTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (abatementRangeOne == null && abatementRangeTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareRange of ComparatorUtils :: ", ex.getMessage());
		}
	}

	/**
	 * Compare compareListOfDocumentReferenceRelatesToComponent
	 * 
	 * @param fieldName(relatesTo)
	 * @param List<relatesToOne>
	 * @param List<relatesToTwo>
	 * @param operationOutcome
	 * @param scenarioName
	 */
	public static void compareListOfDocumentReferenceRelatesToComponent(String fieldName,
			List<DocumentReferenceRelatesToComponent> relatesToListOne,
			List<DocumentReferenceRelatesToComponent> relatesToListTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, relatesToListOne, relatesToListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < relatesToListOne.size();) {
					for (int j = 0; j < relatesToListTwo.size(); j++) {
						compareDocumentReferenceRelatesToComponent(fieldName, relatesToListOne.get(i),
								relatesToListTwo.get(j), operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(relatesToListOne) && ObjectUtils.isEmpty(relatesToListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(relatesToListOne) && ObjectUtils.isNotEmpty(relatesToListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfDocumentReferenceRelatesToComponent of ComparatorUtils :: ", ex);

		}
	}

	/**
	 * Compare DocumentReferenceRelatesToComponent
	 * 
	 * @param fieldName(relatesTo)
	 * @param relatesToOne
	 * @param relatesToTwo
	 * @param operationOutcome
	 * @param scenarioName
	 */
	public static void compareDocumentReferenceRelatesToComponent(String fieldName,
			DocumentReferenceRelatesToComponent relatesToOne, DocumentReferenceRelatesToComponent relatesToTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (relatesToOne != null && relatesToTwo != null) {

				// code
				if (relatesToOne.hasCode() && relatesToTwo.hasCode()) {
					compareString(fieldName + ".code", relatesToOne.getCode().toString(),
							relatesToTwo.getCode().toString(), operationOutcome, scenarioName);
				} else if (relatesToOne.hasCode() && !relatesToTwo.hasCode()) {
					String errorMessage = "The scenario does not require " + fieldName + ".code"
							+ " but submitted file does have " + fieldName + ".code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else if (!relatesToOne.hasCode() && relatesToTwo.hasCode()) {
					String errorMessage = "The scenario requires " + fieldName + ".code"
							+ " but submitted file does not have " + fieldName + ".code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else {
					logger.info(fieldName + ".code information is null OR equal in both inputJson and goldJson");
				}

				// target
				if (relatesToOne.hasTarget() && relatesToTwo.hasTarget()) {
					compareReference(fieldName + ".target", relatesToOne.getTarget(), relatesToTwo.getTarget(),
							operationOutcome, scenarioName);
				} else if (relatesToOne.hasTarget() && !relatesToTwo.hasTarget()) {
					String errorMessage = "The scenario does not require " + fieldName + ".target"
							+ " but submitted file does have " + fieldName + ".target";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".target", errorMessage));
				} else if (!relatesToOne.hasTarget() && relatesToTwo.hasTarget()) {
					String errorMessage = "The scenario requires " + fieldName + ".target"
							+ " but submitted file does not have " + fieldName + ".target";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".target", errorMessage));
				} else {
					logger.info(fieldName + ".target information is null OR equal in both inputJson and goldJson");
				}

			} else if (relatesToOne != null && relatesToTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (relatesToOne == null && relatesToTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareDocumentReferenceRelatesToComponent of ComparatorUtils :: ", ex);

		}

	}

	public static void compareListOfDocumentReferenceContentComponent(String fieldName,
			List<DocumentReferenceContentComponent> contentListOne,
			List<DocumentReferenceContentComponent> contentListTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, contentListOne, contentListTwo, operationOutcome, scenarioName)) {
				for (int i = 0; i < contentListOne.size();) {
					for (int j = 0; j < contentListTwo.size(); j++) {
						compareDocumentReferenceContentComponent(fieldName, contentListOne.get(i),
								contentListTwo.get(j), operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(contentListOne) && ObjectUtils.isEmpty(contentListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(contentListOne) && ObjectUtils.isNotEmpty(contentListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfDocumentReferenceRelatesToComponent of ComparatorUtils :: ", ex);

		}

	}

	private static void compareDocumentReferenceContentComponent(String fieldName,
			DocumentReferenceContentComponent contentOne, DocumentReferenceContentComponent contentTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (contentOne != null && contentTwo != null) {

				// format
				if (contentOne.hasFormat() && contentTwo.hasFormat()) {
					compareCoding(fieldName + ".format", contentOne.getFormat(), contentTwo.getFormat(),
							operationOutcome, scenarioName);
				} else if (contentOne.hasFormat() && !contentTwo.hasFormat()) {
					String errorMessage = "The scenario does not require " + fieldName + ".format"
							+ " but submitted file does have " + fieldName + ".format";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".format", errorMessage));
				} else if (!contentOne.hasFormat() && contentTwo.hasFormat()) {
					String errorMessage = "The scenario requires " + fieldName + ".format"
							+ " but submitted file does not have " + fieldName + ".format";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".format", errorMessage));
				} else {
					logger.info(fieldName + ".format information is null OR equal in both inputJson and goldJson");
				}

				// attachment
				if (contentOne.hasAttachment() && contentTwo.hasAttachment()) {
					compareAttachment(fieldName + ".attachment", contentOne.getAttachment(), contentTwo.getAttachment(),
							operationOutcome, scenarioName);
				} else if (contentOne.hasAttachment() && !contentTwo.hasAttachment()) {
					String errorMessage = "The scenario does not require " + fieldName + ".attachment"
							+ " but submitted file does have " + fieldName + ".attachment";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".attachment", errorMessage));
				} else if (!contentOne.hasAttachment() && contentTwo.hasAttachment()) {
					String errorMessage = "The scenario requires " + fieldName + ".attachment"
							+ " but submitted file does not have " + fieldName + ".attachment";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".attachment", errorMessage));
				} else {
					logger.info(fieldName + ".attachment information is null OR equal in both inputJson and goldJson");
				}

			} else if (contentOne != null && contentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (contentOne == null && contentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareDocumentReferenceRelatesToComponent of ComparatorUtils :: ", ex);

		}

	}

	public static void compareDocumentReferenceContextComponent(String fieldName,
			DocumentReferenceContextComponent contextOne, DocumentReferenceContextComponent contextTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (contextOne != null && contextTwo != null) {

				// event
				if (contextOne.hasEvent() && contextTwo.hasEvent()) {
					compareListOfCodeableConcept(fieldName + ".event", contextOne.getEvent(), contextTwo.getEvent(),
							operationOutcome, scenarioName);
				} else if (contextOne.hasEvent() && !contextTwo.hasEvent()) {
					String errorMessage = "The scenario does not require " + fieldName + ".event"
							+ " but submitted file does have " + fieldName + ".event";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".event", errorMessage));
				} else if (!contextOne.hasEvent() && contextTwo.hasEvent()) {
					String errorMessage = "The scenario requires " + fieldName + ".event"
							+ " but submitted file does not have " + fieldName + ".event";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".event", errorMessage));
				} else {
					logger.info(fieldName + ".event information is null OR equal in both inputJson and goldJson");
				}

				// period
				if (contextOne.hasPeriod() && contextTwo.hasPeriod()) {
					comparePeriod(fieldName + ".period", contextOne.getPeriod(), contextTwo.getPeriod(),
							operationOutcome, scenarioName);
				} else if (contextOne.hasPeriod() && !contextTwo.hasPeriod()) {
					String errorMessage = "The scenario does not require " + fieldName + ".period"
							+ " but submitted file does have " + fieldName + ".period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
				} else if (!contextOne.hasPeriod() && contextTwo.hasPeriod()) {
					String errorMessage = "The scenario requires " + fieldName + ".period"
							+ " but submitted file does not have " + fieldName + ".period";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".period", errorMessage));
				} else {
					logger.info(fieldName + ".period information is null OR equal in both inputJson and goldJson");
				}

				// facilityType
				if (contextOne.hasFacilityType() && contextTwo.hasFacilityType()) {
					compareCodeableConcept(fieldName + ".facilityType", contextOne.getFacilityType(),
							contextTwo.getFacilityType(), operationOutcome, scenarioName);
				} else if (contextOne.hasFacilityType() && !contextTwo.hasFacilityType()) {
					String errorMessage = "The scenario does not require " + fieldName + ".facilityType"
							+ " but submitted file does have " + fieldName + ".facilityType";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".facilityType", errorMessage));
				} else if (!contextOne.hasFacilityType() && contextTwo.hasFacilityType()) {
					String errorMessage = "The scenario requires " + fieldName + ".facilityType"
							+ " but submitted file does not have " + fieldName + ".facilityType";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".facilityType", errorMessage));
				} else {
					logger.info(
							fieldName + ".facilityType information is null OR equal in both inputJson and goldJson");
				}

				// practiceSetting
				if (contextOne.hasPracticeSetting() && contextTwo.hasPracticeSetting()) {
					compareCodeableConcept(fieldName + ".practiceSetting", contextOne.getPracticeSetting(),
							contextTwo.getPracticeSetting(), operationOutcome, scenarioName);
				} else if (contextOne.hasPracticeSetting() && !contextTwo.hasPracticeSetting()) {
					String errorMessage = "The scenario does not require " + fieldName + ".practiceSetting"
							+ " but submitted file does have " + fieldName + ".practiceSetting";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".practiceSetting", errorMessage));
				} else if (!contextOne.hasPracticeSetting() && contextTwo.hasPracticeSetting()) {
					String errorMessage = "The scenario requires " + fieldName + ".practiceSetting"
							+ " but submitted file does not have " + fieldName + ".practiceSetting";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".practiceSetting", errorMessage));
				} else {
					logger.info(
							fieldName + ".practiceSetting information is null OR equal in both inputJson and goldJson");
				}

				// sourcePatientInfo
				if (contextOne.hasSourcePatientInfo() && contextTwo.hasSourcePatientInfo()) {
					compareReference(fieldName + ".sourcePatientInfo", contextOne.getSourcePatientInfo(),
							contextTwo.getSourcePatientInfo(), operationOutcome, scenarioName);
				} else if (contextOne.hasSourcePatientInfo() && !contextTwo.hasSourcePatientInfo()) {
					String errorMessage = "The scenario does not require " + fieldName + ".sourcePatientInfo"
							+ " but submitted file does have " + fieldName + ".sourcePatientInfo";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".sourcePatientInfo", errorMessage));
				} else if (!contextOne.hasSourcePatientInfo() && contextTwo.hasSourcePatientInfo()) {
					String errorMessage = "The scenario requires " + fieldName + ".sourcePatientInfo"
							+ " but submitted file does not have " + fieldName + ".sourcePatientInfo";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".sourcePatientInfo", errorMessage));
				} else {
					logger.info(fieldName
							+ ".sourcePatientInfo information is null OR equal in both inputJson and goldJson");
				}

				// related
				if (contextOne.hasRelated() && contextTwo.hasRelated()) {
					compareListOfReference(fieldName + ".related", contextOne.getRelated(), contextTwo.getRelated(),
							operationOutcome, scenarioName);
				} else if (contextOne.hasRelated() && !contextTwo.hasRelated()) {
					String errorMessage = "The scenario does not require " + fieldName + ".related"
							+ " but submitted file does have " + fieldName + ".related";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".related", errorMessage));
				} else if (!contextOne.hasRelated() && contextTwo.hasRelated()) {
					String errorMessage = "The scenario requires " + fieldName + ".related"
							+ " but submitted file does not have " + fieldName + ".related";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".related", errorMessage));
				} else {
					logger.info(fieldName + ".related information is null OR equal in both inputJson and goldJson");
				}

			} else if (contextOne != null && contextTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (contextOne == null && contextTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareDocumentReferenceRelatesToComponent of ComparatorUtils :: ", ex);

		}

	}

	public static void compareTiming(String effectiveTime, Timing effectiveTiming, Timing effectiveTiming2,
			OperationOutcome operationOutcome, String scenarioName) {

	}

	public static void compareListOfObservationReferenceRangeComponent(String fieldName,
			List<ObservationReferenceRangeComponent> referenceRangeListOne,
			List<ObservationReferenceRangeComponent> referenceRangeListTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, referenceRangeListOne, referenceRangeListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < referenceRangeListOne.size();) {
					for (int j = 0; j < referenceRangeListTwo.size(); j++) {
						compareObservationReferenceRangeComponent(fieldName, referenceRangeListOne.get(i),
								referenceRangeListTwo.get(j), operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(referenceRangeListOne) && ObjectUtils.isEmpty(referenceRangeListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(referenceRangeListOne) && ObjectUtils.isNotEmpty(referenceRangeListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfObservationReferenceRangeComponent of ComparatorUtils :: ", ex);

		}

	}

	private static void compareObservationReferenceRangeComponent(String fieldName,
			ObservationReferenceRangeComponent observationReferenceRangeComponentOne,
			ObservationReferenceRangeComponent observationReferenceRangeComponentTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (observationReferenceRangeComponentOne != null && observationReferenceRangeComponentTwo != null) {

				// low
				if (observationReferenceRangeComponentOne.hasLow() && observationReferenceRangeComponentTwo.hasLow()) {
					compareQuantity(fieldName + ".low", observationReferenceRangeComponentOne.getLow(),
							observationReferenceRangeComponentTwo.getLow(), operationOutcome, scenarioName);
				} else if (observationReferenceRangeComponentOne.hasLow()
						&& !observationReferenceRangeComponentTwo.hasLow()) {
					String errorMessage = "The scenario does not require " + fieldName + ".low"
							+ " but submitted file does have " + fieldName + ".low";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".low", errorMessage));
				} else if (!observationReferenceRangeComponentOne.hasLow()
						&& observationReferenceRangeComponentTwo.hasLow()) {
					String errorMessage = "The scenario requires " + fieldName + ".low"
							+ " but submitted file does not have" + fieldName + ".low";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".low", errorMessage));
				} else {
					logger.info(fieldName + ".low information is null OR equal in both inputJson and goldJson");
				}

				// high
				if (observationReferenceRangeComponentOne.hasHigh()
						&& observationReferenceRangeComponentTwo.hasHigh()) {
					compareQuantity(fieldName + ".high", observationReferenceRangeComponentOne.getHigh(),
							observationReferenceRangeComponentTwo.getHigh(), operationOutcome, scenarioName);
				} else if (observationReferenceRangeComponentOne.hasHigh()
						&& !observationReferenceRangeComponentTwo.hasHigh()) {
					String errorMessage = "The scenario does not require " + fieldName + ".high"
							+ " but submitted file does have " + fieldName + ".high";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".high", errorMessage));
				} else if (!observationReferenceRangeComponentOne.hasHigh()
						&& observationReferenceRangeComponentTwo.hasHigh()) {
					String errorMessage = "The scenario requires " + fieldName + ".high"
							+ " but submitted file does not have" + fieldName + ".high";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".high", errorMessage));
				} else {
					logger.info(fieldName + ".high information is null OR equal in both inputJson and goldJson");
				}

				// type
				if (observationReferenceRangeComponentOne.hasType()
						&& observationReferenceRangeComponentTwo.hasType()) {
					compareCodeableConcept(fieldName + ".type", observationReferenceRangeComponentOne.getType(),
							observationReferenceRangeComponentTwo.getType(), operationOutcome, scenarioName);
				} else if (observationReferenceRangeComponentOne.hasType()
						&& !observationReferenceRangeComponentTwo.hasType()) {
					String errorMessage = "The scenario does not require " + fieldName + ".type"
							+ " but submitted file does have " + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else if (!observationReferenceRangeComponentOne.hasType()
						&& observationReferenceRangeComponentTwo.hasType()) {
					String errorMessage = "The scenario requires " + fieldName + ".type"
							+ " but submitted file does not have" + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else {
					logger.info(fieldName + ".type information is null OR equal in both inputJson and goldJson");
				}

				// appliesTo
				if (observationReferenceRangeComponentOne.hasAppliesTo()
						&& observationReferenceRangeComponentTwo.hasAppliesTo()) {
					compareListOfCodeableConcept(fieldName + ".appliesTo",
							observationReferenceRangeComponentOne.getAppliesTo(),
							observationReferenceRangeComponentTwo.getAppliesTo(), operationOutcome, scenarioName);
				} else if (observationReferenceRangeComponentOne.hasAppliesTo()
						&& !observationReferenceRangeComponentTwo.hasAppliesTo()) {
					String errorMessage = "The scenario does not require " + fieldName + ".appliesTo"
							+ " but submitted file does have " + fieldName + ".appliesTo";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".appliesTo", errorMessage));
				} else if (!observationReferenceRangeComponentOne.hasAppliesTo()
						&& observationReferenceRangeComponentTwo.hasAppliesTo()) {
					String errorMessage = "The scenario requires " + fieldName + ".appliesTo"
							+ " but submitted file does not have" + fieldName + ".appliesTo";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".appliesTo", errorMessage));
				} else {
					logger.info(fieldName + ".appliesTo information is null OR equal in both inputJson and goldJson");
				}

			} else if (observationReferenceRangeComponentOne != null && observationReferenceRangeComponentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (observationReferenceRangeComponentOne == null && observationReferenceRangeComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareObservationReferenceRangeComponent of ComparatorUtils :: ", ex);

		}

	}

	public static void compareListOfObservationComponentComponent(String fieldName,
			List<ObservationComponentComponent> componentListOne, List<ObservationComponentComponent> componentListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, componentListOne, componentListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < componentListOne.size();) {
					for (int j = 0; j < componentListTwo.size(); j++) {
						compareObservationComponentComponent(fieldName, componentListOne.get(i),
								componentListTwo.get(j), operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(componentListOne) && ObjectUtils.isEmpty(componentListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(componentListOne) && ObjectUtils.isNotEmpty(componentListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfObservationComponentComponent of ComparatorUtils :: ", ex);

		}
	}

	private static void compareObservationComponentComponent(String fieldName,
			ObservationComponentComponent observationComponentComponentOne,
			ObservationComponentComponent observationComponentComponentTwo, OperationOutcome operationOutcome,
			String scenarioName) {
		try {
			if (observationComponentComponentOne != null && observationComponentComponentTwo != null) {

				// code
				if (observationComponentComponentOne.hasCode() && observationComponentComponentTwo.hasCode()) {
					compareCodeableConcept(fieldName + ".code", observationComponentComponentOne.getCode(),
							observationComponentComponentTwo.getCode(), operationOutcome, scenarioName);
				} else if (observationComponentComponentOne.hasCode() && !observationComponentComponentTwo.hasCode()) {
					String errorMessage = "The scenario does not require " + fieldName + ".code"
							+ " but submitted file does have " + fieldName + ".code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else if (!observationComponentComponentOne.hasCode() && observationComponentComponentTwo.hasCode()) {
					String errorMessage = "The scenario requires " + fieldName + ".code"
							+ " but submitted file does not have" + fieldName + ".code";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else {
					logger.info(fieldName + ".code information is null OR equal in both inputJson and goldJson");
				}

				// value[x]

				// dataAbsentReason
				if (observationComponentComponentOne.hasDataAbsentReason()
						&& observationComponentComponentTwo.hasDataAbsentReason()) {
					compareCodeableConcept(fieldName + ".dataAbsentReason", observationComponentComponentOne.getCode(),
							observationComponentComponentTwo.getCode(), operationOutcome, scenarioName);
				} else if (observationComponentComponentOne.hasDataAbsentReason()
						&& !observationComponentComponentTwo.hasDataAbsentReason()) {
					String errorMessage = "The scenario does not require " + fieldName + ".dataAbsentReason"
							+ " but submitted file does have " + fieldName + ".dataAbsentReason";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else if (!observationComponentComponentOne.hasDataAbsentReason()
						&& observationComponentComponentTwo.hasDataAbsentReason()) {
					String errorMessage = "The scenario requires " + fieldName + ".dataAbsentReason"
							+ " but submitted file does not have" + fieldName + ".dataAbsentReason";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".dataAbsentReason", errorMessage));
				} else {
					logger.info(fieldName
							+ ".dataAbsentReason information is null OR equal in both inputJson and goldJson");
				}

				// interpretation
				if (observationComponentComponentOne.hasInterpretation()
						&& observationComponentComponentTwo.hasInterpretation()) {
					compareCodeableConcept(fieldName + ".interpretation", observationComponentComponentOne.getCode(),
							observationComponentComponentTwo.getCode(), operationOutcome, scenarioName);
				} else if (observationComponentComponentOne.hasInterpretation()
						&& !observationComponentComponentTwo.hasInterpretation()) {
					String errorMessage = "The scenario does not require " + fieldName + ".interpretation"
							+ " but submitted file does have " + fieldName + ".interpretation";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else if (!observationComponentComponentOne.hasInterpretation()
						&& observationComponentComponentTwo.hasInterpretation()) {
					String errorMessage = "The scenario requires " + fieldName + ".interpretation"
							+ " but submitted file does not have" + fieldName + ".interpretation";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".interpretation", errorMessage));
				} else {
					logger.info(
							fieldName + ".interpretation information is null OR equal in both inputJson and goldJson");
				}

				// referenceRange
				if (observationComponentComponentOne.hasInterpretation()
						&& observationComponentComponentTwo.hasInterpretation()) {
					compareListOfObservationReferenceRangeComponent(fieldName + ".referenceRange",
							observationComponentComponentOne.getReferenceRange(),
							observationComponentComponentTwo.getReferenceRange(), operationOutcome, scenarioName);
				} else if (observationComponentComponentOne.hasInterpretation()
						&& !observationComponentComponentTwo.hasInterpretation()) {
					String errorMessage = "The scenario does not require " + fieldName + ".referenceRange"
							+ " but submitted file does have " + fieldName + ".referenceRange";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".code", errorMessage));
				} else if (!observationComponentComponentOne.hasInterpretation()
						&& observationComponentComponentTwo.hasInterpretation()) {
					String errorMessage = "The scenario requires " + fieldName + ".referenceRange"
							+ " but submitted file does not have" + fieldName + ".referenceRange";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".referenceRange", errorMessage));
				} else {
					logger.info(
							fieldName + ".referenceRange information is null OR equal in both inputJson and goldJson");
				}

			} else if (observationComponentComponentOne != null && observationComponentComponentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (observationComponentComponentOne == null && observationComponentComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareObservationComponentComponent of ComparatorUtils :: ", ex);

		}

	}

	public static void compareIntegerType(String fieldName, IntegerType valueIntegerTypeOne,
			IntegerType valueIntegerTypeTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (valueIntegerTypeOne != null && valueIntegerTypeTwo != null) {

				// value
				if (valueIntegerTypeOne.hasValue() && valueIntegerTypeTwo.hasValue()) {
					compareString(fieldName + ".value", valueIntegerTypeOne.getValue().toString(),
							valueIntegerTypeTwo.getValue().toString(), operationOutcome, scenarioName);
				} else if (valueIntegerTypeOne.hasValue() && !valueIntegerTypeTwo.hasValue()) {
					String errorMessage = "The scenario does not require " + fieldName + ".value"
							+ " but submitted file does have " + fieldName + ".value";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".value", errorMessage));
				} else if (!valueIntegerTypeOne.hasValue() && valueIntegerTypeTwo.hasValue()) {
					String errorMessage = "The scenario requires " + fieldName + ".value"
							+ " but submitted file does not have " + fieldName + ".value";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".value", errorMessage));
				} else {
					logger.info(fieldName + ".value information is null OR equal in both inputJson and goldJson");
				}

			} else if (valueIntegerTypeOne != null && valueIntegerTypeTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (valueIntegerTypeOne == null && valueIntegerTypeTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareIntegerType of ComparatorUtils :: ", ex);

		}
	}

	public static void compareRatio(String fieldName, Ratio valueRatioOne, Ratio valueRatioTwo,
			OperationOutcome operationOutcome, String scenarioName) {
		try {
			if (valueRatioOne != null && valueRatioTwo != null) {

				// numerator
				if (valueRatioOne.hasNumerator() && valueRatioTwo.hasNumerator()) {
					compareQuantity(fieldName + ".numerator", valueRatioOne.getNumerator(),
							valueRatioTwo.getNumerator(), operationOutcome, scenarioName);
				} else if (valueRatioOne.hasNumerator() && !valueRatioTwo.hasNumerator()) {
					String errorMessage = "The scenario does not require " + fieldName + ".numerator"
							+ " but submitted file does have " + fieldName + ".numerator";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".numerator", errorMessage));
				} else if (!valueRatioOne.hasNumerator() && valueRatioTwo.hasNumerator()) {
					String errorMessage = "The scenario requires " + fieldName + ".numerator"
							+ " but submitted file does not have " + fieldName + ".numerator";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".numerator", errorMessage));
				} else {
					logger.info(fieldName + ".numerator information is null OR equal in both inputJson and goldJson");
				}

				// denominator
				if (valueRatioOne.hasDenominator() && valueRatioTwo.hasDenominator()) {
					compareQuantity(fieldName + ".denominator", valueRatioOne.getDenominator(),
							valueRatioTwo.getDenominator(), operationOutcome, scenarioName);
				} else if (valueRatioOne.hasDenominator() && !valueRatioTwo.hasDenominator()) {
					String errorMessage = "The scenario does not require " + fieldName + ".denominator"
							+ " but submitted file does have " + fieldName + ".numerator";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".denominator", errorMessage));
				} else if (!valueRatioOne.hasDenominator() && valueRatioTwo.hasDenominator()) {
					String errorMessage = "The scenario requires " + fieldName + ".denominator"
							+ " but submitted file does not have " + fieldName + ".denominator";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".denominator", errorMessage));
				} else {
					logger.info(fieldName + ".denominator information is null OR equal in both inputJson and goldJson");
				}

			} else if (valueRatioOne != null && valueRatioTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (valueRatioOne == null && valueRatioTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareRatio of ComparatorUtils :: ", ex);

		}

	}

	public static void compareTimeType(String fieldName, TimeType timeTypeOne, TimeType timeTypeTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (timeTypeOne != null && timeTypeTwo != null) {

				// valueTime
				if (timeTypeOne.hasValue() && timeTypeTwo.hasValue()) {
					compareString(fieldName + ".valueTime", timeTypeOne.getValue().toString(),
							timeTypeTwo.getValue().toString(), operationOutcome, scenarioName);
				} else if (timeTypeOne.hasValue() && !timeTypeTwo.hasValue()) {
					String errorMessage = "The scenario does not require " + fieldName + ".valueTime"
							+ " but submitted file does have " + fieldName + ".valueTime";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".valueTime", errorMessage));
				} else if (!timeTypeOne.hasValue() && timeTypeTwo.hasValue()) {
					String errorMessage = "The scenario requires " + fieldName + ".valueTime"
							+ " but submitted file does not have " + fieldName + ".valueTime";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".valueTime", errorMessage));
				} else {
					logger.info(fieldName + ".valueTime information is null OR equal in both inputJson and goldJson");
				}

			} else if (timeTypeOne != null && timeTypeTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (timeTypeOne == null && timeTypeTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareTimeType of ComparatorUtils :: ", ex);

		}

	}

	public static void compareListOfProvenanceAgentComponent(String fieldName,
			List<ProvenanceAgentComponent> agentListOne, List<ProvenanceAgentComponent> agentListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, agentListOne, agentListTwo, operationOutcome, scenarioName)) {
				for (int i = 0; i < agentListOne.size();) {
					for (int j = 0; j < agentListTwo.size(); j++) {
						compareProvenanceAgentComponent(fieldName, agentListOne.get(i), agentListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(agentListOne) && ObjectUtils.isEmpty(agentListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(agentListOne) && ObjectUtils.isNotEmpty(agentListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfProvenanceAgentComponent of ComparatorUtils :: ", ex);

		}

	}

	private static void compareProvenanceAgentComponent(String fieldName,
			ProvenanceAgentComponent provenanceAgentComponentOne, ProvenanceAgentComponent provenanceAgentComponentTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (provenanceAgentComponentOne != null && provenanceAgentComponentTwo != null) {

				// type
				if (provenanceAgentComponentOne.hasType() && provenanceAgentComponentTwo.hasType()) {
					compareCodeableConcept(fieldName + ".type", provenanceAgentComponentOne.getType(),
							provenanceAgentComponentTwo.getType(), operationOutcome, scenarioName);
				} else if (provenanceAgentComponentOne.hasType() && !provenanceAgentComponentTwo.hasType()) {
					String errorMessage = "The scenario does not require " + fieldName + ".type"
							+ " but submitted file does have " + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else if (!provenanceAgentComponentOne.hasType() && provenanceAgentComponentTwo.hasType()) {
					String errorMessage = "The scenario requires " + fieldName + ".type"
							+ " but submitted file does not have " + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else {
					logger.info(fieldName + ".type information is null OR equal in both inputJson and goldJson");
				}

				// role
				if (provenanceAgentComponentOne.hasRole() && provenanceAgentComponentTwo.hasRole()) {
					compareListOfCodeableConcept(fieldName + ".role", provenanceAgentComponentOne.getRole(),
							provenanceAgentComponentTwo.getRole(), operationOutcome, scenarioName);
				} else if (provenanceAgentComponentOne.hasRole() && !provenanceAgentComponentTwo.hasRole()) {
					String errorMessage = "The scenario does not require " + fieldName + ".role"
							+ " but submitted file does have " + fieldName + ".role";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".role", errorMessage));
				} else if (!provenanceAgentComponentOne.hasRole() && provenanceAgentComponentTwo.hasRole()) {
					String errorMessage = "The scenario requires " + fieldName + ".role"
							+ " but submitted file does not have " + fieldName + ".role";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".role", errorMessage));
				} else {
					logger.info(fieldName + ".role information is null OR equal in both inputJson and goldJson");
				}

				// who
				if (provenanceAgentComponentOne.hasWho() && provenanceAgentComponentTwo.hasWho()) {
					compareReference(fieldName + ".who", provenanceAgentComponentOne.getWho(),
							provenanceAgentComponentTwo.getWho(), operationOutcome, scenarioName);
				} else if (provenanceAgentComponentOne.hasWho() && !provenanceAgentComponentTwo.hasWho()) {
					String errorMessage = "The scenario does not require " + fieldName + ".who"
							+ " but submitted file does have " + fieldName + ".who";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".who", errorMessage));
				} else if (!provenanceAgentComponentOne.hasWho() && provenanceAgentComponentTwo.hasWho()) {
					String errorMessage = "The scenario requires " + fieldName + ".who"
							+ " but submitted file does not have " + fieldName + ".who";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".who", errorMessage));
				} else {
					logger.info(fieldName + ".who information is null OR equal in both inputJson and goldJson");
				}

				// onBehalfOf
				if (provenanceAgentComponentOne.hasOnBehalfOf() && provenanceAgentComponentTwo.hasOnBehalfOf()) {
					compareReference(fieldName + ".onBehalfOf", provenanceAgentComponentOne.getOnBehalfOf(),
							provenanceAgentComponentTwo.getOnBehalfOf(), operationOutcome, scenarioName);
				} else if (provenanceAgentComponentOne.hasOnBehalfOf()
						&& !provenanceAgentComponentTwo.hasOnBehalfOf()) {
					String errorMessage = "The scenario does not require " + fieldName + ".onBehalfOf"
							+ " but submitted file does have " + fieldName + ".onBehalfOf";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".onBehalfOf", errorMessage));
				} else if (!provenanceAgentComponentOne.hasOnBehalfOf()
						&& provenanceAgentComponentTwo.hasOnBehalfOf()) {
					String errorMessage = "The scenario requires " + fieldName + ".onBehalfOf"
							+ " but submitted file does not have " + fieldName + ".onBehalfOf";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".onBehalfOf", errorMessage));
				} else {
					logger.info(fieldName + ".onBehalfOf information is null OR equal in both inputJson and goldJson");
				}
			} else if (provenanceAgentComponentOne != null && provenanceAgentComponentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (provenanceAgentComponentOne == null && provenanceAgentComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareProvenanceAgentComponent of ComparatorUtils :: ", ex);

		}

	}

	public static void compareListOfProvenanceEntityComponent(String fieldName,
			List<ProvenanceEntityComponent> entityListOne, List<ProvenanceEntityComponent> entityListTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, entityListOne, entityListTwo, operationOutcome, scenarioName)) {
				for (int i = 0; i < entityListOne.size();) {
					for (int j = 0; j < entityListTwo.size(); j++) {
						compareProvenanceEntityComponent(fieldName, entityListOne.get(i), entityListTwo.get(j),
								operationOutcome, scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(entityListOne) && ObjectUtils.isEmpty(entityListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(entityListOne) && ObjectUtils.isNotEmpty(entityListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfProvenanceEntityComponent of ComparatorUtils :: ", ex);

		}
	}

	private static void compareProvenanceEntityComponent(String fieldName,
			ProvenanceEntityComponent provenanceEntityComponentOne,
			ProvenanceEntityComponent provenanceEntityComponentTwo, OperationOutcome operationOutcome,
			String scenarioName) {

		try {
			if (provenanceEntityComponentOne != null && provenanceEntityComponentTwo != null) {

				// role
				if (provenanceEntityComponentOne.hasRole() && provenanceEntityComponentTwo.hasRole()) {
					compareString(fieldName + ".role", provenanceEntityComponentOne.getRole().toString(),
							provenanceEntityComponentTwo.getRole().toString(), operationOutcome, scenarioName);
				} else if (provenanceEntityComponentOne.hasRole() && !provenanceEntityComponentTwo.hasRole()) {
					String errorMessage = "The scenario does not require " + fieldName + ".role"
							+ " but submitted file does have " + fieldName + ".numerator";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ".numerator", errorMessage));
				} else if (!provenanceEntityComponentOne.hasRole() && provenanceEntityComponentTwo.hasRole()) {
					String errorMessage = "The scenario requires " + fieldName + ".role"
							+ " but submitted file does not have " + fieldName + ".role";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".role", errorMessage));
				} else {
					logger.info(fieldName + ".role information is null OR equal in both inputJson and goldJson");
				}

				// what
				if (provenanceEntityComponentOne.hasWhat() && provenanceEntityComponentTwo.hasWhat()) {
					compareReference(fieldName + ". what", provenanceEntityComponentOne.getWhat(),
							provenanceEntityComponentTwo.getWhat(), operationOutcome, scenarioName);
				} else if (provenanceEntityComponentOne.hasWhat() && !provenanceEntityComponentTwo.hasWhat()) {
					String errorMessage = "The scenario does not require " + fieldName + ". what"
							+ " but submitted file does have " + fieldName + ". what";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". what", errorMessage));
				} else if (!provenanceEntityComponentOne.hasWhat() && provenanceEntityComponentTwo.hasWhat()) {
					String errorMessage = "The scenario requires " + fieldName + ". what"
							+ " but submitted file does not have " + fieldName + ". what";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". what", errorMessage));
				} else {
					logger.info(fieldName + ". what information is null OR equal in both inputJson and goldJson");
				}

			} else if (provenanceEntityComponentOne != null && provenanceEntityComponentTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (provenanceEntityComponentOne == null && provenanceEntityComponentTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareProvenanceEntityComponent of ComparatorUtils :: ", ex);

		}
	}

	public static void compareListOfSignature(String fieldName, List<Signature> signatureListOne,
			List<Signature> signatureListTwo, OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (compareListForSizeEquality(fieldName, signatureListOne, signatureListTwo, operationOutcome,
					scenarioName)) {
				for (int i = 0; i < signatureListOne.size();) {
					for (int j = 0; j < signatureListTwo.size(); j++) {
						compareSignature(fieldName, signatureListOne.get(i), signatureListTwo.get(j), operationOutcome,
								scenarioName);
						i++;
					}
				}
			} else if (ObjectUtils.isNotEmpty(signatureListOne) && ObjectUtils.isEmpty(signatureListTwo)) {
				String errorMessage = "The scenario does not require " + fieldName
						+ " , but submitted file does have OR submited size is greater than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (ObjectUtils.isEmpty(signatureListOne) && ObjectUtils.isNotEmpty(signatureListTwo)) {
				String errorMessage = "The scenario requires " + fieldName
						+ " , but submitted file does not have OR submited size is less than the expected " + fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}
		} catch (Exception ex) {
			logger.error("\n Exception in compareListOfProvenanceEntityComponent of ComparatorUtils :: ", ex);

		}
	}

	private static void compareSignature(String fieldName, Signature signatureOne, Signature signatureTwo,
			OperationOutcome operationOutcome, String scenarioName) {

		try {
			if (signatureOne != null && signatureTwo != null) {

				// type
				if (signatureOne.hasType() && signatureTwo.hasType()) {
					compareListOfCoding(fieldName + ".type", signatureOne.getType(), signatureTwo.getType(),
							operationOutcome, scenarioName);
				} else if (signatureOne.hasType() && !signatureTwo.hasType()) {
					String errorMessage = "The scenario does not require " + fieldName + ".type"
							+ " but submitted file does have " + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else if (!signatureOne.hasType() && signatureTwo.hasType()) {
					String errorMessage = "The scenario requires " + fieldName + ".type"
							+ " but submitted file does not have " + fieldName + ".type";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ".type", errorMessage));
				} else {
					logger.info(fieldName + ".type information is null OR equal in both inputJson and goldJson");
				}

				// when
				if (signatureOne.hasWhen() && signatureTwo.hasWhen()) {
					compareDate(fieldName + ". what", signatureTwo.getWhen(), signatureTwo.getWhen(), operationOutcome,
							scenarioName);
				} else if (signatureOne.hasWhen() && !signatureTwo.hasWhen()) {
					String errorMessage = "The scenario does not require " + fieldName + ". what"
							+ " but submitted file does have " + fieldName + ". what";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". what", errorMessage));
				} else if (!signatureOne.hasWhen() && signatureTwo.hasWhen()) {
					String errorMessage = "The scenario requires " + fieldName + ". what"
							+ " but submitted file does not have " + fieldName + ". what";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". what", errorMessage));
				} else {
					logger.info(fieldName + ". what information is null OR equal in both inputJson and goldJson");
				}

				// who
				if (signatureOne.hasWho() && signatureTwo.hasWho()) {
					compareReference(fieldName + ". who", signatureTwo.getWho(), signatureTwo.getWho(),
							operationOutcome, scenarioName);
				} else if (signatureOne.hasWho() && !signatureTwo.hasWho()) {
					String errorMessage = "The scenario does not require " + fieldName + ". who"
							+ " but submitted file does have " + fieldName + ". who";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". who", errorMessage));
				} else if (!signatureOne.hasWho() && signatureTwo.hasWho()) {
					String errorMessage = "The scenario requires " + fieldName + ". who"
							+ " but submitted file does not have " + fieldName + ". who";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". who", errorMessage));
				} else {
					logger.info(fieldName + ". who information is null OR equal in both inputJson and goldJson");
				}
				// onBehalfOf
				if (signatureOne.hasWho() && signatureTwo.hasWho()) {
					compareReference(fieldName + ". onBehalfOf", signatureTwo.getWho(), signatureTwo.getWho(),
							operationOutcome, scenarioName);
				} else if (signatureOne.hasWho() && !signatureTwo.hasWho()) {
					String errorMessage = "The scenario does not require " + fieldName + ". onBehalfOf"
							+ " but submitted file does have " + fieldName + ". onBehalfOf";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ". onBehalfOf", errorMessage));
				} else if (!signatureOne.hasWho() && signatureTwo.hasWho()) {
					String errorMessage = "The scenario requires " + fieldName + ". onBehalfOf"
							+ " but submitted file does not have " + fieldName + ". onBehalfOf";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ". onBehalfOf", errorMessage));
				} else {
					logger.info(fieldName + ". onBehalfOf information is null OR equal in both inputJson and goldJson");
				}
				// targetFormat
				if (signatureOne.hasTargetFormat() && signatureTwo.hasTargetFormat()) {
					compareString(fieldName + ". targetFormat", signatureTwo.getTargetFormat(),
							signatureTwo.getTargetFormat(), operationOutcome, scenarioName);
				} else if (signatureOne.hasTargetFormat() && !signatureTwo.hasTargetFormat()) {
					String errorMessage = "The scenario does not require " + fieldName + ". targetFormat"
							+ " but submitted file does have " + fieldName + ". targetFormat";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ". targetFormat", errorMessage));
				} else if (!signatureOne.hasTargetFormat() && signatureTwo.hasTargetFormat()) {
					String errorMessage = "The scenario requires " + fieldName + ". targetFormat"
							+ " but submitted file does not have " + fieldName + ". targetFormat";
					operationOutcome
							.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName + ". who", errorMessage));
				} else {
					logger.info(
							fieldName + ". targetFormat information is null OR equal in both inputJson and goldJson");
				}

				// sigFormat
				if (signatureOne.hasWho() && signatureTwo.hasWho()) {
					compareString(fieldName + ". sigFormat", signatureTwo.getSigFormat(), signatureTwo.getSigFormat(),
							operationOutcome, scenarioName);
				} else if (signatureOne.hasWho() && !signatureTwo.hasWho()) {
					String errorMessage = "The scenario does not require " + fieldName + ". sigFormat"
							+ " but submitted file does have " + fieldName + ". sigFormat";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ". sigFormat", errorMessage));
				} else if (!signatureOne.hasWho() && signatureTwo.hasWho()) {
					String errorMessage = "The scenario requires " + fieldName + ". sigFormat"
							+ " but submitted file does not have " + fieldName + ". sigFormat";
					operationOutcome.addIssue(
							createScenarioResults(IssueType.CONFLICT, fieldName + ". sigFormat", errorMessage));
				} else {
					logger.info(fieldName + ". sigFormat information is null OR equal in both inputJson and goldJson");
				}

			} else if (signatureOne != null && signatureTwo == null) {
				String errorMessage = "The scenario does not require " + fieldName + " but submitted file does have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			} else if (signatureOne == null && signatureTwo != null) {
				String errorMessage = "The scenario requires " + fieldName + " but submitted file does not have "
						+ fieldName;
				operationOutcome.addIssue(createScenarioResults(IssueType.CONFLICT, fieldName, errorMessage));
			}

		} catch (Exception ex) {
			logger.error("\n Exception in compareProvenanceEntityComponent of ComparatorUtils :: ", ex);

		}

	}

}
