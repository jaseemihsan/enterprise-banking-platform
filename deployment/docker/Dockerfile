FROM tomcat:10.1-jdk21

LABEL maintainer="admin_01"

# Remove default applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Create directory for JMX Exporter
RUN mkdir -p /opt/jmx

# Copy JMX Exporter
COPY monitoring/jmx/jmx_prometheus_javaagent-1.3.0.jar /opt/jmx/
COPY monitoring/jmx/config.yml /opt/jmx/

# Deploy Banking Application
COPY target/banking-app-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Start Tomcat with JMX Exporter
ENV CATALINA_OPTS="-javaagent:/opt/jmx/jmx_prometheus_javaagent-1.3.0.jar=9404:/opt/jmx/config.yml"

# Expose application and metrics ports
EXPOSE 8080
EXPOSE 9404

CMD ["catalina.sh","run"]
