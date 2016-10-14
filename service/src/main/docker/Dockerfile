FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp
ADD docker-startup.sh /
ADD tds-assessment-service-0.0.1-SNAPSHOT.jar tds-assessment-service.jar
RUN sh -c 'touch /docker-startup.sh && touch /tds-assessment-service.jar'
ENTRYPOINT ["/docker-startup.sh"]