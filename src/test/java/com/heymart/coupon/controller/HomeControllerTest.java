package com.heymart.coupon.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class HomeControllerTest {
    MockMvc mockMvc;
    @InjectMocks
    HomeController homeController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void testCouponList() throws Exception {
        String supermarket = "Raflimart";

        mockMvc.perform(get("/list/" + supermarket))
                .andExpect(status().isOk())
                .andExpect(content().string("Ini isinya list kupon dari " + supermarket + "!"));
    }

    @Test
    void createCouponList() throws Exception {
        String supermarket = "Raflimart";

        mockMvc.perform(post("/create/" + supermarket))
                .andExpect(status().isOk())
                .andExpect(content().string("Pembuatan kupon untuk supermarket " + supermarket + "!"));
    }
}