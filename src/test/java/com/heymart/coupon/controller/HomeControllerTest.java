package com.heymart.coupon.controller;

import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.builder.CouponBuilder;
import com.heymart.coupon.model.enums.CouponType;
import com.heymart.coupon.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
class HomeControllerTest {
    MockMvc mockMvc;
    @Mock
    private CouponService couponService;
    @InjectMocks
    HomeController homeController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void testHelloWorld() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World!"));
    }

    @Test
    void testCouponList() throws Exception {
        String supermarket = "Raflimart";

        mockMvc.perform(get("/list/" + supermarket))
                .andExpect(status().isOk())
                .andExpect(content().string("Ini isinya list kupon dari " + supermarket + "!"));
    }
    @Test
    void testCreateCoupon() throws Exception {
        String supermarket = "Raflimart";
        Coupon expectedCoupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket(supermarket)
                .setIdProduct("P01")
                .getResult();

        when(couponService.createCoupon(any(Coupon.class))).thenReturn(expectedCoupon);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("supermarket", supermarket))
                .andExpect(status().isOk())
                .andExpect(content().string("Pembuatan kupon untuk supermarket " + supermarket + "!"));

        verify(couponService, times(1)).createCoupon(argThat(coupon -> coupon.getSupermarket().equals(supermarket)));
    }
    @Test
    void testUpdateCoupon() throws Exception {
        String supermarket = "Raflimart";

        mockMvc.perform(put("/update/" + supermarket))
                .andExpect(status().isOk())
                .andExpect(content().string("Update kupon untuk supermarket " + supermarket + "!"));
    }
    @Test
    void testDeleteCoupon() throws Exception {
        String supermarket = "Raflimart";

        mockMvc.perform(delete("/delete/" + supermarket))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete kupon untuk supermarket " + supermarket + "!"));
    }
}