package slanzini.zookeeper.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import slanzini.zookeeper.demo.model.User;
import slanzini.zookeeper.demo.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void executeElaboration(List<User> users) {
        // execute some elaboration for the input users
        // in order to simplify poc only log list in input
        users.stream().forEach( user -> LOGGER.info("user: {}",user));
    }
}
