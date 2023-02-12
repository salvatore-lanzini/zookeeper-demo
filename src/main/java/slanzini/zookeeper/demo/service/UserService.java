package slanzini.zookeeper.demo.service;

import slanzini.zookeeper.demo.model.User;

import java.util.List;

public interface UserService {
    void executeElaboration(List<User> users);
}
