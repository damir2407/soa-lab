FROM jboss/wildfly:latest

COPY backend/build/libs/backend-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
COPY EJB/build/libs/EJB-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/

COPY backend/src/main/resources/truststore/keystore.jks /keystore.jks

