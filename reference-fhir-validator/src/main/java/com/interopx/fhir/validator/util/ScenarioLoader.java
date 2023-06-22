package com.interopx.fhir.validator.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.interopx.fhir.validator.controller.ResourceValidationController;

import ca.uhn.fhir.context.FhirContext;

/**
 * This class loads the goldJson based on scenario name to validate source/input
 * resource content with goldJson
 * 
 * @author Swarna Nelaturi
 *
 */
@Component
public class ScenarioLoader {

	private static final Logger logger = LoggerFactory.getLogger(ScenarioLoader.class);

	@Value("${scenario.base.directory}")
	private String baseLocation;

	public String loadGoldJsonBasedOnScenario(FhirContext fhirContext, Resource resource, String scenarioName) {
		logger.info("Entry - ScenarioLoader.loadGoldJsonBasedOnScenario");
		String goldJson = null;

		String resourceName = resource.getResourceType().name();
		/*
		 * if (ScenarioConstants.getScenarioName().containsKey(scenarioName)) {
		 * logger.info("***Actuall location to load gold file *****" + baseLocation +
		 * resourceName + "/" + scenarioName + ".json"); // goldJson =
		 * JSONUtil.readJsonFromFile(baseLocation + resourceName + "/" + // scenarioName
		 * + ".json"); try { goldJson = readFile(baseLocation + resourceName + "/" +
		 * scenarioName + ".json", Charset.defaultCharset()); } catch (IOException e) {
		 * logger.error("Error While loading Gold Json File"); } } else { logger.error(
		 * "***Invalid scenario found *****" + baseLocation + resourceName + "/" +
		 * scenarioName + ".json"); }
		 */
		try {
			goldJson = readFile(baseLocation + resourceName + "/" + scenarioName + ".json",
					Charset.defaultCharset());
		} catch (IOException e) {
			logger.error("Error While loading Gold Json File");
		}

		logger.info("Entry - ScenarioLoader.loadGoldJsonBasedOnScenario");
		return goldJson;
	}

	public String readFile(String path, Charset encoding) throws IOException {
		logger.info("Path::::::::::::::{}",path);
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
