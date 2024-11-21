package org.monkcommerce.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductWiseCouponDetails extends CouponDetails{

    @JsonProperty("product_id")
    private long productId;

    @JsonProperty("discount")
    private double discount;

    @Override
    public String toString() {
        return "{" +
                "\"Product ID\": " + productId + ", " +
                "\"Discount\": " + discount +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductWiseCouponDetails that = (ProductWiseCouponDetails) o;
        return productId == that.productId && Double.compare(that.discount, discount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, discount);
    }
}
