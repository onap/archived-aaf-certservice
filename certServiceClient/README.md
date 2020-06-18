# Cert service client

### Project building
```
mvn clean package
```
    
    
### Install the package into the local repository
```
mvn clean install
```     
    
### Building Docker image and  install the package into the local repository
```
mvn clean install -P docker
```   

### Nexus container image
```
nexus3.onap.org:10001/onap/org.onap.aaf.certservice.aaf-certservice-client:latest
```

### Running local client application as standalone docker container
CertService API and client must be running in same network.

You need certificate and trust anchors (in JKS format) to connect to CertService API via HTTPS. Information how to generate truststore and keystore files you can find in CertService main README
```
docker run \
   --rm \
   --name aafcert-client \
   --env-file <path to enviroment file> \
   --network <docker network of cert service> \
   --mount type=bind,src=<path to local host directory where certificate and trust anchor will be created>,dst=<OUTPUT_PATH (same as in step 1)> \
   --volume <local path to keystore in JKS format>:<KEYSTORE_PATH> \
   --volume <local path to truststore in JKS format>:<TRUSTSTORE_PATH> \
   nexus3.onap.org:10001/onap/org.onap.aaf.certservice.aaf-certservice-client:$VERSION

```
Sample Environment file:
```aidl
#Client envs
REQUEST_URL=https://aaf-cert-service:8443/v1/certificate/
REQUEST_TIMEOUT=10000
OUTPUT_PATH=/var/certs
CA_NAME=RA
OUTPUT_TYPE=P12

#CSR config envs
COMMON_NAME=onap.org
ORGANIZATION=Linux-Foundation
ORGANIZATION_UNIT=ONAP
LOCATION=San-Francisco
STATE=California
COUNTRY=US
SANS=test.onap.org:onap.com

#TLS config envs
KEYSTORE_PATH=/etc/onap/aaf/certservice/certs/certServiceClient-keystore.jks
KEYSTORE_PASSWORD=secret
TRUSTSTORE_PATH=/etc/onap/aaf/certservice/certs/certServiceClient-truststore.jks
TRUSTSTORE_PASSWORD=secret
```

### Logs locally

path: 
```
var/log/onap/aaf/certservice-client/certservice-client.log
```    
### Logs in Docker container
```
docker logs aaf-certservice-client
```
###Exit codes
```
0	Success
1	Invalid client configuration
2	Invalid CSR configuration 
3	Fail in key pair generation
4	Fail in  CSR generation
5	CertService HTTP unsuccessful response
6	Internal HTTP Client connection problem
7	Fail in PKCS12 conversion
8	Fail in Private Key to PEM Encoding
9	Wrong TLS configuration
