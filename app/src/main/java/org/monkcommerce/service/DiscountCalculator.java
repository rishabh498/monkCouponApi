package org.monkcommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.monkcommerce.model.BxGyCouponDetails;
import org.monkcommerce.model.Cart;
import org.monkcommerce.model.CartWiseCouponDetails;
import org.monkcommerce.model.Coupon;
import org.monkcommerce.model.Product;
import org.monkcommerce.model.ProductQuantity;
import org.monkcommerce.model.ProductWiseCouponDetails;
import org.springframework.stereotype.Service;

@Service
public class DiscountCalculator {

    public double calculateDiscount(Coupon coupon, Cart cart) {
        switch (coupon.getType()) {
            case CART_WISE :
                return calculateCartWiseDiscount(coupon, cart);
            case PRODUCT_WISE :
                return calculateProductWiseDiscount(coupon, cart);
            case BXGY :
                return calculateBxGyDiscount(coupon, cart);
            default:
                throw new IllegalArgumentException("Unsupported coupon type: " + coupon.getType());
        }
    }

    protected double calculateCartWiseDiscount(Coupon coupon, Cart cart) {
        CartWiseCouponDetails details = (CartWiseCouponDetails) coupon.getDetails();
        double threshold = details.getThreshold();
        double discount = details.getDiscount();

        if (cart.getTotalPrice() > threshold) {
            double totalDiscount = cart.getTotalPrice() * (discount / 100);
            cart.setTotalDiscount(totalDiscount);
            return totalDiscount;
        }
        return cart.getTotalDiscount();
    }

    protected double calculateProductWiseDiscount(Coupon coupon, Cart cart) {

        ProductWiseCouponDetails details = (ProductWiseCouponDetails) coupon.getDetails();
        long productId = details.getProductId();
        double discount = details.getDiscount();

        double totalDiscount = 0;
        for (Product item : cart.getItems()) {
            if (item.getProductId() == productId) {
                double itemDiscount = item.getPrice() * item.getQuantity() * (discount / 100);
                item.setTotalDiscount(itemDiscount);
                totalDiscount += itemDiscount;
            }
        }
        cart.setTotalDiscount(totalDiscount);
        return totalDiscount;
    }

    protected double calculateBxGyDiscount(Coupon coupon, Cart cart) {
        BxGyCouponDetails details = (BxGyCouponDetails) coupon.getDetails();
        List<ProductQuantity> buyProducts = details.getBuyProducts();
        List<ProductQuantity> getProducts = details.getGetProducts();
        int repetitionLimit = details.getRepetitionLimit();

        // Map to store cart items by productId
        Map<Long, Integer> cartProductCounts = new HashMap<>();
        for (Product item : cart.getItems()) {
            cartProductCounts.put(item.getProductId(), cartProductCounts.getOrDefault(item.getProductId(), 0) + item.getQuantity());
        }

        // Calculate how many times the coupon can be applied based on buy products (minimum)
        int applyTimes = Integer.MAX_VALUE;
        for (ProductQuantity buyProduct : buyProducts) {
            int availableEligibleBuyQty = cartProductCounts.getOrDefault(buyProduct.getProductId(), 0) / buyProduct.getQuantity();
            applyTimes = Math.min(applyTimes, availableEligibleBuyQty);
        }

        // Apply repetition limit
        applyTimes = Math.min(applyTimes, repetitionLimit);

        // Apply the get products (free items)
        List<Product> updatedItems = new ArrayList<>(cart.getItems());
        double totalDiscount = 0.0;

        for (ProductQuantity getProduct : getProducts) {
            int freeProductQty = applyTimes * getProduct.getQuantity();
            for (Product item : updatedItems) {
                if (item.getProductId() == getProduct.getProductId()) {
                    //Check how much quantity of the free item is added to the cart.
                    int availableFreeProductQuantityInCart = item.getQuantity();

                    ///If free product quantity in cart is less than eligilbe free product quantity, 
                    ///apply discount for quantity in the cart(basically making the entire item free), maybe ask user to add some more.
                    if(freeProductQty >= availableFreeProductQuantityInCart){
                        totalDiscount += availableFreeProductQuantityInCart * item.getPrice();
                    }
                    ///Only deduct the free ones
                    else{
                        totalDiscount += freeProductQty * item.getPrice();
                    }
                }
            }
        }

        // Calculate the updated cart totals
        double totalPrice = updatedItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        double finalPrice = totalPrice - totalDiscount;

        // Return the updated cart
        cart.setItems(updatedItems);
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscount(totalDiscount);
        cart.setFinalPrice(finalPrice);

        return cart.getTotalDiscount();
    }

}
