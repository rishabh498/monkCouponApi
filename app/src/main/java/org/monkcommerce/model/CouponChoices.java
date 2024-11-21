package org.monkcommerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CouponChoices {

    @JsonProperty("coupon")
    private Coupon coupon;

    @JsonProperty("discount_applied")
    private Double amountSaved;

    @Override
    public String toString() {
        return "{" +
                "\"coupon_id\" : " + coupon.getId() + "," +
                "\"type\" : \"" + coupon.getType() + "\"," +
                "\"discount\" : " + amountSaved +
                "}";
    }

    public CouponChoices(Coupon coupon, double amountSaved) {
        this.amountSaved = amountSaved;
        this.coupon = coupon;
    }

}
