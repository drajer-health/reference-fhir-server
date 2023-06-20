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

public enum Status {
	PENDING("Pending"),
    COMPLETED("Completed"),
    REQUEST_SENT("Request Sent"),
    IN_PROGRESS("InProgress"),
    ABORTED("Aborted"),
    ERROR("Error"),
    CANCELLED("Cancelled"), 
	EXTRACTION_COMPLETE("Extraction Completed"),
	EXTRACTION_STARTED("Extraction Started"),
	SUCCESS("Success");

    private final String value;

    private Status(String value){
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}
