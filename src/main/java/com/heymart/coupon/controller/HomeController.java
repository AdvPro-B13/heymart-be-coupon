package com.heymart.coupon.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("")
public class HomeController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World!";
    }

    @GetMapping("/list/{supermarket}")
    public String couponList(@PathVariable String supermarket) {
        return "Ini isinya list kupon dari " + supermarket + "!";
    }

    @PostMapping("/create/{supermarket}")
    public String createCoupon(@PathVariable String supermarket) {
        return "Form pembuatan kupon untuk supermarket " + supermarket + "!";
    }

}
