# User Auth Service

### Prerequisites

- Java 1.8
- Maven 3+
- Local SonarQube instance

### Local Envrionment setup

#### SonarQube
- Download and unzip the `sonarqube` v7.7 [from this link](https://www.sonarqube.org/downloads/)
- Unzip and `cd` to the sonarqube app directory.
- Run `./bin/<YOUR_OS>/sonar.sh start`

#### GIT repo
- [basic-web-authentication](https://github.com/WaleedSQ/basic-web-authentication.git)

### Running The App

**Running tests:** `mvn clean test`

**Package into a `jar`:** `mvn clean package`

**Running the app:** `mvn spring-boot:run`

**Running SonarQube scan:** `mvn sonar:sonar`


### Other References

##### Tools & Techs Included

This is a Spring Boot project with `maven` as build and package manager. The related techs in the project
is listed below with proper reference:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#production-ready)
