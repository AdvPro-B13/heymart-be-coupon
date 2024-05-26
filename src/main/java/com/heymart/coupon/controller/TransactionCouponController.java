package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.CouponAction;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.exception.CouponAlreadyUsedException;
import com.heymart.coupon.exception.CouponNotFoundException;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.UserServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import com.heymart.coupon.service.coupon.UsedCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/transaction-coupon")
public class TransactionCouponController implements CouponOperations{
    private final AuthServiceClient authServiceClient;
    private final UserServiceClient userServiceClient;
    private final CouponService<TransactionCoupon> transactionCouponService;
    private final UsedCouponService usedCouponService;

    @Autowired
    public TransactionCouponController(
            AuthServiceClient authServiceClient,
            CouponService<TransactionCoupon> transactionCouponService,
            UserServiceClient userServiceClient,
            UsedCouponService usedCouponService
    ) {
        this.authServiceClient = authServiceClient;
        this.transactionCouponService = transactionCouponService;
        this.userServiceClient = userServiceClient;
        this.usedCouponService = usedCouponService;
    }
    @Override
    public ResponseEntity<Object> createCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization(CouponAction.CREATE.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        if (!userServiceClient.verifySupermarket(authorizationHeader, request.getSupermarketId())) {
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
    public ResponseEntity<Object> findBySupermarketId(
            String authorizationHeader, String supermarketId
    ) {
        if (!authServiceClient.verifyUserAuthorization(
                CouponAction.READ.getValue(), authorizationHeader)
        ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        return ResponseEntity.ok(transactionCouponService.findBySupermarketId(supermarketId));
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
            if (!userServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
            }
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue());
        }
        return ResponseEntity.ok(transactionCouponService.updateCoupon(request));
    }

    @Override
    public CompletableFuture<ResponseEntity<Object>> deleteCoupon(
            @RequestBody CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization(
                CouponAction.DELETE.getValue(), authorizationHeader)
        ) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue()));
        }
        try {
            TransactionCoupon coupon = transactionCouponService.findById(request.getId());
            if (!userServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketId())) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue()));
            }
            CompletableFuture<Void> deleteUsedCouponsFuture = usedCouponService.deleteUsedCouponsByCouponId(request);

            return transactionCouponService.deleteCoupon(request)
                    .thenCombine(deleteUsedCouponsFuture, (voidResult, usedCouponsResult) ->
                            ResponseEntity.status(HttpStatus.OK).build()
                    )
                    .exceptionally(ex -> {
                        if (ex.getCause() instanceof CouponNotFoundException) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue());
                        } else {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(HttpStatus.INTERNAL_SERVER_ERROR.value());
                        }
                    });
        } catch (CouponNotFoundException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue()));
        }
    }
    @PostMapping("/use")
    public ResponseEntity<Object> useCoupon(
            @RequestBody CouponRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization( CouponAction.READ.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        try {

            TransactionCoupon coupon = transactionCouponService.findById(request.getId());
            Long userId = userServiceClient.getUserId(authorizationHeader);
            return ResponseEntity.ok().body(usedCouponService.useCoupon(coupon, userId));
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue());
        } catch (CouponAlreadyUsedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorStatus.COUPON_ALREADY_USED.getValue());
        }
    }
    @GetMapping("/used-coupon/{supermarketId}")
    public ResponseEntity<Object> findUsedCouponsBySupermarketId(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("supermarketId") String supermarketName
    ) {
        if (!authServiceClient.verifyUserAuthorization( CouponAction.READ.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        Long userId = userServiceClient.getUserId(authorizationHeader);
        return ResponseEntity.ok().body(usedCouponService.getUsedCouponBySupermarket(supermarketName, userId));

    }

}
