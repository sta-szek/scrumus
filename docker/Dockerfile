FROM jboss/wildfly

USER root

ENV HOME /opt/jboss
ENV JBOSS_HOME /opt/jboss/wildfly

RUN yum install -y \
	postgresql \
	postgresql-contrib \
	pgadmin3 \
	telnet \
	curl

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

COPY /postgresql /opt/jboss/wildfly/modules/system/layers/base/org/postgresql

EXPOSE 9990:9990 8080:8080

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
