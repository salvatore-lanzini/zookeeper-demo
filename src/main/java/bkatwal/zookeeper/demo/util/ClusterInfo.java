package bkatwal.zookeeper.demo.util;

import java.util.ArrayList;
import java.util.List;

/** @author "Bikas Katwal" 26/03/19 */

public final class ClusterInfo {

  private static ClusterInfo clusterInfo = new ClusterInfo();

  public static ClusterInfo getClusterInfo() {
    return clusterInfo;
  }

  /*
  these will be ephemeral znodes
   */
  private List<String> liveNodes = new ArrayList<>();

  /*
  these will be persistent znodes
   */
  private List<String> allNodes = new ArrayList<>();

  private String master;

  public static void setClusterInfo(ClusterInfo clusterInfo) {
    ClusterInfo.clusterInfo = clusterInfo;
  }

  public List<String> getLiveNodes() {
    return liveNodes;
  }

  public void setLiveNodes(List<String> liveNodes) {
    this.liveNodes = liveNodes;
  }

  public List<String> getAllNodes() {
    return allNodes;
  }

  public void setAllNodes(List<String> allNodes) {
    this.allNodes = allNodes;
  }

  public String getMaster() {
    return master;
  }

  public void setMaster(String master) {
    this.master = master;
  }
}
