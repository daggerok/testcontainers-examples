package daggerok.jupiter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import daggerok.jboss.JBossEapContainer;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class E2eJupiterTest {

  private Network network = Network.newNetwork();

  @Container
  private JBossEapContainer jboss = new JBossEapContainer(network);

  @Container
  private GenericContainer browser = new BrowserWebDriverContainer()
      .withRecordingMode(RECORD_ALL, new File("target"))
      .withCapabilities("chrome".equalsIgnoreCase(System.getProperty("selenide.browser", "chrome"))
                            ? new ChromeOptions() : new FirefoxOptions())
      .withNetwork(network)
      .withNetworkAliases("browser");

  @BeforeEach
  void before() {
    RemoteWebDriver remoteWebDriver = ((BrowserWebDriverContainer) browser).getWebDriver();
    WebDriverRunner.setWebDriver(remoteWebDriver);
  }

  @Test
  void test() {
    Selenide.open(String.format("http://jboss:8080/ui/", jboss.JBOSS_PORT));
    $("#app")
        .shouldBe(Condition.visible)
        .shouldHave(Condition.textCaseSensitive("ololo"))
        .shouldHave(Condition.textCaseSensitive("trololo"));
  }

  @AfterEach
  void afterAll() {
    Selenide.closeWebDriver();
    if (jboss.isRunning()) jboss.stop();
  }
}
