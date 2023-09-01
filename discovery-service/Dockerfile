FROM openjdk:17
ADD target/discovery-service-1.0-SNAPSHOT.jar discovery-service.jar
RUN useradd -ms /bin/bash api-discovery
RUN echo 'api-discovery ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
RUN chown api-discovery:api-discovery discovery-service.jar
USER api-discovery
CMD ["java","-jar","discovery-service.jar"]