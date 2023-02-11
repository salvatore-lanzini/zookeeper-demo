package bkatwal.zookeeper.demo.zkwatchers;

import bkatwal.zookeeper.demo.service.ZkService;
import bkatwal.zookeeper.demo.util.ClusterInfo;
import org.I0Itec.zkclient.IZkChildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static bkatwal.zookeeper.demo.util.ZkDemoUtil.ELECTION_NODE;

/** @author "Bikas Katwal" 27/03/19 */
public class MasterChangeListenerApproach implements IZkChildListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(MasterChangeListenerApproach.class);

  private ZkService zkService;

  public void setZkService(ZkService zkService) {
    this.zkService = zkService;
  }

  /**
   * listens for deletion of sequential znode under /election znode and updates the
   * clusterinfo
   *
   * @param parentPath
   * @param currentChildren
   */
  @Override
  public void handleChildChange(String parentPath, List<String> currentChildren) {
    if (currentChildren.isEmpty()) {
      throw new RuntimeException("No node exists to select master!!");
    } else {
      //get least sequenced znode
      Collections.sort(currentChildren);
      String masterZNode = currentChildren.get(0);

      // once znode is fetched, fetch the znode data to get the hostname of new leader
      String masterNode = zkService.getZNodeData(ELECTION_NODE.concat("/").concat(masterZNode));
      LOGGER.info("new master is: {}", masterNode);

      //update the cluster info with new leader
      ClusterInfo.getClusterInfo().setMaster(masterNode);
    }
  }
}
