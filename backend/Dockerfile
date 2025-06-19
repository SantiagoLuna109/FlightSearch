FROM amazoncorretto:21-alpine

COPY zscaler_root.crt /tmp/zscaler_root.crt
RUN keytool -importcert \
      -file /tmp/zscaler_root.crt \
      -alias zscaler-root \
      -keystore $JAVA_HOME/lib/security/cacerts \
      -storepass changeit -noprompt

WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]