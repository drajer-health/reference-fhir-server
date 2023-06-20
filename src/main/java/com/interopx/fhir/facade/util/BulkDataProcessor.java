package com.interopx.fhir.facade.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.interopx.fhir.facade.service.BulkDataRequestService;

/**
 * This class is called by QueueReceiver Scanner to process bulk data.
 * 
 * @author xyram
 *
 */
@Component
public class BulkDataProcessor {
	private static final Logger logger = LoggerFactory.getLogger(BulkDataProcessor.class);

	@Autowired
	BulkDataRequestService bulkDataRequestService;

	/**
	 * This method processes the bulk data response received from fhir server
	 * 
	 * @param responsePayload
	 * @return
	 */
	public String processMessage(String responsePayload) {
		logger.debug("Inside processMessage method of BulkDataProcessor class");
		String requestId = null;
		String status = null;
		String bulkGuid = null;
		try {

			if (responsePayload != null) {
				JSONObject jsonObject = null;
				jsonObject = new JSONObject(responsePayload);
				if (jsonObject.has("status") && jsonObject.has("response")) {
					status = jsonObject.getString("status");
					JSONObject jsonObj = (JSONObject) jsonObject.get("response");
					bulkGuid = jsonObj.getString("job_id");
					requestId = jsonObj.getString("request_id");
				}
			}

			// Update the Bulk request table
			if (requestId != null && status != null && bulkGuid != null) {
				bulkDataRequestService.updateBulkDataRequest(requestId, bulkGuid, status);
			}
			logger.info("Received message content :{}" , responsePayload);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			logger.debug("Current Time: {}" , dtf.format(now));
			logger.debug("====================================================");
		} catch (Exception e) {
			logger.debug("Exception in processMessage() of BulkDataProcessor class ", e);
		}
		return null;
	}

}
