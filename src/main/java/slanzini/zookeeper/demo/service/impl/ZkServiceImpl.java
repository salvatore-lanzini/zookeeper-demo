package slanzini.zookeeper.demo.service.impl;

import slanzini.zookeeper.demo.service.ZkService;
import slanzini.zookeeper.demo.util.StringSerializer;
import slanzini.zookeeper.demo.util.ZkDemoUtil;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

import static slanzini.zookeeper.demo.util.ZkDemoUtil.*;

/** @author "Salvatore Lanzinil" 11/02/23 */
@Service
public class ZkServiceImpl implements ZkService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ZkServiceImpl.class);

  @Value("${zk.url}")
  private String zkHostPort;

  @Value("${zk.session-timeout}")
  private Integer zkSessionTimeout;

  @Value("${zk.connection-timeout}")
  private Integer zkConnectionTimeout;

  @Autowired
  private ZkDemoUtil zkDemoUtil;

  private ZkClient zkClient;

  @PostConstruct
  public void onStartup(){
    zkClient = new ZkClient(zkHostPort, zkSessionTimeout, zkConnectionTimeout, new StringSerializer());
  }

  public void closeConnection() {
    zkClient.close();
  }

  @Override
  public void addToLiveNodes(String nodeName, String data) {
    if (!zkClient.exists(LIVE_NODES)) {
      zkClient.create(LIVE_NODES, "all live nodes are displayed here", CreateMode.PERSISTENT);
    }
    String childNode = LIVE_NODES.concat("/").concat(nodeName);
    if (zkClient.exists(childNode)) {
      return;
    }
    zkClient.create(childNode, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
  }

  @Override
  public List<String> getLiveNodes() {
    if (!zkClient.exists(LIVE_NODES)) {
      throw new RuntimeException("No node /liveNodes exists");
    }
    return zkClient.getChildren(LIVE_NODES);
  }

  @Override
  public void addToAllNodes(String nodeName, String data) {
    if (!zkClient.exists(ALL_NODES)) {
      zkClient.create(ALL_NODES, "all live nodes are displayed here", CreateMode.PERSISTENT);
    }
    String childNode = ALL_NODES.concat("/").concat(nodeName);
    if (zkClient.exists(childNode)) {
      return;
    }
    zkClient.create(childNode, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
  }

  @Override
  public List<String> getAllNodes() {
    if (!zkClient.exists(ALL_NODES)) {
      throw new RuntimeException("No node /allNodes exists");
    }
    return zkClient.getChildren(ALL_NODES);
  }

  @Override
  public void deleteNodeFromCluster(String node) {
    zkClient.delete(ALL_NODES.concat("/").concat(node));
    zkClient.delete(LIVE_NODES.concat("/").concat(node));
  }

  @Override
  public void createAllParentNodes() {
    if (!zkClient.exists(ALL_NODES)) {
      zkClient.create(ALL_NODES, "all live nodes are displayed here", CreateMode.PERSISTENT);
    }
    if (!zkClient.exists(LIVE_NODES)) {
      zkClient.create(LIVE_NODES, "all live nodes are displayed here", CreateMode.PERSISTENT);
    }
    if (!zkClient.exists(ELECTION_NODE)) {
      zkClient.create(ELECTION_NODE, "election node", CreateMode.PERSISTENT);
    }
  }

  @Override
  public String getLeaderNodeData() {
    if (!zkClient.exists(ELECTION_NODE)) {
      throw new RuntimeException("No node /election2 exists");
    }
    List<String> nodesInElection = zkClient.getChildren(ELECTION_NODE);
    Collections.sort(nodesInElection);
    String masterZNode = nodesInElection.get(0);
    return getZNodeData(ELECTION_NODE.concat("/").concat(masterZNode));
  }

  @Override
  public String getZNodeData(String path) {
    return zkClient.readData(path, null);
  }

  @Override
  public void createNodeInElectionZnode(String data) {
    if (!zkClient.exists(ELECTION_NODE)) {
      zkClient.create(ELECTION_NODE, "election node", CreateMode.PERSISTENT);
    }
    zkClient.create(ELECTION_NODE.concat("/node"), data, CreateMode.EPHEMERAL_SEQUENTIAL);
  }

  @Override
  public void registerChildrenChangeWatcher(String path, IZkChildListener iZkChildListener) {
    zkClient.subscribeChildChanges(path, iZkChildListener);
  }

  @Override
  public void registerZkSessionStateListener(IZkStateListener iZkStateListener) {
    zkClient.subscribeStateChanges(iZkStateListener);
  }
}
