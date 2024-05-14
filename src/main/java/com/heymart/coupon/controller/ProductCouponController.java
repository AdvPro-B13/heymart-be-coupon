package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import com.heymart.coupon.service.coupon.ProductCouponServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-coupon")
public class ProductCouponController implements CouponOperations<ProductCoupon>{
    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private CouponService<ProductCoupon> productCouponService;
    @Override
    public ResponseEntity<?> createCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        String token = authServiceClient.getTokenFromHeader(authorizationHeader);
        if (!authServiceClient.verifyUserAuthorization("coupon:create", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        if (!authServiceClient.verifySupermarket(token, request.getSupermarketName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {
            ((ProductCouponServiceImpl) productCouponService).findByIdProduct(request.getIdProduct());
        } catch (RuntimeException e) {
            return ResponseEntity.ok(productCouponService.createCoupon(request));
        }
        return  ResponseEntity.status(HttpStatus.CONFLICT).body("Coupon Already Exist");
    }

    @Override
    public ResponseEntity<?> findAll(
            String authorizationHeader
    ) {
        String token = authServiceClient.getTokenFromHeader(authorizationHeader);
        if (!authServiceClient.verifyUserAuthorization("coupon:read", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(productCouponService.findAllCoupons());
    }


    @Override
    public ResponseEntity<?> findById(
            String authorizationHeader, String id
    ) {
        String token = authServiceClient.getTokenFromHeader(authorizationHeader);
        if (!authServiceClient.verifyUserAuthorization("coupon:read", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {
            ProductCoupon coupon = productCouponService.findById(id);
            return ResponseEntity.ok(coupon);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found");
        }

    }
    @GetMapping("/idProduct/{idProduct}")
    public ResponseEntity<?> findByIdProduct(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable("idProduct") String idProduct
    ) {
        String token = authServiceClient.getTokenFromHeader(authorizationHeader);
        if (!authServiceClient.verifyUserAuthorization("coupon:read", token)) {
            System.out.println(token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        System.out.println("konz");
        try {
            ProductCoupon coupon = ((ProductCouponServiceImpl) productCouponService).findByIdProduct(idProduct);
            return ResponseEntity.ok(coupon);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found");
        }

    }

    @Override
    public ResponseEntity<?> findBySupermarketName(
            String authorizationHeader, String supermarketName
    ) {
        String token = authServiceClient.getTokenFromHeader(authorizationHeader);
        if (!authServiceClient.verifyUserAuthorization("coupon:read", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(productCouponService.findBySupermarketName(supermarketName));
    }

    @Override
    public ResponseEntity<?> updateCoupon(
            CouponRequest request, String authorizationHeader
    ) {
        String token = authServiceClient.getTokenFromHeader(authorizationHeader);
        if (!authServiceClient.verifyUserAuthorization("coupon:update", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {
            ProductCoupon coupon = productCouponService.findById(request.getId());
            if (!authServiceClient.verifySupermarket(token, coupon.getSupermarketName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found");
        }
        return ResponseEntity.ok(productCouponService.updateCoupon(request));
    }

    @Override
    public ResponseEntity<?> deleteCoupon(
            CouponRequest request, String authorizationHeader) {
        String token = authServiceClient.getTokenFromHeader(authorizationHeader);
        if (!authServiceClient.verifyUserAuthorization("coupon:delete", token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {
            ProductCoupon coupon = productCouponService.findById(request.getId());
            if (!authServiceClient.verifySupermarket(token, coupon.getSupermarketName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
