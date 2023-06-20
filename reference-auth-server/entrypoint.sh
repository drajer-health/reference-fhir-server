#!/bin/sh

if [ "${INJECT_JWKS_JSON}" = "true" ]; then
    apt-get update
    apt-get install awscli -y
    mkdir -p /usr/local/tomcat/webapps/publickey
	aws s3 cp --region us-east-1 s3://arcadia.provisioning.development/encryption/fhir-api/RS384PublicKey.json /usr/local/tomcat/webapps/publickey
fi

exec "$@"