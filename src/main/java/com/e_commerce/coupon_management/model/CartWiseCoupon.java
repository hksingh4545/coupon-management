package com.e_commerce.coupon_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class CartWiseCoupon implements CouponDetails {
    @JsonProperty("threshold")
    int threshold;
    @JsonProperty("discount")
    int discount;

    public CartWiseCoupon(int threshold, int discount) {
        this.threshold = threshold;
        this.discount = discount;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public double applyDiscount(double cartTotal, List<CartRequest.CartItem> cartItems) {
        if (cartTotal >= threshold) {
            return (cartTotal * discount / 100);
        }
        return cartTotal;
    }

}
