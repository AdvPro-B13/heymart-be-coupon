package com.heymart.coupon.model;

public class ProductCoupon extends Coupon {
    String product;
    public ProductCoupon(String id, int percentDiscount, int fixedDiscount,
                             int maxDiscount, String supermarket, String product) {
        this.id = id;
        this.supermarket = supermarket;
        this.product = product;

        if (percentDiscount<0) {
            throw new IllegalArgumentException();
        } else {
            this.percentDiscount = percentDiscount;
        }

        if (fixedDiscount<0 || fixedDiscount >= maxDiscount) {
            throw new IllegalArgumentException();
        } else {
            this.fixedDiscount = fixedDiscount;
            this.maxDiscount = maxDiscount;
        }
    }
}
