package com.Ecom.controller;

import com.Ecom.model.HomeAd;
import com.Ecom.service.HomeAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home-ads")
@CrossOrigin("*")
public class HomeAdController {

    @Autowired
    private HomeAdService service;

    @PostMapping
    public ResponseEntity<HomeAd> createAd(@RequestBody HomeAd ad) {
        return ResponseEntity.ok(service.save(ad));
    }

    @GetMapping
    public ResponseEntity<List<HomeAd>> getActiveAds() {
        return ResponseEntity.ok(service.getActiveAds());
    }
}
