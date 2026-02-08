FROM jenkins/jenkins:lts-jdk17

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

COPY security.groovy /usr/share/jenkins/ref/init.groovy.d/security.groovy
COPY credentials.groovy /usr/share/jenkins/ref/init.groovy.d/credentials.groovy

USER root
RUN apt-get update && apt-get install -y docker.io

COPY jenkins_id_rsa /usr/share/jenkins/ref/jenkins_id_rsa
RUN chmod 600 /usr/share/jenkins/ref/jenkins_id_rsa && chown jenkins:jenkins /usr/share/jenkins/ref/jenkins_id_rsa

USER jenkins