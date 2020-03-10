package daggerok.jboss;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class JBossEapContainer extends GenericContainer<JBossEapContainer> {

  public static final int JBOSS_PORT = 8080;
  public static final String IMAGE = String.format("daggerok/jboss-eap-test:%s", Instant.now().getEpochSecond() / 10);

  // super("daggerok/jboss-eap-6.4:6.4.22-alpine");
  // using ../ui/Dockerfile file: docker build -t daggerok/test-image ../ui
  private JBossEapContainer(ImageFromDockerfile dockerfile) {
    super(dockerfile);
    withExposedPorts(JBOSS_PORT);
    waitingFor(new HttpWaitStrategy().forPort(JBOSS_PORT)
                                     .forPath("/ui/")
                                     .forStatusCode(200)
                                     .withStartupTimeout(Duration.ofSeconds(15)));
  }

  public static JBossEapContainer withNet(Network network, String... aliases) {
    Objects.requireNonNull(network, "Network may not be null.");
    String rootProjectDir = Paths.get(".").toAbsolutePath().toFile().getParentFile().getParent();
    Path dockerfilePath = Paths.get(rootProjectDir, "ui"); // $rootProjectDir/ui/Dockerfile
    ImageFromDockerfile dockerfile = new ImageFromDockerfile(IMAGE).withFileFromPath(".", dockerfilePath);
    return new JBossEapContainer(dockerfile).withNetwork(network)
                                            .withNetworkAliases(
                                                Stream.concat(Arrays.stream(Optional.ofNullable(aliases)
                                                                                    .orElse(new String[0])),
                                                              Stream.of("jboss", "ui"))
                                                      .distinct()
                                                      .toArray(String[]::new));
  }
}
