package daggerok.jupiter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.nio.file.Paths;

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

  private Network network = Network.newNetwork();

  @Container
  private JBossEapContainer jboss = new JBossEapContainer(network);

  @Container
  private GenericContainer browser = new BrowserWebDriverContainer()
      .withRecordingMode(RECORD_ALL, recordings.toFile())
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

    SelenideElement app = $("#app");
    log.info(app.text());

    String text = app.shouldBe(Condition.visible)
                     .shouldHave(Condition.textCaseSensitive("ololo"))
                     .shouldHave(Condition.textCaseSensitive("trololo"))
                     .text();
    log.info(text);
    Selenide.sleep(123);
  }

  @AfterEach
  void afterAll() {
    Selenide.closeWebDriver();
    if (jboss.isRunning()) jboss.stop();
  }
}
