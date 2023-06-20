package com.interopx.fhir.auth.server.service.impl;

import com.interopx.fhir.auth.server.dao.PatientDao;
import com.interopx.fhir.auth.server.model.DafPatientJson;
import com.interopx.fhir.auth.server.service.PatientService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("patientService")
@Transactional
public class PatientServiceImpl implements PatientService {

  @Autowired private PatientDao patientDao;

  @Override
  @Transactional
  public List<DafPatientJson> getAllPatient() {
    return this.patientDao.getAllPatient();
  }

  @Override
  @Transactional
  public DafPatientJson getPatientById(int id) {
    return this.patientDao.getPatientById(id);
  }

  @Override
  @Transactional
  public List<DafPatientJson> getPatientJsonForBulkData(List<Integer> patients, Date start) {
    return this.patientDao.getPatientJsonForBulkData(patients, start);
  }
}
