package slanzini.zookeeper.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import slanzini.zookeeper.demo.model.User;
import slanzini.zookeeper.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/execute-elaboration")
    public ResponseEntity<?> executeElaboration(@RequestBody List<User> users){
        userService.executeElaboration(users);
        return ResponseEntity.ok(null);
    }
}
