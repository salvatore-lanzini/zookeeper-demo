package slanzini.zookeeper.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import slanzini.zookeeper.demo.exception.ClusterSynchronizationException;
import slanzini.zookeeper.demo.exception.UserApiException;
import slanzini.zookeeper.demo.model.User;
import slanzini.zookeeper.demo.service.*;
import slanzini.zookeeper.demo.util.ZkDemoUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ZkDemoUtil zkDemoUtil;

    @Autowired
    private UserApiService userApiService;

    @Autowired
    private ZkService zkService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClusterSynchronizationService clusterSynchronizationService;

    @Override
    public void scheduleUsersElaboration() {
        if(zkDemoUtil.amILeader()) {
            // leader elaboration - get users from api, divide users by member in cluster, dispatch users to members
            LOGGER.info("I'm the leader");
            try {
                var users = userApiService.getUsers();
                var liveNodes = zkService.getLiveNodes();
                elaborateUsersElaboration(users,liveNodes);
            } catch (UserApiException e) {
                LOGGER.error("Error in get all users from Api!: message: {}",e.getMessage());
            }
        }else
            // follower elaboration - do nothing!
            LOGGER.info("I'm not the leader");
    }

    private void elaborateUsersElaboration(List<User> users, List<String> liveNodes){
        var myNode = zkDemoUtil.getHostPostOfServer();
        var liveNodesSize = liveNodes.size();
        var partitionList = clusterSynchronizationService.divideListEqually(users,liveNodesSize);
        for(int i=0; i<liveNodesSize; i++){
            var usersPartitionated = partitionList.get(i);
            var liveNode = liveNodes.get(i);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Runnable runnable = () -> {
                try {
                    if( liveNode.equals(myNode) )
                        // I'm the node (leader). Invoke directly userService
                        userService.executeElaboration(usersPartitionated);
                    else
                        // Dispatch users to member
                        clusterSynchronizationService.dispatchUsersToMember(usersPartitionated,liveNode);
                } catch (ClusterSynchronizationException e) {
                    LOGGER.error("Impossible to dispatch users for member: {}",liveNode);
                }
            };
            executorService.execute(runnable);
        }
    }
}
