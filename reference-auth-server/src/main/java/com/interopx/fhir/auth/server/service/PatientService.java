package com.interopx.fhir.auth.server.service;

import com.interopx.fhir.auth.server.model.DafPatientJson;
import java.util.Date;
import java.util.List;

public interface PatientService {

  public List<DafPatientJson> getAllPatient();

  public DafPatientJson getPatientById(int id);

  public List<DafPatientJson> getPatientJsonForBulkData(List<Integer> patients, Date start);
}
