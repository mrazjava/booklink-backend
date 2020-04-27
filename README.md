[![release](https://github.com/mrazjava/booklink-backend/workflows/release/badge.svg?branch=master)](https://github.com/mrazjava/booklink-backend/actions?query=workflow%3Arelease) 
[![pre-release](https://github.com/mrazjava/booklink-backend/workflows/pre-release/badge.svg?branch=master)](https://github.com/mrazjava/booklink-backend/actions?query=workflow%3Apre-release) 
[![snapshot](https://github.com/mrazjava/booklink-backend/workflows/snapshot/badge.svg?branch=develop)](https://github.com/mrazjava/booklink-backend/actions?query=workflow%3Asnapshot)
# Booklink - core services
Backend business logic of a booklink service. Exposes operations via REST. Used by frontends.

## It's Alive
- production (not setup yet)
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

We can also run quickly against other environments using additional profiles. Here we run against our AWS `pre` release<sup>1</sup> 
database:
```
mvn clean spring-boot:run -Dspring-boot.run.profiles=local,aws-db-pre1
```
See `src/main/resources/` for additional profiles or build your own.

<sup>1</sup> | AWS resources have limited (admin) access from the outside world - use sandbox

## Sandbox
We can make a local docker image and run it off [sandbox](https://github.com/mrazjava/booklink#sandbox) `local` environment. This is helpful when testing new code prior merging to `develop` branch which is basis for sandbox staging. Running local image via sandbox is like simulating a staging environment; one can make sure that all automated upgrade scripts migrate correctly to central `develop` branch and that all tests are passing.

We build a local image just like any other image, except we build it off a custom branch and tag it as `local`:
```
mvn clean package
docker build -t mrazjava/booklink-backend:local .
```

It is possible also possible to run via `mvn` against [sandbox](https://github.com/mrazjava/booklink#sandbox) database, in which case `spring.datasource.url` must be overriden via `APP_BE_DB_URL` env variable. 

To run against a `sandbox` database, say `local` adjusting config on-the-fly via ENV overrides:
```
mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-DAPP_BE_DB_URL=jdbc:postgresql://localhost:5432/booklink_sndbx_local -DAPP_BE_HIBERNATE_DDL_AUTO=validate -DAPP_SPRING_DATA_INIT=never -DAPP_FLYWAY_ENABLED=true"
```
or, same thing with less typing using a pre-configured profile:
```
mvn clean spring-boot:run -Dspring-boot.run.profiles=local,sndbx-local
```

## DB GUI
There are number of graphical tools available to administer the database. Here are mentioned ones used for booklink:

#### pgAdmin4
Sandbox comes packaged with `pgAdmin4` but using it is not required to try the backend locally, or even to develop some basic functions. However, administering the database during the development will probably become a necessity at some point. All PostgreSQL and pgAdmin4 credentials are defined in `docker-compose.yml`.

Upon a first login to `pgAdmin4` server connection to docker container running postgres must be established. Remember that persistence layer runs off docker engine so for pgAdmin4 sees the database at `pg` host, not localhost.

#### DBeaver
An alternative and excellent GUI for local administration of the database is [DBeaver](https://dbeaver.io/). This is a fat client which if preferred, must be downloaded and installed separately.

## Database Schema Management
Default behavior of application with respect to the database varies depending on the environment:

#### Local Development
Upon application startup (`mvn spring-boot:run`) the database will be re-built every time because of `spring.jpa.hibernate.ddl-auto:create`. Dummy data will be imported from `src/main/resources/import.sql`.

#### Sandbox
Database schema will be validated because of `APP_BE_HIBERNATE_DDL_AUTO: validate` override, and application will fail fast if schema does not match against the entities. However, schema changes should be migrated automatically (via [flyway](https://flywaydb.org/)) on startup before hibernate validation kicks in. It is a responsibility of a developer working on a feature to introduce a correct flyway [migration script](https://flywaydb.org/getstarted/how).

#### AWS
Database schema changes are migrated manually ("by hand"). Verified migration scripts from Sandbox (`stg`) are used as the basis for AWS manual db migration changes to `pre` which in turn are basis for AWS `live` migration.

### Migration
A new feature will often result in updated database schema (entity changes). A process of migrating database changes from `local` feature to `stg` is as follows:

Database change migration scripts are computed as a difference between the old and new schemas. Once finished on a feature branch, we extract the latest schema script. 
 
From a `feature/*` branch with the latest entities:
```
mvn clean compile hibernate54-ddl:gen-ddl
```
Full schema DDL will be available at `target/generated-resources/sql/ddl/auto/postgresql95.sql`. Save this script to some location because it will be compared against DDL produced off `develop` branch.

Next, we switch to `develop` branch and run the same maven command again. This produces DDL for staging schema. The 
two schemas are compared and the difference makes up the basis for migration script. Even though the DIFF is an 
excellent starting point, the migration itself should be proof read and adjusted manually, then tested as needed.

Once migration changes are ready, the are placed in `src/main/resources/db/migration/VXXX_*` file. If migration is 
correctly scripted out, sandbox will apply it automatically.

To get more help on options available to `hibernate54-ddl` maven plugin, run:
```
mvn hibernate54-ddl:help -Ddetail=true
```
