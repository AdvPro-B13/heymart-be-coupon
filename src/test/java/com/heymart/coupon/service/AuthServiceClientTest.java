package com.heymart.coupon.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class AuthServiceClientTest {

    @InjectMocks
    AuthServiceClient authServiceClient;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testVerifyUserAuthorization_Successful() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer validToken");
        HttpEntity<String> entity = new HttpEntity<>("{\"action\":\"someAction\"}", headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), eq(entity), eq(String.class))).thenReturn(responseEntity);

        boolean result = authServiceClient.verifyUserAuthorization("someAction", "validToken");
        assertTrue(result);
    }

    @Test
    public void testVerifyUserAuthorization_Failed() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer invalidToken");
        HttpEntity<String> entity = new HttpEntity<>("{\"action\":\"someAction\"}", headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        when(restTemplate.postForEntity(anyString(), eq(entity), eq(String.class))).thenReturn(responseEntity);

        boolean result = authServiceClient.verifyUserAuthorization("someAction", "invalidToken");
        assertFalse(result);
    }

    @Test
    public void testVerifyUserAuthorization_Exception() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenThrow(new RuntimeException());

        boolean result = authServiceClient.verifyUserAuthorization("someAction", "anyToken");
        assertFalse(result);
    }

    @Test
    public void testGetTokenFromHeader_Valid() {
        String token = authServiceClient.getTokenFromHeader("Bearer validToken");
        assertEquals("validToken", token);
    }

    @Test
    public void testGetTokenFromHeader_Invalid() {
        String token = authServiceClient.getTokenFromHeader("InvalidHeader");
        assertNull(token);
    }
    @Test
    public void testGetTokenFromHeader_Null() {
        String token = authServiceClient.getTokenFromHeader(null);
        assertNull(token);
    }
}
