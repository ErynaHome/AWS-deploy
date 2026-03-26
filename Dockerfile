#1. 빌드 
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests 

#2. 실행
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082  

ENV GOOGLE_API_KEY=${GOOGLE_API_KEY}
# ❌ 이 줄 삭제! (docker-compose에서 이미 설정함)
# ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/foodbot?serverTimezone=Asia/Seoul

ENTRYPOINT ["java", "-jar", "app.jar"]