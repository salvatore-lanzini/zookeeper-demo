package bkatwal.zookeeper.demo.zkwatchers;

import bkatwal.zookeeper.demo.util.ClusterInfo;
import org.I0Itec.zkclient.IZkChildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/** @author "Bikas Katwal" 27/03/19 */
public class AllNodesChangeListener implements IZkChildListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(AllNodesChangeListener.class);

  /**
   * - This method will be invoked for any change in /all_nodes children
   * - During registering this
   * listener make sure you register with path /all_nodes
   * - after receiving notification it will update the local clusterInfo object
   *
   * @param parentPath this will be passed as /all_nodes
   * @param currentChildren current list of children, children's string value is znode name which is
   *     set as server hostname
   */
  @Override
  public void handleChildChange(String parentPath, List<String> currentChildren) {
    LOGGER.info("current all node size: {}", currentChildren.size());
    ClusterInfo.getClusterInfo().getAllNodes().clear();
    ClusterInfo.getClusterInfo().getAllNodes().addAll(currentChildren);
  }
}
