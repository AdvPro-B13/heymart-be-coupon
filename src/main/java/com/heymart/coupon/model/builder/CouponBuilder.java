package com.heymart.coupon.model.builder;
import com.heymart.coupon.model.Coupon;
public class CouponBuilder {
    String id;
    String type;
    int percentDiscount;
    int fixedDiscount;
    int maxDiscount;
    String supermarket;
    String idProduct;
    int minTransaction;


    public CouponBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public CouponBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public CouponBuilder setPercentDiscount(int percentDiscount) {
        this.percentDiscount = percentDiscount;
        return this;
    }

    public CouponBuilder setFixedDiscount(int fixedDiscount) {
        this.fixedDiscount = fixedDiscount;
        return this;
    }

    public CouponBuilder setIdProduct(String idProduct) {
        this.idProduct = idProduct;
        return this;
    }

    public CouponBuilder setMaxDiscount(int maxDiscount) {
        this.maxDiscount = maxDiscount;
        return this;
    }

    public CouponBuilder setMinTransaction(int minTransaction) {
        this.minTransaction = minTransaction;
        return this;
    }

    public CouponBuilder setSupermarket(String supermarket) {
        this.supermarket = supermarket;
        return this;
    }

    public Coupon getResult() {
        return new Coupon(id, type, percentDiscount, fixedDiscount, maxDiscount, supermarket, idProduct, minTransaction);
    }
}
