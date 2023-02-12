package slanzini.zookeeper.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import slanzini.zookeeper.demo.service.ScheduleService;
import slanzini.zookeeper.demo.util.ZkDemoUtil;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ZkDemoUtil zkDemoUtil;

    @Override
    public void scheduleUsersElaboration() {
        /** TODO in this place implement logic
         * if am i leader:
         *  1) call external system in order to get all users to elaborate
         *  2) split users by size of clusters
         *  3) invoke, via rest api, every member in the cluster to execute elaboration on subset users (also leader himself)
         * else
         *  1) do nothing (it will leader perform action on every member in the cluster)
        **/
        if(zkDemoUtil.amILeader())
            LOGGER.info("I'm the leader");
        else
            LOGGER.info("I'm not the leader");
    }
}
