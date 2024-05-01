package com.heymart.coupon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name = "ProductCoupon")
public class ProductCoupon extends Coupon {

    @Column(name = "id_product", nullable = false)
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