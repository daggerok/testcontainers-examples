# testcontainers examples [![JBoss EAP 6.4.22 Testcontainers Selenide Dockerfile Firefox E2E tests](https://github.com/daggerok/testcontainers-examples/workflows/JBoss%20EAP%206.4.22%20Testcontainers%20Selenide%20Dockerfile%20Firefox%20E2E%20tests/badge.svg)](https://github.com/daggerok/testcontainers-examples/actions)

* [ui](ui) - JavaEE web app under test for JBoss EAP runtime
* [jboss-testcontainers-selenide-local-e2e-tests](jboss-testcontainers-selenide-local-e2e-tests) - Run custom JBoss EAP testcontainer, wait for health conditions and finally execute local Selenide e2e tests in Chrome browser
  testcontainer, wait for health conditions and finally execute local Selenide e2e tests in Chrome browser
  ```bash
  # docker is required
  ./mvnw clean package -f ui -DskipTests
  ./mvnw clean test -f jboss-testcontainers-selenide-local-e2e-tests
  # gradle
  ./gradlew clean build -p ui ; ./gradlew clean test -p jboss-testcontainers-selenide-local-e2e-tests
  ```
* [jboss-testcontainers-selenide-remote-e2e-tests](jboss-testcontainers-selenide-remote-e2e-tests) - Run custom JBoss
  EAP testcontainer, wait for health conditions and finally execute remote web driver Selenide e2e tests in Chrome or
  Firefox browsers
* [jboss-testcontainers-selenide-dockerfile-e2e-tests](jboss-testcontainers-selenide-dockerfile-e2e-tests) - Run custom
  JBoss EAP testcontainer by using existing external Dockerfile, wait for health conditions and finally execute remote
  web driver Selenide e2e tests in Chrome or Firefox browsers
  ```bash
  # docker is required
  ./mvnw clean
  ./mvnw -DskipTests -f ui clean package
  ./mvnw -f jboss-testcontainers-selenide-remote-e2e-tests clean test
  # gradle
  ./gradlew clean build -p ui ; ./gradlew clean test -p jboss-testcontainers-selenide-dockerfile-e2e-tests
  ```

## JBoss as test-container selenide testing

_requires Docker_

```bash
##./mvnw clean ; ./mvnw -f ui package ; ./mvnw -f jboss-testcontainers-selenide-local-e2e-tests clean test
#./mvnw clean ; ./mvnw -f ui package ; ./mvnw -f jboss-testcontainers-selenide-remote-e2e-tests clean test
./mvnw clean ; ./mvnw -f ui package ; ./mvnw -f jboss-testcontainers-selenide-dockerfile-e2e-tests clean test
```

_windows_

```batch
mvnw clean package -DskipTests
mvnw test
```

to switch browser, use: `-Dselenide.browser=firefox`

_gradle_

```bash
./gradlew clean ui:war ; ./gradlew test
# on ci:
./gradlew clean ui:war ; ./gradlew test -x jboss-testcontainers-selenide-local-e2e-tests:test
```

_windows_

```batch
gradlew clean build -x test
gradlew test
```

## other repositories

* https://github.com/daggerok/spring-data-jpa-repositories-test-containers-example
* https://github.com/daggerok/testcontainers-selenide-example
* https://github.com/selenide-examples/testcontainers
* https://github.com/testcontainers/testcontainers-java/tree/master/examples
* https://github.com/testcontainers/testcontainers-java-examples

## resources

* [YouTube: Testing Spring Boot Applications](https://www.youtube.com/watch?v=Wpz6b8ZEgcU&feature=youtu.be&t=2088s)
* https://github.com/sdras/awesome-actions
* https://github.com/actions/virtual-environments/blob/master/images/linux/Ubuntu1804-README.md
