package com.heymart.coupon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "used")
public class UsedCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id", nullable = false)
    private UUID couponId;

    @Column(name = "supermarket_id", nullable = false)
    private String supermarketId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public UsedCoupon(UUID couponId, String supermarketId, Long userId) {
        this.couponId = couponId;
        this.supermarketId = supermarketId;
        this.userId = userId;
    }
}
