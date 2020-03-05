package daggerok.jupiter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import daggerok.jboss.JBossEapContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.codeborne.selenide.Selenide.$;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class E2eJupiterTest {

  private static Network network = Network.newNetwork();

  @Container
  private static JBossEapContainer jboss = new JBossEapContainer(network);

  @Test
  void test() {
    Selenide.open(String.format("%s/ui/", jboss.getJBossUrl()));
    $("#app")
        .shouldBe(Condition.visible)
        .shouldHave(Condition.textCaseSensitive("ololo"))
        .shouldHave(Condition.textCaseSensitive("trololo"));
  }

  @AfterAll
  void afterAll() {
    Selenide.closeWebDriver();
    if (jboss.isRunning()) jboss.stop();
  }
}
