/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.util;

public enum ResourceTypes {
    ALLERGYINTOLERANCE("AllergyIntolerance"),
    BUNDLE("Bundle"),
    CAREPLAN("CarePlan"),
    CARETEAM("CareTeam"),
    CLAIM("Claim"),
    CLAIMRESPONSE("ClaimResponse"),
    CONDITION("Condition"),
    COVERAGE("Coverage"),
    LISTRESOURCE("ListResource"),
    LIST("List"),
    DEVICE("Device"),
    DIAGNOSTICREPORT("DiagnosticReport"),
    DOCUMENTREFERENCE("DocumentReference"),
    ENCOUNTER("Encounter"),
    ENDPOINT("Endpoint"),
    EXPLANATIONOFBENEFIT("ExplanationOfBenefit"),
    FAMILYMEMBERHISTORY("FamilyMemberHistory"),
    GOAL("Goal"),
    GROUP("Group"),
    HEALTHCARESERVICE("HealthcareService"),
    IMMUNIZATION("Immunization"),
    LOCATION("Location"),
    MEDICATIONADMINISTRATION("MedicationAdministration"),
    MEDICATIONDISPENSE("MedicationDispense"),
    MEDICATIONREQUEST("MedicationRequest"),
    MEDICATIONKNOWLEDGE("MedicationKnowledge"),
    MEDICATION("Medication"),
    MEDICATIONSTATEMENT("MedicationStatement"),
    OBSERVATION("Observation"),
    ORGANIZATION("Organization"),
    PATIENT("Patient"),
    PRACTITIONER("Practitioner"),
    PRACTITIONERROLE("PractitionerRole"),
    PROCEDURE("Procedure"),
    PROVENANCE("Provenance"),
    RELATEDPERSON("RelatedPerson"),
    SERVICEREQUEST("ServiceRequest"),
    ORGANIZATIONAFFILIATION("OrganizationAffiliation"),
    INSURANCEPLAN("InsurancePlan"),
    BINARY("Binary");

    private final String value;

    private ResourceTypes(String value){
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}
