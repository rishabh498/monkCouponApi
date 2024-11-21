package org.monkcommerce.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BxGyCouponDetails extends CouponDetails{

    @JsonProperty("buy_products")
    private List<ProductQuantity> buyProducts;

    @JsonProperty("get_products")
    private List<ProductQuantity> getProducts;

    @JsonProperty("repition_limit")
    private int repetitionLimit;

    @Override
    public String toString() {
        return "{" +
                "\"buyProducts\": " + buyProducts + ", " +
                "\"getProducts\": " + getProducts + ", " +
                "\"repetitionLimit\": " + repetitionLimit + 
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BxGyCouponDetails that = (BxGyCouponDetails) o;
        return repetitionLimit == that.repetitionLimit &&
                Objects.equals(buyProducts, that.buyProducts) &&
                Objects.equals(getProducts, that.getProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyProducts, getProducts, repetitionLimit);
    }
}
