package com.e_commerce.coupon_management.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ApplyCouponResponse {

    @JsonProperty("updated_cart")
    private UpdatedCart updatedCart;

    public ApplyCouponResponse(UpdatedCart updatedCart) {
        this.updatedCart = updatedCart;
    }

    public UpdatedCart getUpdatedCart() {
        return updatedCart;
    }

    public static class UpdatedCart {
        @JsonProperty("items")
        private List<CartItemResponse> items;

        @JsonProperty("total_price")
        private double totalPrice;

        @JsonProperty("total_discount")
        private double totalDiscount;

        @JsonProperty("final_price")
        private double finalPrice;

        public UpdatedCart(List<CartItemResponse> items, double totalPrice, double totalDiscount, double finalPrice) {
            this.items = items;
            this.totalPrice = totalPrice;
            this.totalDiscount = totalDiscount;
            this.finalPrice = finalPrice;
        }

        // Getters and Setters
    }

    public static class CartItemResponse {
        @JsonProperty("product_id")
        private int productId;

        @JsonProperty("quantity")
        private int quantity;

        @JsonProperty("price")
        private double price;

        @JsonProperty("total_discount")
        private double totalDiscount;

        public CartItemResponse(int productId, int quantity, double price, double totalDiscount) {
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
            this.totalDiscount = totalDiscount;
        }

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

        public double getTotalDiscount() {
            return totalDiscount;
        }

        public void setTotalDiscount(double totalDiscount) {
            this.totalDiscount = totalDiscount;
        }

        // Getters and Setters
    }
}
