/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.dao;

import java.sql.Connection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interopx.fhir.facade.configuration.HibernateConfiguration;
import com.interopx.fhir.facade.model.QueueConfiguration;
import com.interopx.fhir.facade.service.AbstractDao;
/**
 * This class is responsible for performing database operations of saving,updating and deleting Queue configuration into database
 * @author xyram
 *
 */
@Repository
public class QueueConfigurationDaoImpl extends AbstractDao implements QueueConfigurationDao {
	@Autowired
	HibernateConfiguration hibernateConfig;

	@Override
	public void saveConfiguration(QueueConfiguration queueConfiguration) {
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			session.save(queueConfiguration);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateConfiguration(QueueConfiguration queueConfiguration) {
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			session.update(queueConfiguration);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public QueueConfiguration getConfigurationById(Integer queueConfigId) {
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			QueueConfiguration queueConfiguration = session.get(QueueConfiguration.class, queueConfigId);
			return queueConfiguration;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteConfiguration(Integer queueConfigId) {
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			QueueConfiguration queueConfiguration = session.get(QueueConfiguration.class, queueConfigId);
			session.delete(queueConfiguration);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<QueueConfiguration> getAllQueueConfiguration() {
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			Criteria criteria = session.createCriteria(QueueConfiguration.class);
			return (List<QueueConfiguration>) criteria.list();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
