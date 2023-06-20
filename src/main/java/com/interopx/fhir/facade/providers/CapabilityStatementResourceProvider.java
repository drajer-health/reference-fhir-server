/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.providers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.r4.hapi.rest.server.ServerCapabilityStatementProvider;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceOperationComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestSecurityComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementSoftwareComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.RestfulCapabilityMode;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.facade.util.CommonUtil;
import com.interopx.fhir.facade.util.ProfileConstants.CommonProfiles;
import com.interopx.fhir.facade.util.ProfileConstants.UsCoreProfiles;
import com.interopx.fhir.facade.util.ResourceTypes;

import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.api.server.RequestDetails;
/**
 * This class generates the capability Statement of the server
 * @author xyram
 *
 */
public class CapabilityStatementResourceProvider extends ServerCapabilityStatementProvider {
	private static final Logger logger = LoggerFactory.getLogger(CapabilityStatementResourceProvider.class);

	@Metadata
	public CapabilityStatement getConformance(HttpServletRequest request, RequestDetails theRequestDetails) {		
		logger.debug("Inside getConformance method of CapabilityStatementResourceProvider class");
		CapabilityStatement conformance = super.getServerConformance(request, theRequestDetails);
		conformance.getFhirVersion();
		conformance.setId("IxFhirFacade");
		conformance.setUrl(theRequestDetails.getCompleteUrl());
		conformance.setVersion("2.0");
		conformance.setName("InteropX FHIR Facade");
		conformance.setStatus(PublicationStatus.ACTIVE);
		conformance.setPublisher("InteropX");
		List<CanonicalType> instantiatesList = new ArrayList<>();
		CanonicalType instantiateURL = new CanonicalType();
		instantiateURL.setValue("http://hl7.org/fhir/uv/bulkdata/CapabilityStatement/bulk-data");
		instantiatesList.add(instantiateURL);
		conformance.setInstantiates(instantiatesList);
		// Set Software
		CapabilityStatementSoftwareComponent softwareComponent = new CapabilityStatementSoftwareComponent();
		softwareComponent.setName("IX FHIR Facade");
		softwareComponent.setVersion("1.6");
		conformance.setSoftware(softwareComponent);
		//Set format
		List<CodeType> formats = new ArrayList<>();
		CodeType theFormat = new CodeType();
		theFormat.setValue("xml");
		formats.add(theFormat);
		theFormat = new CodeType();
		theFormat.setValue("json");
		formats.add(theFormat);
		conformance.setFormat(formats);
		// Set Rest
		List<CapabilityStatementRestComponent> restList = new ArrayList<>();
		CapabilityStatementRestComponent rest = new CapabilityStatementRestComponent();
		rest.setMode(RestfulCapabilityMode.SERVER);

		CapabilityStatementRestSecurityComponent restSecurity = new CapabilityStatementRestSecurityComponent();

		Extension conformanceExtension = new Extension(
				"http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");
		
		if(CommonUtil.authMap != null && !CommonUtil.authMap.isEmpty()) {
			conformanceExtension.addExtension(new Extension("authorize", new UriType(CommonUtil.authMap.get("authorization_url"))));
			conformanceExtension.addExtension(new Extension("token", new UriType(CommonUtil.authMap.get("token_url"))));
		}
		restSecurity.addExtension(conformanceExtension);

		CodeableConcept serviceCC = new CodeableConcept();
		List<Coding> theCodingList = new ArrayList<>();
		Coding theCoding = new Coding();
		theCoding.setCode("SMART-on-FHIR");
		theCoding.setSystem("http://terminology.hl7.org/CodeSystem/restful-security-service");
		theCodingList.add(theCoding);
		serviceCC.setCoding(theCodingList);
		serviceCC.setText("OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org)");
		restSecurity.getService().add(serviceCC);
		restSecurity.setCors(true);
		rest.setSecurity(restSecurity);

		List<CapabilityStatement.CapabilityStatementRestResourceComponent> resources = conformance.getRest().get(0)
				.getResource();
		for (CapabilityStatement.CapabilityStatementRestResourceComponent resource : resources) {
			resource.setSearchRevInclude(resource.getSearchInclude()); // HAPI Fhir is not setting this value as per
			List<StringType> revIncludeList = new ArrayList<>();															// design, manually setting the values
			List<CanonicalType> supportedProfileList = new ArrayList<>();
			StringType revInclude = new StringType();
			CanonicalType theSupportedProfile = new CanonicalType();
			if (resource.getType().equalsIgnoreCase(ResourceTypes.ALLERGYINTOLERANCE.toString())) {
				theSupportedProfile.setValue(UsCoreProfiles.ALLERGYINTOLERANCE_URL);
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.CAREPLAN.toString())) {
				theSupportedProfile.setValue(UsCoreProfiles.CAREPLAN_URL);
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.CARETEAM.toString())) {
				theSupportedProfile.setValue(UsCoreProfiles.CARETEAM_URL);
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				List<StringType> includeList = new ArrayList<>();															 
				StringType include = new StringType();
				include.setValue("CareTeam:participant");
				includeList.add(include);
				resource.setSearchInclude(includeList);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.CONDITION.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.CONDITION_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.DEVICE.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.DEVICE_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.DIAGNOSTICREPORT.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.DIAGNOSTICREPORT_LAB_URL);
				CanonicalType noteSupportedProfile = new CanonicalType();
				noteSupportedProfile.setValue(UsCoreProfiles.DIAGNOSTICREPORT_NOTE_URL);
				supportedProfileList.add(noteSupportedProfile);
				List<StringType> includeList = new ArrayList<>();															 
				StringType include = new StringType();
				include.setValue("DiagnosticReport:performer");
				includeList.add(include);
				resource.setSearchInclude(includeList);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.DOCUMENTREFERENCE.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.DOCUMENTREFERENCE_URL);
				List<StringType> includeList = new ArrayList<>();															 
				StringType include = new StringType();
				include.setValue("DocumentReference:custodian");
				includeList.add(include);
				include = new StringType();
				include.setValue("DocumentReference:author");
				includeList.add(include);
				resource.setSearchInclude(includeList);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.ENCOUNTER.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.ENCOUNTER_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.GOAL.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.GOAL_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.IMMUNIZATION.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.IMMUNIZATION_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.LOCATION.toString())) {
				List<StringType> includeList = new ArrayList<>();															 
				StringType include = new StringType();
				include.setValue("Location:organization");
				includeList.add(include);
				resource.setSearchInclude(includeList);
				theSupportedProfile.setValue(UsCoreProfiles.LOCATION_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.MEDICATIONADMINISTRATION.toString())) {
					revInclude.setValue("Provenance:target");
					revIncludeList.add(revInclude);
					resource.setSearchRevInclude(revIncludeList);
					theSupportedProfile.setValue(CommonProfiles.MEDICATIONADMINISTRATION_PROFILE_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.MEDICATION.toString())) {
				theSupportedProfile.setValue(UsCoreProfiles.MEDICATION_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.MEDICATIONREQUEST.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.MEDICATIONREQUEST_URL);
				List<StringType> includeList = new ArrayList<>();															 
				StringType include = new StringType();
				include.setValue("MedicationRequest:medication");
				includeList.add(include);
				resource.setSearchInclude(includeList);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.OBSERVATION.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.OBSERVATION_LAB_URL);
				CanonicalType pediatricBmi = new CanonicalType();
				pediatricBmi.setValue(UsCoreProfiles.OBSERVATION_PEDIATRIC_BMI);
				supportedProfileList.add(pediatricBmi);
				CanonicalType pediatricHeadPercentile = new CanonicalType();
				pediatricHeadPercentile
						.setValue(UsCoreProfiles.OBSERVATION_PEDIATRIC_HEAD_OCCIPITAL_FRONTAL_CIRCUMFERENCE_PERCENTILE);
				supportedProfileList.add(pediatricHeadPercentile);
				CanonicalType pediatricWeightHeight = new CanonicalType();
				pediatricWeightHeight.setValue(UsCoreProfiles.OBSERVATION_PEDIATRIC_WEIGHT_HEIGHT);
				supportedProfileList.add(pediatricWeightHeight);
				CanonicalType pulseOximetry = new CanonicalType();
				pulseOximetry.setValue(UsCoreProfiles.OBSERVATION_PULSE_OXIMETRY);
				supportedProfileList.add(pulseOximetry);
				CanonicalType socialHistory = new CanonicalType();
				socialHistory.setValue(UsCoreProfiles.OBSERVATION_SOCIALHISTORY_URL);
				supportedProfileList.add(socialHistory);
				CanonicalType commonBP = new CanonicalType();
				commonBP.setValue(CommonProfiles.OBSERVATION_BP);
				supportedProfileList.add(commonBP);
				CanonicalType commonBMI = new CanonicalType();
				commonBMI.setValue(CommonProfiles.OBSERVATION_BMI);
				supportedProfileList.add(commonBMI);
				CanonicalType commonBodyWeight = new CanonicalType();
				commonBodyWeight.setValue(CommonProfiles.OBSERVATION_BODYWEIGHT);
				supportedProfileList.add(commonBodyWeight);
				CanonicalType commonBodyHeight = new CanonicalType();
				commonBodyHeight.setValue(CommonProfiles.OBSERVATION_BODYHEIGHT);
				supportedProfileList.add(commonBodyHeight);
				CanonicalType commonHeartRate = new CanonicalType();
				commonHeartRate.setValue(CommonProfiles.OBSERVATION_HEARTRATE);
				supportedProfileList.add(commonHeartRate);
				CanonicalType commonRespRate = new CanonicalType();
				commonRespRate.setValue(CommonProfiles.OBSERVATION_RESPRATE);
				supportedProfileList.add(commonRespRate);
				CanonicalType commonBodyTemp = new CanonicalType();
				commonBodyTemp.setValue(CommonProfiles.OBSERVATION_BODYTEMP);
				supportedProfileList.add(commonBodyTemp);
				List<StringType> includeList = new ArrayList<>();															 
				StringType include = new StringType();
				include.setValue("Observation:performer");
				includeList.add(include);
				resource.setSearchInclude(includeList);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.ORGANIZATION.toString())) {
				theSupportedProfile.setValue(UsCoreProfiles.ORGANIZATION_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.PATIENT.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.PATIENT_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.PRACTITIONER.toString())) {
				theSupportedProfile.setValue(UsCoreProfiles.PRACTITIONER_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.PRACTITIONERROLE.toString())) {
				List<StringType> includeList = new ArrayList<>();															 
				StringType include = new StringType();
				include.setValue("PractitionerRole:practitioner");
				includeList.add(include);
				include = new StringType();
				include.setValue("PractitionerRole:organization");
				includeList.add(include);
				resource.setSearchInclude(includeList);
				theSupportedProfile.setValue(UsCoreProfiles.PRACTITIONERROLE_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.PROCEDURE.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.PROCEDURE_URL);
			} else if (resource.getType().equalsIgnoreCase(ResourceTypes.PROVENANCE.toString())) {
				revInclude.setValue("Provenance:target");
				revIncludeList.add(revInclude);
				resource.setSearchRevInclude(revIncludeList);
				theSupportedProfile.setValue(UsCoreProfiles.PROVENANCE_URL);
			} 
			supportedProfileList.add(theSupportedProfile);
			resource.setSupportedProfile(supportedProfileList);
			
			if (resource.getType().equalsIgnoreCase(ResourceTypes.GROUP.toString())) {
				((CapabilityStatementRestResourceOperationComponent) resource.addOperation().addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/capabilitystatement-expectation", new CodeType("SHOULD"))))
		        .setDefinition("http://hl7.org/fhir/uv/bulkdata/OperationDefinition/group-export")
		        .setDocumentation("FHIR Operation to obtain a detailed set of FHIR resources of diverse resource types pertaining to all patients in specified [Group](https://www.hl7.org/fhir/group.html).\\n\\nIf a FHIR server supports Group-level data export, it SHOULD support reading and searching for `Group` resource. This enables clients to discover available groups based on stable characteristics such as `Group.identifier`.\\n\\nThe [Patient Compartment](https://www.hl7.org/fhir/compartmentdefinition-patient.html) SHOULD be used as a point of reference for recommended resources to be returned and, where applicable, Patient resources SHOULD be returned. Other resources outside of the patient compartment that are helpful in interpreting the patient data (such as Organization and Practitioner) MAY also be returned.")
		        .setName("export");
				resource.setProfile("");
				
			}
		}
		
		List<CapabilityStatementRestResourceOperationComponent> operationList = new ArrayList<>();
		CapabilityStatementRestResourceOperationComponent exportOperation = new CapabilityStatementRestResourceOperationComponent();
        exportOperation.setName("export");
        exportOperation.setDefinition("http://hl7.org/fhir/uv/bulkdata/OperationDefinition/export");
        exportOperation.setDocumentation("FHIR Operation to export data from a FHIR server, whether or not it is associated with a patient. This supports use cases like backing up a server, or exporting terminology data by restricting the resources returned using the `_type` parameter.");
        exportOperation.addExtension(new Extension("http://hl7.org/fhir/StructureDefinition/capabilitystatement-expectation", new CodeType("SHOULD")));
        operationList.add(exportOperation);
		
        rest.setOperation(operationList);
		rest.setResource(resources);
		restList.add(rest);
		conformance.setRest(restList);

		return conformance;
	}
}