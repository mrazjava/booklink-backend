[![release](https://github.com/mrazjava/booklink-backend/workflows/release/badge.svg?branch=master)](https://github.com/mrazjava/booklink-backend/actions?query=workflow%3Arelease) 
[![pre-release](https://github.com/mrazjava/booklink-backend/workflows/pre-release/badge.svg?branch=master)](https://github.com/mrazjava/booklink-backend/actions?query=workflow%3Apre-release) 
[![snapshot](https://github.com/mrazjava/booklink-backend/workflows/snapshot/badge.svg?branch=develop)](https://github.com/mrazjava/booklink-backend/actions?query=workflow%3Asnapshot)
# Booklink - core services
Backend business logic of a booklink service. Exposes operations via REST. Used by UI frontends.

## Stack
- Java 11
- Spring Boot
- REST / Swagger
- PostgreSQL / Hibernate

## Quick Start
```
docker-compose up
mvn clean spring-boot:run
```
Backend will run on port `8080`. PostgreSQL will run on port `5432`. PgAdmin4 will run on port `8100`.

## pgAdmin4
Access to `pgAdmin4` is not required to try the backend locally, or even to develop some basic functions. However, 
sooner or later administering the database during the development will become a necessity. To log into `pgAdmin4` 
enter the following credentials:

* Login e-mail address: value of `PGADMIN_DEFAULT_EMAIL` defined in `docker-compose.yml`
* Login password: value of `PGADMIN_DEFAULT_PASSWORD` defined in `docker-compose.yml`

Upon a first login to `pgAdmin4` server connection to docker container running postgres must be established. Right 
click `Servers`, choose `Create->Server...`:

* Tab: `General`
    - Field: `Name` set to `docker`
* Tab: `Connection`
    - Field: `Host` set to `pg`
    - Field: `Username` set to value of `POSTGRES_USER` defined in `docker-compose.yml`
    - Field: `Password` set to value of `POSTGRES_PASSWORD` defined in `docker-compose.yml`

Leave all other fields as set, click `Save`. Connection to database should be established.
