package com.heymart.coupon.model.builder;

import com.heymart.coupon.model.TransactionCoupon;

public class TransactionCouponBuilder extends CouponBuilder<TransactionCouponBuilder> {
    private int minTransaction;

    public TransactionCouponBuilder setMinTransaction(int minTransaction) {
        this.minTransaction = minTransaction;
        return this;
    }

    @Override
    protected TransactionCouponBuilder self() {
        return this;
    }

    public TransactionCoupon build() {
        return new TransactionCoupon(percentDiscount, fixedDiscount, maxDiscount, supermarketId, minTransaction);
    }
}
