package com.Ecom.dto;

public class PlaceOrderRequest {

    private Long addressId;       // Selected shipping address
    private String paymentMethod; // COD / RAZORPAY / CARD / UPI

    public PlaceOrderRequest() {}

    public PlaceOrderRequest(Long addressId, String paymentMethod) {
        this.addressId = addressId;
        this.paymentMethod = paymentMethod;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
