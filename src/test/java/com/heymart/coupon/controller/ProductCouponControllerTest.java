package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.exception.CouponNotFoundException;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.UserServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import com.heymart.coupon.service.coupon.ProductCouponOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductCouponControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthServiceClient authServiceClient;
    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private CouponService<ProductCoupon> couponService;

    @Mock
    private ProductCouponOperation productCouponOperation;

    @InjectMocks
    private ProductCouponController controller;

    private ProductCoupon coupon;
    private ProductCoupon coupon2;
    private final String authorizationHeader = "Bearer valid-token";
    @BeforeEach
    void setup() {
        coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketId("Supermarket")
                .setIdProduct("123")
                .build();
        coupon2 = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketId("Supermarket2")
                .setIdProduct("123")
                .build();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testCreateCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        request.setSupermarketId("Supermarket");
        request.setIdProduct("Product");

        when(authServiceClient.verifyUserAuthorization(any(), any())).thenReturn(false);

        ResponseEntity<Object> response = controller.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testCreateCoupon_WrongSupermarket() {
        CouponRequest request = new CouponRequest();
        request.setSupermarketId("Supermarket");
        request.setIdProduct("Product");

        when(authServiceClient.verifyUserAuthorization(any(), any())).thenReturn(true);
        when(userServiceClient.verifySupermarket(any(), any())).thenReturn(false);

        ResponseEntity<Object> response = controller.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }


    @Test
    void testCreateCouponSuccess() {
        CouponRequest request = new CouponRequest();
        request.setSupermarketId("Supermarket");
        request.setIdProduct("Product");

        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(userServiceClient.verifySupermarket("authHeader", request.getSupermarketId())).thenReturn(true);
        when(productCouponOperation.findByIdProduct(request.getIdProduct())).thenThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));
        when(couponService.createCoupon(any())).thenReturn(coupon);

        ResponseEntity<Object> response = controller.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    void testCreateCouponAlreadyExists() {
        CouponRequest request = new CouponRequest();
        request.setSupermarketId("Supermarket");
        request.setIdProduct("Product");

        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(userServiceClient.verifySupermarket("authHeader", request.getSupermarketId())).thenReturn(true);
        when(productCouponOperation.findByIdProduct(request.getIdProduct())).thenReturn(coupon);

        ResponseEntity<Object> response = controller.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(ErrorStatus.COUPON_ALREADY_EXIST.getValue(), response.getBody());
    }
    @Test
    void testFindAllCoupons_Authorized() throws Exception {

        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        List<ProductCoupon> coupons = Arrays.asList(coupon, coupon2); // Mock some data
        given(couponService.findAllCoupons()).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/all")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))); // Assert that two coupons are returned
    }

    @Test
    void testFindAllCoupons_Unauthorized() throws Exception {
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(false);
        List<ProductCoupon> coupons = Arrays.asList(coupon, coupon2);
        given(couponService.findAllCoupons()).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/all")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testFindCouponById_Authorized() throws Exception {
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        given(couponService.findById("1")).willReturn(coupon);

        mockMvc.perform(get("/product-coupon/id/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxDiscount", is(15)));
    }

    @Test
    void testFindCouponById_Unauthorized() throws Exception {
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(false);
        given(couponService.findById("1")).willReturn(coupon);

        mockMvc.perform(get("/product-coupon/id/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testFindCouponById_NotFound() throws Exception {
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        given(couponService.findById("1")).willThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));

        mockMvc.perform(get("/product-coupon/id/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindCouponByIdProduct_Authorized() throws Exception {
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        given(productCouponOperation.findByIdProduct("1")).willReturn(coupon);

        mockMvc.perform(get("/product-coupon/id-product/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxDiscount", is(15)));
    }

    @Test
    void testFindCouponByIdProduct_Unauthorized() throws Exception {
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(false);
        given(productCouponOperation.findByIdProduct("1")).willReturn(coupon);

        mockMvc.perform(get("/product-coupon/id-product/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testFindCouponByIdProduct_NotFound() throws Exception {
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        given(productCouponOperation.findByIdProduct("1")).willThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));

        mockMvc.perform(get("/product-coupon/id-product/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindCouponsBySupermarketId_Authorized() throws Exception {
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        List<ProductCoupon> coupons = Arrays.asList(coupon);
        given(couponService.findBySupermarketId("Supermarket")).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/supermarket/Supermarket")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testFindCouponsBySupermarketId_Unauthorized() throws Exception {
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(false);
        List<ProductCoupon> coupons = Arrays.asList(coupon);
        given(couponService.findBySupermarketId("Supermarket")).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/supermarket/Supermarket")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = controller.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testUpdateCouponNotFound() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));

        ResponseEntity<Object> response = controller.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), response.getBody());
    }

    @Test
    void testUpdateCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();
        request.setId("1");
        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClient.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(false);

        ResponseEntity<Object> response = controller.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testUpdateCouponSuccess() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClient.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(true);
        when(couponService.updateCoupon(any())).thenReturn(coupon);

        ResponseEntity<Object> response = controller.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = controller.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.join().getBody());
    }

    @Test
    void testDeleteCouponNotFound() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));

        CompletableFuture<ResponseEntity<Object>> response = controller.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), response.join().getBody());
    }

    @Test
    void testDeleteCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClient.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = controller.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.join().getBody());
    }

    @Test
    void testDeleteCouponSuccess() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClient.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(true);
        when(couponService.deleteCoupon(any())).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Object>> response = controller.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.NO_CONTENT, response.join().getStatusCode());
    }
}