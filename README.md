# Iot - Control environment (server + admin)

This is the server for app : https://github.com/uzuhinta/react-native_iot-control-environment

## Tech stack

-   Java 8
-   MySQL
-   VueJs

## Instruction

```sh
# Step 1: install dependency

# Step 2: read file src/main/resources/application.properties
# change many properties:
# spring.datasource.username=....
# spring.datasource.password=...

# Step 3: create mysql database named "iot"

# Step 4: run build spring app
./mvnw spring-boot:run

# Step 5: if it isn't error, you can see result
# FE: http://localhost:8080/agriculture (username: quannar178, password: 123456)
# API document: http://localhost:8080/agriculture/swagger-ui.html
```
