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

To run against local `sandbox` environment:
```
cd [BOOKLINK_SANDBOX_PROJECT_DIR]
./sandbox.sh local # launch persistence only
cd [BOOKLINK_BACKEND_PROJECT_DIR]
mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-DAPP_BE_DB_URL=jdbc:postgresql://localhost:5432/booklink_sndbx_local -DAPP_BE_HIBERNATE_DDL_AUTO=validate"
```
Note that in lieu of running `docker-compose up` we run sandbox which does the same thing bringing up the database, 
only for a different environment.

To dump latest database schema, run the app with additional profile, almost always in local dev:
```
mvn clean spring-boot:run -Dspring-boot.run.profiles=local,dump-db-schema
```
Schema will be generated to `target/db-schema-latest.sql`. A schema is then compared with schema dump from 
stable branch, say staging, and migration script is determined off a difference between the two.

## Local Docker Image
Booklink [sandbox](https://github.com/mrazjava/booklink#sandbox) can run off a local image too. This is helpful when testing new feature prior merging to `develop` 
ensuring that staging environment will not be negatively impacted by things like database schema changes or other high 
impact refactoring efforts. In other words, running local image via sandbox is like simulating a staging environment; 
one can make sure that all automated upgrade scripts migrate correctly to central `develop` branch and that all tests are passing.

We build a local image just like any other image, except we build it off a custom branch and tag it as `local`:
```
mvn clean package
docker build -t mrazjava/booklink-backend:local .
```
See sandbox for details on how to run off a local image.

## pgAdmin4
Access to `pgAdmin4` is not required to try the backend locally, or even to develop some basic functions. However, 
administering the database during the development will probably become a necessity. All PostgreSQL and pgAdmin4 
credentials are defined in `docker-compose.yml`.

Upon a first login to `pgAdmin4` server connection to docker container running postgres must be established. Remember 
that persistence layer runs off docker engine so for pgAdmin4 sees the database at `pg` host, not localhost.

## Sandbox
It is possible to run against [sandbox](https://github.com/mrazjava/booklink#sandbox) local database, in which case 
`APP_BE_DB_URL` must be overriden.  It's also helpful to compare sandbox staging database to actively developed 
(latest) database version in order to compare schemas, etc.
