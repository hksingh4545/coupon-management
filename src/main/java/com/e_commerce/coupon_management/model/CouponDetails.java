package com.e_commerce.coupon_management.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Map;


public interface CouponDetails {
    double applyDiscount(double cartTotal, List<CartRequest.CartItem> cartItems);

}
