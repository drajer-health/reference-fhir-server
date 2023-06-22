package com.interopx.fhir.validator.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateConversionUtils {

	private static final Logger logger = LoggerFactory.getLogger(DateConversionUtils.class);

	public static String convertDateIntoString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String convertedDate = dateFormat.format(date);
		return convertedDate;

	}

	public static Date convertStringIntoDate(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date convertedDate = null;
		try {
			convertedDate = dateFormat.parse(date);
		} catch (ParseException e) {
			logger.error("Error while parsing date", e);
		}
		return convertedDate;

	}
}
