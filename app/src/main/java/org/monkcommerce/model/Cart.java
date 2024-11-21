package org.monkcommerce.model;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Cart {

    // List of items in the cart
    @JsonProperty("items")
    private List<Product> items; 

    // Price before any discount
    private double totalPrice; 

    // Total discount applied
    private double totalDiscount; 

    // Final price post discount
    private double finalPrice; 

    @Override
    public String toString() {
        return "{" +
                "\"updated_cart\": {" +
                "\"items\": " + items.toString() + ", " +
                "\"total_price\": " + totalPrice + ", " +
                "\"total_discount\": " + totalDiscount + ", " +
                "\"final_price\": " + finalPrice + " " +
                "}}";
    }
}
