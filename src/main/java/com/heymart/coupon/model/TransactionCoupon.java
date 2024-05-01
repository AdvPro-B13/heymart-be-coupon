package com.heymart.coupon.model;

import com.heymart.coupon.model.enums.CouponType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
@Setter
@Getter
@Entity
@Table(name = "TransactionCoupon")
public class TransactionCoupon extends Coupon {

    @Column(name = "min_transaction", nullable = false)
    private int minTransaction;

    // Protected no-arg constructor for JPA
    protected TransactionCoupon() {
        super();
    }

    // Constructor that uses the protected constructor from Coupon
    public TransactionCoupon(int percentDiscount, int fixedDiscount,
                             int maxDiscount, String supermarketName, int minTransaction) {
        super(percentDiscount, fixedDiscount, maxDiscount, supermarketName);
        if (minTransaction<0) {
            throw new IllegalArgumentException("Minimum transaction cannot be negative");
        } else {
            this.minTransaction = minTransaction;
        }
    }
}