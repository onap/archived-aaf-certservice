# Cert service

### For developers
    * AAF Cert Service is a Spring Boot application
    * Code style
        Use Google code formatter in your IDE.
        For IntelliJ use [https://plugins.jetbrains.com/plugin/8527-google-java-format]
        For other IDEs use []https://github.com/google/google-java-format]

### Running Locally
    ```
     mvn spring-boot:run

    ```

### Project building
    ```
     mvn clean package

    ```
    
### Building Docker image
    ```
    docker build -t cert-service .

    ```

### Running Docker container
    ```
    docker run -p 8080:8080 --name cert-service cert-service

    ```

### Health Check
 Browser:
 
    ```
     http://<localhost>:8080/actuator/health
     
    ```
     
 Curl:   
 
    ```
     curl localhost:8080/actuator/health 
     
    ```   
 Should return {"status":"UP"}

### Running CSITs
Pull csit repository
    
    ```
     https://gerrit.onap.org/r/admin/repos/integration/csit
    
    ```
Go to created directory and run
    
    ```
     sudo ./run-csit.sh plans/aaf/cert-service
    
    ```
### Logs locally

path: 

    ```
     var/log/onap/aaf/certservice/
    ```    
### Logs in Docker container
    ```
     docker exec -it cert-service bash
    ```

path:

    ```
      cd var/log/onap/aaf/certservice
    ```
You should see:    
audit.log  error.log  trace.log


     
     

