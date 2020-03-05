package daggerok.jupiter;

import com.codeborne.selenide.*;
import daggerok.jboss.JBossEapContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class E2eJupiterTest {

  private static final Logger log = LoggerFactory.getLogger(E2eJupiterTest.class);

  private static final String classPath = System.getProperty("java.class.path");
  private static final int gradle = classPath.split(".gradle").length;
  private static final int maven = classPath.split(".m2").length;
  private static final String outputDir = gradle > maven ? "build" : "target";
  private static final Path recordings = Paths.get(".", outputDir)
                                              .toAbsolutePath();
  static {
    log.info("maven/gradle: {}/{}", maven, gradle);
    recordings.toFile().mkdirs();
  }

  private static final Network network = Network.newNetwork();

  @Container
  private static final JBossEapContainer jboss = JBossEapContainer.withNet(network);

  @Container
  private static final GenericContainer browser = new BrowserWebDriverContainer()
      .withRecordingMode(RECORD_ALL, recordings.toFile())
      .withCapabilities("chrome".equalsIgnoreCase(System.getProperty("selenide.browser", "chrome"))
                            ? new ChromeOptions() : new FirefoxOptions())
      .withNetwork(network)
      .withNetworkAliases("browser");

  @BeforeEach
  void beforeEach() {
    RemoteWebDriver remoteWebDriver = ((BrowserWebDriverContainer) browser).getWebDriver();
    WebDriverRunner.setWebDriver(remoteWebDriver);

    Configuration.timeout = 15000;
    if (!jboss.isRunning()) jboss.start();
    while (!jboss.isHealthy()) {
      jboss.waitingFor(new HttpWaitStrategy().forPort(jboss.JBOSS_PORT)
                                             .forPath("/ui/")
                                             .forStatusCode(200)
                                             .withStartupTimeout(Duration.ofSeconds(15)));
    }
  }

  @Test
  void test() {
    Selenide.open(String.format("http://jboss:%d/ui/", jboss.JBOSS_PORT));

    SelenideElement app = $("#app");
    log.info(app.text());

    String text = app.shouldBe(Condition.visible)
                     .shouldHave(Condition.textCaseSensitive("ololo"))
                     .shouldHave(Condition.textCaseSensitive("trololo"))
                     .text();
    log.info(text);
  }

  @Test
  void test2() {
    Selenide.open(String.format("http://jboss:%d/ui/", jboss.JBOSS_PORT));

    SelenideElement app = $("#app");
    log.info(app.text());

    String text = app.shouldBe(Condition.visible)
                     .shouldHave(Condition.textCaseSensitive("ololo"))
                     .shouldHave(Condition.textCaseSensitive("trololo"))
                     .text();
    log.info(text);
  }

  @AfterEach
  void afterEach() {
    // Selenide.closeWebDriver();
    if (jboss.isRunning()) jboss.stop();
    // if (browser.isRunning()) browser.stop();
  }
}
