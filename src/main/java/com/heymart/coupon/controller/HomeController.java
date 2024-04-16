package com.heymart.coupon.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("")
public class HomeController {

    @GetMapping("/{supermarket}")
    public String getPayments(@PathVariable String supermarket) {
        return "Ini isinya list kupon dari " + supermarket + "!";
    }

}
