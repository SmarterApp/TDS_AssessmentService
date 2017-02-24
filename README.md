# TDS_AssessmentService
## Overview
The `TDS_Assessment` (aka Assessment Support Service) consists of two modules:

* **client:** Contains the POJOs/classes needed for a consumer to interact with the Assessment Support Service
* **service:** REST endpoints that provide TDS assessmnt data

## Build
To build the **client** and **service**, use the "parent" `pom.xml` that is contained in the `TDS_AssessmentService` directory:

* `mvn clean install -f /path/to/parent/pom.xml`

To build the **client**:

* `mvn clean install -f /path/to/client/pom.xml`

To build the **service**:

* `mvn clean install -f /path/to/service/pom.xml`

To build the service and run integration tests:
  
* `mvn clean install -Dintegration-tests.skip=false -f /path/to/service/pom.xml`

## Run
### Run in IDE
To run the Assessment Support Service in the IDE, update the `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password` properties defined in the `service/src/main/resources/application.properties` file and set them to appropriate values.

### Run .JAR
To run the compiled jar built by one of the build commands above, use the following:

```
java -Xms256m -Xmx512m \
    -jar /path/to/target/tds-assessment-service-0.0.1-SNAPSHOT.jar \
    --server-port="8080" \
    --server.undertow.buffer-size=16384 \
    --server.undertow.buffers-per-region=20 \
    --server.undertow.io-threads=64 \
    --server.undertow.worker-threads=512 \
    --server.undertow.direct-buffers=true \
    --spring.datasource.url="jdbc:mysql://[db server name]:[db port]/assessments" \
    --spring.datasource.username="[MySQL user name]" \
    --spring.datasource.password="[MySQL user password]" \
    --spring.datasource.type=com.zaxxer.hikari.HikariDataSource
```