package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RequestMapping("/api/coupon")
public interface CouponOperations {
    @PostMapping("/create")
    public ResponseEntity<Object> createCoupon(
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
    @GetMapping("/supermarket/{supermarketId}")
    public ResponseEntity<Object> findBySupermarketId(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("supermarketId") String supermarketName
    );
    @PutMapping("/update")
    public ResponseEntity<Object> updateCoupon(
            @RequestBody CouponRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );
    @DeleteMapping("/delete")
    public CompletableFuture<ResponseEntity<Object>> deleteCoupon(
            @RequestBody CouponRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    );

}
