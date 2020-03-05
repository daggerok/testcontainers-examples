// package daggerok.jboss;
//
// import org.testcontainers.containers.GenericContainer;
// import org.testcontainers.containers.Network;
// import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
//
// import java.nio.file.Paths;
// import java.time.Duration;
// import java.util.Arrays;
// import java.util.Objects;
// import java.util.Optional;
// import java.util.UUID;
// import java.util.concurrent.Future;
// import java.util.stream.Stream;
//
// public class JBossEapContainer_backup extends GenericContainer<JBossEapContainer_backup> {
//
//   public static final int JBOSS_PORT = 8080;
//
//   public static GenericContainer withNet(Network network, String... aliases) {
//     // super("daggerok/jboss-eap-6.4:6.4.22-alpine");
//     // using ../ui/Dockerfile file: docker build -t daggerok/test-image ../ui
//     Future<String> dockerfile = new org.testcontainers.images.builder.ImageFromDockerfile("daggerok/jboss-eap-test-container")
//         .withFileFromPath(".", Paths.get(String.format("%s/ui", Paths.get(".").toAbsolutePath().toFile()
//                                                                      .getParentFile().getParent())));
//     GenericContainer eap = new GenericContainer(dockerfile);
//     eap.withExposedPorts(JBOSS_PORT);
//     eap.withNetwork(Objects.requireNonNull(network, "Network may not be null."));
//     eap.withNetworkAliases(Stream.concat(Arrays.stream(Optional.ofNullable(aliases)
//                                                                .orElse(new String[0])),
//                                          Stream.of("jboss", "ui"))
//                                  .distinct()
//                                  .toArray(String[]::new));
//     eap.withLabel("RANDOM", UUID.randomUUID().toString());
//     // eap.withCopyFileToContainer(MountableFile.forHostPath(Paths.get("..", "ui", maven > gradle ? "target" : "build/libs", "ui-1.0.0-SNAPSHOT.war")),
//     //                         "/home/jboss/jboss-eap-6.4/standalone/deployments/ui.war");
//     return eap.waitingFor(new HttpWaitStrategy().forPort(JBOSS_PORT)
//                                                 .forPath("/ui/")
//                                                 .forStatusCode(200)
//                                                 .withStartupTimeout(Duration.ofSeconds(15)));
//   }
//
//   private JBossEapContainer_backup() {}
// }
