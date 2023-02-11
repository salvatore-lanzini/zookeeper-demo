package bkatwal.zookeeper.demo.configuration;

import bkatwal.zookeeper.demo.service.ZkService;
import bkatwal.zookeeper.demo.util.ZkDemoUtil;
import bkatwal.zookeeper.demo.zkwatchers.*;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/** @author "Bikas Katwal" 26/03/19 */
@Configuration
public class BeanConfig {

  @Autowired
  private ZkDemoUtil zkDemoUtil;
  @Autowired
  private ZkService zkService;

  @Bean(name = "allNodesChangeListener")
  @Scope("singleton")
  public IZkChildListener allNodesChangeListener() {
    return new AllNodesChangeListener();
  }

  @Bean(name = "liveNodeChangeListener")
  @Scope("singleton")
  public IZkChildListener liveNodeChangeListener() {
    return new LiveNodeChangeListener();
  }

  @Bean(name = "masterChangeListener")
  @Scope("singleton")
  public IZkChildListener masterChangeListener2() {
    MasterChangeListenerApproach2 masterChangeListener = new MasterChangeListenerApproach2();
    masterChangeListener.setZkService(zkService);
    return masterChangeListener;
  }

  @Bean(name = "connectStateChangeListener")
  @Scope("singleton")
  public IZkStateListener connectStateChangeListener() {
    ConnectStateChangeListener connectStateChangeListener = new ConnectStateChangeListener(zkDemoUtil);
    connectStateChangeListener.setZkService(zkService);
    return connectStateChangeListener;
  }
}
