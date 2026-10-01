FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN cp src/main/resources/META-INF/persistence.xml.example src/main/resources/META-INF/persistence.xml \
    && mvn -B -DskipTests package

FROM tomcat:10.1-jdk17

# Xóa các ứng dụng mặc định để làm sạch Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/maillist-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Mở cổng 8080
EXPOSE 8080

# Khởi động máy chủ Tomcat (tự động nhận biến $PORT từ Render)
CMD ["sh", "-c", "if [ -n \"$PORT\" ]; then sed -i \"s/port=\\\"8080\\\"/port=\\\"$PORT\\\"/g\" /usr/local/tomcat/conf/server.xml; fi && catalina.sh run"]
