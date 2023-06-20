package com.interopx.fhir.facade.util;

public final class AppConstants {
    private AppConstants(){
    }
    public static final String SINCE="_since";
    public static final String END="_end";
    public static final String TYPE="_type";
    public static final String PREFER="Prefer";
    public static final String ACCEPT="Accept";
    public static final String RESPOND_ASYNC="respond-async";
    public static final String CONTENT_TYPE="application/fhir+json";
    public static final String ACCEPTED="Accepted";
    public static final String HTTP="http";
    public static final String HTTPS="https";
    public static final String BULK_DATA="bulkdata";
    public static final String FHIR="fhir";
    public static final String R4="r4";
    public static final String TIME_ZONE="GMT";
    public static final String DATE_FORMAT="EEE, d MMM yyyy HH:mm:ss zzz";
    public static final String EXPIRES="Expires";
    public static final String CONTENT_LOCATION="Content-Location";
    public static final String EXPORT="$export";
    public static final String EXPORTS="$exports";
    public static final String EXPORTlOCATION="$export-poll-location";
    public static final String X_PROGRESS="X-Progress";
    public static final String DOWNLOAD="download";
    public static final String LINK="Link";
    public static final String CATALINABASE = "catalina.base";
    public static final String RETRYAFTER = "Retry-After";
    public static final String ONETWENTY = "120";
    public static final String SINGLEFORWARDSLASH = "/";
    public static final String DOUBLEFORWARDSLASH = "//";
    public static final String LESSTHAN = "<";
    public static final String GREATERTHAN = ">";
    public static final String INPROGRESS = "In-Progress";
    public static final String COMPLETED = "Completed";
    public static final String NDJSONCONTENTTYPE = "application/fhir+ndjson";
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String DOTNDJSON = ".ndjson";
    public static final String NEWLINECHARACTER = "\n";
    public static final String ASYNCEXECUTOR = "asyncExecutor";
    public static final String ENDPOINT_URL = "endpointUrl";
	public static final String ENCOUNTER = "encounter";
	
	public static final String PRACTITIONERROLE_PRACTITIONER="PractitionerRole:practitioner";
	public static final String PRACTITIONERROLE_ORGANIZATION="PractitionerRole:organization";
	public static final String MEDICATIONREQUEST_MEDICATION="MedicationRequest:medication";	
	public static final String PROVENANCE_TARGET="Provenance:target";
	public static final String UNDERSCORE_INCLUDE="_include";
	public static final String ENCOUNTER_PARAMETER="encounter";
	public static final String UNDERSCORE_REVINCLUDE="_revinclude";
	public static final String PIPE_CHARACTER="|";
	public static final String OBSERVATION_PERFORMER="Observation:performer";
	public static final String DIAGNOSTICREPORT_PERFORMER="DiagnosticReport:performer";
	public static final String DOCUMENTREFERENCE_AUTHOR="DocumentReference:author";
	public static final String LOCATION_ORGANIZATION="Location:organization";
	public static final String DOCUMENTREFERENCE_CUSTODIAN="DocumentReference:custodian";
	public static final String CARETEAM_PARTICIPANT="CareTeam:participant";
	
	public static final String PRACTITIONER_NAME = "practitioner.name";
	public static final String PRACTITIONER_IDENTIFIER = "practitioner.identifier";
	
	public static final String DEBUG_REQUEST_ID = "RequestID==>>";
    public static final String DEBUG_FHIR_FACADE_EVENT = "|FHIR FACADE|EventName==>>";
    
   public static final String DEBUG_REST_OPERATION_TYPE_FOR_RESOURCE= "Rest OperationType::::{} for Resource {}";
   public static final String PATIENT_PATH="Patient/";
   
   public static final String ERROR_PROCESSING_REQUEST="Error in processing request ";
}