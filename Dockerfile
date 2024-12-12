# 使用官方的OpenJDK镜像作为基础镜像
FROM openjdk:11-jre-slim

    # 设置工作目录
WORKDIR /app

    # 将构建好的jar文件复制到镜像中
COPY sendMsg2-0.0.1-SNAPSHOT.jar /app/sendMsg2.jar

    # 暴露应用运行的端口（如果有）
EXPOSE 8080

    # 运行应用
CMD ["java", "-jar", "sendMsg2.jar"]