package org.monkcommerce.model;

import org.monkcommerce.util.CouponDetailsDeserialiser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
public class Coupon {

    //ID of the coupon
    private int id;

    //cart-wise, product-wise, bxgy (so far)
    @JsonProperty("type")
    private CouponType type;

    // JSON string converted to type-specific details
    @JsonProperty("details")
    @JsonDeserialize(using = CouponDetailsDeserialiser.class)
    private CouponDetails details; 

    @Override
    public String toString() {
        return "{" +
                "\"id\": " + id + ", " +
                "\"type\": \"" + type.toString() + "\", " +
                "\"details\": " + details.toString() +
                "}";
    }
}
