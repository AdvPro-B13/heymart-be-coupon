package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.CouponAction;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.exception.CouponNotFoundException;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.UserServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import com.heymart.coupon.service.coupon.ProductCouponOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/product-coupon")
public class ProductCouponController implements CouponOperations{
    private final AuthServiceClient authServiceClient;
    private final UserServiceClient userServiceClient;

    private final CouponService<ProductCoupon> productCouponService;

    private final ProductCouponOperation productCouponOperation;

    @Autowired
    public ProductCouponController(
            AuthServiceClient authServiceClient,
            CouponService<ProductCoupon> productCouponService,
            ProductCouponOperation productCouponOperation,
            UserServiceClient userServiceClient
    ) {
        this.authServiceClient = authServiceClient;
        this.productCouponService = productCouponService;
        this.productCouponOperation = productCouponOperation;
        this.userServiceClient = userServiceClient;
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
        try {
            productCouponOperation.findByIdProduct(request.getIdProduct());
        } catch (RuntimeException e) {
            return ResponseEntity.ok(productCouponService.createCoupon(request));
        }
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorStatus.COUPON_ALREADY_EXIST.getValue());
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
        return ResponseEntity.ok(productCouponService.findAllCoupons());
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
            ProductCoupon coupon = productCouponService.findById(id);
            return ResponseEntity.ok(coupon);
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue());
        }

    }
    @GetMapping("/id-product/{idProduct}")
    public ResponseEntity<Object> findByIdProduct(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("idProduct") String idProduct
    ) {
        if (!authServiceClient.verifyUserAuthorization(
                CouponAction.READ.getValue(), authorizationHeader)
        ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        try {
            ProductCoupon coupon = productCouponOperation.findByIdProduct(idProduct);
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
        return ResponseEntity.ok(productCouponService.findBySupermarketId(supermarketId));
    }

    @Override
    public ResponseEntity<Object> updateCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization(CouponAction.UPDATE.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
        }
        try {
            ProductCoupon coupon = productCouponService.findById(request.getId());
            if (!userServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue());
            }
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue());
        }
        return ResponseEntity.ok(productCouponService.updateCoupon(request));
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
            ProductCoupon coupon = productCouponService.findById(request.getId());
            if (!userServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketId())) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue()));
            }
            return productCouponService.deleteCoupon(request)
                    .thenApply(voidResult -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        } catch (CouponNotFoundException e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue()));
        }
    }
}
