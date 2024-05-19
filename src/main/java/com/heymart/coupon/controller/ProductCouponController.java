package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import com.heymart.coupon.service.coupon.ProductCouponOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/product-coupon")
public class ProductCouponController implements CouponOperations{
    private final AuthServiceClient authServiceClient;

    private final CouponService<ProductCoupon> productCouponService;

    private final ProductCouponOperation productCouponOperation;

    @Autowired
    public ProductCouponController(
            AuthServiceClient authServiceClient,
            CouponService<ProductCoupon> productCouponService,
            ProductCouponOperation productCouponOperation
    ) {
        this.authServiceClient = authServiceClient;
        this.productCouponService = productCouponService;
        this.productCouponOperation = productCouponOperation;
    }

    @Override
    public CompletableFuture<ResponseEntity<Object>> createCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:create", authorizationHeader)) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
        }
        if (!authServiceClient.verifySupermarket(authorizationHeader, request.getSupermarketName())) {
            System.out.println(request.getSupermarketName());
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
        }
        try {
            productCouponOperation.findByIdProduct(request.getIdProduct());
        } catch (Exception e) {
            if (e.getMessage().equals("Coupon not found")) {
                return productCouponService.createCoupon(request)
                        .thenApply(ResponseEntity::ok);
            }
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.CONFLICT).body("Coupon Already Exist"));
    }
    @Override
    public ResponseEntity<Object> findAll(
            String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:read", authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(productCouponService.findAllCoupons());
    }


    @Override
    public ResponseEntity<Object> findById(
            String authorizationHeader, String id
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:read", authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {
            ProductCoupon coupon = productCouponService.findById(id);
            return ResponseEntity.ok(coupon);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found");
        }

    }
    @GetMapping("/id-product/{idProduct}")
    public ResponseEntity<Object> findByIdProduct(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("idProduct") String idProduct
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:read", authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {
            ProductCoupon coupon = productCouponOperation.findByIdProduct(idProduct);
            return ResponseEntity.ok(coupon);
        } catch (Exception e) {
            System.out.println(e);
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
        return ResponseEntity.ok(productCouponService.findBySupermarketName(supermarketName));
    }

    @Override
    public CompletableFuture<ResponseEntity<Object>> updateCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization("coupon:update", authorizationHeader)) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
        }
        try {
            ProductCoupon coupon = productCouponService.findById(request.getId());
            if (!authServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketName())) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found"));
        }
        return productCouponService.updateCoupon(request)
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
            ProductCoupon coupon = productCouponService.findById(request.getId());
            if (!authServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketName())) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
            }
            return productCouponService.deleteCoupon(request)
                    .thenApply(voidResult -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found"));
        }
    }

}
