#!/bin/bash
FROM tomcat:9.0.68-jre8-temurin-jammy
COPY *.war /usr/local/tomcat/webapps/
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*
ADD /mal /usr/local/tomcat/webapps/ROOT/
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
CMD ["catalina.sh","run"]
