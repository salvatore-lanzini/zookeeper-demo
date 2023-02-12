package slanzini.zookeeper.demo.controller;

import slanzini.zookeeper.demo.model.Person;
import slanzini.zookeeper.demo.util.ClusterInfo;
import slanzini.zookeeper.demo.util.DataStorage;
import slanzini.zookeeper.demo.util.ZkDemoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/** @author "Salvatore Lanzinil" 11/02/23 */
@RestController
public class ZookeeperDemoController {

  @Autowired
  private ZkDemoUtil zkDemoUtil;

  private RestTemplate restTemplate = new RestTemplate();

  @PutMapping("/person/{id}/{name}")
  public ResponseEntity<String> savePerson(
          HttpServletRequest request,
          @PathVariable("id") Integer id,
          @PathVariable("name") String name) {

    String requestFrom = request.getHeader("request_from");
    String leader = ClusterInfo.getClusterInfo().getMaster();
    if (!zkDemoUtil.isEmpty(requestFrom) && requestFrom.equalsIgnoreCase(leader)) {
      Person person = new Person(id, name);
      DataStorage.setPerson(person);
      return ResponseEntity.ok("SUCCESS");
    }
    // If I am leader I will broadcast data to all live node, else forward request to leader
    if (zkDemoUtil.amILeader()) {
      List<String> liveNodes = ClusterInfo.getClusterInfo().getLiveNodes();

      int successCount = 0;
      for (String node : liveNodes) {

        if (zkDemoUtil.getHostPostOfServer().equals(node)) {
          Person person = new Person(id, name);
          DataStorage.setPerson(person);
          successCount++;
        } else {
          String requestUrl =
                  "http://"
                          .concat(node)
                          .concat("zk-demo")
                          .concat("/")
                          .concat("person")
                          .concat("/")
                          .concat(String.valueOf(id))
                          .concat("/")
                          .concat(name);
          HttpHeaders headers = new HttpHeaders();
          headers.add("request_from", leader);
          headers.setContentType(MediaType.APPLICATION_JSON);

          HttpEntity<String> entity = new HttpEntity<>(headers);
          restTemplate.exchange(requestUrl, HttpMethod.PUT, entity, String.class).getBody();
          successCount++;
        }
      }

      return ResponseEntity.ok()
              .body("Successfully update ".concat(String.valueOf(successCount)).concat(" nodes"));
    } else {
      String requestUrl =
              "http://"
                      .concat(leader)
                      .concat("zk-demo")
                      .concat("/")
                      .concat("person")
                      .concat("/")
                      .concat(String.valueOf(id))
                      .concat("/")
                      .concat(name);
      HttpHeaders headers = new HttpHeaders();

      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<String> entity = new HttpEntity<>(headers);
      return restTemplate.exchange(requestUrl, HttpMethod.PUT, entity, String.class);
    }
  }

  @GetMapping("/persons")
  public ResponseEntity<List<Person>> getPerson() {

    return ResponseEntity.ok(DataStorage.getPersonListFromStorage());
  }

  @GetMapping("/clusterInfo")
  public ResponseEntity<ClusterInfo> getClusterinfo() {

    return ResponseEntity.ok(ClusterInfo.getClusterInfo());
  }
}
