package com.interopx.fhir.validator.util;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.r4.model.Resource;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ca.uhn.fhir.context.FhirContext;

/**
 * 
 * This utility helps to handle JSON data conversion like converting JSON String
 * into JSONObject/Map/List and vice-versa, reading JSON file from resource
 * folder or a specified path and converting it into JSONObject/Map/List.
 * 
 * @author Swarna Nelaturi
 * 
 */
public class JSONUtil {
	private static final Logger logger = LoggerFactory.getLogger(JSONUtil.class);
	private static final String ENTRY = "entry";

	/**
	 * Converts JSON String into HashMap.
	 * 
	 * @param json JSON String
	 * @return Map
	 */
	public static Map<String, Object> jsonToMap(String json) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		ObjectMapper mapper = new ObjectMapper();
		try {
			// Convert JSON to map
			map = (HashMap<String, Object>) mapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});

			System.out.println(" ------ " + map);

		} catch (JsonGenerationException e) {
			logger.error("Error While generating Json ",e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("Error While Json Malling",e.getMessage());
		} catch (IOException e) {
			logger.error("Error in jsonToMap ",e.getMessage());
		}

		return map;
	}

	/**
	 * Converts JSON String into ArrayList.
	 * 
	 * @param json JSON String
	 * @return List
	 */
	public static List<Object> jsonToList(String json) {

		List<Object> list = new ArrayList<Object>();

		ObjectMapper mapper = new ObjectMapper();
		try {
			// Convert JSON to List
			list = mapper.readValue(json, new TypeReference<List<Object>>() {
			});

		} catch (JsonGenerationException e) {
			logger.error("Error While generating Json in jsonToList ",e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("Error While Json Malling in jsonToList",e.getMessage());
		} catch (IOException e) {
			logger.error("Error in jsonToList ",e.getMessage());
		}

		return list;
	}

	/**
	 * Converts JSON String into Object of type
	 * 
	 * @param <T>        Generic Type of the Run time object
	 * @param json       the JSON to be de-serialized.
	 * @param objectType Generic
	 * @return Object of Generic type
	 */
	public static <T> T jsonToObject(String json, Class<T> objectType) {
		try {
			return json != null ? new ObjectMapper().readValue(json, objectType) : null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converts JSON String into List of genericType objects
	 *
	 * @param <T>        generic type
	 * @param json       the JSON to be de-serialized.
	 * @param objectType Generic class
	 * @return List of Generic type objects
	 */
	public static <T> List<T> jsonToObjects(String json, Class<T> objectType) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return json != null
					? mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, objectType))
					: null;
		} catch (Exception e) {
			logger.error("Error in jsonToObjects ",e.getMessage());
			return null;
		}
	}

	/**
	 * Converts list into JSON String.
	 * 
	 * @param list Any of the List implementation
	 * @return JSON String
	 */
	public static String listToJson(List<Object> list) {
		String json = null;
		ObjectMapper mapperObj = new ObjectMapper();
		try {
			json = mapperObj.writeValueAsString(list);
		} catch (Exception e) {
			logger.error("Error in listToJson ",e.getMessage());
		}

		return json;
	}

	/**
	 * Converts map into JSON String.
	 * 
	 * @param map Any of the Map implementation
	 * @return JSON String
	 */
	public static String mapToJson(Map<String, Object> map) {

		String json = null;
		ObjectMapper mapperObj = new ObjectMapper();
		try {
			json = mapperObj.writeValueAsString(map);
		} catch (Exception e) {
			logger.error("Error in mapToJson ",e.getMessage());
		}

		return json;
	}

	/**
	 * Reads a JSON File from a specified path and convert it into JSONObject
	 * 
	 * @param filepath Absolute file path of the JSON file
	 * @return JSONObject
	 */
	public static String readJsonFromFile(String filepath) {
		logger.info("****Reading file from path ****");
		JSONParser jsonParser = new JSONParser();
		String jsonObject = null;
		try (FileReader reader = new FileReader(filepath)) {
			logger.info("****Reading file from path - process ****" + filepath);
			jsonObject = (String) jsonParser.parse(reader);
			logger.info("****Reading file from path - Completed ****" + jsonObject);
		} catch (IOException | ParseException e) {
			logger.info("**** Error in Reading file from path ****" + e.getMessage());
		}
		logger.info("****Reading file from path - content ****" + jsonObject);
		return jsonObject;
	}

	/**
	 * Convert string into JSONObject
	 * 
	 * @param filepath Absolute file path of the JSON file
	 * @return JSONObject
	 */
	public static JSONObject stringToJson(String jsonString) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = (JSONObject) jsonParser.parse(jsonString);
		} catch (ParseException e) {
			logger.error("Error in stringToJson ",e.getMessage());
		}
		return jsonObject;
	}

	public static String getResourceName(String resource) {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, Object>>() {
		}.getType();
		Map<String, Object> sourceMap = gson.fromJson(resource, type);
		String resourceName = (String) sourceMap.get("resourceType");
		return resourceName;

	}

	public static Resource stringToFhirResource(FhirContext fhirContext, String jsonString) {
		return (Resource) fhirContext.newJsonParser().parseResource(jsonString);
	}

	/**
	 * Reads a JSON file from Resource folder and converts into JSON String
	 * 
	 * @param resourceName Name of the JSON file
	 * @return JSON String
	 * @throws IOException
	 */
	public static String resourceFileToJsonString(String mappingFileName) throws IOException {
		return new String(Files.readAllBytes(Paths.get(mappingFileName)));

	}

	/**
	 * Reads a JSON file from Resource folder and converts into Map
	 * 
	 * @param resourceName Name of the JSON file
	 * @return Map
	 * @throws IOException
	 */
	public static Map convertResourceJSONFileToMap(String resourceName) throws IOException {
		return jsonToMap(resourceFileToJsonString(resourceName));
	}

	/**
	 * Reads a JSON file from Resource folder and converts into List
	 * 
	 * @param resourceName Name of the JSON file
	 * @return List
	 * @throws IOException
	 */
	public static List convertResourceJSONFileToList(String resourceName) throws IOException {
		return jsonToList(resourceFileToJsonString(resourceName));
	}

	public static List<Resource> getResources(FhirContext fhirContext, String jsonString) {
		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
		List<Resource> resources = new ArrayList<Resource>();
		if (jsonObject.has(ENTRY)) {
			org.json.JSONArray keyArray = jsonObject.getJSONArray(ENTRY);
			keyArray.forEach(entry -> {
				if (ObjectUtils.isNotEmpty(entry)) {
					org.json.JSONObject entryObject = (org.json.JSONObject) entry;
					if (entryObject.get("resource") != null) {
						Resource resource = (Resource) fhirContext.newJsonParser()
								.parseResource(entryObject.get("resource").toString());
						resources.add(resource);
					}
				}
			});
		} else {
			Resource resource = (Resource) fhirContext.newJsonParser()
					.parseResource(jsonString);
			resources.add(resource);
		}
		return resources;
	}

	public static String fhirResourceToString(FhirContext r4Context, Resource resource) {
		return r4Context.newJsonParser().encodeResourceToString(resource);
	}
}
