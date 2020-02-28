package daggerok.jboss;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategyTarget;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class JBossEapContainer extends GenericContainer<JBossEapContainer> {

  private static final int JBOSS_PORT = 8080;

  public JBossEapContainer(Network network, String... aliases) {
    super("daggerok/jboss-eap-6.4:6.4.22-alpine");
    withExposedPorts(JBOSS_PORT);
    withNetwork(Objects.requireNonNull(network, "Network may not be null."));
    withNetworkAliases(Stream.concat(Arrays.stream(Optional.ofNullable(aliases)
                                                           .orElse(new String[0])),
                                     Stream.of("jboss", "ui"))
                             .distinct()
                             .toArray(String[]::new));
    System.out.println("Current absolute path: " + Paths.get(".").toAbsolutePath());
    withCopyFileToContainer(MountableFile.forHostPath(Paths.get("..", "ui", "target", "ui-1.0.0-SNAPSHOT.war")),
                            "/home/jboss/jboss-eap-6.4/standalone/deployments/ui.war");
    waitingFor(new HttpWaitStrategy().forPort(JBOSS_PORT)
                                     .forPath("/ui/")
                                     .forStatusCode(200));
  }

  public String getJBossUrl() {
    return String.format("http://%s:%d",
                         this.getContainerIpAddress(),
                         this.getMappedPort(JBOSS_PORT));
  }
}
