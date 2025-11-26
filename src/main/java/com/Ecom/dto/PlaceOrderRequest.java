package com.Ecom.dto;

import java.util.List;

public class PlaceOrderRequest {

    private Long addressId;  // Shipping address
    private List<Long> cartItemIds;  // Items to include in the order
    private String paymentMethod;    // COD / CARD / UPI
    private String notes;            // Optional order note

    public PlaceOrderRequest() {}

    public PlaceOrderRequest(Long addressId, List<Long> cartItemIds, String paymentMethod, String notes) {
        this.addressId = addressId;
        this.cartItemIds = cartItemIds;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public void setCartItemIds(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
