package com.interopx.fhir.validator.component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r5.context.SimpleWorkerContext;
import org.hl7.fhir.r5.elementmodel.Manager;
import org.hl7.fhir.r5.formats.FormatUtilities;
import org.hl7.fhir.r5.model.FhirPublication;
import org.hl7.fhir.r5.model.ImplementationGuide;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.VersionUtilities;
import org.hl7.fhir.utilities.cache.FilesystemPackageCacheManager;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.hl7.fhir.utilities.cache.ToolsVersion;
import org.hl7.fhir.utilities.json.JSONUtil;
import org.hl7.fhir.validation.ValidationEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class Validator {

	private final Logger logger = LoggerFactory.getLogger(Validator.class);

	private static ValidationEngine hl7Validator;
	private FilesystemPackageCacheManager packageManager;

	public Validator() {
	}

	/**
	 * Creates the HL7 Validator to which can then be used for validation.
	 *
	 * @param igFile The igFile the validator is loaded with.
	 * @throws Exception If the validator cannot be created
	 */
	public Validator(List<String> igFiles) throws Exception {
		final String fhirSpecVersion = "4.0";
		final String definitions = VersionUtilities.packageForVersion(fhirSpecVersion) + "#"
				+ VersionUtilities.getCurrentVersion(fhirSpecVersion);
		// Temporarily disabling the Terminology Server
		final String txServer = getTxServerUrl();
		//final String txServer = null;
		final String txLog = null;
		final String fhirVersion = "4.0.1";
		logger.info("initializing hl7Validator inside  Validator");
		hl7Validator = new ValidationEngine(definitions);
		for(String igFile:igFiles) {
			logger.info("Loading IG:::::{} ",igFile);
			hl7Validator.loadIg(igFile, true);
		}
		hl7Validator.connectToTSServer(txServer, txLog, FhirPublication.fromCode(fhirVersion));
		hl7Validator.setNative(false);
		hl7Validator.setAnyExtensionsAllowed(true);
		hl7Validator.prepare();
		packageManager = new FilesystemPackageCacheManager(true, ToolsVersion.TOOLS_VERSION);
	}

	public List<String> getStructures() {
		List<StructureDefinition> structures = hl7Validator.getContext().getStructures();
		return structures.stream().map(StructureDefinition::getUrl).collect(Collectors.toList());
	}

	public OperationOutcome validate(byte[] resource, String profile) throws Exception {
		ArrayList<String> patientProfiles = new ArrayList<>(Arrays.asList(profile.split(",")));
		try {
			Manager.FhirFormat fmt = FormatUtilities.determineFormat(resource);
			return hl7Validator.validate(null, resource, fmt, patientProfiles);

		} catch (Exception e) {
			logger.info("error while validating resource", e);
		}
		return null;
	}

	private String getTxServerUrl() {
		if (disableTxValidation()) {
			return null;
		}

		if (System.getenv("TX_SERVER_URL") != null) {
			return System.getenv("TX_SERVER_URL");
		} else {
			return "http://tx.fhir.org";
		}
	}

	private boolean disableTxValidation() {
		return System.getenv("DISABLE_TX") != null;
	}

	/**
	 * Provides a list of known IGs that can be retrieved and loaded.
	 *
	 * @return the list of IGs.
	 */
	public Map<String, String> getKnownIGs() throws IOException {
		Map<String, String> igs = new HashMap<>();
		packageManager.listAllIds(igs);
		return igs;
	}

	/**
	 * Load a profile into the validator.
	 * 
	 * @param profile the profile to be loaded
	 */
	public void loadProfile(Resource profile) {
		SimpleWorkerContext context = hl7Validator.getContext();
		context.cacheResource(profile);
	}

	private List<String> getProfileUrls(String id) throws IOException {
		String[] fragments = id.split("#");
		id = fragments[0];
		String version = fragments.length > 1 ? fragments[1] : null;
		NpmPackage npm = packageManager.loadPackage(id, version);
		InputStream in = npm.load(".index.json");
		JsonObject index = (JsonObject) JsonParser.parseString(TextFile.streamToString(in));

		JsonArray files = index.getAsJsonArray("files");
		List<String> profileUrls = new ArrayList<>();
		for (JsonElement f : files) {
			JsonObject file = (JsonObject) f;
			String type = JSONUtil.str(file, "resourceType");
			String url = JSONUtil.str(file, "url");
			if (type.equals("StructureDefinition")) {
				profileUrls.add(url);
			}
		}
		Collections.sort(profileUrls);
		return profileUrls;
	}

	/**
	 * Load an IG into the validator.
	 *
	 * @param id the package ID of the FHIR IG to be loaded
	 * @return a list of profile URLs for the loaded IG
	 */
	public List<String> loadIg(String id) throws Exception {
		hl7Validator.loadIg(id, true);
		return getProfileUrls(id);
	}

	/**
	 * Get a mapping from IG URL to a list of profile URLs supported by the IG.
	 *
	 * @return a mapping from IG URL to a list of profile URLs supported by the IG.
	 */
	public Map<String, List<String>> getProfilesByIg() {
		List<ImplementationGuide> igs = hl7Validator.getContext().allImplementationGuides();
		return igs.stream().collect(
				Collectors.toMap(ig -> ig.getPackageId() + (ig.hasVersion() ? "#" + ig.getVersion() : ""), ig -> {
					try {
						return getProfileUrls(ig.getPackageId());
					} catch (IOException e) {
						return new ArrayList<>();
					}
				}, (existing, replacement) -> existing));
	}

	/**
	 * Load a profile from a file.
	 *
	 * @param src the file path
	 * @throws IOException if the file fails to load
	 */
	public void loadProfileFromFile(String src) throws IOException {
		byte[] resource = loadResourceFromFile(src);
		Manager.FhirFormat fmt = FormatUtilities.determineFormat(resource);
		Resource profile = FormatUtilities.makeParser(fmt).parse(resource);
		loadProfile(profile);
	}

	private byte[] loadResourceFromFile(String src) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL file = classLoader.getResource(src);
		return IOUtils.toByteArray(file);
	}

}