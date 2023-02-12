package slanzini.zookeeper.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import slanzini.zookeeper.demo.exception.UserApiException;
import slanzini.zookeeper.demo.model.User;
import slanzini.zookeeper.demo.service.UserApiService;

import java.util.Arrays;
import java.util.List;

@Service
public class UserApiServiceImpl implements UserApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserApiServiceImpl.class);

    private final static RestTemplate REST_TEMPLATE = new RestTemplate();

    @Value("${user-api.base-path}")
    private String userApiBasePath;

    @Override
    public List<User> getUsers() throws UserApiException {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()//
                .scheme("http")//
                .host(userApiBasePath)//
                .path("/user")//
                .build();
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
            LOGGER.debug("UserApi http Request: get All Users");
            return Arrays.asList(REST_TEMPLATE.exchange(uriComponents.toUriString(), HttpMethod.GET, httpEntity,User[].class).getBody());
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserApiException(e.getMessage(), e);
        }
    }
}
