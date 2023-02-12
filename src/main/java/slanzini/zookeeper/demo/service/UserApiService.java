package slanzini.zookeeper.demo.service;

import slanzini.zookeeper.demo.exception.UserApiException;
import slanzini.zookeeper.demo.model.User;

import java.util.List;

public interface UserApiService {
    List<User> getUsers() throws UserApiException;
}
