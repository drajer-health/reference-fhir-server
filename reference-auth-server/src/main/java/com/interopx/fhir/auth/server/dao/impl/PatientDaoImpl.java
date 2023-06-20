package com.interopx.fhir.auth.server.dao.impl;

import com.interopx.fhir.auth.server.dao.AbstractDao;
import com.interopx.fhir.auth.server.dao.PatientDao;
import com.interopx.fhir.auth.server.model.DafPatientJson;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("patientDao")
public class PatientDaoImpl extends AbstractDao implements PatientDao {
  final Logger log = (Logger) LoggerFactory.getLogger(PatientDaoImpl.class);

  public DafPatientJson getPatientById(int id) {
    log.debug("Start in getPatientById() of PatientDaoImpl class ");
    DafPatientJson dafPatientJson = null;
    try {
      dafPatientJson = new DafPatientJson();
      dafPatientJson = (DafPatientJson) getSession().get(DafPatientJson.class, id);

    } catch (Exception e) {
      log.error("Exception in getPatientById() of PatientDaoImpl class ");
    }
    log.debug("End in getPatientById() of PatientDaoImpl class ");
    return dafPatientJson;
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Override
  public List<DafPatientJson> getAllPatient() {
    log.debug("Start in getAllPatient() of PatientDaoImpl class ");
    List<DafPatientJson> list = null;
    try {
      Criteria criteria = getSession().createCriteria(DafPatientJson.class);
      list = new ArrayList<>();
      list = (List<DafPatientJson>) criteria.list();
    } catch (Exception e) {
      log.error("Exception in getAllPatient() of PatientDaoImpl class ");
    }
    log.debug("End in getAllPatient() of PatientDaoImpl class ");
    return list;
  }

  @SuppressWarnings({"unchecked", "deprecation"})
  @Override
  @Transactional
  public List<DafPatientJson> getPatientJsonForBulkData(List<Integer> patients, Date start) {
    log.debug("Start in getPatientJsonForBulkData() of PatientDaoImpl class ");
    List<DafPatientJson> list = null;
    try {
      Criteria criteria = getSession().createCriteria(DafPatientJson.class);
      if (patients != null) {
        criteria.add(Restrictions.in("id", patients));
      }
      if (start != null) {
        criteria.add(Restrictions.ge("updated", start));
      }
      list = new ArrayList<>();
      list = criteria.list();
    } catch (Exception e) {
      log.error("Exception in getPatientJsonForBulkData() of PatientDaoImpl class ");
    }
    log.debug("End in getPatientJsonForBulkData() of PatientDaoImpl class ");
    return list;
  }
}
