package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.CouponAction;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import com.heymart.coupon.service.coupon.ProductCouponOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (!authServiceClient.verifyUserAuthorization(
                CouponAction.CREATE.getValue(), authorizationHeader)
        ) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue()));
        }
        if (!authServiceClient.verifySupermarket(authorizationHeader, request.getSupermarketName())) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue()));
        }
        try {
            productCouponOperation.findByIdProduct(request.getIdProduct());
        } catch (Exception e) {
            if (e.getMessage().equals(ErrorStatus.COUPON_NOT_FOUND.getValue())) {
                return productCouponService.createCoupon(request)
                        .thenApply(ResponseEntity::ok);
            }
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorStatus.COUPON_ALREADY_EXIST.getValue()));
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        return ResponseEntity.ok(productCouponService.findBySupermarketName(supermarketName));
    }

    @Override
    public CompletableFuture<ResponseEntity<Object>> updateCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        if (!authServiceClient.verifyUserAuthorization(
                CouponAction.UPDATE.getValue(), authorizationHeader)
        ) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue()));
        }
        try {
            ProductCoupon coupon = productCouponService.findById(request.getId());
            if (!authServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketName())) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue()));
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue()));
        }
        return productCouponService.updateCoupon(request)
                .thenApply(ResponseEntity::ok);
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
            if (!authServiceClient.verifySupermarket(authorizationHeader, coupon.getSupermarketName())) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorStatus.UNAUTHORIZED.getValue()));
            }
            return productCouponService.deleteCoupon(request)
                    .thenApply(voidResult -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorStatus.COUPON_NOT_FOUND.getValue()));
        }
    }
}
