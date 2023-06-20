package com.interopx.fhir.facade.util;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;

/**
 * @class HibSessionFactorySingleton -> This is helper class to implement Singleton class with using
 *     getSessionFactory() method
 * @author BirendraKumar
 * @date JUN 13,2022
 */
public class HibSessionFactorySingleton {

  // Static variable reference of instance of type Singleton
  private static SessionFactory sessionFactory = null;

  private HibSessionFactorySingleton() {}

  /**
   * static method to get SessionFactory object
   *
   * @param entityManagerFactory
   * @return
   */
  @SuppressWarnings("deprecation")
  public static SessionFactory getSessionFactory(EntityManagerFactory entityManagerFactory) {
    if (entityManagerFactory == null) {
      return null;
    }
    if (sessionFactory == null) {
      HibernateEntityManagerFactory entityManagerFactoryImpl =
          (HibernateEntityManagerFactory) entityManagerFactory;
      sessionFactory = entityManagerFactoryImpl.getSessionFactory();
    }
    return sessionFactory;
  }
}
