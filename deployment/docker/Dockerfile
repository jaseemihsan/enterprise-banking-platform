FROM tomcat:10.1-jdk21

LABEL maintainer="admin_01"

# Remove default applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy Banking WAR
COPY target/banking-app-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh","run"]
