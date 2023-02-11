package slanzini.zookeeper.demo.zkwatchers;

import slanzini.zookeeper.demo.util.ClusterInfo;
import org.I0Itec.zkclient.IZkChildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/** @author "Salvatore Lanzinil" 11/02/23 */
public class LiveNodeChangeListener implements IZkChildListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(LiveNodeChangeListener.class);

  /**
   * - This method will be invoked for any change in /live_nodes children
   * - During registering this listener make sure you register with path /live_nodes
   * - after receiving notification it will update the local clusterInfo object
   *
   * @param parentPath this will be passed as /live_nodes
   * @param currentChildren new list of children that are present in /live_nodes, children's string alue is znode name which is set as server hostname
   */
  @Override
  public void handleChildChange(String parentPath, List<String> currentChildren) {
    LOGGER.info("current live size: {}", currentChildren.size());
    ClusterInfo.getClusterInfo().getLiveNodes().clear();
    ClusterInfo.getClusterInfo().getLiveNodes().addAll(currentChildren);
  }
}
