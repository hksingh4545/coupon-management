package com.e_commerce.coupon_management.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


public class ApplicableCouponsResponse {
    @JsonProperty("applicable_coupons")
    private List<CouponResponse> applicableCoupons;

    public ApplicableCouponsResponse(List<CouponResponse> applicableCoupons) {
        this.applicableCoupons = applicableCoupons;
    }

    public List<CouponResponse> getApplicableCoupons() {
        return applicableCoupons;
    }

    public void setApplicableCoupons(List<CouponResponse> applicableCoupons) {
        this.applicableCoupons = applicableCoupons;
    }

    public static class CouponResponse {
        @JsonProperty("coupon_id")
        private Long couponId;

        @JsonProperty("type")
        private String type;

        @JsonProperty("discount")
        private double discount;

        public CouponResponse(Long couponId, String type, double discount) {
            this.couponId = couponId;
            this.type = type;
            this.discount = discount;
        }

        // Getters and Setters
        public Long getCouponId() {
            return couponId;
        }

        public void setCouponId(Long couponId) {
            this.couponId = couponId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }
    }
}
