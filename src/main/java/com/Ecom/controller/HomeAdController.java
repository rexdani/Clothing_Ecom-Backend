package com.Ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecom.model.HomeAd;
import com.Ecom.service.HomeAdService;

@RestController
@RequestMapping("/home-ads")
@CrossOrigin("*")
public class HomeAdController {

    @Autowired
    private HomeAdService service;

    // Create new ad
    @PostMapping
    public ResponseEntity<HomeAd> createAd(@RequestBody HomeAd ad) {
        return ResponseEntity.ok(service.save(ad));
    }

    // Get all ads (for admin - includes inactive)
    @GetMapping
    public ResponseEntity<List<HomeAd>> getAllAds() {
        return ResponseEntity.ok(service.findAll());
    }

    // Get only active ads (for public API)
    @GetMapping("/active")
    public ResponseEntity<List<HomeAd>> getActiveAds() {
        return ResponseEntity.ok(service.getActiveAds());
    }

    // Get ad by ID
    @GetMapping("/{id}")
    public ResponseEntity<HomeAd> getAdById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Update ad
    @PutMapping("/{id}")
    public ResponseEntity<HomeAd> updateAd(@PathVariable Long id, @RequestBody HomeAd ad) {
        return ResponseEntity.ok(service.update(id, ad));
    }

    // Delete ad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    // Get ad image
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getAdImage(@PathVariable Long id) {
        byte[] image = service.getImageById(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(image);
    }
}

