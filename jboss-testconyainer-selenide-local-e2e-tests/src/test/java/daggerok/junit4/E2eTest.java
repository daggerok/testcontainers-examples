package daggerok.junit4;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import daggerok.jboss.JBossEapContainer;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.codeborne.selenide.Selenide.$;

@Testcontainers
public class E2eTest {

  private Network network = Network.newNetwork();

  @Rule // @Rule must be public, @ClassRule must be static
  public JBossEapContainer jboss = new JBossEapContainer(network);

  @Test
  public void test() {
    Selenide.open(String.format("%s/ui/", jboss.getJBossUrl()));
    $("#app")
        .shouldBe(Condition.visible)
        .shouldHave(Condition.textCaseSensitive("ololo"))
        .shouldHave(Condition.textCaseSensitive("trololo"));
  }

  @After
  public void after() {
    Selenide.closeWebDriver();
    if (jboss.isRunning()) jboss.stop();
  }
}
