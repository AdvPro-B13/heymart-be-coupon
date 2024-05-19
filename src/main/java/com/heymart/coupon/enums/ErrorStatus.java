package com.heymart.coupon.enums;

import lombok.Getter;

@Getter
public enum ErrorStatus {
    COUPON_NOT_FOUND("Coupon Not Found"),
    UNAUTHORIZED("Unauthorized"),
    COUPON_ALREADY_EXIST("Coupon Already Exist");

    private final String value;
    private ErrorStatus(String value) {
        this.value = value;
    }
}
