package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.CouponAction;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.exception.CouponNotFoundException;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.UserServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/transaction-coupon")
public class TransactionCouponController implements CouponOperations{
    private final AuthServiceClient authServiceClient;
    private final UserServiceClient userServiceClient;
    private final CouponService<TransactionCoupon> transactionCouponService;

    @Autowired
    public TransactionCouponController(
            AuthServiceClient authServiceClient,
            CouponService<TransactionCoupon> transactionCouponService,
            UserServiceClient userServiceClient
    ) {
        this.authServiceClient = authServiceClient;
        this.transactionCouponService = transactionCouponService;
        this.userServiceClient = userServiceClient;
    }
    @Override
    public ResponseEntity<Object> createCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization(CouponAction.CREATE.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        if (!userServiceClient.verifySupermarket(authorizationHeader, request.getSupermarketName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        return ResponseEntity.ok(transactionCouponService.createCoupon(request));
    }

    @Override
    public ResponseEntity<Object> findAll(
            String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization(
                CouponAction.READ.getValue(), authorizationHeader)
        ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        return ResponseEntity.ok(transactionCouponService.findAllCoupons());
    }

    @Override
    public ResponseEntity<Object> findById(
            String authorizationHeader, String id
    ) {
        if (!authServiceClient.verifyUserAuthorization(
                CouponAction.READ.getValue(), authorizationHeader)
        ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        try {
            TransactionCoupon coupon = transactionCouponService.findById(id);
            return ResponseEntity.ok(coupon);
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue());
        }
    }

    @Override
    public ResponseEntity<Object> findBySupermarketName(
            String authorizationHeader, String supermarketName
    ) {
        if (!authServiceClient.verifyUserAuthorization(
                CouponAction.READ.getValue(), authorizationHeader)
        ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        return ResponseEntity.ok(transactionCouponService.findBySupermarketName(supermarketName));
    }
    @Override
    public ResponseEntity<Object> updateCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization( CouponAction.UPDATE.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        try {
            TransactionCoupon coupon = transactionCouponService.findById(request.getId());
            if (!userServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
            }
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue());
        }
        return ResponseEntity.ok(transactionCouponService.updateCoupon(request));
    }

    @Override
    public CompletableFuture<ResponseEntity<Object>> deleteCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization(
                CouponAction.DELETE.getValue(), authorizationHeader)
        ) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue()));
        }
        try {
            TransactionCoupon coupon = transactionCouponService.findById(request.getId());
            if (!userServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketName())) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue()));
            }
            return transactionCouponService.deleteCoupon(request)
                    .thenApply(voidResult -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        } catch (CouponNotFoundException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue()));
        }
    }
}
