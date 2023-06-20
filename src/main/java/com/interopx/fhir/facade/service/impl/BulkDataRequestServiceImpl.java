package com.interopx.fhir.facade.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interopx.fhir.facade.dao.BulkDataRequestDao;
import com.interopx.fhir.facade.model.BulkDataRequest;
import com.interopx.fhir.facade.service.BulkDataRequestService;
import com.interopx.fhir.facade.util.Status;

@Service
@Transactional
public class BulkDataRequestServiceImpl implements BulkDataRequestService {
	private final Logger logger = LoggerFactory.getLogger(BulkDataRequestServiceImpl.class);
	@Autowired
	private BulkDataRequestDao bdrDao;
	@Value("${time.out.limit}")
	private long threadTimeOutTime;

	public BulkDataRequest saveBulkDataRequest(BulkDataRequest bdr) {
		return bdrDao.saveBulkDataRequest(bdr);
	}

	public BulkDataRequest getBulkDataRequestByJobId(String id) {
		return bdrDao.getBulkDataRequestByJobId(id);
	}

	@Override
	public List<BulkDataRequest> getBulkDataRequestsByProcessedFlag(Boolean flag) {
		return bdrDao.getBulkDataRequestsByProcessedFlag(flag);
	}

	@Override
	public Integer deleteRequestById(Integer id) {
		return bdrDao.deleteRequestById(id);
	}

	@Override
	public void updateBulkDataRequest(String requestId, String bulkGuid, String status) {
		bdrDao.updateBulkDataRequest(requestId, bulkGuid, status);

	}

	@Override
	public BulkDataRequest getBulkDataRequest(String requestId) {
		return bdrDao.getBulkDataRequest(requestId);
	}

	@Override
	@Async("taskExecutor")
	public CompletableFuture<BulkDataRequest> getResponseFromQueue(String requestId) throws InterruptedException {
		logger.debug("----Inside BulkDataRequestServiceImpl,getting response from Queue{}",
				Thread.currentThread().getName());
		BulkDataRequest response = getResponse(requestId);
		return CompletableFuture.completedFuture(response);
	}

	private BulkDataRequest getResponse(String requestId) throws InterruptedException {
		logger.debug("------Inside  getResponse method for requestId {} --------", requestId);
		boolean isReceived = false;
		BulkDataRequest bulkDataRequest = null;
		long deactiveTimeForThread = System.currentTimeMillis() + TimeUnit.MILLISECONDS.toMillis(threadTimeOutTime);
		logger.debug("deactiveTimeForThread=======>{}", deactiveTimeForThread);

		while (!isReceived) {
			logger.debug("----task2: {} ", Thread.currentThread().getName());
			Thread.sleep(2000);
			bulkDataRequest = bdrDao.getBulkDataRequestById(requestId);

			if (deactiveTimeForThread < System.currentTimeMillis()) {
				logger.debug("-------TRY TO TERMINATE ASYNC THREAD---- {}", Thread.currentThread().getName());
				if (Thread.currentThread().isAlive()) {
					logger.debug("ASYSC THREAD IS ALIVE ==============>{}", Thread.currentThread().getName());
					sleep(1000);
					Thread.currentThread().interrupt();
					if (Thread.currentThread().isInterrupted() == true) {
						logger.debug("-----THREAD GOT INTERRUPTED------ ");
						bdrDao.updateBulkDataRequestStatus(requestId, Status.ABORTED.toString());
						break;
					}
				}
			}

			if (bulkDataRequest.getJobId() != null
					&& bulkDataRequest.getJobStatus().equalsIgnoreCase(Status.SUCCESS.toString())) {
				logger.debug("------JobId status found in DB for requestId {} --------", requestId);
				isReceived = true;
				break;
			}
		}

		return bulkDataRequest;

	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			logger.error(Thread.currentThread().getName(), " {} is interrupted");
			logger.error("{}", e);
		}
	}
}
