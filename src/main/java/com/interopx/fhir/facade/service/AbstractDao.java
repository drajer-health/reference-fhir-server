/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.facade.util.HibSessionFactorySingleton;

/** @author admin */
public abstract class AbstractDao {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDao.class);

 
  /** The entityManager. */
  @PersistenceContext private EntityManager entityManager;

  /**
   * Gets the session.
   *
   * @return the session
   */
  protected Session getSession() {
    LOGGER.debug("Entry - getSession Method in AbstractDao ");
    return HibSessionFactorySingleton.getSessionFactory(entityManager.getEntityManagerFactory())
        .getCurrentSession();
  }

  /**
   * Persist.
   *
   * @param entity the entity
   */
  public void persist(Object entity) {
    LOGGER.debug("Entry - persist Method in AbstractDao ");

    getSession().persist(entity);
  }

  /**
   * Delete.
   *
   * @param entity the entity
   */
  public void delete(Object entity) {
    LOGGER.debug("Entry - delete Method in AbstractDao ");

    getSession().delete(entity);
  }

  /**
   * Update.
   *
   * @param entity the entity
   */
  public void update(Object entity) {
    LOGGER.debug("Entry - delete Method in AbstractDao ");

    getSession().update(entity);
  }
 
}
