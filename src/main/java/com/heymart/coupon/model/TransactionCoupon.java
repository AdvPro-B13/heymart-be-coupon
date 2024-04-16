package com.heymart.coupon.model;

public class TransactionCoupon extends Coupon {
    int minTransaction;
    public TransactionCoupon(String id, int percentDiscount, int fixedDiscount,
                             int maxDiscount, String supermarket, int minTransaction) {
        this.id = id;
        this.supermarket = supermarket;

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

        if (minTransaction<0) {
            throw new IllegalArgumentException();
        } else {
            this.minTransaction = minTransaction;
        }
    }
}
