package com.Ecom.repository;

import com.Ecom.model.HomeAd;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HomeAdRepository extends JpaRepository<HomeAd, Long> {
    List<HomeAd> findByActiveTrue();
    
}
