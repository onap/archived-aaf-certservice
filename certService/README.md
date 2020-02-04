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
    
### Running CSITs
Pull csit repository
    
    https://gerrit.onap.org/r/admin/repos/integration/csit
    
Go to created directory and run
    
    sudo ./run-csit.sh plans/aaf/cert-service
