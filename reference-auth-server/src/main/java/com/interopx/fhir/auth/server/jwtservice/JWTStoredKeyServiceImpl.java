package com.interopx.fhir.auth.server.jwtservice;

import ch.qos.logback.classic.Logger;
import com.google.common.base.Strings;
import com.interopx.fhir.auth.server.keystore.JWKSStore;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.slf4j.LoggerFactory;

public class JWTStoredKeyServiceImpl implements JWTStoredKeyService {
  // map of identifier to signer
  private Map<String, JWSSigner> signers = new HashMap<>();

  // map of identifier to verifier
  private Map<String, JWSVerifier> verifiers = new HashMap<>();

  /** Logger for this class */
  private static final Logger logger = (Logger) LoggerFactory.getLogger(JWTServiceImpl.class);

  private String defaultSignerKeyId;

  private JWSAlgorithm defaultAlgorithm;

  // map of identifier to key
  private Map<String, JWK> keys = new HashMap<>();

  /**
   * Build this service based on the keys given. All public keys will be used to make verifiers, all
   * private keys will be used to make signers.
   *
   * @param keys
   * @throws InvalidKeySpecException
   * @throws NoSuchAlgorithmException
   */
  public JWTStoredKeyServiceImpl(Map<String, JWK> keys)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    logger.debug("Start in JWTStoredKeyServiceImpl() of JWTStoredKeyServiceImpl class ");
    this.keys = keys;
    buildSignersAndVerifiers();
    logger.debug("End in JWTStoredKeyServiceImpl() of JWTStoredKeyServiceImpl class ");
  }

  /**
   * Build this service based on the given keystore. All keys must have a key
   *
   * @param keyStore
   * @throws InvalidKeySpecException
   * @throws NoSuchAlgorithmException
   */
  public JWTStoredKeyServiceImpl(JWKSStore keyStore)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    logger.debug(
        "Start in JWTStoredKeyServiceImpl(JWKSStore keyStore) of JWTStoredKeyServiceImpl class ");
    // convert all keys in the keystore to a map based on key id
    if (keyStore != null && keyStore.getJwkSet() != null) {
      for (JWK key : keyStore.getKeys()) {
        if (!Strings.isNullOrEmpty(key.getKeyID())) {
          // use the key ID that's built into the key itself
          this.keys.put(key.getKeyID(), key);
        } else {
          // create a random key id
          String fakeKid = UUID.randomUUID().toString();
          this.keys.put(fakeKid, key);
        }
      }
    }
    buildSignersAndVerifiers();
    logger.debug(
        "End in JWTStoredKeyServiceImpl(JWKSStore keyStore) of JWTStoredKeyServiceImpl class ");
  }

  /** @return the defaultSignerKeyId */
  @Override
  public String getDefaultSignerKeyId() {
    return defaultSignerKeyId;
  }

  /** @param defaultSignerKeyId the defaultSignerKeyId to set */
  public void setDefaultSignerKeyId(String defaultSignerId) {
    this.defaultSignerKeyId = defaultSignerId;
  }

  /** @return */
  @Override
  public JWSAlgorithm getDefaultSigningAlgorithm() {
    return defaultAlgorithm;
  }

  public void setDefaultSigningAlgorithmName(String algName) {
    defaultAlgorithm = JWSAlgorithm.parse(algName);
  }

  public String getDefaultSigningAlgorithmName() {
    if (defaultAlgorithm != null) {
      return defaultAlgorithm.getName();
    } else {
      return null;
    }
  }

  /**
   * Build all of the signers and verifiers for this based on the key map.
   *
   * @throws
   * @throws
   */
  private void buildSignersAndVerifiers() throws NoSuchAlgorithmException, InvalidKeySpecException {
    logger.debug("Start in buildSignersAndVerifiers() of JWTStoredKeyServiceImpl class ");
    try {
      for (Map.Entry<String, JWK> jwkEntry : keys.entrySet()) {
        String id = jwkEntry.getKey();
        JWK jwk = jwkEntry.getValue();
        try {
          if (jwk instanceof RSAKey) {
            // build RSA signers & verifiers

            if (jwk.isPrivate()) { // only add the signer if there's a private key
              RSASSASigner signer = new RSASSASigner((RSAKey) jwk);
              signers.put(id, signer);
            }

            RSASSAVerifier verifier = new RSASSAVerifier((RSAKey) jwk);
            verifiers.put(id, verifier);

          } else {
            logger.error("Unknown key type: " + jwk);
          }
        } catch (JOSEException e) {
          logger.error(
              "Exception in buildSignersAndVerifiers() of JWTStoredKeyServiceImpl class ", e);
        }
      }

      if (defaultSignerKeyId == null && keys.size() == 1) {
        setDefaultSignerKeyId(keys.keySet().iterator().next());
      }
    } catch (Exception e) {
      logger.error("Exception in buildSignersAndVerifiers() of JWTStoredKeyServiceImpl class ", e);
    }
    logger.debug("End in buildSignersAndVerifiers() of JWTStoredKeyServiceImpl class ");
  }

  @Override
  public void signJwt(SignedJWT jwt) {
    logger.debug("Start in signJwt(SignedJWT jwt) of JWTStoredKeyServiceImpl class ");
    if (getDefaultSignerKeyId() == null) {
      logger.error("Tried to call default signing with no default signer ID set");
      throw new IllegalStateException(
          "Tried to call default signing with no default signer ID set");
    }

    JWSSigner signer = signers.get(getDefaultSignerKeyId());

    try {
      jwt.sign(signer);
    } catch (JOSEException e) {

      logger.error("Failed to sign JWT, error was: ", e);
    }
    logger.debug("End in signJwt(SignedJWT jwt) of JWTStoredKeyServiceImpl class ");
  }

  @Override
  public void signJwt(SignedJWT jwt, JWSAlgorithm alg) {
    logger.debug(
        "Start in signJwt(SignedJWT jwt, JWSAlgorithm alg) of JWTStoredKeyServiceImpl class ");

    JWSSigner signer = null;

    for (JWSSigner s : signers.values()) {
      if (s.supportedJWSAlgorithms().contains(alg)) {
        signer = s;
        break;
      }
    }

    if (signer == null) {
      logger.error("No matching algirthm found for alg=" + alg);
    }

    try {
      jwt.sign(signer);
    } catch (JOSEException e) {

      logger.error("Failed to sign JWT, error was: ", e);
    }
    logger.debug(
        "End in signJwt(SignedJWT jwt, JWSAlgorithm alg) of JWTStoredKeyServiceImpl class ");
  }

  @Override
  public boolean validateSignature(SignedJWT jwt) {
    logger.debug("Start in validateSignature(SignedJWT jwt) of JWTStoredKeyServiceImpl class ");
    try {
      for (JWSVerifier verifier : verifiers.values()) {
        try {
          if (jwt.verify(verifier)) {
            return true;
          }
        } catch (JOSEException e) {
          logger.error("Failed to validate signature with " + verifier + " error message: ", e);
        }
      }
    } catch (Exception e) {
      logger.error("Exception in validateSignature() of  JWTStoredKeyServiceImpl class ", e);
    }
    logger.debug("End in validateSignature(SignedJWT jwt) of JWTStoredKeyServiceImpl class ");
    return false;
  }

  @Override
  public Map<String, JWK> getAllPublicKeys() {
    logger.debug("Start in getAllPublicKeys() of JWTStoredKeyServiceImpl class ");
    Map<String, JWK> pubKeys = new HashMap<>();
    try {
      for (String keyId : keys.keySet()) {
        JWK key = keys.get(keyId);
        JWK pub = key.toPublicJWK();
        if (pub != null) {
          pubKeys.put(keyId, pub);
        }
      }
    } catch (Exception e) {
      logger.error("Exception in getAllPublicKeys() of  JWTStoredKeyServiceImpl class ", e);
    }
    logger.debug("End in getAllPublicKeys() of JWTStoredKeyServiceImpl class ");
    return pubKeys;
  }

  @Override
  public Collection<JWSAlgorithm> getAllSigningAlgsSupported() {
    logger.debug("Start in getAllSigningAlgsSupported() of JWTStoredKeyServiceImpl class ");
    Set<JWSAlgorithm> algs = new HashSet<>();
    try {
      for (JWSSigner signer : signers.values()) {
        algs.addAll(signer.supportedJWSAlgorithms());
      }

      for (JWSVerifier verifier : verifiers.values()) {
        algs.addAll(verifier.supportedJWSAlgorithms());
      }
    } catch (Exception e) {
      logger.error(
          "Exception in getAllSigningAlgsSupported() of  JWTStoredKeyServiceImpl class ", e);
    }
    logger.debug("End in getAllSigningAlgsSupported() of JWTStoredKeyServiceImpl class ");
    return algs;
  }
}
