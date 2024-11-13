# 使用官方的Java运行时作为父镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 将Maven依赖复制到容器中
COPY target/*.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
