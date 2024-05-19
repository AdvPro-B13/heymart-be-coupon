package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.Coupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RequestMapping("/default")
public interface CouponOperations {
    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Object>> createCoupon(
            @RequestBody CouponRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );
    @GetMapping("/all")
    public ResponseEntity<Object> findAll(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> findById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("id") String id
    );
    @GetMapping("/supermarket/{supermarketName}")
    public ResponseEntity<Object> findBySupermarketName(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("supermarketName") String supermarketName
    );
    @PutMapping("/update")
    public CompletableFuture<ResponseEntity<Object>> updateCoupon(
            @RequestBody CouponRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );
    @DeleteMapping("/delete")
    public CompletableFuture<ResponseEntity<Object>> deleteCoupon(
            @RequestBody CouponRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );

}
