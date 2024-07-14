package com.home.trade.controller;

import com.home.trade.service.MarketService.WithAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WithAuthController {


    @Autowired
    private WithAuthService withAuthService;

    @GetMapping("/balance")
    @ResponseBody
    public ResponseEntity<?> getBalance() {
        return new ResponseEntity<>(withAuthService.balance(), HttpStatus.FOUND);
    }

    @GetMapping("/open")
    @ResponseBody
    public ResponseEntity<?> openOrder(@RequestParam(value = "code", defaultValue = "BTCUSDT") String code) {
        return new ResponseEntity<>(withAuthService.openOrder(code), HttpStatus.FOUND);
    }

    @GetMapping("/account")
    @ResponseBody
    public ResponseEntity<?> getAccount() {
        return new ResponseEntity<>(withAuthService.account(), HttpStatus.FOUND);
    }
}
