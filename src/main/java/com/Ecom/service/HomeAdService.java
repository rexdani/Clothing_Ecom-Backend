package com.Ecom.service;

import com.Ecom.model.HomeAd;
import com.Ecom.repository.HomeAdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeAdService {

    @Autowired
    private HomeAdRepository repo;

    // Save ad (converts base64 to byte[] if provided)
    public HomeAd save(HomeAd ad) {
        // If imageBase64 is provided, convert to byte[]
        if (ad.getImageBase64() != null && !ad.getImageBase64().isEmpty()) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(ad.getImageBase64());
                ad.setImage(imageBytes);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid base64 image data", e);
            }
        }
        return repo.save(ad);
    }

    // Update ad by ID
    public HomeAd update(Long id, HomeAd ad) {
        Optional<HomeAd> existingOpt = repo.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("HomeAd not found with id: " + id);
        }
        
        HomeAd existing = existingOpt.get();
        existing.setTitle(ad.getTitle());
        existing.setType(ad.getType());
        existing.setRedirectUrl(ad.getRedirectUrl());
        existing.setActive(ad.isActive());
        
        // Only update image if new base64 is provided
        if (ad.getImageBase64() != null && !ad.getImageBase64().isEmpty()) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(ad.getImageBase64());
                existing.setImage(imageBytes);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid base64 image data", e);
            }
        }
        
        return repo.save(existing);
    }

    // Find by ID with base64
    public HomeAd findById(Long id) {
        Optional<HomeAd> adOpt = repo.findById(id);
        if (adOpt.isEmpty()) {
            throw new RuntimeException("HomeAd not found with id: " + id);
        }
        
        HomeAd ad = adOpt.get();
        return addBase64ToAd(ad);
    }

    // Find all ads (for admin) with base64
    public List<HomeAd> findAll() {
        List<HomeAd> ads = repo.findAll();
        return ads.stream()
                .map(this::addBase64ToAd)
                .collect(Collectors.toList());
    }

    // Fetch only active ads + safely attach Base64 (for public API)
    public List<HomeAd> getActiveAds() {
        List<HomeAd> ads = repo.findByActiveTrue();
        return ads.stream()
                .map(this::addBase64ToAd)
                .collect(Collectors.toList());
    }

    // Delete ad by ID
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("HomeAd not found with id: " + id);
        }
        repo.deleteById(id);
    }

    // Get raw image bytes by ID (for image endpoint)
    public byte[] getImageById(Long id) {
        Optional<HomeAd> adOpt = repo.findById(id);
        if (adOpt.isEmpty()) {
            return null;
        }
        return adOpt.get().getImage();
    }

    // Helper method to add base64 to ad (creates copy to avoid modifying JPA entity)
    private HomeAd addBase64ToAd(HomeAd ad) {
        HomeAd copy = HomeAd.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .type(ad.getType())
                .redirectUrl(ad.getRedirectUrl())
                .active(ad.isActive())
                .build();

        if (ad.getImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(ad.getImage());
            copy.setImageBase64(base64);
        }

        return copy;
    }
}

