package com.interopx.fhir.facade.util;

public enum Parameter {
	SP_IDENTIFIER("identifier"),
	SP_DATE("date"),
	SP_PATIENT("patient"),
	ENCOUNTER("encounter"),
	SP_STATUS("status"),
	SP_CODE("code"),
	SP_CLINICAL_STATUS("clinical-status"),
	SP_ONSET_DATE("onset-date"),
	SP_PERIOD("period"),
	SP_TYPE("type"),
	SP_CATEGORY("category"),
	SP_LIFECYCLE_STATUS("lifecycle-status"),
	SP_TARGET_DATE("target-date"),
	SP_NAME("name"),
	SP_ADDRESS("address"),
	SP_ADDRESS_CITY("address-city"),
	SP_ADDRESS_STATE("address-state"),
	SP_ADDRESS_POSTALCODE("address-postalcode"),
	SP_INTENT("intent"),
	SP_AUTHOREDON("authoredon"),
	SP_FAMILY("family"),
	SP_BIRTHDATE("birthdate"),
	SP_GENDER("gender"),
	SP_SPECIALTY("specialty"),
	SP_CLASS("class"),
	SP_CONTEXT("context"),
	REQUESTED_OPERATION("requestedOperation"),
	REQUEST_URL("requestURL"),
	REQUEST_ID("requestId"),
	GROUP_ID("groupId"),
	RESOURCES_PER_FILE("resourcesPerFile"),
	SINCE("_since"),
	TYPE("_type"),
	RESOURCETYPE("resourceType");
	
    private final String value;

    private Parameter(String value){
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}
