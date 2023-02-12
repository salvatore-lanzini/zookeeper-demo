package slanzini.zookeeper.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import slanzini.zookeeper.demo.exception.ClusterSynchronizationException;
import slanzini.zookeeper.demo.model.User;
import slanzini.zookeeper.demo.service.ClusterSynchronizationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ClusterSynchronizationServiceImpl implements ClusterSynchronizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterSynchronizationServiceImpl.class);

    private final static RestTemplate REST_TEMPLATE = new RestTemplate();

    @Override
    public List<List<User>> divideListEqually(List<User> input, int memberSize) {
        int numElements = input.size();
        int numElementsPerList = numElements / memberSize;
        int numExtraElements = numElements % memberSize;
        AtomicInteger currentIndex = new AtomicInteger();
        return IntStream.range(0, memberSize)
                .mapToObj(i -> {
                    int numSubListElements = numElementsPerList + (i < numExtraElements ? 1 : 0);
                    List<User> subList = input.subList(currentIndex.get(), currentIndex.addAndGet(numSubListElements));
                    return new ArrayList<>(subList);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void dispatchUsersToMember(List<User> users, String member) throws ClusterSynchronizationException {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()//
                .scheme("http")//
                .host(member)//
                .path("/zk-demo/user/execute-elaboration")//
                .build();
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<List<User>> httpEntity = new HttpEntity<>(users, httpHeaders);
            LOGGER.debug("Cluster Sync http Request: post Users execute-elaboration");
            REST_TEMPLATE.postForEntity(uriComponents.toString(), httpEntity, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClusterSynchronizationException(e.getMessage(), e);
        }
    }
}
