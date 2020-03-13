all: step_1 step_2 step_3 step_4 step_5 step_6 step_7 step_8 step_9 step_10 step_11 step_12 step_13 step_14
.PHONY: all
#Clear certificates
clear:
	@echo "Clear certificates"
	rm certServiceClient-keystore.jks certServiceServer-keystore.jks root.crt truststore.jks
	@echo "#####done#####"

#Generate root private and public key
step_1:
	@echo "(Generate root private and public key)"
	keytool -genkeypair -v -alias root -keyalg RSA -keysize 4096 -validity 9999 -keystore root-keystore.jks \
    -dname "CN=root.com, OU=Root Org, O=Root Company, L=Wroclaw, ST=Dolny Slask, C=PL" -keypass secret \
    -storepass secret -ext BasicConstraints:critical="ca:true"
	@echo "#####done#####"

#Export public key as certificate
step_2:
	@echo "(Export public key as certificate)"
	keytool -exportcert -alias root -keystore root-keystore.jks -storepass secret -file root.crt -rfc
	@echo "#####done#####"

#Self-signed root (import root certificate into truststore)
step_3:
	@echo "(Self-signed root (import root certificate into truststore))"
	keytool -importcert -alias root -keystore truststore.jks -file root.crt -storepass secret -noprompt
	@echo "#####done#####"

#Generate client private and public key
step_4:
	@echo "Generate intermediate private and public key"
	keytool -genkeypair -v -alias certServiceClient -keyalg RSA -keysize 2048 -validity 9999 \
    -keystore certServiceClient-keystore.jks -storetype JKS \
    -dname "CN=certServiceClient.com,OU=certServiceClient company,O=certServiceClient org,L=Wroclaw,ST=Dolny Slask,C=PL" \
    -keypass secret -storepass secret -ext BasicConstraints:critical="ca:true"
	@echo "####done####"

#Generate certificate request client
step_5:
	@echo "Generate certificate request intermediate"
	keytool -certreq -keystore certServiceClient-keystore.jks -alias certServiceClient -storepass secret -file certServiceClient.csr
	@echo "####done####"

#Signed client request by root(root -> client)
step_6:
	@echo "Signed intermediate request by root(root -> intermediate)"
	keytool -gencert -v -keystore root-keystore.jks -storepass secret -alias root -infile certServiceClient.csr \
    -outfile certServiceClientByRoot.crt -rfc -ext bc=0  -ext ExtendedkeyUsage="serverAuth,clientAuth" \
    -ext SubjectAlternativeName:="DNS:localhost"
	@echo "####done####"

#Import root certificate into client
step_7:
	@echo "Import root certificate into intermediate"
	keytool -importcert -file root.crt -destkeystore certServiceClient-keystore.jks -alias root -storepass secret -noprompt
	@echo "####done####"

#Import signed certificate into client
step_8:
	@echo "Import signed certificate into intermediate"
	keytool -importcert -file certServiceClientByRoot.crt -destkeystore certServiceClient-keystore.jks -alias certServiceClient \
    -storepass secret -noprompt
	@echo "####done####"

#Generate server private and public key
step_9:
	@echo "Generate intermediate private and public key(server)"
	keytool -genkeypair -v -alias aaf-cert-service-service -keyalg RSA -keysize 2048 -validity 9999 \
    -keystore certServiceServer-keystore.jks -storetype JKS \
    -dname "CN=aaf-cert-service-service.com,OU=certServiceServer company,O=certServiceServer org,L=Wroclaw,ST=Dolny Slask,C=PL" \
    -keypass secret -storepass secret -ext BasicConstraints:critical="ca:true"
	@echo "####done####"

#Generate certificate request server
step_10:
	@echo "Generate certificate request intermediate(server)"
	keytool -certreq -keystore certServiceServer-keystore.jks -alias aaf-cert-service-service -storepass secret -file certServiceServer.csr
	@echo "####done####"

#Signed server request by root(root -> server)
step_11:
	@echo "Signed intermediate request by root(root -> intermediate)(server)"
	keytool -gencert -v -keystore root-keystore.jks -storepass secret -alias root -infile certServiceServer.csr \
    -outfile certServiceServerByRoot.crt -rfc -ext bc=0  -ext ExtendedkeyUsage="serverAuth,clientAuth" \
    -ext SubjectAlternativeName:="DNS:aaf-cert-service-service"
	@echo "####done####"

#Import root certificate into server
step_12:
	@echo "Import root certificate into intermediate(server)"
	keytool -importcert -file root.crt -destkeystore certServiceServer-keystore.jks -alias root -storepass secret -noprompt
	@echo "####done####"

#Import signed certificate into server
step_13:
	@echo "Import signed certificate into intermediate(server)"
	keytool -importcert -file certServiceServerByRoot.crt -destkeystore certServiceServer-keystore.jks -alias aaf-cert-service-service \
    -storepass secret -noprompt
	@echo "####done####"
#Clear unused certificates
step_14:
	@echo "Clear unused certificates"
	rm certServiceClientByRoot.crt certServiceClient.csr root-keystore.jks certServiceServerByRoot.crt  certServiceServer.csr
	@echo "#####done#####"















