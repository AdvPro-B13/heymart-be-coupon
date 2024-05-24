package com.heymart.coupon.model.builder;

public abstract class CouponBuilder<T extends CouponBuilder<T>> {
    protected int percentDiscount;
    protected int fixedDiscount;
    protected int maxDiscount;
    protected String supermarketId;

    public T setPercentDiscount(int percentDiscount) {
        this.percentDiscount = percentDiscount;
        return self();
    }

    public T setFixedDiscount(int fixedDiscount) {
        this.fixedDiscount = fixedDiscount;
        return self();
    }

    public T setMaxDiscount(int maxDiscount) {
        this.maxDiscount = maxDiscount;
        return self();
    }

    public T setSupermarketId(String supermarketId) {
        this.supermarketId = supermarketId;
        return self();
    }

    protected abstract T self();
}
