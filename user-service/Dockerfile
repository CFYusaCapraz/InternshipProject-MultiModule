FROM openjdk:17
ADD target/user-service-1.0-SNAPSHOT.jar user-service.jar
RUN useradd -ms /bin/bash api-user
RUN echo 'api-user ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
RUN chown api-user:api-user user-service.jar
USER api-user
CMD ["java","-jar","user-service.jar"]