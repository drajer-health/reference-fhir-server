package com.interopx.fhir.facade.dao;

import java.sql.Connection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interopx.fhir.facade.configuration.HibernateConfiguration;
import com.interopx.fhir.facade.model.AuthConfiguration;
import com.interopx.fhir.facade.service.AbstractDao;
/**
 * This class is responsible for performing database operations related to Authorization and Authentication
 * @author xyram
 *
 */
@Repository
public class AuthConfigurationDaoImpl extends AbstractDao implements AuthConfigurationDao {
	@Autowired
	HibernateConfiguration hibernateConfig;

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<AuthConfiguration> getAuthConfiguration() {
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			Criteria criteria = session.createCriteria(AuthConfiguration.class);
			return (List<AuthConfiguration>) criteria.list();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
