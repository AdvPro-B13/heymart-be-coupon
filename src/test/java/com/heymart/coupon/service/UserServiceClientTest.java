package com.heymart.coupon.service;

import com.heymart.coupon.dto.UserResponse;
import com.heymart.coupon.exception.CouponAlreadyUsedException;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.UsedCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.repository.UsedCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
class UserServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UsedCouponRepository usedCouponRepository;

    @InjectMocks
    private UserServiceClient userServiceClient;

    private TransactionCoupon coupon;

    @BeforeEach
    void setUp() {
        coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketId("SUPERMARKET123")
                .setMinTransaction(50)
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUseCoupon_success() {
        String token = "validToken";
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setSupermarketId("SUPERMARKET123");

        ResponseEntity<UserResponse> responseEntity = new ResponseEntity<>(userResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(UserResponse.class)
        )).thenReturn(responseEntity);

        when(usedCouponRepository.existsByUserIdAndCoupon(1L, coupon)).thenReturn(false);
        when(usedCouponRepository.save(any(UsedCoupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsedCoupon result = userServiceClient.useCoupon(token, coupon);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(coupon, result.getCoupon());
        assertEquals("SUPERMARKET123", result.getSupermarketId());

        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(UserResponse.class)
        );
        verify(usedCouponRepository, times(1)).existsByUserIdAndCoupon(1L, coupon);
        verify(usedCouponRepository, times(1)).save(any(UsedCoupon.class));
    }

    @Test
    void testUseCoupon_couponAlreadyUsed() {
        // Arrange
        String token = "validToken";
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setSupermarketId("SUPERMARKET123");

        ResponseEntity<UserResponse> responseEntity = new ResponseEntity<>(userResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(UserResponse.class)
        )).thenReturn(responseEntity);

        when(usedCouponRepository.existsByUserIdAndCoupon(1L, coupon)).thenReturn(true);

        // Act & Assert
        assertThrows(CouponAlreadyUsedException.class, () -> {
            userServiceClient.useCoupon(token, coupon);
        });

        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(UserResponse.class)
        );
        verify(usedCouponRepository, times(1)).existsByUserIdAndCoupon(1L, coupon);
        verify(usedCouponRepository, never()).save(any(UsedCoupon.class));
    }
    @Test
    void testVerifySupermarket_Successful() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer validToken");

        UserResponse supermarketResponse = new UserResponse();
        supermarketResponse.setSupermarketId("HeyMart");

        ResponseEntity<UserResponse> responseEntity = new ResponseEntity<>(supermarketResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(UserResponse.class)))
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

        UserResponse userResponse = new UserResponse();
        userResponse.setSupermarketId("SomeOtherMart");

        ResponseEntity<UserResponse> responseEntity = new ResponseEntity<>(userResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(UserResponse.class)))
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

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(UserResponse.class)))
                .thenThrow(new RuntimeException());

        boolean result = userServiceClient.verifySupermarket("anyToken", "HeyMart");
        assertFalse(result);
    }
    @Test
    void testUseCoupon_nullResponse() {
        String token = "validToken";
        ResponseEntity<UserResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(UserResponse.class)
        )).thenReturn(responseEntity);

        assertThrows(AssertionError.class, () -> {
            userServiceClient.useCoupon(token, coupon);
        });

        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(UserResponse.class)
        );
        verify(usedCouponRepository, never()).existsByUserIdAndCoupon(anyLong(), any(TransactionCoupon.class));
        verify(usedCouponRepository, never()).save(any(UsedCoupon.class));
    }

}
