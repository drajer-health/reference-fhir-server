package com.interopx.fhir.auth.server.dao;

import com.interopx.fhir.auth.server.model.DafPatientJson;
import java.util.Date;
import java.util.List;

/** Created by Prabhushankar.Byrapp on 8/22/2015. */
public interface PatientDao {
  public List<DafPatientJson> getAllPatient();

  public DafPatientJson getPatientById(int id);

  public List<DafPatientJson> getPatientJsonForBulkData(List<Integer> patients, Date start);
}
