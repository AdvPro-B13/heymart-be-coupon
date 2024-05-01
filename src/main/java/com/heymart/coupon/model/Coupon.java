package com.heymart.coupon.model;

import com.heymart.coupon.model.enums.CouponType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Setter
@Getter
@MappedSuperclass
public abstract class Coupon {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "percent_discount", nullable = false)
    int percentDiscount;

    @Column(name = "fixed_discount", nullable = false)
    int fixedDiscount;

    @Column(name = "max_discount", nullable = false)
    int maxDiscount;

    @Column(name = "supermarket_name", nullable = false)
    String supermarketName;

    protected Coupon() {
    }
    protected Coupon(int percentDiscount, int fixedDiscount, int maxDiscount, String supermarketName) {
        if (percentDiscount<0) {
            throw new IllegalArgumentException("Percent discount cannot be negative");
        } else {
            this.percentDiscount = percentDiscount;
        }

        if (fixedDiscount<0) {
            throw new IllegalArgumentException("Fixed discount cannot be negative");
        } else {
            this.fixedDiscount = fixedDiscount;
        }

        if (maxDiscount < fixedDiscount) {
            throw new IllegalArgumentException("Max discount must be greater than or equal to fixed discount");
        } else {
            this.maxDiscount = maxDiscount;
        }

        if (supermarketName == null || supermarketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Supermarket name cannot be empty");
        } else {
            this.supermarketName = supermarketName;
        }
    }
}
