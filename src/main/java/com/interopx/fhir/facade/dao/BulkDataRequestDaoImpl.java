package com.interopx.fhir.facade.dao;

import java.sql.Connection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.interopx.fhir.facade.configuration.HibernateConfiguration;
import com.interopx.fhir.facade.model.BulkDataRequest;
import com.interopx.fhir.facade.service.AbstractDao;
import com.interopx.fhir.facade.util.Status;

/**
 * This class is responsible for getting,updating bulk data request and response
 * into database
 * 
 * @author xyram
 *
 */
@Repository
public class BulkDataRequestDaoImpl extends AbstractDao implements BulkDataRequestDao {
	private final Logger logger = LoggerFactory.getLogger(BulkDataRequestDaoImpl.class);

	@Autowired
	HibernateConfiguration hibernateConfig;

	public BulkDataRequest saveBulkDataRequest(BulkDataRequest bdr) {
		logger.debug("Start in saveBulkDataRequest() of BulkDataRequestDaoImpl class ");
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			session.saveOrUpdate(bdr);
		} catch (Exception e) {
			logger.error("Exception in saveBulkDataRequest() of BulkDataRequestDaoImpl class ", e);
		}
		logger.debug("End in saveBulkDataRequest() of BulkDataRequestDaoImpl class ");
		return bdr;
	}

	@SuppressWarnings("deprecation")
	public BulkDataRequest getBulkDataRequestByJobId(String id) {
		logger.debug("Start in getBulkDataRequestByJobId() of BulkDataRequestDaoImpl class ");
		BulkDataRequest bdr = null;
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			Criteria criteria = session.createCriteria(BulkDataRequest.class);
			criteria.add(Restrictions.eq("jobId", id));
			bdr = (BulkDataRequest) criteria.uniqueResult();
		} catch (Exception e) {
			logger.error("Exception in getBulkDataRequestById() of BulkDataRequestDaoImpl class ", e);
		}
		logger.debug("End in getBulkDataRequestById() of BulkDataRequestDaoImpl class ");
		return bdr;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<BulkDataRequest> getBulkDataRequestsByProcessedFlag(Boolean flag) {
		logger.debug("Start in getBulkDataRequestsByProcessedFlag() of BulkDataRequestDaoImpl class ");
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			Criteria crit = session.createCriteria(BulkDataRequest.class);
			crit.add(Restrictions.eq("processedFlag", flag));
			return crit.list();

		} catch (Exception e) {
			logger.error("Exception in getBulkDataRequestsByProcessedFlag() of BulkDataRequestDaoImpl class ", e);
		}
		logger.debug("End in getBulkDataRequestsByProcessedFlag() of BulkDataRequestDaoImpl class ");
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Integer deleteRequestById(Integer id) {
		logger.debug("Start in deleteRequestById() of BulkDataRequestDaoImpl class ");
		Integer res = null;
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			Query qry = session.createQuery("delete from DafBulkDataRequest d where d.requestId=:id");
			qry.setParameter("id", id);
			res = qry.executeUpdate();
		} catch (Exception e) {
			logger.error("Exception in deleteRequestById() of BulkDataRequestDaoImpl class ", e);
		}
		logger.debug("End in deleteRequestById() of BulkDataRequestDaoImpl class ");
		return res;
	}

	/**
	 * Method to update the response payload and status in bull_data_request table
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void updateBulkDataRequest(String requestId, String bulkGuid, String status) {
		logger.info("----Inside updateBulkDataRequest() method of BulkDataRequestDaoImpl class---------");
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			Criteria criteria = session.createCriteria(BulkDataRequest.class);
			criteria.add(Restrictions.eq("requestId", requestId));
			BulkDataRequest bulkDataRequest = (BulkDataRequest) criteria.uniqueResult();
			bulkDataRequest.setJobStatus(status);
			bulkDataRequest.setJobId(bulkGuid);
			bulkDataRequest.setProcessedFlag(true);
			bulkDataRequest.setStatusMessage(Status.EXTRACTION_COMPLETE.toString());
			session.update(bulkDataRequest);

		} catch (Exception e) {
			logger.error("----Exception in updating bulk data response status in db------", e);

		}
	}

	/**
	 * Method to retrieve the bulk data request based on requestId
	 */
	@SuppressWarnings("deprecation")
	@Override
	public BulkDataRequest getBulkDataRequest(String requestId) {
		logger.info("----Inside getBulkDataRequest() method of BulkDataRequestDaoImpl class---------");
		BulkDataRequest bulkDataRequest = null;
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			Criteria criteria = session.createCriteria(BulkDataRequest.class);
			criteria.add(Restrictions.eq("requestId", requestId));
			bulkDataRequest = (BulkDataRequest) criteria.uniqueResult();
		} catch (Exception e) {
			logger.error("----Exception in getting bulk data request from db------", e);
		}
		return bulkDataRequest;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BulkDataRequest getBulkDataRequestById(String requestId) {

		logger.info("----Inside getRequestResponseLogById() method of AzureQueueDaoImpl{} class-----");
		BulkDataRequest bulkDataRequest = null;
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			Criteria criteria = session.createCriteria(BulkDataRequest.class);
			criteria.add(Restrictions.eq("requestId", requestId));
			bulkDataRequest = (BulkDataRequest) criteria.uniqueResult();
			if (bulkDataRequest != null) {
				session.refresh(bulkDataRequest);
			}
		} catch (Exception e) {
			logger.error("----Exception in getting request from db------" , e);

		}
		return bulkDataRequest;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updateBulkDataRequestStatus(String requestId, String status) {
		logger.info("----Inside updateStatus() method of BulkDataRequestDaoImpl{} class---------");
		try (Connection connection = hibernateConfig.getConnection()) {
			Session session = getSession();
			BulkDataRequest bulkDataRequest = null;
			Criteria criteria = session.createCriteria(BulkDataRequest.class);
			criteria.add(Restrictions.eq("requestId", requestId));
			bulkDataRequest = (BulkDataRequest) criteria.uniqueResult();
			bulkDataRequest.setJobStatus(status);
			session.update(bulkDataRequest);

		} catch (Exception e) {
			logger.error("----Exception in updating response status in db------" , e);
		}

	}

}
