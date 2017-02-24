FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp
ADD docker-startup.sh /
ADD tds-assessment-service-*.jar tds-assessment-service.jar
RUN sh -c 'touch /docker-startup.sh && touch /tds-assessment-service.jar'
RUN apk --no-cache add curl
ENTRYPOINT ["/docker-startup.sh"]