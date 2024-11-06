package com.e_commerce.coupon_management.controller;

import com.e_commerce.coupon_management.model.*;
import com.e_commerce.coupon_management.service.DiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/v1/api")
@RestController
public class CouponController {
    DiscountService discountService;

    public CouponController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/coupons")
    void createCoupon(@RequestBody CouponDTO couponDTO) {

        // Process the couponDTO based on its type
        discountService.saveCoupon(couponDTO);
    }

    @GetMapping("/coupons")
    public List<Discount> getAllCoupons() {
       return  discountService.getAllCoupons();
    }

    @GetMapping("/coupons/{id}")
    public ResponseEntity<Discount> getCouponById(@PathVariable Long id) {
        Optional<Discount> coupon = discountService.getCouponById(id);
        return coupon.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/coupons/{id}")
    public String updateCoupon(@PathVariable Long id, @RequestBody CouponDTO couponDTO) {
        boolean updated = discountService.updateCoupon(id, couponDTO);
        if (updated) {
            return "Coupon updated successfully";
        } else {
            return "Coupon not found";
        }
    }
    @DeleteMapping("/coupons/{id}")
    public String deleteCoupon(@PathVariable Long id) {
        boolean deleted = discountService.deleteCoupon(id);
        if (deleted) {
            return "Coupon deleted successfully";
        } else {
            return "Coupon not found";
        }
    }

    @PostMapping("/applicable-coupons")
    public ApplicableCouponsResponse allApplicableCoupon(@RequestBody CartRequest cartRequest) {

        List<CartRequest.CartItem> items = cartRequest.getCart().getItems();

        // Calculate the cart total for all items
        double cartTotal = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Pass the list of CartItem objects to the service to apply applicable discounts
        List<ApplicableCouponsResponse.CouponResponse> applicableCoupons = discountService.getApplicableDiscounts(items);

        // Construct the response object
        ApplicableCouponsResponse response = new ApplicableCouponsResponse(applicableCoupons);

        return response;

    }

    @PostMapping("/apply-coupon/{couponId}")
    public ApplyCouponResponse applyCoupon(
            @PathVariable Long couponId,
            @RequestBody CartRequest cartRequest) {

        return  discountService.applyCoupon(couponId, cartRequest.getCart().getItems());
    }
}
