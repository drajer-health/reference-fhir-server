package com.interopx.fhir.facade.exception;

/**
 * The class contains severity, code and diagnostics for any exceptions used for OperationOutcome
 *
 * @author admin
 */
public class Issue{
    public String severity;
    public String code;
    public String diagnostics;
    
    
    public Issue() {}
	public Issue(String severity, String code, String diagnostics) {
		super();
		this.severity = severity;
		this.code = code;
		this.diagnostics = diagnostics;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDiagnostics() {
		return diagnostics;
	}
	public void setDiagnostics(String diagnostics) {
		this.diagnostics = diagnostics;
	}    
}