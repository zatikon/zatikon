#!/bin/bash

set -eu

CERTDIR="$(dirname "$0")/../ssl"

mkdir -p "$CERTDIR"
cd "$CERTDIR"

[[ $# -eq 1 ]] || { echo "Usage: ./genkey.sh DOMAIN" >&2; exit; }

DOMAIN=$1

read -sp "Enter keystore password: " KEY_PWD

keytool -genkeypair -alias server -keyalg EC -sigalg SHA384withECDSA -keysize 256 -keystore serverkeystore.p12 -storetype pkcs12 -v -storepass "$KEY_PWD" -validity 365 -ext san=ip:127.0.0.1 -dname "C = Unknown, ST = Unknown, L = Unknown, O = Unknown, OU = Unknown, CN = ${DOMAIN}"

keytool -exportcert -alias server -file server.cer -keystore serverkeystore.p12 -storepass "$KEY_PWD"

# Truststore has to have a password becaaaause... it has to
yes | keytool -import -file server.cer -alias server -keystore truststore.p12 -storepass 123456

cp truststore.p12 ../src/main/resources/certs/truststore.p12
