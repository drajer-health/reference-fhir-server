package com.interopx.fhir.auth.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import static org.mockito.Mockito.doThrow;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.interopx.fhir.auth.server.service.ClientsService;
import com.nimbusds.jwt.JWT;

@RunWith(SpringRunner.class)
@SpringBootTest
class CommonUtilTest {
	
	@InjectMocks
	CommonUtil commonUtil;

	@Mock
	ClientsService clientRegistrationService;

	@Mock
	Date date;

	@Mock
	JWT jwt;

	@Mock
	File file;

	@Mock
	SimpleDateFormat simpleDateFormat;

	
	@Test
	void testGenerateRandomString() {
		int length = 10;
		String str = CommonUtil.generateRandomString(length);
		assertNotNull(str);
	}

	@Test
	void testBase64Encoder() {

		String string = "42536789hsdfghjkah";
		String str = CommonUtil.base64Encoder(string);
		// when(Base64.encodeBase64(string.getBytes()).clone());
		assertNotNull(str);
	}

	@Test
	void testBase64Decoder() {

		String string = "456789fsgkgckvjzxbh";
		String str = CommonUtil.base64Decoder(string);
		assertNotNull(str);

	}

	@SuppressWarnings("null")
	@Test
	void testDownloadFIleByName() throws IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		MockHttpServletResponse response = new MockHttpServletResponse();
		File downloadFile = new File("src/test/resources/test.txt");
//				"C:\\arcadia-auth-server-backup\\arcadia-auth-server-backup\\src\\test\\resources\\test.txt");
		response.setContentType("Content-Disposition");
		response.setIntHeader("attachment; filename=\"%s\"", 3);
		OutputStream object = new FileOutputStream(downloadFile.getAbsolutePath());
//        byte[] b= {1,2,3};
//        object.write(b);
//        object.write(4);
//        object.write(8);
//        object.close();
		int i = CommonUtil.downloadFIleByName(downloadFile, response);

		assertNotNull(i);
	}

	@Test
	void testDownloadFIleByNameFor404() throws IOException {
		HttpServletResponse response = mock(HttpServletResponse.class);
		String path = "EmailUtil.java";
		File downloadFile = new File(path);
		assertEquals(404, commonUtil.downloadFIleByName(downloadFile, response));
	}

	@Test
	void testGetBaseUrl() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		String baseUrl = request.getScheme() + "://" + request.getServerName()
				+ ("http".equals(request.getScheme()) && request.getServerPort() == 80
						|| "https".equals(request.getScheme()) && request.getServerPort() == 443 ? ""
								: ":" + request.getServerPort())
				+ request.getContextPath();
		baseUrl = baseUrl.concat("/");
		assertEquals(baseUrl, CommonUtil.getBaseUrl(request));

	}

	@Test
	void testGetBaseUrl1() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		String baseUrl = request.getScheme() + "://" + request.getServerName()
				+ ("http".equals(request.getScheme()) && request.getServerPort() == 80
						|| "https".equals(request.getScheme()) && request.getServerPort() == 443 ? ""
								: ":" + request.getServerPort())
				+ request.getContextPath();
		baseUrl = baseUrl.concat("/");
		assertEquals(baseUrl, CommonUtil.getBaseUrl(request));
	}

	@Test
	void testConvertTimestampToUnixTime() throws ParseException {
		String timestamp = "2022-08-01 10:30:39";
		Integer i = CommonUtil.convertTimestampToUnixTime(timestamp);
		assertNotNull(i);
	}

	@Test
	void testConvertTimestampToUnixTimeforNull() throws ParseException {
		String timestamp = null;
		Integer i = CommonUtil.convertTimestampToUnixTime(timestamp);
		assertNull(commonUtil.convertTimestampToUnixTime(timestamp));
	}

	@Test
	void testConvertToDateFormat() throws ParseException {
		String timeStamp = "Wed Oct 16 00:00:00 CEST 2013";
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		Date parsedDate = null;
		parsedDate = sdf.parse(timeStamp);
		assertEquals(parsedDate, commonUtil.convertToDateFormat(timeStamp));
	}

	@Test
	void testConvertToDateFormatException() throws ParseException {
		String timeStamp = "Wed  CEST 2013";
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		Date parsedDate = null;
		doThrow(RuntimeException.class).when(simpleDateFormat).parse(timeStamp);
		// parsedDate = sdf.parse(timeStamp);
		assertNull(commonUtil.convertToDateFormat(timeStamp));
	}

	@Test
	void testIsValid() {

	}

	@Test
	void testIsValidScopes() throws UnsupportedEncodingException {
		String scopesRequested = "scope";
		List<String> registeredScope = new ArrayList<>();
		registeredScope.add("scope");
		registeredScope.add("bcd");
		String scope = scopesRequested.replaceAll("\\s+", ",");
		List<String> reqScopes = Arrays.asList(scope.split(","));
		Boolean booleanvalue = true;
		assertEquals(booleanvalue, commonUtil.isValidScopes(scopesRequested, registeredScope));

	}

	@Test
	void testIsValidScopesForFalse() throws UnsupportedEncodingException {
		String scopesRequested = "scope";
		List<String> registeredScope = new ArrayList<>();
		registeredScope.add("abc");
		registeredScope.add("bcd");
		String scope = scopesRequested.replaceAll("\\s+", ",");
		List<String> reqScopes = Arrays.asList(scope.split(","));
		Boolean booleanvalue = false;
		assertEquals(booleanvalue, commonUtil.isValidScopes(scopesRequested, registeredScope));
	}

	@Test
	void testGenerateUUID() {
		UUID uuid = UUID.randomUUID();
		String str = CommonUtil.generateUUID();
		assertNotNull(str);
	}

}
