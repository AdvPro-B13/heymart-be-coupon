package com.heymart.coupon.service;

import com.heymart.coupon.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class UserServiceClientImpl implements UserServiceClient {
    RestTemplate restTemplate;
    @Value("${user.api}")
    String userServiceUrl;
    @Autowired
    public UserServiceClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifySupermarket(String token, String supermarketId) {
        if (token == null || supermarketId == null) {
            return false;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            String url = userServiceUrl + "/get";
            ResponseEntity<UserResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserResponse.class
            );
            return supermarketId.equals(Objects.requireNonNull(response.getBody()).getSupermarketId());
        }
        catch(Exception e){
            return false;
        }
    }
    public Long getUserId(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = userServiceUrl + "/get";
        UserResponse response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UserResponse.class
        ).getBody();
        assert response != null;
        return response.getId();
    }
}
