package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

public class ProductCouponControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private CouponService<ProductCoupon> couponService;

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
    public void testCreateCoupon_Unauthorized() throws Exception {
        CouponRequest request = new CouponRequest();
        String authorizationHeader = "Bearer invalid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("invalid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:create", "invalid-token")).willReturn(false);

        mockMvc.perform(post("/product-coupon/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testCreateCoupon_NullToken() throws Exception {
        CouponRequest request = new CouponRequest();

        given(authServiceClient.getTokenFromHeader("")).willReturn(null);
        given(authServiceClient.verifyUserAuthorization("coupon:create", "invalid-token")).willReturn(true);

        mockMvc.perform(post("/product-coupon/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "=")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testCreateCoupon_WrongSupermarket() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setSupermarketName("rafli-mart");
        String authorizationHeader = "Bearer valid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:create", "valid-token")).willReturn(true);
        given(authServiceClient.verifySupermarket("valid-token","rafli-mart")).willReturn(false);

        given(couponService.createCoupon(request)).willReturn(coupon);

        mockMvc.perform(post("/product-coupon/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", authorizationHeader))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testCreateCoupon_Authorized() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setSupermarketName("rafli-mart");
        String authorizationHeader = "Bearer valid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:create", "valid-token")).willReturn(true);
        given(authServiceClient.verifySupermarket("valid-token","rafli-mart")).willReturn(true);

        given(couponService.createCoupon(request)).willReturn(coupon);

        mockMvc.perform(post("/product-coupon/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk());
    }
    @Test
    public void testFindAllCoupons_Authorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:read", "valid-token")).willReturn(true);
        List<ProductCoupon> coupons = Arrays.asList(coupon,coupon2); // Mock some data
        given(couponService.findAllCoupons()).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/all")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))); // Assert that two coupons are returned
    }
    @Test
    public void testFindAllCoupons_Unauthorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:read", "valid-token")).willReturn(false);
        List<ProductCoupon> coupons = Arrays.asList(coupon,coupon2);
        given(couponService.findAllCoupons()).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/all")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testFindCouponById_Authorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:read", "valid-token")).willReturn(true);
        given(couponService.findById("1")).willReturn(coupon);

        mockMvc.perform(get("/product-coupon/id/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxDiscount", is(15)));
    }
    @Test
    public void testFindCouponById_Unauthorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:read", "valid-token")).willReturn(false);
        given(couponService.findById("1")).willReturn(coupon);

        mockMvc.perform(get("/product-coupon/id/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testFindCouponById_NotFound() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:read", "valid-token")).willReturn(true);
        given(couponService.findById("1")).willThrow(new RuntimeException("Coupon not found"));

        mockMvc.perform(get("/product-coupon/id/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testFindCouponsBySupermarketName_Authorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:read", "valid-token")).willReturn(true);
        List<ProductCoupon> coupons = Collections.singletonList(coupon);
        given(couponService.findBySupermarketName("Supermarket")).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/supermarket/Supermarket")
                    .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    @Test
    public void testFindCouponsBySupermarketName_Unauthorized() throws Exception {
        String authorizationHeader = "Bearer valid-token";
        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:read", "valid-token")).willReturn(false);
        List<ProductCoupon> coupons = Collections.singletonList(coupon);
        given(couponService.findBySupermarketName("Supermarket")).willReturn(coupons);

        mockMvc.perform(get("/product-coupon/supermarket/Supermarket")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testUpdateCoupon_Authorized() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setId("id");
        String authorizationHeader = "Bearer valid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:update", "valid-token")).willReturn(true);
        given(couponService.findById("id")).willReturn(coupon);
        given(authServiceClient.verifySupermarket("valid-token","Supermarket")).willReturn(true);

        given(couponService.updateCoupon(request)).willReturn(coupon);

        mockMvc.perform(put("/product-coupon/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk());
    }
    @Test
    public void testUpdateCoupon_NotFound() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setId("id");
        String authorizationHeader = "Bearer valid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:update", "valid-token")).willReturn(true);
        given(couponService.findById("id")).willThrow(new RuntimeException());

        mockMvc.perform(put("/product-coupon/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testUpdateCoupon_Unauthorized() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setId("id");
        String authorizationHeader = "Bearer invalid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("invalid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:update", "invalid-token")).willReturn(false);

        mockMvc.perform(put("/product-coupon/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        given(authServiceClient.verifyUserAuthorization("coupon:update", "invalid-token")).willReturn(true);
        given(couponService.findById("id")).willReturn(coupon);
        given(authServiceClient.verifySupermarket("invalid-token","Supermarket")).willReturn(false);

        given(couponService.updateCoupon(request)).willReturn(coupon);

        mockMvc.perform(put("/product-coupon/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testDeleteCoupon_Authorized() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setId("id");
        String authorizationHeader = "Bearer valid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:delete", "valid-token")).willReturn(true);
        given(couponService.findById("id")).willReturn(coupon);
        given(authServiceClient.verifySupermarket("valid-token","Supermarket")).willReturn(true);


        mockMvc.perform(delete("/product-coupon/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
    @Test
    public void testDeleteCoupon_NotFound() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setId("id");
        String authorizationHeader = "Bearer valid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:delete", "valid-token")).willReturn(true);
        given(couponService.findById("id")).willThrow(new RuntimeException());


        mockMvc.perform(delete("/product-coupon/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testDeleteCoupon_Unauthorized() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setId("id");
        String authorizationHeader = "Bearer invalid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("invalid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:delete", "invalid-token")).willReturn(false);

        mockMvc.perform(delete("/product-coupon/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        given(authServiceClient.verifyUserAuthorization("coupon:delete", "invalid-token")).willReturn(true);
        given(couponService.findById("id")).willReturn(coupon);
        given(authServiceClient.verifySupermarket("invalid-token","Supermarket")).willReturn(false);

        mockMvc.perform(delete("/product-coupon/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isUnauthorized());
    }
}
