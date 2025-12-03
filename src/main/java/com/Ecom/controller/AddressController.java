package com.Ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Ecom.model.Address;
import com.Ecom.service.AddressService;

@RestController
@RequestMapping("/address")
@CrossOrigin(origins = "*")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // GET all addresses for user
    @GetMapping
    public ResponseEntity<List<Address>> getAddresses(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(addressService.getUserAddresses(email));
    }

    // ADD new address
    @PostMapping
    public ResponseEntity<Address> addAddress(
            Authentication auth,
            @RequestBody Address address) {

        String email = auth.getName();
        return ResponseEntity.ok(addressService.addAddress(email, address));
    }

    // UPDATE address
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody Address newData) {

        String email = auth.getName();
        return ResponseEntity.ok(addressService.updateAddress(email, id, newData));
    }

    // DELETE address
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(
            Authentication auth,
            @PathVariable Long id) {

        String email = auth.getName();
        addressService.deleteAddress(email, id);

        return ResponseEntity.ok("Address deleted successfully");
    }
}
