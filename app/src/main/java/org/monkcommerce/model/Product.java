package org.monkcommerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Product {

    // ID of the product
    @JsonProperty("product_id")
    private Long productId; 

    // Quantity of the product
    @JsonProperty(("quantity"))
    private int quantity; 

    // Price of the product
    @JsonProperty("price")
    private double price; 

    // Discount applied to this item
    private double totalDiscount; 

    @Override
    public String toString() {
        return "{" +
                "\"product_id\": " + productId + ", " +
                "\"quantity\": " + quantity + ", " +
                "\"price\": " + price + ", " + "}";
    }

}
