package com.heymart.coupon.service;

public interface AuthServiceClient {
    public boolean verifyUserAuthorization(String action, String authorizationHeader);
}
