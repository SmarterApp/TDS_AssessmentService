# Assessment Service Spring Configuration
This document has an example of the Spring application configuration with notes about the different properties.  Examples provided where possible.

```
# Contains properties for the tds-assessment-service.
# Properties not defined in a profile are global to all profiles.  Profiles sections can override the globabl values.

# Undertow Web Server configuration
server:
  port: ${SERVER_PORT:8080}
  undertow:
    buffer-size: 8192
    max-regions: 10
    io-threads: 4
    worker-threads: 32
    direct-buffers: true

# General Spring Datasource information that doesn't need to change
spring:
  datasource:
    hikari:
      maximum-pool-size: 32
      minimum-idle: 8
      idle-timeout: 120000
      connectionTestQuery: "SELECT 1"

# Flyway should not be run by default in the assessment service
flyway:
  enabled: false

# Health checks.  Disable redis and rabbit health checks.
management:
  health:
    redis:
      enabled: false
    rabbit:
      enabled: false

----
spring:
  profiles: # set this to the profile for this configuration.  This allows multiple configurations in a single yml file.
  datasource:
    url: jdbc:mysql://<DB Server>/itembank # DB Connection information
    username: # DB User
    password: # DB Password
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: 'com.mysql.jdbc.Driver'
  redis: 
    sentinel: # this section is if redis is in configured in Kubernetes
      master: mymaster
      nodes: redis-sentinel-service:26379
    host: # Configure AWS Redis server and place server name here
  rabbitmq:
    addresses: # the addresses for rabbitmq
    username: # username to connect to rabbitmq
    password: # password to connect to rabbit mq

tds:
  cache:
    implementation: # cache implementation you want to use i.e 'redis'

logstash-destination: # logstash destination

```