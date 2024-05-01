package com.heymart.coupon.model;

import com.heymart.coupon.model.enums.CouponType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
@Setter
@Getter
@Entity
@Table(name = "ProductCoupon")
public class ProductCoupon extends Coupon {

    @Column(name = "id_product", nullable = false)
    private String idProduct;

    // Protected no-arg constructor for JPA
    protected ProductCoupon() {
        super();
    }

    // Constructor that uses the protected constructor from Coupon
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