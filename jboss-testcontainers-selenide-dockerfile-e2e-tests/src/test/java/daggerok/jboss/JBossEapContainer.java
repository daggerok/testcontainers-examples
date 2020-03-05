package daggerok.jboss;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class JBossEapContainer extends GenericContainer<JBossEapContainer> {

  public static final int JBOSS_PORT = 8080;

  public JBossEapContainer(Network network, String... aliases) {
    // super("daggerok/jboss-eap-6.4:6.4.22-alpine");
    super(new org.testcontainers.images.builder.ImageFromDockerfile()
              // docker build -f ui/Dockerfile -t daggerok/ui ..
              .withFileFromPath(".", Paths.get("..", "ui")));
    withExposedPorts(JBOSS_PORT);
    withNetwork(Objects.requireNonNull(network, "Network may not be null."));
    withNetworkAliases(Stream.concat(Arrays.stream(Optional.ofNullable(aliases)
                                                           .orElse(new String[0])),
                                     Stream.of("jboss", "ui"))
                             .distinct()
                             .toArray(String[]::new));
    // withCopyFileToContainer(MountableFile.forHostPath(Paths.get("..", "ui", "target", "ui-1.0.0-SNAPSHOT.war")),
    //                         "/home/jboss/jboss-eap-6.4/standalone/deployments/ui.war");
    waitingFor(new HttpWaitStrategy().forPort(JBOSS_PORT)
                                     .forPath("/ui/")
                                     .forStatusCode(200)
                                     .withStartupTimeout(Duration.ofSeconds(15)));
  }
}
