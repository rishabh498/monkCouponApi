package org.monkcommerce.service;

import org.monkcommerce.model.BxGyCouponDetails;
import org.monkcommerce.model.Cart;
import org.monkcommerce.model.CartWiseCouponDetails;
import org.monkcommerce.model.Coupon;
import org.monkcommerce.model.CouponChoices;

import org.monkcommerce.model.Product;
import org.monkcommerce.model.ProductQuantity;
import org.monkcommerce.model.ProductWiseCouponDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CouponHandler {

    private static final Map<Integer,Coupon> couponList = new ConcurrentHashMap<>();
    private final DiscountCalculator discountCalculator;
    private static AtomicInteger couponKey = new AtomicInteger();

    public CouponHandler(DiscountCalculator discountCalculator) {
        this.discountCalculator = discountCalculator;
    }

    public Coupon createCoupon(Coupon coupon) {
        // Assuming that the coupon object has been validated and all required data is passed

        //Set coupon ID
        int uniqueCouponKey = couponKey.getAndIncrement();
        coupon.setId(uniqueCouponKey);
        
        couponList.put(uniqueCouponKey, coupon);
        return coupon;
    }

    // @SuppressWarnings("unchecked")
    // private CouponDetails createCouponDetails(CouponType couponType, Map<String, Object> details){

    //     CouponDetails couponDetails = null;
    //     switch (couponType) {
    //         case CART_WISE :

    //             couponDetails = new CartWiseCouponDetails();
    //             Object thresholdValue = details.get("threshold");
    //             Object discountValue = details.get("discount");

    //             ((CartWiseCouponDetails) couponDetails).setThreshold(((Number) thresholdValue).doubleValue());
    //             ((CartWiseCouponDetails) couponDetails).setDiscount(((Number) discountValue).doubleValue());

    //             break;

    //         case PRODUCT_WISE :

    //             couponDetails = new ProductWiseCouponDetails();
    //             Object productIdValue = details.get("product_id");
    //             Object productDiscountValue = details.get("discount");

    //             ((ProductWiseCouponDetails) couponDetails).setProductId(((Number) productIdValue).longValue());
    //             ((ProductWiseCouponDetails) couponDetails).setDiscount(((Number) productDiscountValue).doubleValue());
    //             break;

    //         case BXGY :
    //             couponDetails = new BxGyCouponDetails();
    //             List<Map<String, Object>> buyProductsList = (List<Map<String, Object>>) details.get("buy_products");
    //             List<ProductQuantity> buyProducts = new ArrayList<>();
    //             for (Map<String, Object> buyProduct : buyProductsList) {

    //                 Object bxGyproductIdValue = buyProduct.get("product_id");
    //                 Object bxGyproductQuantityValue = buyProduct.get("quantity");

    //                 buyProducts.add(new ProductQuantity(((Number) bxGyproductIdValue).longValue(), ((Number) bxGyproductQuantityValue).intValue()));
    //             }
    //             ((BxGyCouponDetails) couponDetails).setBuyProducts(buyProducts);

    //             List<Map<String, Object>> getProductsList = (List<Map<String, Object>>) details.get("get_products");
    //             List<ProductQuantity> getProducts = new ArrayList<>();
    //             for (Map<String, Object> getProduct : getProductsList) {

    //                 Object bxGyproductIdValue = getProduct.get("product_id");
    //                 Object bxGyproductQuantityValue = getProduct.get("quantity");

    //                 getProducts.add(new ProductQuantity(((Number) bxGyproductIdValue).longValue(), ((Number) bxGyproductQuantityValue).intValue()));
    //             }
    //             ((BxGyCouponDetails) couponDetails).setGetProducts(getProducts);

    //             ((BxGyCouponDetails) couponDetails).setRepetitionLimit((Integer) details.get("repition_limit"));
    //             break;

    //         default:
    //             throw new IllegalArgumentException("Unknown coupon type");
    //     }

    //     return couponDetails;
    // }

    public List<Coupon> getAllCoupons() {

        List<Coupon> coupons = new ArrayList<>();
        for (Map.Entry<Integer, Coupon> entry : couponList.entrySet()) {
            coupons.add(entry.getValue());
        }
        return coupons;
    }

    public Coupon getCoupon(int id) {
        if(couponList.get(id)==null){
            return null;
        }
        return couponList.get(id);
    }

    public Coupon deleteCoupon(int id) {
        
        if(couponList.get(id)==null){
            return null;
        }

        return couponList.remove(id);
    }

    public Coupon updateCoupon(int id, Coupon coupon) {
        
        Coupon oldCoupon = couponList.get(id);
        if(oldCoupon==null){
            return null;
        }

        return couponList.put(id, coupon);

    }

    public Cart calculateDiscount(int couponId, Cart cart) {

        Coupon coupon = getCoupon(couponId);
        
        //Complete Buy List
        List<Product> buyProductsList = cart.getItems();

        //Initialize cart total cost
        double cartRunningCost = 0;

        //Run through with each item in the payload and create a product to add to cart. 
        for (Product buyProduct : buyProductsList) {
            
            cartRunningCost += buyProduct.getQuantity()*buyProduct.getPrice();
        }

        //Set the total cost of the cart without discount
        cart.setTotalPrice(cartRunningCost);

        System.out.println("Cart's total cost is :" + cart.getTotalPrice());

        if(coupon==null){
            System.out.println(("No discounts can be applied, as coupon doesn't exist"));    
            //Set the total cost of the cart without discount
            cart.setFinalPrice(cartRunningCost);
            return cart;
        }


        System.out.println("Applying coupon : "+coupon.toString());

        discountCalculator.calculateDiscount(coupon, cart);

        cart.setFinalPrice(cart.getTotalPrice()-cart.getTotalDiscount());

        return cart;
    }

    // Get applicable coupons for a cart
    public List<CouponChoices> getApplicableCoupons(Cart cart) {

        //Assuming cart payload will have a structure like buy_products : [{productId : 112, quantity : 5, price: 60}, {productId : 101, quantity : 8, price : 90}] and 
        //our instore memory or any db can fetch the price of the product if it is not in the payload 
    
        //Complete Buy List
        List<Product> buyProductsList = cart.getItems();

        // System.out.println(buyProductsList);

        //Initialize cart total cost
        double cartRunningCost = 0;

        //Run through with each item in the payload and create a product to add to cart. 
        for (Product buyProduct : buyProductsList) {
            cartRunningCost += buyProduct.getQuantity()*buyProduct.getPrice();
        }

        //Set the total cost of the cart without discount
        cart.setTotalPrice(cartRunningCost);

        System.out.println("Cart's total cost is :" + cart.getTotalPrice());

        List<CouponChoices> applicableCoupons = new ArrayList<>();

        System.out.println("Finding all eligible coupons");

        for (Coupon coupon : getAllCoupons()) {
            switch (coupon.getType()) {
                case CART_WISE:
                    if (cart.getTotalPrice() > ((CartWiseCouponDetails) coupon.getDetails()).getThreshold()) {
                        discountCalculator.calculateCartWiseDiscount(coupon, cart);
                        applicableCoupons.add(new CouponChoices(coupon, cart.getTotalDiscount()));
                    }
                    break;
                case PRODUCT_WISE:
                    long productId = ((ProductWiseCouponDetails) coupon.getDetails()).getProductId();
                    for (Product item : cart.getItems()) {
                        if (item.getProductId() == productId) {
                            discountCalculator.calculateProductWiseDiscount(coupon, cart);
                            applicableCoupons.add(new CouponChoices(coupon,cart.getTotalDiscount()));
                            break;
                        }
                    }
                    break;
                case BXGY:
                    if (isBxGyCouponApplicable(coupon, cart)) {
                        discountCalculator.calculateBxGyDiscount(coupon, cart);
                        applicableCoupons.add(new CouponChoices(coupon,cart.getTotalDiscount()));
                    }
                    break;
            }
        }

        return applicableCoupons;
    }

    private boolean isBxGyCouponApplicable(Coupon coupon, Cart cart) {
        List<ProductQuantity> buyProducts = ((BxGyCouponDetails)coupon.getDetails()).getBuyProducts();
        Map<Long, Integer> cartProductCounts = new HashMap<>();
        for (Product item : cart.getItems()) {
            cartProductCounts.put(item.getProductId(), cartProductCounts.getOrDefault(item.getProductId(), 0) + item.getQuantity());
        }

        // Check if there are enough products from the buy list
        for (ProductQuantity buyProduct : buyProducts) {
            
            int cartQuantity = cartProductCounts.getOrDefault(buyProduct.getProductId(), 0);
            if (cartQuantity < buyProduct.getQuantity()) {
                return false; 
            }
        }

        return true;
    }

}