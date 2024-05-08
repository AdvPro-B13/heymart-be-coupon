package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction-coupon")
public class TransactionCouponController implements CouponOperations<TransactionCoupon>{
    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private CouponService<TransactionCoupon> couponService;
    @Override
    public ResponseEntity<?> createCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        String token = authServiceClient.getTokenFromHeader(authorizationHeader);
        if (token == null | !authServiceClient.verifyUserAuthorization("coupon:create", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(couponService.createCoupon(request));
    }

    @Override
    public ResponseEntity<List<TransactionCoupon>> findAll() {
        return ResponseEntity.ok(couponService.findAllCoupons());
    }

    @Override
    public ResponseEntity<TransactionCoupon> findById(String id) {
        return ResponseEntity.ok(couponService.findById(id));
    }

    @Override
    public ResponseEntity<?> updateCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        String token = authServiceClient.getTokenFromHeader(authorizationHeader);
        if (token == null | !authServiceClient.verifyUserAuthorization("coupon:update", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(couponService.updateCoupon(request));
    }

    @Override
    public ResponseEntity<?> deleteCoupon(
            CouponRequest request, String authorizationHeader) {
        String token = authServiceClient.getTokenFromHeader(authorizationHeader);
        if (token == null | !authServiceClient.verifyUserAuthorization("coupon:delete", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        couponService.deleteCoupon(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
