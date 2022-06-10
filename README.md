# Spring Security DB Auth Example

SpringSecurity database auhentication example for Spring Web MVC

## Run:

```
cd docker
docker compose up -d --build
```

`./mvnw.cmd spring-boot:run '-Dspring-boot.run.profiles=mariadb'`

`./mvnw spring-boot:run '-Dspring-boot.run.profiles=mariadb'`

## Default User

application.properties

```
spring.security.user.name=username
spring.security.user.password=password
spring.security.user.roles=USER
```
