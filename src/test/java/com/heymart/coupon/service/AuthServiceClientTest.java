package com.heymart.coupon.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.heymart.coupon.dto.SupermarketResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

class AuthServiceClientTest {

    @InjectMocks
    AuthServiceClient authServiceClient;

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

    @Test
    void testVerifySupermarket_Successful() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer validToken");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        SupermarketResponse supermarketResponse = new SupermarketResponse();
        supermarketResponse.setSupermarketName("HeyMart");

        ResponseEntity<SupermarketResponse> responseEntity = new ResponseEntity<>(supermarketResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(SupermarketResponse.class)))
                .thenReturn(responseEntity);

        boolean result = authServiceClient.verifySupermarket("Bearer validToken", "HeyMart");
        assertTrue(result);
    }

    @Test
    void testVerifySupermarket_Failed() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer validToken");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        SupermarketResponse supermarketResponse = new SupermarketResponse();
        supermarketResponse.setSupermarketName("SomeOtherMart");

        ResponseEntity<SupermarketResponse> responseEntity = new ResponseEntity<>(supermarketResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(SupermarketResponse.class)))
                .thenReturn(responseEntity);

        boolean result = authServiceClient.verifySupermarket("validToken", "HeyMart");
        assertFalse(result);
    }
    @Test
    void testVerifySupermarket_NullTokenOrSupermarket() {
        assertFalse(authServiceClient.verifySupermarket("token", null));
        assertFalse(authServiceClient.verifySupermarket(null, "HeyMart"));
    }

    @Test
    void testVerifySupermarket_Exception() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer anyToken");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(SupermarketResponse.class)))
                .thenThrow(new RuntimeException());

        boolean result = authServiceClient.verifySupermarket("anyToken", "HeyMart");
        assertFalse(result);
    }
}
