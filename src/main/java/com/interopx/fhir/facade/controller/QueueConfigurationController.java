/*******************************************************************************
 * Copyright (c) 2020-2022 InteropX LLC, www.interopx.com
 *
 * All Rights Reserved.
 *
 * The source code contained herein, and the packages generated from the source code, are and remain the property of InteropX, LLC. 
 * Access to this source code and the generated packages is granted to authorized customers of InteropX under an InteropX Software License Agreement (SLA).
 * Any use of the source code or the generated packages, not explicitly covered under the signed SLA, is prohibited.
 *******************************************************************************/
package com.interopx.fhir.facade.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.interopx.fhir.facade.service.QueueConfigurationService;
import com.interopx.fhir.facade.util.QueueConfigurationDto;
/**
 * This class is responsible for saving,updating and deleting Queue configuration
 * @author xyram
 *
 */
@RestController
@RequestMapping("/configuration")
public class QueueConfigurationController {
	@Autowired
	private QueueConfigurationService queueConfigurationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueConfigurationController.class);

	/**
	 * This method saves the configuration parameters into database
	 * @param queueConfigurationDto
	 * @return status
	 */
	@PostMapping("/save")
	public ResponseEntity<String> saveConfiguration(@RequestBody QueueConfigurationDto queueConfigurationDto) {
		try {
			queueConfigurationService.saveConfiguration(queueConfigurationDto);
			return new ResponseEntity<>("Configuration created successfully", HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Exception in saveConfiguration Method in QueueConfigurationController:{} ", e.getMessage());
			return new ResponseEntity<>("Unable to save configuration", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	/**
	 * This method updates the configuration parameters into database
	 * @param queueConfigurationDto
	 * @return status
	 */
	@PutMapping("/update/{queueConfigId}")
	public ResponseEntity<String> updateConfiguration(@PathVariable("queueConfigId")  Integer queueConfigId,@RequestBody QueueConfigurationDto queueConfigurationDto) {	
		try {
			queueConfigurationService.updateConfiguration(queueConfigurationDto,queueConfigId);
			return new ResponseEntity<>("Configuration updated successfully", HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Exception in updateConfiguration Method in QueueConfigurationController:{} ", e.getMessage());
			return new ResponseEntity<>("Unable to update configuration", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}


	/**
	 * This method delete the configuration parameters
	 * @param queueConfigurationDto
	 * @return status
	 */
	@DeleteMapping("/delete/{queueConfigId}")
	public ResponseEntity<String> deleteConfiguration(@PathVariable("queueConfigId") Integer queueConfigId) {
		try {
			queueConfigurationService.deleteConfiguration(queueConfigId);
			return new ResponseEntity<>("Configuration deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Exception in deleteConfiguration Method in QueueConfigurationController:{} ", e.getMessage());
			return new ResponseEntity<>("Unable to delete configuration", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
