package daggerok.junit4;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import daggerok.jboss.JBossEapContainer;
import org.junit.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

@Testcontainers
public class E2eTest {

  private static Network network = Network.builder()
                                          .driver("bridge")
                                          .id("test-net")
                                          .build();

  @ClassRule
  public static JBossEapContainer jboss = new JBossEapContainer(network, "jboss", "jboss.test-net");

  @ClassRule
  public static final GenericContainer browser = new BrowserWebDriverContainer()
      .withRecordingMode(RECORD_ALL, Paths.get(".", "target").toAbsolutePath().toFile())
      .withCapabilities("chrome".equalsIgnoreCase(System.getProperty("selenide.browser", "chrome"))
                            ? DesiredCapabilities.chrome() : DesiredCapabilities.firefox())
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
