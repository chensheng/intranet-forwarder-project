FROM openjdk:8-jdk-alpine
WORKDIR /usr/local
VOLUME /tmp
RUN mkdir -p app/config && mkdir -p app/logs \
    && echo 'http://mirrors.ustc.edu.cn/alpine/v3.5/main' > /etc/apk/repositories \
    && echo 'http://mirrors.ustc.edu.cn/alpine/v3.5/community' >>/etc/apk/repositories \
    && apk update && apk add -U tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone
COPY target/*.jar app/app.jar
WORKDIR /usr/local/app
CMD ["java", "-jar", "app.jar"]