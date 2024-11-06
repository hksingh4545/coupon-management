package com.e_commerce.coupon_management.service;

import com.e_commerce.coupon_management.model.*;
import com.e_commerce.coupon_management.repository.DiscountRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DiscountService(DiscountRepository discountRepository, ObjectMapper objectMapper) {
        this.discountRepository = discountRepository;
        this.objectMapper = objectMapper;
    }

    public void saveCoupon(CouponDTO couponDTO){
        Discount discount = new Discount(null,couponDTO.getType(),couponDTO.getDetails());
        discountRepository.save(discount);
    }

    public List<Discount> getAllCoupons() {
        return discountRepository.findAll();
    }

    public Optional<Discount> getCouponById(Long id) {
        return discountRepository.findById(id);
    }

    public boolean updateCoupon(Long id, CouponDTO couponDTO) {
        Optional<Discount> optionalDiscount = discountRepository.findById(id);
        if (optionalDiscount.isPresent()) {
            Discount discount = optionalDiscount.get();
            discount.setType(couponDTO.getType());
            discount.setDetails(couponDTO.getDetails());
            discountRepository.save(discount);
            return true;
        }
        return false;
    }

    public boolean deleteCoupon(Long id) {
        if (discountRepository.existsById(id)) {
            discountRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ApplicableCouponsResponse.CouponResponse> getApplicableDiscounts(List<CartRequest.CartItem> cartItems) {
        // Calculate cart total
        double cartTotal = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Load discounts from the database
        List<Discount> discounts = discountRepository.findAll();
        List<ApplicableCouponsResponse.CouponResponse> applicableCoupons = new ArrayList<>();

        // Apply each discount rule to the cart
        for (Discount discount : discounts) {
            CouponDetails couponDetails = deserializeCouponDetails(discount.getType(), discount.getDetails());

            // Apply discount to the cart and calculate discount amount
            double discountAmount = couponDetails.applyDiscount(cartTotal, cartItems);
            if (discountAmount > 0) {  // Only add if discount is applicable
                applicableCoupons.add(
                        new ApplicableCouponsResponse.CouponResponse(discount.getId(), discount.getType(), discountAmount)
                );
            }
        }
        return applicableCoupons;
    }
    public ApplyCouponResponse applyCoupon(Long couponId, List<CartRequest.CartItem> cartItems) {
        double cartTotal = calculateCartTotal(cartItems);
        Optional<Discount> discountOpt = discountRepository.findById(couponId);

        if (discountOpt.isEmpty()) {
            throw new IllegalArgumentException("Coupon not found");
        }

        Discount discount = discountOpt.get();
        CouponDetails couponDetails = deserializeCouponDetails(discount.getType(), discount.getDetails());

        // Calculate the total discount applied by the coupon
        double totalDiscount = couponDetails.applyDiscount(cartTotal, cartItems);
        double finalPrice = cartTotal - totalDiscount;

        // Prepare per-item discount details
        List<ApplyCouponResponse.CartItemResponse> itemResponses = new ArrayList<>();
        double totalItemDiscount = 0;

        for (CartRequest.CartItem item : cartItems) {
            double itemDiscount = calculateItemDiscount(couponDetails, item, cartItems); // Calculate item-specific discount

            totalItemDiscount += itemDiscount;

            itemResponses.add(new ApplyCouponResponse.CartItemResponse(
                    item.getProductId(),
                    item.getQuantity(),
                    item.getPrice(),
                    itemDiscount
            ));
        }

        // Construct the updated cart response with all totals
        ApplyCouponResponse.UpdatedCart updatedCart = new ApplyCouponResponse.UpdatedCart(
                itemResponses,
                cartTotal,
                totalItemDiscount,
                finalPrice
        );

        return new ApplyCouponResponse(updatedCart);
    }

    private double calculateItemDiscount(CouponDetails couponDetails, CartRequest.CartItem item, List<CartRequest.CartItem> cartItems) {
        // Handle specific item discounts based on coupon type
        if (couponDetails instanceof ProductWiseCoupon) {
            ProductWiseCoupon productCoupon = (ProductWiseCoupon) couponDetails;
            if (productCoupon.getProductId() == item.getProductId()) {
                return item.getPrice() * item.getQuantity() * (productCoupon.getDiscount() / 100.0);
            }
        } else if (couponDetails instanceof BxgyCoupon) {
            BxgyCoupon bxgyCoupon = (BxgyCoupon) couponDetails;
            return bxgyCoupon.applyDiscount(0, cartItems) / cartItems.size(); // Divide equally among items (or adjust as needed)
        }
        return 0;
    }
    /**
     * Deserializes the JSON details of a discount into the appropriate CouponDetails subclass.
     *
     * @param type        Type of the discount (cart-wise, product-wise, bxgy).
     * @param detailsJson JSON representation of the discount details.
     * @return A CouponDetails instance that can apply the discount logic.
     */
    private CouponDetails deserializeCouponDetails(String type, String detailsJson) {
        try {
            if ("product-wise".equals(type)) {
                return objectMapper.readValue(detailsJson, ProductWiseCoupon.class);
            } else if ("cart-wise".equals(type)) {
                return objectMapper.readValue(detailsJson, CartWiseCoupon.class);
            } else if ("bxgy".equals(type)) {
                return objectMapper.readValue(detailsJson, BxgyCoupon.class);
            }
            throw new IllegalArgumentException("Unknown discount type: " + type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize coupon details", e);
        }
    }

    private double calculateCartTotal(List<CartRequest.CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
