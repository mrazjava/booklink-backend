# Templates
Any custom overrides of templates generated by various libraries live here. Most common generators of templates 
would be Swagger (codegen) and Spring (mvc).

## Swagger Codegen
Templates generated by swagger codegen are java files for REST CLIENT access to depot API. Note that template 
customization does not work with versions older than `3.0.21` (see bugs).

To override a template, we start by copying the original template from swagger generator dependency:
```
<dependency>
  <groupId>io.swagger.codegen.v3</groupId>
  <artifactId>swagger-codegen-generators</artifactId>
</dependency>
``` 

#### Example
1) In `application.yml` set `spring.mustache.check-template-location` to `true`.
2) In `pom.xml`, under `swagger-codegen-maven-plugin` configuration, set `templateDirectory` to `src/main/resources/templates/swagger/`.
3) From the `swagger-codegen-generators` JAR dependency, copy `/handlebars/Java/ApiClient.mustache` to template dir from #2.
4) Customize mustache template as desired (as a test, maybe just add a comment).
5) Run the app and check `target/generated-sources/depot-api` to observe customized changes.

#### Documentation 
- https://app.swaggerhub.com/help/enterprise/config/codegen-templates
- https://blog.lahteenmaki.net/making-swagger-codegen-tolerable.html

#### Bugs
Encountered bugs which affect this project (or have affected it in the past).
- `>3.0.20` [broken logging](https://github.com/swagger-api/swagger-codegen/issues/10515)
- `<=3.0.20` [template overriding](https://github.com/swagger-api/swagger-codegen/issues/10272)

## Spring MVC
Not used