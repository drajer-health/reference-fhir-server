package com.interopx.fhir.auth.server.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/** AbstractDao */
public abstract class AbstractDao {
  @Autowired private SessionFactory sessionFactory;
  /**
   * get Session
   *
   * @return Session value
   */
  protected Session getSession() {
	return sessionFactory.getCurrentSession();	  
  }
  /**
   * persist
   *
   * @param entity value
   */
  public void persist(Object entity) {
    getSession().persist(entity);
  }
  /**
   * delete
   *
   * @param entity value
   */
  public void delete(Object entity) {
    getSession().delete(entity);
  }

  /**
   * get Session
   *
   * @return Session value
   */
  protected Session getOpenSession() {
    return sessionFactory.openSession();
  }
}
