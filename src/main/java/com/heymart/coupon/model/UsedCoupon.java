package com.heymart.coupon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "used")
public class UsedCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "username_id", nullable = false)
    private String usernameId;
    public UsedCoupon(Coupon coupon, String usernameId) {
        this.coupon = coupon;
        this.usernameId = usernameId;
    }

}
