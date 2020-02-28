# testcontainers examples [![JBoss EAP 6.4.22 Testcontainer Selenide remote E2E tests](https://github.com/daggerok/testcontainers-examples/workflows/JBoss%20EAP%206.4.22%20Testcontainer%20Selenide%20remote%20E2E%20tests/badge.svg)](https://github.com/daggerok/testcontainers-examples/actions?query=workflow%3A%22JBoss+EAP+6.4.22+Testcontainer+Selenide+remote+E2E+tests%22)

* [ui](ui) - JavaEE web app under test for JBoss EAP runtime
* [jboss-testcontainer-selenide-local-e2e-tests](jboss-testcontainer-selenide-local-e2e-tests) - Run custom JBoss EAP testcontainer, wait for health conditions and finally execute local Selenide e2e tests in Chrome browser

## JBoss as test-container selenide testing

_requires Docker_

```bash
#./mvnw clean ; ./mvnw -f ui package ; ./mvnw -f jboss-testcontainer-selenide-local-e2e-tests clean test
./mvnw clean ; ./mvnw -f ui package ; ./mvnw -f jboss-testcontainer-selenide-remote-e2e-tests clean test
# -Dselenide.browser=firefox
```

## other repositories

* https://github.com/daggerok/testcontainers-examples
* https://github.com/daggerok/testcontainers-selenide-example
* https://github.com/selenide-examples/testcontainers
* https://github.com/testcontainers/testcontainers-java/tree/master/examples
* https://github.com/testcontainers/testcontainers-java-examples

## resources

* https://github.com/sdras/awesome-actions
* https://github.com/actions/virtual-environments/blob/master/images/linux/Ubuntu1804-README.md
