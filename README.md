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
Backend will run on port `8080`. PostgreSQL will run on port `5432`. PgAdmin4 will run on port `5500`.

## Persistence
Access to `pgAdmin4` is not required to try the backend locally, or even to develop some basic functions. However, 
administering the database during the development will probably become a necessity. All PostgreSQL and pgAdmin4 
credentials are defined in `docker-compose.yml`.

Upon a first login to `pgAdmin4` server connection to docker container running postgres must be established. Remember 
that persistence layer runs off docker engine so for pgAdmin4 sees the database at `pg` host, not localhost.

## Sandbox
It is possible to run against [sandbox](https://github.com/mrazjava/booklink#sandbox) local database, in which case 
`APP_BE_DB_URL` must be overriden.  It's also helpful to compare sandbox staging database to actively developed 
(latest) database version in order to compare schemas, etc.
