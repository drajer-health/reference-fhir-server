package com.interopx.fhir.auth.server.jwtservice;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.interopx.fhir.auth.server.exception.AuthServerException;
import com.interopx.fhir.auth.server.keystore.JWKSStore;
import com.interopx.fhir.auth.server.model.Clients;
import com.interopx.fhir.auth.server.service.ClientsService;
import com.nimbusds.jose.jwk.JWKSet;

import ch.qos.logback.classic.Logger;

@Service
public class JWKSCache {

	/** Logger for this class */
	private static final Logger logger = (Logger) LoggerFactory.getLogger(JWKSCache.class);

	protected static final RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private ClientsService clientRegistrationService;

	// map of jwk set uri -> signing/validation service built on the keys found in
	// that jwk set
	private LoadingCache<String, JWTService> validators;

	public JWKSCache() {
		logger.debug("Start in JWKSCache() of JWKSCache class ");
		try {
			this.validators = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS) // expires 1 hour after
					// fetch
					.maximumSize(100)
					.build(new JWKSetVerifierFetcher(HttpClientBuilder.create().useSystemProperties().build()));
		} catch (Exception e) {
			logger.error("Exception in JWKSCache() of JWKSCache class ", e);
		}
		logger.debug("End in JWKSCache() of JWKSCache class ");
	}

	/**
	 * @param jwksUri
	 * @return
	 * @throws ExecutionException
	 * @see com.google.common.cache.Cache#get(java.lang.Object)
	 */
	public JWTService getValidator(String jwksUri) {
		logger.debug("Start in getValidator() of JWKSCache class ");
		try {
			return validators.get(jwksUri);
		} catch (UncheckedExecutionException | ExecutionException e) {
			logger.error("Couldn't load JWK Set from " + jwksUri + ": ", e);
		}
		logger.debug("End in getValidator() of JWKSCache class ");
		return null;
	}

	private class JWKSetVerifierFetcher extends CacheLoader<String, JWTService> {
		private HttpComponentsClientHttpRequestFactory httpFactory;
		private RestTemplate restTemplate;

		JWKSetVerifierFetcher(HttpClient httpClient) {
			this.httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			this.restTemplate = new RestTemplate(httpFactory);
		}

		@Override
		public JWTService load(String key) throws Exception {
			logger.debug("Start in load() of JWKSCache class ");
			String jsonString = restTemplate.getForObject(key, String.class);
			JWKSet jwkSet = JWKSet.parse(jsonString);

			JWKSStore keyStore = new JWKSStore(jwkSet);

			JWTService service = new JWTServiceImpl(keyStore);
			logger.debug("End in load() of JWKSCache class ");
			return service;
		}
	}

	public JWTStoredKeyService loadPublicKey(String clientId) throws Exception {
		logger.debug("Start in loadStoredPublicKey() of JWKSCache class for {} ", clientId);
		Clients client = clientRegistrationService.getClient(clientId);
		if (client != null) {
			JWKSet jwkSet = JWKSet.parse(readPublicKeyFromUrl(client.getJku()));
			JWKSStore keyStore = new JWKSStore(jwkSet);
			JWTStoredKeyService service = new JWTStoredKeyServiceImpl(keyStore);
			return service;
		}
		logger.debug("End in loadStoredPublicKey() of JWKSCache class for {} ", clientId);
		return null;
	}

	public JWTStoredKeyService getJWTStoredKeyService(String url) throws Exception {
		JWKSet jwkSet = null;
		try {
			jwkSet = JWKSet.parse(readPublicKeyFromUrl(url));
		} catch (Exception e) {
			throw new AuthServerException(500, "Error while parsing public key at " + url);
		}
		JWKSStore keyStore = new JWKSStore(jwkSet);
		return new JWTStoredKeyServiceImpl(keyStore);
	}

	public String readPublicKeyFromUrl(String url) {
		logger.debug("Start in readPublicKeyFromUrl() for : {}", url);
		try {
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
			headers.add("Accept", "application/json");
			headers.add("Content-Type", "application/json");
			HttpEntity<Object> request = new HttpEntity<>(headers);
			ResponseEntity<String> exchange = this.restTemplate.exchange(url, HttpMethod.GET, request, String.class);
			String body = exchange.getBody();
			logger.info("Successfully read the public key for {}", url);
			return body;
		} catch (Exception e) {
			logger.debug("Not able to read public key");
			logger.debug("Exception while reading public key from {}", url);
			throw new AuthServerException(500, "Could not read the public key at : " + url);
		}
	}

}
