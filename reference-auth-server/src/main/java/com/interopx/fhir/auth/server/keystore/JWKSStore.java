package com.interopx.fhir.auth.server.keystore;

import ch.qos.logback.classic.Logger;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

public class JWKSStore {
  private static final Logger logger = (Logger) LoggerFactory.getLogger(JWKSStore.class);

  private JWKSet jwkSet;

  private Resource location;

  public JWKSStore() {}

  // loading public key
  public JWKSStore(JWKSet jwkSet) {
    this.jwkSet = jwkSet;
    initializeJwkSet();
  }

  private void initializeJwkSet() {
    logger.debug("Start in initializeJwkSet() of JWKSStore class ");

    if (jwkSet == null) {
      if (location != null) {

        if (location.exists() && location.isReadable()) {

          try {
            // read in the file from disk
            String s =
                CharStreams.toString(
                    new InputStreamReader(location.getInputStream(), Charsets.UTF_8));

            // parse it into a jwkSet object
            jwkSet = JWKSet.parse(s);
          } catch (IOException e) {
            logger.error("Key Set resource could not be read: " + location);
            throw new IllegalArgumentException("Key Set resource could not be read: " + location);
          } catch (ParseException e) {
            logger.error("Key Set resource could not be parsed: " + location);
            throw new IllegalArgumentException("Key Set resource could not be parsed: " + location);
          }

        } else {
          logger.error("Key Set resource could not be read: " + location);
          throw new IllegalArgumentException("Key Set resource could not be read: " + location);
        }

      } else {
        logger.error("Key store must be initialized with at least one of a jwkSet or a location.");
        throw new IllegalArgumentException(
            "Key store must be initialized with at least one of a jwkSet or a location.");
      }
    }
    logger.debug("End in initializeJwkSet() of JWKSStore class ");
  }

  public JWKSet getJwkSet() {
    return jwkSet;
  }

  public void setJwkSet(JWKSet jwkSet) {
    this.jwkSet = jwkSet;
    initializeJwkSet();
  }

  public Resource getLocation() {
    return location;
  }

  public void setLocation(Resource location) {
    this.location = location;
    initializeJwkSet();
  }

  public List<JWK> getKeys() {
    if (jwkSet == null) {
      initializeJwkSet();
    }
    return jwkSet.getKeys();
  }
}
