package com.heymart.coupon.model.builder;

public abstract class CouponBuilder<T extends CouponBuilder<T>> {
    protected int percentDiscount;
    protected int fixedDiscount;
    protected int maxDiscount;
    protected String supermarketName;

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

    public T setSupermarketName(String supermarketName) {
        this.supermarketName = supermarketName;
        return self();
    }

    protected abstract T self();
}
