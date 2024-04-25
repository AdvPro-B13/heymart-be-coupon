package com.heymart.coupon.model;

import com.heymart.coupon.model.enums.CouponType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Setter
@Getter
@Entity
@Table(name = "Coupon")
public class Coupon {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    @Column(name = "type", nullable = false)
    String type;
    @Column(name = "percent_discount", nullable = false)
    int percentDiscount;
    @Column(name = "fixed_discount", nullable = false)
    int fixedDiscount;
    @Column(name = "max_discount", nullable = false)
    int maxDiscount;
    @Column(name = "supermarket", nullable = false)
    String supermarket;
    @Column(name = "id_product")
    String idProduct;
    @Column(name = "min_transaction")
    int minTransaction;
    public Coupon() {
    }
    public Coupon(String type, int percentDiscount, int fixedDiscount,
                         int maxDiscount, String supermarket, String idProduct, int minTransaction) {
        if (CouponType.contains(type)) {
                this.type = type;
        } else {
            throw new IllegalArgumentException();
        }

        if (percentDiscount<0) {
            throw new IllegalArgumentException();
        } else {
            this.percentDiscount = percentDiscount;
        }

        if (fixedDiscount<0 || fixedDiscount >= maxDiscount) {
            throw new IllegalArgumentException();
        } else {
            this.fixedDiscount = fixedDiscount;
            this.maxDiscount = maxDiscount;
        }

        if (supermarket == null) {
            throw new IllegalArgumentException();
        } else {
            this.supermarket = supermarket;
        }

        if (minTransaction<0) {
            throw new IllegalArgumentException();
        }

        if (minTransaction>0 && idProduct != null) {
            throw new IllegalArgumentException();
        } else {
            this.minTransaction = minTransaction;
            this.idProduct = idProduct;
        }
    }
}