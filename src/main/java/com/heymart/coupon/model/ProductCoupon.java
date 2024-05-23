package com.heymart.coupon.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "ProductCoupon")
public class ProductCoupon extends Coupon {

    @Column(name = "id_product", nullable = false, unique = true)
    private String idProduct;

    protected ProductCoupon() {
        super();
    }

    public ProductCoupon(int percentDiscount, int fixedDiscount,
                             int maxDiscount, String supermarketName, String idProduct) {
        super(percentDiscount, fixedDiscount, maxDiscount, supermarketName);
        if (idProduct == null || idProduct.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty");
        } else {
            this.idProduct = idProduct;
        }
    }
}