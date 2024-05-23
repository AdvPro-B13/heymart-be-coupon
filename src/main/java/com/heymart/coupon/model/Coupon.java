package com.heymart.coupon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Coupon {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "percent_discount", nullable = false)
    int percentDiscount;

    @Column(name = "fixed_discount", nullable = false)
    int fixedDiscount;

    @Column(name = "max_discount", nullable = false)
    int maxDiscount;

    @Column(name = "supermarket_id", nullable = false)
    String supermarketId;

    protected Coupon(int percentDiscount, int fixedDiscount, int maxDiscount, String supermarketId) {
        if (percentDiscount == 0 && fixedDiscount == 0) {
            throw new IllegalArgumentException("Percent discount and Fixed discount cannot both be zero");
        }

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

        if (supermarketId == null || supermarketId.trim().isEmpty()) {
            throw new IllegalArgumentException("Supermarket Id cannot be empty");
        } else {
            this.supermarketId = supermarketId;
        }
    }
    public void setPercentDiscount(int percentDiscount) {
        if (percentDiscount < 0) {
            throw new IllegalArgumentException("Percent discount cannot be negative");
        }
        this.percentDiscount = percentDiscount;
    }

    public void setFixedDiscount(int fixedDiscount) {
        if (fixedDiscount < 0) {
            throw new IllegalArgumentException("Fixed discount cannot be negative");
        }
        if (this.maxDiscount < fixedDiscount) {
            throw new IllegalArgumentException("Fixed discount must be smaller than or equal to Max Discount");
        }
        this.fixedDiscount = fixedDiscount;
    }

    public void setMaxDiscount(int maxDiscount) {
        if (maxDiscount < this.fixedDiscount) {
            throw new IllegalArgumentException("Max discount must be greater than or equal to fixed discount");
        }
        this.maxDiscount = maxDiscount;
    }
}
