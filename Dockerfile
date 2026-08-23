# Sử dụng base image Tomcat 10 chạy JDK 17 (tương thích Jakarta EE)
FROM tomcat:10.1-jdk17

# Xóa các ứng dụng mặc định để làm sạch Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file war đã build vào làm ứng dụng mặc định (ROOT.war)
COPY target/maillist-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Mở cổng 8080
EXPOSE 8080

# Khởi động máy chủ Tomcat
CMD ["catalina.sh", "run"]