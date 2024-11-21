package org.monkcommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.monkcommerce.model.CartWrapper;
import org.monkcommerce.model.Coupon;
import org.monkcommerce.model.CouponChoices;
import org.monkcommerce.service.CouponHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponHandler couponHandler;

    public CouponController(CouponHandler couponHandler) {
        this.couponHandler = couponHandler;
    }

    //Coupon Creation
    @PostMapping
    public ResponseEntity<String> createCoupon(@RequestBody Coupon coupon) {
        Coupon couponInit = couponHandler.createCoupon(coupon);
        if(couponInit==null){
            return ResponseEntity.badRequest().body("Invalid coupon data");
        }
        //maybe add a few more checks about duplicate coupon addition attempts, or some internal errors. 
        System.out.println("Coupon stored: "+couponInit.toString() );
        return ResponseEntity.ok("Coupon created successfully "+couponInit.toString());
    }

    //Read all Coupons
    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return ResponseEntity.ok(couponHandler.getAllCoupons());
    }


    //Read Coupon with specific ID
    @GetMapping("/{id}")
    public ResponseEntity<String> getCoupon(@PathVariable int id) {
        Coupon coupon = couponHandler.getCoupon(id);
        if(coupon==null){
            return ResponseEntity.badRequest().body("No Coupon exists with ID: " + id);
        }
        
        return ResponseEntity.ok(coupon.toString());
    }

    //Delete Coupons with specific ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable int id) {
        Coupon coupon = couponHandler.deleteCoupon(id);
        if(coupon==null){
            return ResponseEntity.badRequest().body("Coupon doesn't exist.");
        }
        //maybe add a few more checks about duplicate coupon addition attempts, or some internal errors. 
        return ResponseEntity.ok("Coupon deleted successfully");
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> updateCoupon(@PathVariable int id, @RequestBody Coupon updatedCoupon) {
        Coupon coupon = couponHandler.updateCoupon(id, updatedCoupon);
        if(coupon ==null){
            return ResponseEntity.badRequest().body("Coupon doesn't exist.");
        }
        //maybe add a few more checks about duplicate coupon addition attempts, or some internal errors. 

        return ResponseEntity.ok(coupon.toString() + " has been updated");
    }

    //Find all applicable coupons
    @PostMapping("/applicable-coupons")
    public ResponseEntity<String> findAllCoupons(@RequestBody CartWrapper cartWrapper){

        System.out.printf("Received Cart: " + cartWrapper.toString());

        List<CouponChoices>  applicableList = couponHandler.getApplicableCoupons(cartWrapper.getCart());

        if (applicableList.isEmpty()) {
        System.out.println("No applicables coupon found.");
        return ResponseEntity.ok("{\"applicable_coupons\": []}");  // If no coupons found, return empty array
        }

        // Create a map to structure the response
        Map<String, Object> response = new HashMap<>();
        response.put("applicable_coupons", applicableList);

        // Use ObjectMapper to convert the map to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonResponse = objectMapper.writeValueAsString(response);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\": \"Internal Server Error\"}");
        }

    }

    //Apply Coupon with specific ID
    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<String> calculateDiscount(@PathVariable int id, @RequestBody CartWrapper cartWrapper) {
        return ResponseEntity.ok(couponHandler.calculateDiscount(id, cartWrapper.getCart()).toString());
    }
}
