package slanzini.zookeeper.demo.configuration;

import slanzini.zookeeper.demo.service.ZkService;
import slanzini.zookeeper.demo.util.ZkDemoUtil;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import slanzini.zookeeper.demo.zkwatchers.AllNodesChangeListener;
import slanzini.zookeeper.demo.zkwatchers.ConnectStateChangeListener;
import slanzini.zookeeper.demo.zkwatchers.LiveNodeChangeListener;
import slanzini.zookeeper.demo.zkwatchers.MasterChangeListenerApproach;

/** @author "Salvatore Lanzinil" 11/02/23 */
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
    MasterChangeListenerApproach masterChangeListener = new MasterChangeListenerApproach();
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
