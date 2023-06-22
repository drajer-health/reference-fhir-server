package com.interopx.fhir.validator.resource.content.comparer;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interopx.fhir.validator.util.ComparatorUtils;
import com.interopx.fhir.validator.util.ScenarioConstants.ResourceNames;
import com.interopx.fhir.validator.util.ScenarioConstants.ScenarioNameConstants;

public class DeviceContentComparator {
	public static final Logger logger = LoggerFactory.getLogger(DeviceContentComparator.class);

	public static void compare(Resource sourceResource, Resource targetResource, String scenarioName,
			OperationOutcome operationOutcome) {

		logger.info("Entry - DeviceContentComparator.compare");
		logger.info("Entry - DeviceContentComparator.compare - sourceResource ::\n" + sourceResource);
		logger.info("Entry - DeviceContentComparator.compare - targetResource ::\n" + targetResource);

		if (ObjectUtils.isNotEmpty(sourceResource) && ObjectUtils.isNotEmpty(targetResource)) {
			Device sourceDevice = (Device) sourceResource;
			Device targetDevice = (Device) targetResource;
			logger.info("sourceProcedure :::::\n" + sourceDevice);
			if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_FULL_DEVICE)) {
				logger.info("sourceDevice ::::: compareFullContent\n");
				compareFullContent(sourceDevice, targetDevice, operationOutcome,scenarioName);
				logger.info("sourceDevice ::::: compareFullContent\n");
			} else if (scenarioName.equalsIgnoreCase(ScenarioNameConstants.USCDI_ADDITIONAL_DEVICE)) {
				compareAdditionalContent(sourceDevice, targetDevice, operationOutcome,scenarioName);
			}
		}
		logger.info("Exit - DeviceContentComparator.compare");
	}

	private static void compareAdditionalContent(Device sourceDevice, Device targetDevice,
			OperationOutcome operationOutcome,String scenarioName) {

		// udiCarrier
		ComparatorUtils.compareListOfDeviceUdiCarrierComponent(ResourceNames.UDI_CARRIER, sourceDevice.getUdiCarrier(),
				targetDevice.getUdiCarrier(), operationOutcome,scenarioName);
		// distinctIdentifier
		ComparatorUtils.compareString(ResourceNames.DISTINCT_IDENTIFIER, sourceDevice.getDistinctIdentifier(),
				targetDevice.getDistinctIdentifier(), operationOutcome,scenarioName);
		// manufactureDate
		ComparatorUtils.compareDate(ResourceNames.MANUFACTURER_DATE, sourceDevice.getManufactureDate(),
				targetDevice.getManufactureDate(), operationOutcome,scenarioName);
		// expirationDate
		ComparatorUtils.compareDate(ResourceNames.EXPIRATION_DATE, sourceDevice.getExpirationDate(),
				targetDevice.getExpirationDate(), operationOutcome,scenarioName);

		// lotNumber
		ComparatorUtils.compareString(ResourceNames.LOT_NUMBER, sourceDevice.getLotNumber(),
				targetDevice.getLotNumber(), operationOutcome,scenarioName);

		// serialNumber
		ComparatorUtils.compareString(ResourceNames.SERIAL_NUMBER, sourceDevice.getSerialNumber(),
				targetDevice.getSerialNumber(), operationOutcome,scenarioName);
		// type
		ComparatorUtils.compareCodeableConcept(ResourceNames.TYPE, sourceDevice.getType(), targetDevice.getType(),
				operationOutcome,scenarioName);
		// patient
		ComparatorUtils.compareReference(ResourceNames.PATIENT, sourceDevice.getPatient(), targetDevice.getPatient(),
				operationOutcome,scenarioName);
	}

	private static void compareFullContent(Device sourceDevice, Device targetDevice,
			OperationOutcome operationOutcome,String scenarioName) {

		//id
		ComparatorUtils.compareString(ResourceNames.ID,sourceDevice.getId(),targetDevice.getId(),operationOutcome,scenarioName);
		
		// identifier
		ComparatorUtils.compareListOfIdentifier(ResourceNames.IDENTIFIER, sourceDevice.getIdentifier(),
				targetDevice.getIdentifier(), operationOutcome,scenarioName);

		// definition
		ComparatorUtils.compareReference(ResourceNames.DEFINITION, sourceDevice.getDefinition(),
				targetDevice.getDefinition(), operationOutcome,scenarioName);

		// udiCarrier
		ComparatorUtils.compareListOfDeviceUdiCarrierComponent(ResourceNames.UDI_CARRIER, sourceDevice.getUdiCarrier(),
				targetDevice.getUdiCarrier(), operationOutcome,scenarioName);

		// status
		String source = null;
		String target=null;
		if(sourceDevice.getStatus()!=null) {
			source = sourceDevice.getStatus().toString();	
		}
		if(targetDevice.getStatus()!=null) {
			target = targetDevice.getStatus().toString();	
		}
		ComparatorUtils.compareString(ResourceNames.STATUS,source,target,operationOutcome, scenarioName);

		// statusReason
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.STATUS_REASON, sourceDevice.getStatusReason(),
				targetDevice.getStatusReason(), operationOutcome,scenarioName);

		// distinctIdentifier
		ComparatorUtils.compareString(ResourceNames.DISTINCT_IDENTIFIER, sourceDevice.getDistinctIdentifier(),
				targetDevice.getDistinctIdentifier(), operationOutcome,scenarioName);

		// manufacturer
		ComparatorUtils.compareString(ResourceNames.MANUFACTURER, sourceDevice.getManufacturer(),
				targetDevice.getManufacturer(), operationOutcome,scenarioName);

		// manufactureDate
		ComparatorUtils.compareDate(ResourceNames.MANUFACTURER_DATE, sourceDevice.getManufactureDate(),
				targetDevice.getManufactureDate(), operationOutcome,scenarioName);

		// expirationDate
		ComparatorUtils.compareDate(ResourceNames.EXPIRATION_DATE, sourceDevice.getExpirationDate(),
				targetDevice.getExpirationDate(), operationOutcome,scenarioName);

		// lotNumber
		ComparatorUtils.compareString(ResourceNames.LOT_NUMBER, sourceDevice.getLotNumber(),
				targetDevice.getLotNumber(), operationOutcome,scenarioName);

		// serialNumber
		ComparatorUtils.compareString(ResourceNames.SERIAL_NUMBER, sourceDevice.getSerialNumber(),
				targetDevice.getSerialNumber(), operationOutcome,scenarioName);

		// deviceName
		ComparatorUtils.compareListOfDeviceDeviceNameComponent(ResourceNames.DEVICE_NAME, sourceDevice.getDeviceName(),
				targetDevice.getDeviceName(), operationOutcome,scenarioName);

		// modelNumber
		ComparatorUtils.compareString(ResourceNames.MODEL_NUMBER, sourceDevice.getModelNumber(),
				targetDevice.getModelNumber(), operationOutcome,scenarioName);

		// partNumber
		ComparatorUtils.compareString(ResourceNames.PART_NUMBER, sourceDevice.getPartNumber(),
				targetDevice.getPartNumber(), operationOutcome,scenarioName);

		// type
		ComparatorUtils.compareCodeableConcept(ResourceNames.TYPE, sourceDevice.getType(), targetDevice.getType(),
				operationOutcome,scenarioName);

		// specialization
		ComparatorUtils.compareListOfDeviceSpecializationComponent(ResourceNames.SPECIALIZATION,
				sourceDevice.getSpecialization(), targetDevice.getSpecialization(), operationOutcome,scenarioName);

		// version
		ComparatorUtils.compareListOfDeviceVersionComponent(ResourceNames.VERSION, sourceDevice.getVersion(),
				targetDevice.getVersion(), operationOutcome,scenarioName);

		// property
		ComparatorUtils.compareListOfDevicePropertyComponent(ResourceNames.PROPERTY, sourceDevice.getProperty(),
				targetDevice.getProperty(), operationOutcome,scenarioName);

		// patient
		ComparatorUtils.compareReference(ResourceNames.PATIENT, sourceDevice.getPatient(), targetDevice.getPatient(),
				operationOutcome,scenarioName);
		// owner
		ComparatorUtils.compareReference(ResourceNames.OWNER, sourceDevice.getOwner(), targetDevice.getOwner(),
				operationOutcome,scenarioName);

		// contact
		ComparatorUtils.compareListOfContactPoint(ResourceNames.CONTACT, sourceDevice.getContact(),
				targetDevice.getContact(), operationOutcome,scenarioName);

		// location
		ComparatorUtils.compareReference(ResourceNames.LOCATION, sourceDevice.getLocation(), targetDevice.getLocation(),
				operationOutcome,scenarioName);

		// url
		ComparatorUtils.compareString(ResourceNames.URL, sourceDevice.getUrl(), targetDevice.getUrl(),
				operationOutcome,scenarioName);

		// note
		ComparatorUtils.compareListOfAnnotation(ResourceNames.NOTE, sourceDevice.getNote(), targetDevice.getNote(),
				operationOutcome,scenarioName);

		// safety
		ComparatorUtils.compareListOfCodeableConcept(ResourceNames.SAFETY, sourceDevice.getSafety(),
				targetDevice.getSafety(), operationOutcome,scenarioName);

		// parent
		ComparatorUtils.compareReference(ResourceNames.PARENT, sourceDevice.getParent(), targetDevice.getParent(),
				operationOutcome,scenarioName);

	}

}
