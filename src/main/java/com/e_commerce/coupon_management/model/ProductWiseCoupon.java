package com.e_commerce.coupon_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class ProductWiseCoupon implements CouponDetails{
    @JsonProperty("product_id")
    int productId = 0;
    @JsonProperty("discount")
    int discount = 0;

    public ProductWiseCoupon(int productId, int discount) {
        this.productId = productId;
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public double applyDiscount(double cartTotal, List<CartRequest.CartItem> cartItems) {
        for (CartRequest.CartItem item : cartItems) {
            if (item.getProductId() == productId) {
                double productTotal = item.getQuantity() * item.getPrice();
                return productTotal * ((double) discount / 100);
            }
        }
        return cartTotal;
    }
}
