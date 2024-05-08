package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
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
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

public class TransactionCouponControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private CouponService<TransactionCoupon> couponService;

    @InjectMocks
    private TransactionCouponController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testCreateCoupon_Unauthorized() throws Exception {
        CouponRequest request = new CouponRequest(); // Populate this with test data
        String authorizationHeader = "Bearer invalid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("invalid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:create", "invalid-token")).willReturn(false);

        mockMvc.perform(post("/transaction-coupon/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testCreateCoupon_NullToken() throws Exception {
        CouponRequest request = new CouponRequest();

        given(authServiceClient.getTokenFromHeader("ian gay")).willReturn(null);
        given(authServiceClient.verifyUserAuthorization("coupon:create", "invalid-token")).willReturn(true);

        mockMvc.perform(post("/transaction-coupon/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "ian gay")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testCreateCoupon_Authorized() throws Exception {
        CouponRequest request = new CouponRequest();
        String authorizationHeader = "Bearer valid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:create", "valid-token")).willReturn(true);

        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(0)
                .build();
        given(couponService.createCoupon(request)).willReturn(coupon);

        mockMvc.perform(post("/transaction-coupon/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk());
    }
    @Test
    public void testFindAllCoupons_Authorized() throws Exception {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(0)
                .build();
        TransactionCoupon coupon2 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(0)
                .build();
        List<TransactionCoupon> coupons = Arrays.asList(coupon,coupon2); // Mock some data
        given(couponService.findAllCoupons()).willReturn(coupons);

        mockMvc.perform(get("/transaction-coupon/")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))); // Assert that two coupons are returned
    }
    @Test
    public void testFindCouponById() throws Exception {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(0)
                .build();

        given(couponService.findById("1")).willReturn(coupon);

        mockMvc.perform(get("/transaction-coupon/1")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxDiscount", is(15))); // Adjust this depending on the actual properties
    }
    @Test
    public void testUpdateCoupon_Authorized() throws Exception {
        CouponRequest request = new CouponRequest();
        String authorizationHeader = "Bearer valid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:update", "valid-token")).willReturn(true);

        TransactionCoupon updatedCoupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(0)
                .build();
        given(couponService.updateCoupon(request)).willReturn(updatedCoupon);

        mockMvc.perform(put("/transaction-coupon/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk());
    }
    @Test
    public void testUpdateCoupon_Unauthorized() throws Exception {
        CouponRequest request = new CouponRequest();
        String authorizationHeader = "Bearer invalid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("invalid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:update", "invalid-token")).willReturn(false);

        mockMvc.perform(put("/transaction-coupon/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testUpdateCoupon_NullToken() throws Exception {
        CouponRequest request = new CouponRequest();

        given(authServiceClient.getTokenFromHeader("rafli wibu")).willReturn(null);
        given(authServiceClient.verifyUserAuthorization("coupon:update", "invalid-token")).willReturn(true);

        mockMvc.perform(put("/transaction-coupon/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "rafli wibu")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteCoupon_Authorized() throws Exception {
        CouponRequest request = new CouponRequest(); // Make sure this is populated as needed for the tes
        String authorizationHeader = "Bearer valid-token";

        // Mock the AuthServiceClient to simulate a valid token being supplied
        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("valid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:delete", "valid-token")).willReturn(true);

        // Perform the test
        mockMvc.perform(delete("/transaction-coupon/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isNoContent()); // Expecting HTTP 204 No Content on successful deletion
    }
    @Test
    public void testDeleteCoupon_Unauthorized() throws Exception {
        CouponRequest request = new CouponRequest(); // Populate this with test data
        String authorizationHeader = "Bearer invalid-token";

        given(authServiceClient.getTokenFromHeader(authorizationHeader)).willReturn("invalid-token");
        given(authServiceClient.verifyUserAuthorization("coupon:delete", "invalid-token")).willReturn(false);

        mockMvc.perform(delete("/transaction-coupon/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testDeleteCoupon_NullToken() throws Exception {
        CouponRequest request = new CouponRequest();

        given(authServiceClient.getTokenFromHeader("ian gay")).willReturn(null);
        given(authServiceClient.verifyUserAuthorization("coupon:delete", "invalid-token")).willReturn(true);

        mockMvc.perform(delete("/transaction-coupon/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "ian gay")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }


}
