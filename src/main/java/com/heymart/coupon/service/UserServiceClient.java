package com.heymart.coupon.service;

public interface UserServiceClient {
    public boolean verifySupermarket(String token, String supermarketId);
    public Long getUserId(String token);
}
