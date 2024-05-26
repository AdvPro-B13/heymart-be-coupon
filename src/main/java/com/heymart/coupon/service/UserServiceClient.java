package com.heymart.coupon.service;

import com.heymart.coupon.model.UsedCoupon;

import java.util.UUID;

public interface UserServiceClient {
    public boolean verifySupermarket(String token, String supermarketId);
    public Long getUserId(String token);
}
