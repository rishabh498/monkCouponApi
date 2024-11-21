package org.monkcommerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductQuantity {

    //ID of the product
    @JsonProperty("product_id")
    private Long productId;

    //Quantity of the product
    @JsonProperty("quantity")
    private Integer quantity;

    public ProductQuantity(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "{" +
                "Product ID:" + productId +
                ", Quantity:" + quantity +
                '}';
    }

}
