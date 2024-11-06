package com.e_commerce.coupon_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CouponDTODeserializer.class)
public class CouponDTO {
    @JsonProperty("type")
    String type;
    @JsonProperty("details")
    String details;

    public CouponDTO() {
    }

    public CouponDTO(String type, String details) {
        this.type = type;
        this.details = details;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public String getType() {
        return type;
    }
}
