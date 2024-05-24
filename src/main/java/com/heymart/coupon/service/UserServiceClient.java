package com.heymart.coupon.service;

import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.UsedCoupon;

public interface UserServiceClient {
    public boolean verifySupermarket(String token, String supermarketId);
    public UsedCoupon useCoupon(String token, TransactionCoupon coupon);
}
