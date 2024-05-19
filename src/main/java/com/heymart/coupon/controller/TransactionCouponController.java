package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/transaction-coupon")
public class TransactionCouponController implements CouponOperations<TransactionCoupon>{
    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    @Qualifier("transactionCouponService")
    CouponService<TransactionCoupon> transactionCouponService;
    @Override
    public CompletableFuture<ResponseEntity<Object>> createCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:create", authorizationHeader)) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
        }
        if (!authServiceClient.verifySupermarket(authorizationHeader, request.getSupermarketName())) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
        }
        return transactionCouponService.createCoupon(request)
                .thenApply(ResponseEntity::ok);
    }

    @Override
    public ResponseEntity<Object> findAll(
            String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:read", authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(transactionCouponService.findAllCoupons());
    }

    @Override
    public ResponseEntity<Object> findById(
            String authorizationHeader, String id
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:read", authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {
            TransactionCoupon coupon = transactionCouponService.findById(id);
            return ResponseEntity.ok(coupon);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found");
        }
    }

    @Override
    public ResponseEntity<Object> findBySupermarketName(
            String authorizationHeader, String supermarketName
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:read", authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(transactionCouponService.findBySupermarketName(supermarketName));
    }
    @Override
    public CompletableFuture<ResponseEntity<Object>> updateCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:update", authorizationHeader)) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
        }
        try {
            TransactionCoupon coupon = transactionCouponService.findById(request.getId());
            if (!authServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketName())) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found"));
        }
        return transactionCouponService.updateCoupon(request)
                .thenApply(ResponseEntity::ok);
    }

    @Override
    public CompletableFuture<ResponseEntity<Object>> deleteCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:delete", authorizationHeader)) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
        }
        try {
            TransactionCoupon coupon = transactionCouponService.findById(request.getId());
            if (!authServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketName())) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
            }
            return transactionCouponService.deleteCoupon(request)
                    .thenApply(voidResult -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found"));
        }
    }
}
