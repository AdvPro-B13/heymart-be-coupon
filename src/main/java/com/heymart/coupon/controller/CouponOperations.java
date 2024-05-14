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
    @GetMapping("/all")
    public ResponseEntity<?> findAll(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );
    @GetMapping("/id/{id}")
    public ResponseEntity<?> findById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("id") String id
    );
    @GetMapping("/supermarket/{supermarketName}")
    public ResponseEntity<?> findBySupermarketName(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("supermarketName") String supermarketName
    );
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
