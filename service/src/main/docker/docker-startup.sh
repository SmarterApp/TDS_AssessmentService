#!/bin/sh
#-----------------------------------------------------------------------------------------------------------------------
# File:  docker-startup.sh
#
# Desc:  Start the tds-assessment-service.jar with the appropriate properties.
#
#-----------------------------------------------------------------------------------------------------------------------

java \
    -Dspring.datasource.url="jdbc:mysql://${ASSESSMENT_DB_HOST}:${ASSESSMENT_DB_PORT}/${ASSESSMENT_DB_NAME}" \
    -Dspring.datasource.username="${ASSESSMENT_DB_USER}" \
    -Dspring.datasource.password="${ASSESSMENT_DB_PASSWORD}" \
    -Dspring.datasource.type=com.zaxxer.hikari.HikariDataSource \
    -Dspring.datasource.driver-class-name=com.mysql.jdbc.Driver \
    -Dspring.datasource.testWhileIdle=true \
    -Dspring.datasource.timeBetweenEvictionRunsMillis=60000 \
    -Dspring.datasource.validationQuery="SELECT 1" \
    -Dflyway.enabled=false \
    -jar /tds-assessment-service.jar \
    --server-port="8080" \
    --server.undertow.buffer-size=16384 \
    --server.undertow.buffers-per-region=20 \
    --server.undertow.io-threads=64 \
    --server.undertow.worker-threads=512 \
    --server.undertow.direct-buffers=true \
