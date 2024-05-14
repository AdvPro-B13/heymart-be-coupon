package com.heymart.coupon.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.heymart.coupon.dto.SupermarketResponse;
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
        MockitoAnnotations.openMocks(this);
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
    @Test
    public void testVerifySupermarket_Successful() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer validToken");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        SupermarketResponse supermarketResponse = new SupermarketResponse();
        supermarketResponse.setSupermarketName("HeyMart");

        ResponseEntity<SupermarketResponse> responseEntity = new ResponseEntity<>(supermarketResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(SupermarketResponse.class)))
                .thenReturn(responseEntity);

        boolean result = authServiceClient.verifySupermarket("validToken", "HeyMart");
        assertTrue(result);
    }

    @Test
    public void testVerifySupermarket_Failed() {
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
    public void testVerifySupermarket_Exception() {
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
