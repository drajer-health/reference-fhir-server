package com.interopx.fhir.facade.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.interopx.fhir.facade.model.BulkDataRequest;



public interface BulkDataRequestService {

	public BulkDataRequest saveBulkDataRequest(BulkDataRequest bdr);
	public BulkDataRequest getBulkDataRequestByJobId(String id);
	public List<BulkDataRequest> getBulkDataRequestsByProcessedFlag(Boolean flag);
	public Integer deleteRequestById(Integer id);
	void updateBulkDataRequest(String requestId, String bulkGuid, String status);
	public BulkDataRequest getBulkDataRequest(String requestId);
	CompletableFuture<BulkDataRequest> getResponseFromQueue(String requestId) throws InterruptedException;
	

}
