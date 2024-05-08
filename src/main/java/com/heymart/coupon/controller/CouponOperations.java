package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.Coupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/default")
public interface CouponOperations<T extends Coupon> {
    @PostMapping("/create")
    public ResponseEntity<?> createCoupon(
            @RequestBody CouponRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );
    @GetMapping("/")
    public ResponseEntity<List<T>> findAll();
    @GetMapping("/{id}")
    public ResponseEntity<T> findById(@PathVariable String id);
    @PutMapping("/update")
    public ResponseEntity<?> updateCoupon(
            @RequestBody CouponRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCoupon(
            @RequestBody CouponRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );
}
