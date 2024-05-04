package com.heymart.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequest {
    private String id;
    private int percentDiscount;
    private int fixedDiscount;
    private int maxDiscount;
    private String supermarketName;
    private String idProduct;
    private int minTransaction;
}
