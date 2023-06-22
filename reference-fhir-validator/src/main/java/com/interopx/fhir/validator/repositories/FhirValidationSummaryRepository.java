package com.interopx.fhir.validator.repositories;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.interopx.fhir.validator.entity.FhirValidationSummaryReport;

@Transactional(readOnly = true)
@Repository
public interface FhirValidationSummaryRepository
    extends JpaRepository<FhirValidationSummaryReport, String> {
	
	 @Query("SELECT v FROM FhirValidationSummaryReport v WHERE lower(v.resourceName) like lower(:resourceName) OR v.resourceName like %:resourceName%")
	 public List<FhirValidationSummaryReport> getReportByResourceName(String resourceName); 
	
     @Query("SELECT v FROM FhirValidationSummaryReport v WHERE v.validatedOn= :validatedOn")
	 public List<FhirValidationSummaryReport> getReportByDate(Timestamp validatedOn);
	
     @Query("SELECT v FROM FhirValidationSummaryReport v WHERE lower(v.resourceName) like lower(:resourceName) AND v.validatedOn=:validatedOn")
	 public List<FhirValidationSummaryReport> getReportByNameAndDate(String resourceName,Timestamp validatedOn);

}
