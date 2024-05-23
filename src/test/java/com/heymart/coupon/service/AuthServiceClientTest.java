package com.heymart.coupon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AuthServiceClientTest {

    @InjectMocks
    AuthServiceClient authServiceClient;
    @InjectMocks
    UserServiceClient userServiceClient;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyUserAuthorization_Successful() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer validToken");
        HttpEntity<String> entity = new HttpEntity<>("{\"action\":\"someAction\"}", headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), eq(entity), eq(String.class))).thenReturn(responseEntity);

        boolean result = authServiceClient.verifyUserAuthorization("someAction", "Bearer validToken");
        assertTrue(result);
    }

    @Test
    void testVerifyUserAuthorization_Failed() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer invalidToken");
        HttpEntity<String> entity = new HttpEntity<>("{\"action\":\"someAction\"}", headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        when(restTemplate.postForEntity(anyString(), eq(entity), eq(String.class))).thenReturn(responseEntity);

        boolean result = authServiceClient.verifyUserAuthorization("someAction", "Bearer invalidToken");
        assertFalse(result);
    }
    @Test
    void testVerifyUserAuthorization_TokenNull() {
        boolean result = authServiceClient.verifyUserAuthorization("someAction", null);
        assertFalse(result);
    }

    @Test
    void testVerifyUserAuthorization_Exception() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenThrow(new RuntimeException());

        boolean result = authServiceClient.verifyUserAuthorization("someAction", "anyToken");
        assertFalse(result);
    }
}
