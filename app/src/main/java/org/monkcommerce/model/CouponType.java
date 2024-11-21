package org.monkcommerce.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CouponType {
    CART_WISE("cart-wise"),   
    PRODUCT_WISE("product-wise"),
    BXGY("bxgy");

    private final String description;

    // Constructor
    CouponType(String description) {
        this.description = description;
    }

    // Getter for the description as "-" are not supported in enums. 
    @JsonValue
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

    // Method to get enum from description
    @JsonCreator
    public static CouponType fromDescription(String description) {
        for (CouponType type : CouponType.values()) {
            if (type.getDescription().equalsIgnoreCase(description)) {
                return type;
            }
        }
        
        return null;
    }
}
