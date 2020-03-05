package daggerok.jupiter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import daggerok.jboss.JBossEapContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class E2eJupiterTest {

  private final Path recordings = Paths.get(".", "target")
                                       .toAbsolutePath();

  private final Network network = Network.newNetwork();

  @Container
  private final JBossEapContainer jboss = new JBossEapContainer(network);

  @Container
  private final GenericContainer browser = new BrowserWebDriverContainer()
      .withRecordingMode(RECORD_ALL, recordings.toFile())
      .withCapabilities("chrome".equalsIgnoreCase(System.getProperty("selenide.browser", "chrome"))
                            ? new ChromeOptions() : new FirefoxOptions())
      .withNetwork(network)
      .withNetworkAliases("browser");

  @BeforeEach
  void before() {
    recordings.toFile().mkdirs();
    RemoteWebDriver remoteWebDriver = ((BrowserWebDriverContainer) browser).getWebDriver();
    WebDriverRunner.setWebDriver(remoteWebDriver);
  }

  @Test
  void test() {
    Selenide.open(String.format("http://jboss:%d/ui/", jboss.JBOSS_PORT));

    String text = $("#app").shouldBe(Condition.visible)
                           .shouldHave(Condition.textCaseSensitive("ololo"))
                           .shouldHave(Condition.textCaseSensitive("trololo"))
                           .text();

    LoggerFactory.getLogger(getClass()).info(text);
  }

  @AfterEach
  void afterAll() {
    Selenide.closeWebDriver();
    if (jboss.isRunning()) jboss.stop();
  }
}
