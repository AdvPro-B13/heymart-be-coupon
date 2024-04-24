package com.heymart.coupon.model.enums;
import lombok.Getter;
@Getter
public enum CouponType {
    PRODUCT("PRODUCT"),
    TRANSACTION("TRANSACTION");

    private final String value;

    private CouponType(String value) {
        this.value = value;
    }

    public static boolean contains(String param) {
        for (CouponType orderStatus : CouponType.values()) {
            if (orderStatus.name().equals(param)) {
                return true;
            }
        }
        return false;
    }
}
