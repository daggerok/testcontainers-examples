# testcontainers examples [![CI](https://github.com/daggerok/testcontainers-examples/workflows/CI/badge.svg)](https://github.com/daggerok/testcontainers-examples/actions)

* [ui](ui) - JavaEE web app under test for JBoss EAP runtime
* [jboss-testconyainer-selenide-local-e2e-tests](jboss-testconyainer-selenide-local-e2e-tests) - Run custom JBoss EAP testcontainer, wait for health conditions and finally execute local Selenide e2e tests in Chrome browser

## JBoss as test-container local selenide testing

_requires Docker_

```bash
docker rm -f -v `docker ps -aq` ; ./mvnw -f ui package ; ./mvnw -f jboss-testconyainer-selenide-local-e2e-tests clean test -Dselenide.timeout=12345
```

## resources

* https://github.com/sdras/awesome-actions
