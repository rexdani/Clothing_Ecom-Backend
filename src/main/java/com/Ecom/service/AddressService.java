package com.Ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.Address;
import com.Ecom.model.User;
import com.Ecom.repository.AddressRepository;
import com.Ecom.repository.UserRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private UserRepository userRepo;

    // Get all addresses for logged in user
    public List<Address> getUserAddresses(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return addressRepo.findByUser(user);
    }

    // Add address
    public Address addAddress(String email, Address addr) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        addr.setUser(user);
        return addressRepo.save(addr);
    }

    // Update address
    public Address updateAddress(String email, Long id, Address newData) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address addr = addressRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Ownership check
        if (!addr.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Access denied");
        }

        addr.setFullName(newData.getFullName());
        addr.setPhoneNumber(newData.getPhoneNumber());
        addr.setStreet(newData.getStreet());
        addr.setCity(newData.getCity());
        addr.setState(newData.getState());
        addr.setCountry(newData.getCountry());
        addr.setPostalCode(newData.getPostalCode());

        return addressRepo.save(addr);
    }

    // Delete address
    public void deleteAddress(String email, Long id) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address addr = addressRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!addr.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Access denied");
        }

        addressRepo.delete(addr);
    }
}
