[![release](https://github.com/mrazjava/booklink-backend/workflows/release/badge.svg?branch=master)](https://github.com/mrazjava/booklink-backend/actions?query=workflow%3Arelease) 
[![pre-release](https://github.com/mrazjava/booklink-backend/workflows/pre-release/badge.svg?branch=master)](https://github.com/mrazjava/booklink-backend/actions?query=workflow%3Apre-release) 
[![snapshot](https://github.com/mrazjava/booklink-backend/workflows/snapshot/badge.svg?branch=develop)](https://github.com/mrazjava/booklink-backend/actions?query=workflow%3Asnapshot)
# Booklink - core services
Backend business logic of a booklink service. Exposes operations via REST. Used by frontends.

## It's Alive
Booklink is deployed in AWS, and branded as booklinktrove.com. Because we are self funded for the time being, we spin production 
environment only periodically to test releases. Keeping production environment down until we are ready with the go-live helps us save on our AWS bill. However, our 
pre-release environment is available 24/7 although it is an environment hosted in European zone with fairly small resources.
- [production](https://be.booklinktrove.com/actuator/info)
- [pre-release](https://pre-be.booklinktrove.com/actuator/info)

## Stack
- Java 11
- Spring Boot
- REST / Swagger
- PostgreSQL / Hibernate

## Quick Start
It's easiest to run with default config out of the box:
```
docker-compose up
mvn clean spring-boot:run
```
Backend will run on port `8080`. PostgreSQL will run on port `5433`. PgAdmin4 will run on port `5501`.

It's possible to run without `docker-compose` but in that case database and other artifacts normally 
faciliated by docker must be provided explicitly. Here is an example when we run<sup>1</sup> booklink directly 
against AWS artifacts:
```
mvn clean spring-boot:run -Dspring-boot.run.profiles=local,pre-aws
```
See `src/main/resources/` for additional custom profiles, or build your own.

<sup>1</sup> | Authenticates AWS with credentials/config from your `~/.aws/` directory. Booklink AWS resources have limited (admin) access not available to the public. Either use your own AWS infrastructure, contact me for special access, or use sandbox.

## Sandbox
We can make a custom docker image from our latest work and run it off [sandbox](https://github.com/mrazjava/booklink#sandbox) `local` environment. This is helpful when testing new code prior merging to `develop`, which is the basis for sandbox staging. Running local image via sandbox is like simulating a staging environment; one can make sure that all automated db scripts migrate correctly and that all tests are passing.

We build a local image just like any other image, except we build it off whatever branch and tag it as `local`:
```
mvn clean package
docker build -t mrazjava/booklink-backend:local .
```
Then, to run off sandbox `local` the image we have just built, invoke:
```
./sandbox local -b
```
It is also possible to run via `mvn` against [sandbox](https://github.com/mrazjava/booklink#sandbox) database, in which case `spring.datasource.url` must be overriden via `APP_BE_DB_URL` env variable. 

To run against a `sandbox` database, say `local`, first start sandbox bare bone:
```
./sandbox local
```
and adjusting config on-the-fly via ENV overrides, kick off maven run:
```
mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-DAPP_BE_DB_URL=jdbc:postgresql://localhost:5432/booklink_sndbx_local -DAPP_BE_HIBERNATE_DDL_AUTO=validate -DAPP_SPRING_DATA_INIT=never -DAPP_FLYWAY_ENABLED=true"
```
Here is the same maven run with less typing (using a pre-configured profile):
```
mvn clean spring-boot:run -Dspring-boot.run.profiles=local,sndbx-local
```

## Database Schema Management
Default behavior of application with respect to the database varies depending on the environment:

#### Local Development
Upon application startup (`mvn spring-boot:run`) the database will be re-built every time by Hibernate, due to `spring.jpa.hibernate.ddl-auto:create`. Dummy data will be imported from `src/main/resources/data-postgresql.sql`.

#### Sandbox
Database schema will be validated because of `APP_BE_HIBERNATE_DDL_AUTO: validate` override, and application will fail fast if schema does not match against the entities. However, schema changes should be migrated automatically (via [flyway](https://flywaydb.org/)) on startup before hibernate validation kicks in.

#### AWS
Database schema changes are migrated manually ("by hand"). Verified migration scripts from Sandbox (`stg`) are used as the basis for AWS manual db migration changes to `pre` which in turn are basis for AWS `live` migration.

### Other
Full schema dump off Hibernate can be produced with maven:
```
mvn clean compile hibernate54-ddl:gen-ddl
```
To get more help on options available to `hibernate54-ddl` maven plugin, run:
```
mvn hibernate54-ddl:help -Ddetail=true
```
