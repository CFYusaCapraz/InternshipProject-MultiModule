FROM openjdk:17
ADD target/api-gateway-1.0-SNAPSHOT.jar gateway-service.jar
RUN useradd -ms /bin/bash api-gateway
RUN echo 'api-gateway ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
RUN chown api-gateway:api-gateway gateway-service.jar
USER api-gateway
CMD ["java","-jar","gateway-service.jar"]