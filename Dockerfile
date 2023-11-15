FROM sonatype/nexus3:3.62.0

# Before docker  build
# do "mvn  clean package -Dmaven.javadoc.skip=true" to build the plugin jar

ENV RELEASE_TAG=1.2.0-SNAPSHOT
USER root
RUN mkdir -p /opt/sonatype/nexus/system/fr/auchan/nexus3-gitlabauth-plugin/${RELEASE_TAG}/
COPY target/nexus3-gitlabauth-plugin-${RELEASE_TAG}.jar /opt/sonatype/nexus/system/fr/auchan/nexus3-gitlabauth-plugin/${RELEASE_TAG}/
COPY target/feature/feature.xml /opt/sonatype/nexus/system/fr/auchan/nexus3-gitlabauth-plugin/${RELEASE_TAG}/nexus3-gitlabauth-plugin-${RELEASE_TAG}-features.xml
COPY pom.xml /opt/sonatype/nexus/system/fr/auchan/nexus3-gitlabauth-plugin/${RELEASE_TAG}/nexus3-gitlabauth-plugin-${RELEASE_TAG}.pom
RUN echo '<?xml version="1.0" encoding="UTF-8"?><metadata><groupId>fr.auchan</groupId><artifactId>nexus3-gitlabauth-plugin</artifactId><versioning><release>${RELEASE_TAG}</release><versions><version>${RELEASE_TAG}</version></versions><lastUpdated>20190130132608</lastUpdated></versioning></metadata>' > /opt/sonatype/nexus/system/fr/auchan/nexus3-gitlabauth-plugin/maven-metadata-local.xml
RUN echo "mvn\:fr.auchan/nexus3-gitlabauth-plugin/${RELEASE_TAG} = 200" >> /opt/sonatype/nexus/etc/karaf/startup.properties

USER nexus
