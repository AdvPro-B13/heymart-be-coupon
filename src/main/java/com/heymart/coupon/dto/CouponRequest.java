package com.heymart.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
