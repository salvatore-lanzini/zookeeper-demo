package bkatwal.zookeeper.demo.service;

import java.util.List;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;

/** @author "Bikas Katwal" 26/03/19 */
public interface ZkService {

  void addToLiveNodes(String nodeName, String data);

  List<String> getLiveNodes();

  void addToAllNodes(String nodeName, String data);

  List<String> getAllNodes();

  void deleteNodeFromCluster(String node);

  void createAllParentNodes();

  String getLeaderNodeData();

  String getZNodeData(String path);

  void createNodeInElectionZnode(String data);

  void registerChildrenChangeWatcher(String path, IZkChildListener iZkChildListener);

  void registerZkSessionStateListener(IZkStateListener iZkStateListener);
}
