package com.e_commerce.coupon_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CartRequest {
    @JsonProperty("cart")
    private Cart cart;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public static class Cart {
        @JsonProperty("items")
        private List<CartItem> items;

        public List<CartItem> getItems() {
            return items;
        }

        public void setItems(List<CartItem> items) {
            this.items = items;
        }
    }

    public static class CartItem {
        @JsonProperty("product_id")
        private int productId;

        @JsonProperty("quantity")
        private int quantity;

        @JsonProperty("price")
        private double price;

        // Getters and Setters
        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
