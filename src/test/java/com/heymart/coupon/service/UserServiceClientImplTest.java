package com.heymart.coupon.service;

import com.heymart.coupon.dto.UserResponse;
import com.heymart.coupon.exception.CouponAlreadyUsedException;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.UsedCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.repository.UsedCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
class UserServiceClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UsedCouponRepository usedCouponRepository;

    @InjectMocks
    private UserServiceClientImpl userServiceClientImpl;

    private TransactionCoupon coupon;
    private UUID couponId;
    @BeforeEach
    void setUp() {
        coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketId("SUPERMARKET123")
                .setMinTransaction(50)
                .build();
        couponId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
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

        when(usedCouponRepository.existsByUserIdAndCouponId(1L, couponId)).thenReturn(false);
        when(usedCouponRepository.save(any(UsedCoupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Long result = userServiceClientImpl.getUserId(token);

        assertNotNull(result);
        assertEquals(1L, result);

        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(UserResponse.class)
        );
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

        boolean result = userServiceClientImpl.verifySupermarket("Bearer validToken", "HeyMart");
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

        boolean result = userServiceClientImpl.verifySupermarket("validToken", "HeyMart");
        assertFalse(result);
    }
    @Test
    void testVerifySupermarket_NullTokenOrSupermarket() {
        assertFalse(userServiceClientImpl.verifySupermarket("token", null));
        assertFalse(userServiceClientImpl.verifySupermarket(null, "HeyMart"));
    }

    @Test
    void testVerifySupermarket_Exception() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer anyToken");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(UserResponse.class)))
                .thenThrow(new RuntimeException());

        boolean result = userServiceClientImpl.verifySupermarket("anyToken", "HeyMart");
        assertFalse(result);
    }

    @Test
    void testGetUserId_nullResponse() {
        String token = "validToken";
        ResponseEntity<UserResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(UserResponse.class)
        )).thenReturn(responseEntity);

        assertThrows(AssertionError.class, () -> {
            userServiceClientImpl.getUserId(token);
        });

        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(UserResponse.class)
        );
        verify(usedCouponRepository, never()).existsByUserIdAndCouponId(anyLong(), any(UUID.class));
        verify(usedCouponRepository, never()).save(any(UsedCoupon.class));
    }
}
