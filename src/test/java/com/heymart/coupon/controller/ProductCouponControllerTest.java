package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.service.AuthServiceClient;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

public class ProductCouponControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private CouponService<ProductCoupon> couponService;

    @Mock
    private ProductCouponOperation productCouponOperation;

    @InjectMocks
    private ProductCouponController controller;

    private ProductCoupon coupon;
    private ProductCoupon coupon2;
    @BeforeEach
    public void setup() {
        coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();
        coupon2 = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket2")
                .setIdProduct("123")
                .build();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testCreateCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        request.setSupermarketName("Supermarket");
        request.setIdProduct("Product");

        when(authServiceClient.verifyUserAuthorization(any(), any())).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = controller.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testCreateCoupon_WrongSupermarket() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setSupermarketName("Supermarket");
        request.setIdProduct("Product");

        when(authServiceClient.verifyUserAuthorization(any(), any())).thenReturn(true);
        when(authServiceClient.verifySupermarket(any(), any())).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = controller.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }


    @Test
    public void testCreateCouponSuccess() {
        CouponRequest request = new CouponRequest();
        request.setSupermarketName("Supermarket");
        request.setIdProduct("Product");

        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(authServiceClient.verifySupermarket("authHeader", request.getSupermarketName())).thenReturn(true);
        when(productCouponOperation.findByIdProduct(request.getIdProduct())).thenThrow(new RuntimeException("Coupon not found"));
        when(couponService.createCoupon(any())).thenReturn(CompletableFuture.completedFuture(coupon));

        CompletableFuture<ResponseEntity<Object>> response = controller.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        assertNotNull(response.join().getBody());
    }
    @Test
    public void testCreateCouponAlreadyExists() {
        CouponRequest request = new CouponRequest();
        request.setSupermarketName("Supermarket");
        request.setIdProduct("Product");

        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(authServiceClient.verifySupermarket("authHeader", request.getSupermarketName())).thenReturn(true);
        when(productCouponOperation.findByIdProduct(request.getIdProduct())).thenReturn(coupon);

        CompletableFuture<ResponseEntity<Object>> response = controller.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.CONFLICT, response.join().getStatusCode());
        assertEquals("Coupon Already Exist", response.join().getBody());
    }

    @Test
    public void testCreateCouponError() {
        CouponRequest request = new CouponRequest();
        request.setSupermarketName("Supermarket");
        request.setIdProduct("Product");

        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(authServiceClient.verifySupermarket("authHeader", request.getSupermarketName())).thenReturn(true);
        when(productCouponOperation.findByIdProduct(request.getIdProduct())).thenThrow(new RuntimeException("Random Error"));
        when(couponService.createCoupon(any())).thenReturn(CompletableFuture.completedFuture(coupon));

        CompletableFuture<ResponseEntity<Object>> response = controller.createCoupon(request, "authHeader");

        assertNotEquals(HttpStatus.OK, response.join().getStatusCode());
        assertNotNull(response.join().getBody());
    }
    @Test
    public void testFindAllCoupons_Authorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        List<ProductCoupon> coupons = Arrays.asList(coupon, coupon2); // Mock some data
        given(couponService.findAllCoupons()).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/all")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))); // Assert that two coupons are returned
    }

    @Test
    public void testFindAllCoupons_Unauthorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(false);
        List<ProductCoupon> coupons = Arrays.asList(coupon, coupon2);
        given(couponService.findAllCoupons()).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/all")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testFindCouponById_Authorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        given(couponService.findById("1")).willReturn(coupon);

        mockMvc.perform(get("/product-coupon/id/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxDiscount", is(15)));
    }

    @Test
    public void testFindCouponById_Unauthorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(false);
        given(couponService.findById("1")).willReturn(coupon);

        mockMvc.perform(get("/product-coupon/id/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testFindCouponById_NotFound() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        given(couponService.findById("1")).willThrow(new RuntimeException("Coupon not found"));

        mockMvc.perform(get("/product-coupon/id/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindCouponByIdProduct_Authorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        given(productCouponOperation.findByIdProduct("1")).willReturn(coupon);

        mockMvc.perform(get("/product-coupon/id-product/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxDiscount", is(15)));
    }

    @Test
    public void testFindCouponByIdProduct_Unauthorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(false);
        given(productCouponOperation.findByIdProduct("1")).willReturn(coupon);

        mockMvc.perform(get("/product-coupon/id-product/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testFindCouponByIdProduct_NotFound() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        given(productCouponOperation.findByIdProduct("1")).willThrow(new RuntimeException("Coupon not found"));

        mockMvc.perform(get("/product-coupon/id-product/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindCouponsBySupermarketName_Authorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(true);
        List<ProductCoupon> coupons = Arrays.asList(coupon);
        given(couponService.findBySupermarketName("Supermarket")).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/supermarket/Supermarket")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testFindCouponsBySupermarketName_Unauthorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.verifyUserAuthorization("coupon:read", "Bearer valid-token")).willReturn(false);
        List<ProductCoupon> coupons = Arrays.asList(coupon);
        given(couponService.findBySupermarketName("Supermarket")).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/supermarket/Supermarket")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = controller.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testUpdateCouponNotFound() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenThrow(new RuntimeException("Coupon not found"));

        CompletableFuture<ResponseEntity<Object>> response = controller.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
        assertEquals("Coupon not found", response.join().getBody());
    }

    @Test
    public void testUpdateCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();
        request.setId("1");
        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenReturn(coupon);
        when(authServiceClient.verifySupermarket("authHeader", coupon.getSupermarketName())).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = controller.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testUpdateCouponSuccess() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenReturn(coupon);
        when(authServiceClient.verifySupermarket("authHeader", coupon.getSupermarketName())).thenReturn(true);
        when(couponService.updateCoupon(any())).thenReturn(CompletableFuture.completedFuture(coupon));

        CompletableFuture<ResponseEntity<Object>> response = controller.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        assertNotNull(response.join().getBody());
    }

    @Test
    public void testDeleteCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = controller.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testDeleteCouponNotFound() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenThrow(new RuntimeException("Coupon not found"));

        CompletableFuture<ResponseEntity<Object>> response = controller.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
        assertEquals("Coupon not found", response.join().getBody());
    }

    @Test
    public void testDeleteCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenReturn(coupon);
        when(authServiceClient.verifySupermarket("authHeader", coupon.getSupermarketName())).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = controller.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testDeleteCouponSuccess() {
        CouponRequest request = new CouponRequest();
        request.setId("1");

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(couponService.findById(request.getId())).thenReturn(coupon);
        when(authServiceClient.verifySupermarket("authHeader", coupon.getSupermarketName())).thenReturn(true);
        when(couponService.deleteCoupon(any())).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Object>> response = controller.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.NO_CONTENT, response.join().getStatusCode());
    }
}