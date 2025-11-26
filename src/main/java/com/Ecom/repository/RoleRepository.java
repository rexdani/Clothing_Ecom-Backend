package com.Ecom.repository;

import com.Ecom.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Find role by name
    Optional<Role> findByName(String name);
}
