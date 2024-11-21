package org.monkcommerce.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import org.monkcommerce.model.CartWiseCouponDetails;
import org.monkcommerce.model.CouponDetails;
import org.monkcommerce.model.ProductWiseCouponDetails;
import org.monkcommerce.model.BxGyCouponDetails;;;

public class CouponDetailsDeserialiser extends JsonDeserializer<CouponDetails> {

    @Override
    public CouponDetails deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        //Determine the type based on the presence of specific fields
        if (node.has("threshold") && node.has("discount")) {
            // CartWiseCouponDetails
            return p.getCodec().treeToValue(node, CartWiseCouponDetails.class);
        } else if (node.has("product_id") && node.has("discount")) {
            // ProductWiseCouponDetails
            return p.getCodec().treeToValue(node, ProductWiseCouponDetails.class);
        } else if (node.has("buy_products") && node.has("get_products")) {
            // BxGyCouponDetails
            return p.getCodec().treeToValue(node, BxGyCouponDetails.class);
        } else {
            throw new IllegalArgumentException("Unknown coupon details structure: " + node.toString());
        }
    }
}
