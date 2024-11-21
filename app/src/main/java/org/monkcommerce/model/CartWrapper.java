package org.monkcommerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CartWrapper {
    
    @JsonProperty("cart")
    private Cart cart;

}