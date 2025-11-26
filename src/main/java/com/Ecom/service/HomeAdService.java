package com.Ecom.service;

import com.Ecom.model.HomeAd;
import com.Ecom.repository.HomeAdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeAdService {

    @Autowired
    private HomeAdRepository repo;

    // Save ad (image stored as BLOB)
    public HomeAd save(HomeAd ad) {
        return repo.save(ad);
    }

    // Fetch only active ads + safely attach Base64
    public List<HomeAd> getActiveAds() {
        List<HomeAd> ads = repo.findByActiveTrue();

        // Very important: create copies to avoid modifying JPA managed entities
        return ads.stream().map(ad -> {
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
        }).collect(Collectors.toList());
    }
}
