FROM openjdk:17
ADD target/inventory-service-1.0-SNAPSHOT.jar inventory-service.jar
RUN useradd -ms /bin/bash api-inventory
RUN echo 'api-inventory ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
RUN chown api-inventory:api-inventory inventory-service.jar
USER api-inventory
CMD ["java","-jar","inventory-service.jar"]