package com.heymart.coupon.enums;

import lombok.Getter;

@Getter
public enum CouponAction {
    CREATE("coupon:create"),
    READ("coupon:read"),
    UPDATE("coupon:update"),
    DELETE("coupon:delete");
    private final String value;
    private CouponAction(String value) {
        this.value = value;
    }
}
