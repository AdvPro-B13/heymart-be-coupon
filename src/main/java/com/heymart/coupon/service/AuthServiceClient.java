package com.heymart.coupon.service;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceClient {
    RestTemplate restTemplate = new RestTemplate();

    public boolean verifyUserAuthorization(String action, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        String jsonBody = "{\"action\":\"" + action + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            String authServiceUrl = "http://localhost:8081/api/auth/verify";
            ResponseEntity<?> response = restTemplate.postForEntity(authServiceUrl, entity, String.class);
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
}
