package bkatwal.zookeeper.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/** @author "Bikas Katwal" 27/03/19 */
@Component
@Scope("singleton")
public final class ZkDemoUtil {

  @Value("${server.port}")
  private Integer serverPort;

  public static final String ELECTION_MASTER = "/election/master";
  public static final String ELECTION_NODE = "/election";
  public static final String ELECTION_NODE_2 = "/election2";
  public static final String LIVE_NODES = "/liveNodes";
  public static final String ALL_NODES = "/allNodes";

  private static String ipPort = null;

  public String getHostPostOfServer() {
    if (ipPort != null) {
      return ipPort;
    }
    String ip;
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      throw new RuntimeException("failed to fetch Ip!", e);
    }
    ipPort = ip.concat(":").concat(String.valueOf(serverPort));
    return ipPort;
  }

  public static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }

  private ZkDemoUtil() {}
}
