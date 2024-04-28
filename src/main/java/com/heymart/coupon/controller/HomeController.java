package com.heymart.coupon.controller;
import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.builder.CouponBuilder;
import com.heymart.coupon.model.enums.CouponType;
import com.heymart.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("")
public class HomeController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World!";
    }

    @GetMapping("/list/{supermarket}")
    public String couponList(@PathVariable String supermarket) {
        return "Ini isinya list kupon dari " + supermarket + "!";
    }

    @PostMapping("/create")
    public String createCoupon(@RequestParam("supermarket") String supermarket) {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket(supermarket)
                .setIdProduct("P01")
                .getResult();
        couponService.createCoupon(coupon);
        return "Pembuatan kupon untuk supermarket " + supermarket + "!";
    }

    @PutMapping("/update/{supermarket}")
    public String updateCoupon(@PathVariable String supermarket) {
        return "Update kupon untuk supermarket " + supermarket + "!";
    }
    @DeleteMapping("/delete/{supermarket}")
    public String deleteCoupon(@PathVariable String supermarket) {
        return "Delete kupon untuk supermarket " + supermarket + "!";
    }
}
