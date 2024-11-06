package com.e_commerce.coupon_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

class ProductDiscount {
    @JsonProperty("product_id")
    int productId = 0 ;
    @JsonProperty("discount")
    int quantity = 0 ;

    public ProductDiscount(int quantity, int productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}


public class BxgyCoupon implements CouponDetails{
    @JsonProperty("buy_products")
        List<ProductDiscount> buyProducts ;
    @JsonProperty("get_products")
        List<ProductDiscount> getProducts ;
    @JsonProperty("repition_limit")
        int repitionLimit = 0;

    public BxgyCoupon(List<ProductDiscount> buyProducts, List<ProductDiscount> getProducts, int repitionLimit) {
        this.buyProducts = buyProducts;
        this.getProducts = getProducts;
        this.repitionLimit = repitionLimit;
    }

    public List<ProductDiscount> getBuyProducts() {
        return buyProducts;
    }

    public void setBuyProducts(List<ProductDiscount> buyProducts) {
        this.buyProducts = buyProducts;
    }

    public List<ProductDiscount> getGetProducts() {
        return getProducts;
    }

    public void setGetProducts(List<ProductDiscount> getProducts) {
        this.getProducts = getProducts;
    }

    public int getRepitionLimit() {
        return repitionLimit;
    }

    public void setRepitionLimit(int repitionLimit) {
        this.repitionLimit = repitionLimit;
    }

    @Override
    public double applyDiscount(double cartTotal, List<CartRequest.CartItem> cartItems) {
        int applicableRepetitions = Math.min(repitionLimit, calculateApplicableRepetitions(cartItems));
        double discountAmount = 0;

        for (int i = 0; i < applicableRepetitions; i++) {
            for (ProductDiscount getProduct : getProducts) {
                for (CartRequest.CartItem item : cartItems) {
                    if (item.getProductId() == getProduct.getProductId()) {
                        discountAmount += item.getPrice() * getProduct.getQuantity();
                        break;
                    }
                }
            }
        }

        return discountAmount;
    }

    private int calculateApplicableRepetitions(List<CartRequest.CartItem> cartItems) {
        int maxRepetitions = Integer.MAX_VALUE;

        for (ProductDiscount buyProduct : buyProducts) {
            for (CartRequest.CartItem item : cartItems) {
                if (item.getProductId() == buyProduct.getProductId()) {
                    int possibleRepetitions = item.getQuantity() / buyProduct.getQuantity();
                    maxRepetitions = Math.min(maxRepetitions, possibleRepetitions);
                    break;
                }
            }
        }

        return maxRepetitions;
    }
}
