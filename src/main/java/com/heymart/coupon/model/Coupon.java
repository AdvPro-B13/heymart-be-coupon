package com.heymart.coupon.model;
import com.heymart.coupon.model.enums.CouponType;
import lombok.Setter;
import lombok.Getter;
@Setter
@Getter
public class Coupon {
    String id;
    String type;
    int percentDiscount;
    int fixedDiscount;
    int maxDiscount;
    String supermarket;
    String idProduct;
    int minTransaction;
    public Coupon(String id, String type, int percentDiscount, int fixedDiscount,
                         int maxDiscount, String supermarket, String idProduct, int minTransaction) {
        if (id == null) {
            throw new IllegalArgumentException();
        } else {
            this.id = id;
        }

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