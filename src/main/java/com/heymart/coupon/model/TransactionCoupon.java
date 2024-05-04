package com.heymart.coupon.model;


import jakarta.persistence.*;
import lombok.Getter;
@Getter
@Entity
@Table(name = "TransactionCoupon")
public class TransactionCoupon extends Coupon {

    @Column(name = "min_transaction", nullable = false)
    private int minTransaction;

    protected TransactionCoupon() {
        super();
    }

    public TransactionCoupon(int percentDiscount, int fixedDiscount,
                             int maxDiscount, String supermarketName, int minTransaction) {
        super(percentDiscount, fixedDiscount, maxDiscount, supermarketName);
        if (minTransaction<0) {
            throw new IllegalArgumentException("Minimum transaction cannot be negative");
        } else {
            this.minTransaction = minTransaction;
        }
    }

    public void setMinTransaction(int minTransaction) {
        this.minTransaction = minTransaction;
    }
}