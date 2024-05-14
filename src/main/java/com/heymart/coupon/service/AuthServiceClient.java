package com.heymart.coupon.service;
import com.heymart.coupon.dto.SupermarketResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class AuthServiceClient {
    RestTemplate restTemplate = new RestTemplate();
    @Value("${auth.api}")
    String authServiceUrl;
    public boolean verifyUserAuthorization(String action, String token) {
        if (token == null) {
            return  false;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        String jsonBody = "{\"action\":\"" + action + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            String url = authServiceUrl + "/verify";
            ResponseEntity<?> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        }
        catch(Exception e){
            return false;
        }
    }
    public String getTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return  authorizationHeader.substring(7);
    }
    public boolean verifySupermarket(String token, String supermarketName) {
        if (token == null || supermarketName == null) {
            return  false;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            String url = authServiceUrl + "/user";;
            ResponseEntity<SupermarketResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    SupermarketResponse.class
            );
            return supermarketName.equals(Objects.requireNonNull(response.getBody()).getSupermarketName());
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
}
