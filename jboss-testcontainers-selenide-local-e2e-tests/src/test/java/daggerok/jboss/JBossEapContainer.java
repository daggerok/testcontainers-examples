package daggerok.jboss;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class JBossEapContainer extends GenericContainer<JBossEapContainer> {

  private static final String classPath = System.getProperty("java.class.path");
  private static final int gradle = classPath.split(".gradle").length;
  private static final int maven = classPath.split(".m2").length;

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

    Path warFilePath = maven > gradle ? Paths.get("..", "ui", "target", "ui-1.0.0-SNAPSHOT.war")
        : Paths.get("..", "ui", "build", "libs", "ui-1.0.0-SNAPSHOT.war");

    if (!warFilePath.toFile().exists()) logger().warn("Attention! File " + warFilePath + " doesn't exists!");
    logger().debug("Current WAR file path: " + warFilePath.toFile());
    withLabel("RANDOM", UUID.randomUUID().toString());

    withCopyFileToContainer(MountableFile.forHostPath(warFilePath),
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
