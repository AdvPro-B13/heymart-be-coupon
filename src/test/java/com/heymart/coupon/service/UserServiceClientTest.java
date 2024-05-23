package com.heymart.coupon.service;

import com.heymart.coupon.dto.SupermarketResponse;
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

class UserServiceClientTest {

    @InjectMocks
    UserServiceClient userServiceClient;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        boolean result = userServiceClient.verifySupermarket("Bearer validToken", "HeyMart");
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

        boolean result = userServiceClient.verifySupermarket("validToken", "HeyMart");
        assertFalse(result);
    }
    @Test
    void testVerifySupermarket_NullTokenOrSupermarket() {
        assertFalse(userServiceClient.verifySupermarket("token", null));
        assertFalse(userServiceClient.verifySupermarket(null, "HeyMart"));
    }

    @Test
    void testVerifySupermarket_Exception() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer anyToken");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(SupermarketResponse.class)))
                .thenThrow(new RuntimeException());

        boolean result = userServiceClient.verifySupermarket("anyToken", "HeyMart");
        assertFalse(result);
    }
}
