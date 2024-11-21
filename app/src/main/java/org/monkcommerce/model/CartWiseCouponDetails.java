package org.monkcommerce.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CartWiseCouponDetails extends CouponDetails{
    
    @JsonProperty("threshold")
    private double threshold;

    @JsonProperty("discount")
    private double discount;

    @Override
    public String toString() {
        return "{" +
            "\"threshold\": " + threshold + ", " +
            "\"discount\": " + discount + 
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartWiseCouponDetails that = (CartWiseCouponDetails) o;
        return Double.compare(that.threshold, threshold) == 0 &&
                Double.compare(that.discount, discount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(threshold, discount);
    }
}
