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

import org.hibernate.Session;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interopx.fhir.facade.configuration.HibernateConfiguration;
import com.interopx.fhir.facade.exception.InternalErrorException;
import com.interopx.fhir.facade.model.RequestResponseLog;
import com.interopx.fhir.facade.service.AbstractDao;
/**
 * This class is responsible for saving,updating response from Azure Queue into database
 * @author xyram
 *
 */
@Repository
public class AzureQueueDaoImpl extends AbstractDao implements AzureQueueDao {

	private static final Logger logger = LoggerFactory.getLogger(AzureQueueDaoImpl.class);

	@Autowired
	HibernateConfiguration hibernateConfig;

	@Override
	public void updateResponse(RequestResponseLog requestResponseLog) {
		logger.info("----Inside updateResponse() method of AzureQueueDaoImpl class------");
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			session.update(requestResponseLog);
		} catch (Exception e) {
			logger.error("Exception in updating response in db " , e);
			throw new InternalErrorException("500 Internal Server Error: Unable to process request",
					getOperationOutcome(500));
		}
	}

	@Override
	public void saveRequest(RequestResponseLog requestResponseLog) {
		logger.info("----Inside saveRequest() method of AzureQueueDaoImpl class------");
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			session.save(requestResponseLog);
		} catch (Exception e) {
			logger.error("Exception in saving request in db" , e);
			throw new InternalErrorException("500 Internal Server Error: Unable to process request",
					getOperationOutcome(500));
		}

	}

	@Override
	public RequestResponseLog getRequestResponseLogById(String requestId) {
		logger.info("----Inside getRequestResponseLogById() method of AzureQueueDaoImpl class-----");
		RequestResponseLog requestResponseLog = null;
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			requestResponseLog = session.get(RequestResponseLog.class, requestId);
			if (requestResponseLog != null) {
				session.refresh(requestResponseLog);
			}
		} catch (Exception e) {
			logger.error("Exception in getting request from db------" , e);
			throw new InternalErrorException("500 Internal Server Error: Unable to process request",
					getOperationOutcome(500));
		}
		return requestResponseLog;
	}

	@Override
	public void updateStatus(String requestId, String status) {
		logger.info("----Inside updateStatus() method of AzureQueueDaoImpl class---------");
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			RequestResponseLog requestResponseLog = (RequestResponseLog) session.get(RequestResponseLog.class,
					requestId);
			requestResponseLog.setStatus(status);
			session.update(requestResponseLog);

		} catch (Exception e) {
			logger.error("Exception in updating response status in db------" , e);
			throw new InternalErrorException("500 Internal Server Error: Unable to process request",
					getOperationOutcome(500));
		}
	}

	/**
	 * Method to generate the Operation Outcome
	 * 
	 * @param statusCode
	 * @return
	 */
	public static OperationOutcome getOperationOutcome(int statusCode) {
		OperationOutcome operationOutcome = new OperationOutcome();
		if (statusCode == 500) {
			operationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR)
					.setCode(OperationOutcome.IssueType.PROCESSING)
					.setDiagnostics("Server Error: Unable to process Request.");
		}
		return operationOutcome;
	}

}
