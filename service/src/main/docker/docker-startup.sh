#!/bin/sh
#-----------------------------------------------------------------------------------------------------------------------
# File:  docker-startup.sh
#
# Desc:  Start the tds-assessment-service.jar with the appropriate properties.
#
#-----------------------------------------------------------------------------------------------------------------------

java $JAVA_OPTS -jar /tds-assessment-service.jar
