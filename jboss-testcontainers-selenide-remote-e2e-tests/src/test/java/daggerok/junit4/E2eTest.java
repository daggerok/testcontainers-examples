package daggerok.junit4;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import daggerok.jboss.JBossEapContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

@Testcontainers
public class E2eTest {

  private static final Logger log = LoggerFactory.getLogger(E2eTest.class);

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

  private static Network network = Network.builder()
                                          .driver("bridge")
                                          .id("test-net")
                                          .build();

  @ClassRule
  public static JBossEapContainer jboss = new JBossEapContainer(network, "jboss", "jboss.test-net");

  @ClassRule
  public static final GenericContainer browser = new BrowserWebDriverContainer()
      .withRecordingMode(RECORD_ALL, recordings.toFile())
      .withCapabilities("chrome".equalsIgnoreCase(System.getProperty("selenide.browser", "chrome"))
                            ? new ChromeOptions() : new FirefoxOptions())
      .withNetwork(network)
      .withNetworkAliases("browser");

  @Before
  public void before() {
    RemoteWebDriver remoteWebDriver = ((BrowserWebDriverContainer) browser).getWebDriver();
    WebDriverRunner.setWebDriver(remoteWebDriver);
  }

  @Test
  public void test() {
    Selenide.open(String.format("http://jboss.test-net:8080/ui/", JBossEapContainer.JBOSS_PORT));

    SelenideElement app = $("#app");
    log.info(app.text());

    String text = app.shouldBe(Condition.visible)
                     .shouldHave(Condition.textCaseSensitive("ololo"))
                     .shouldHave(Condition.textCaseSensitive("trololo"))
                     .text();
    log.info(text);
    Selenide.sleep(1234);
  }

  @After
  public void after() {
    Selenide.closeWebDriver();
    if (jboss.isRunning()) jboss.stop();
  }
}
