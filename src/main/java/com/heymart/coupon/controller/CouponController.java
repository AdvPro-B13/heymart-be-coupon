package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.service.ProductCouponServiceImpl;
import com.heymart.coupon.service.TransactionCouponServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class CouponController {

    @Autowired
    private TransactionCouponServiceImpl transactionCouponService;

    @Autowired
    private ProductCouponServiceImpl productCouponService;

    @PostMapping("/product")
    public ResponseEntity<ProductCoupon> createProductCoupon(@RequestBody CouponRequest request) {
        return ResponseEntity.ok(productCouponService.createCoupon(request));
    }

    @PostMapping("/transaction")
    public ResponseEntity<TransactionCoupon> createTransactionCoupon(@RequestBody CouponRequest request) {
        return ResponseEntity.ok(transactionCouponService.createCoupon(request));
    }

    @PutMapping("/product")
    public ResponseEntity<ProductCoupon> updateProductCoupon(@RequestBody CouponRequest request) {
        return ResponseEntity.ok(productCouponService.updateCoupon(request));
    }

    @PutMapping("/transaction")
    public ResponseEntity<TransactionCoupon> updateTransactionCoupon(@RequestBody CouponRequest request) {
        return ResponseEntity.ok(transactionCouponService.updateCoupon(request));
    }

    @DeleteMapping("/product")
    public ResponseEntity<Void> deleteProductCoupon(@RequestBody CouponRequest request) {
        productCouponService.deleteCoupon(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/transaction")
    public ResponseEntity<Void> deleteTransactionCoupon(@RequestBody CouponRequest request) {
        transactionCouponService.deleteCoupon(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/product")
    public ResponseEntity<List<ProductCoupon>> findAllProductCoupons() {
        return ResponseEntity.ok(productCouponService.findAllCoupons());
    }

    @GetMapping("/transaction")
    public ResponseEntity<List<TransactionCoupon>> findAllTransactionCoupons() {
        return ResponseEntity.ok(transactionCouponService.findAllCoupons());
    }
}
