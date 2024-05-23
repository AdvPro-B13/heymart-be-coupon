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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "supermarket_id", nullable = false)
    private String supermarketId;

    public UsedCoupon(Coupon coupon, Long userId, String supermarketId) {
        this.coupon = coupon;
        this.userId = userId;
        this.supermarketId = supermarketId;
    }
}
