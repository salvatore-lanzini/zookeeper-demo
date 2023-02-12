package slanzini.zookeeper.demo.service;

import slanzini.zookeeper.demo.exception.ClusterSynchronizationException;
import slanzini.zookeeper.demo.model.User;

import java.util.List;

public interface ClusterSynchronizationService {
    List<List<User>> divideListEqually(List<User> input, int memberSize);
    void dispatchUsersToMember(List<User> users, String member) throws ClusterSynchronizationException;
}
