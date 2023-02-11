package bkatwal.zookeeper.demo.zkwatchers;

import bkatwal.zookeeper.demo.model.Person;
import bkatwal.zookeeper.demo.service.ZkService;
import bkatwal.zookeeper.demo.util.ClusterInfo;
import bkatwal.zookeeper.demo.util.DataStorage;
import bkatwal.zookeeper.demo.util.ZkDemoUtil;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/** @author "Bikas Katwal" 02/04/19 */
public class ConnectStateChangeListener implements IZkStateListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectStateChangeListener.class);

  private ZkService zkService;
  private RestTemplate restTemplate = new RestTemplate();

  private ZkDemoUtil zkDemoUtil;

  public ConnectStateChangeListener(ZkDemoUtil zkDemoUtil) {
    this.zkDemoUtil = zkDemoUtil;
  }

  public void setZkService(ZkService zkService) {
    this.zkService = zkService;
  }

  @Override
  public void handleStateChanged(KeeperState state) throws Exception {
    LOGGER.info(state.name()); // 1. disconnected, 2. expired, 3. SyncConnected
  }

  @Override
  public void handleNewSession() throws Exception {
    LOGGER.info("connected to zookeeper");

    // sync data from master
    syncDataFromMaster();

    // add new znode to /live_nodes to make it live
    zkService.addToLiveNodes(zkDemoUtil.getHostPostOfServer(), "cluster node");
    ClusterInfo.getClusterInfo().getLiveNodes().clear();
    ClusterInfo.getClusterInfo().getLiveNodes().addAll(zkService.getLiveNodes());

    // re try creating znode under /election
    // this is needed, if there is only one server in cluster
    zkService.createNodeInElectionZnode(zkDemoUtil.getHostPostOfServer());
    ClusterInfo.getClusterInfo().setMaster(zkService.getLeaderNodeData());
  }

  @Override
  public void handleSessionEstablishmentError(Throwable error) throws Exception {
    LOGGER.info("could not establish session");
  }

  private void syncDataFromMaster() {
    // BKTODO need try catch here for session not found
    if (zkDemoUtil.getHostPostOfServer().equals(ClusterInfo.getClusterInfo().getMaster())) {
      return;
    }
    String requestUrl;
    requestUrl = "http://".concat(ClusterInfo.getClusterInfo().getMaster().concat("/zk-demo/persons"));
    List<Person> persons = restTemplate.getForObject(requestUrl, List.class);
    DataStorage.getPersonListFromStorage().clear();
    DataStorage.getPersonListFromStorage().addAll(persons);
  }
}
