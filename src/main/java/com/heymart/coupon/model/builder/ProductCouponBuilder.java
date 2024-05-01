package com.heymart.coupon.model.builder;

import com.heymart.coupon.model.ProductCoupon;

public class ProductCouponBuilder extends CouponBuilder<ProductCouponBuilder> {
    private String idProduct;

    public ProductCouponBuilder setIdProduct(String idProduct) {
        this.idProduct = idProduct;
        return this;
    }

    @Override
    protected ProductCouponBuilder self() {
        return this;
    }

    public ProductCoupon build() {
        return new ProductCoupon(percentDiscount, fixedDiscount, maxDiscount, supermarketName, idProduct);
    }
}
