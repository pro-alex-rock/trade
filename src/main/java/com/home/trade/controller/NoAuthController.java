package com.home.trade.controller;

import com.home.trade.service.MarketService.NoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class NoAuthController {

    @Autowired
    private NoAuthService binanceClient;

    @GetMapping("/price")
    @ResponseBody
    public ResponseEntity<?> getPrice(@RequestParam(value = "code", required = false) String code) {
        if (code == null) {
            return new ResponseEntity<>(binanceClient.priceList(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(binanceClient.priceOne(code), HttpStatus.FOUND);
        }
    }

    @GetMapping("/ticker/price")
    @ResponseBody
    public ResponseEntity<?> markPrice(@RequestParam(value = "code", required = false) String code) {
        if (code == null) {
            return new ResponseEntity<>(binanceClient.markPriceList(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(binanceClient.markPriceOne(code), HttpStatus.FOUND);
        }
    }

    @GetMapping("/ticker/24hr")
    @ResponseBody
    public ResponseEntity<?> changesPrice24(@RequestParam(value = "code", required = false) String code) {
        if (code == null) {
            return new ResponseEntity<>(binanceClient.changesPrice24HrList(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(binanceClient.changesPrice24HrOne(code), HttpStatus.FOUND);
        }
    }

    @GetMapping("/{symbol}")
    @ResponseBody
    public ResponseEntity<?> getOrderBook(@PathVariable("symbol") String symbol) {
        return new ResponseEntity<>(binanceClient.orderBook(symbol), HttpStatus.FOUND);
    }

    @GetMapping("/{symbol}/{interval}")
    @ResponseBody
    public ResponseEntity<?> getCandlestick(@PathVariable("symbol") String symbol,
                                            @PathVariable("interval") String interval) {
        return new ResponseEntity<>(binanceClient.candlestick(symbol, interval, Optional.empty()), HttpStatus.FOUND);
    }

    @GetMapping("/time")
    public ResponseEntity<?> getTime() {
        return new ResponseEntity<>(binanceClient.time(), HttpStatus.FOUND);
    }
}


