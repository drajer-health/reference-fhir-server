package com.interopx.fhir.facade.dao;

import java.util.List;

import com.interopx.fhir.facade.model.BulkDataRequest;

public interface BulkDataRequestDao {
	public BulkDataRequest saveBulkDataRequest(BulkDataRequest bdr);
	public BulkDataRequest getBulkDataRequestByJobId(String id);
	public List<BulkDataRequest> getBulkDataRequestsByProcessedFlag(Boolean flag);
	public Integer deleteRequestById(Integer id);
	public void updateBulkDataRequest(String requestId, String bulkGuid, String status);
	public BulkDataRequest getBulkDataRequest(String requestId);
	public BulkDataRequest getBulkDataRequestById(String requestId);
	void updateBulkDataRequestStatus(String requestId,String status);

}
