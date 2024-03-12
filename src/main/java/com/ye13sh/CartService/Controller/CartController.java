package com.ye13sh.CartService.Controller;

import com.ye13sh.CartService.DTO.CommonDTO;
import com.ye13sh.CartService.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CartController {
    @Autowired
    CartService service;
    @PostMapping("/insert/cart/{productName}")
    public ResponseEntity<String> insertCart(@RequestBody CommonDTO dto, @PathVariable("productName") String productName){
        service.insertCart(dto,productName);
        return ResponseEntity.ok("Product added to cart");
    }
    @GetMapping("/cart/products")
    public ResponseEntity<String> getAllCart(){
        service.getAllCartProducts();
        return ResponseEntity.ok("All cart products");
    }
    @PutMapping("/update/cart/{productName}")
    public ResponseEntity<String> updateCart(@RequestBody CommonDTO dto,@PathVariable("productName") String productName){
        service.updateCart(dto,productName);
        return ResponseEntity.ok("Cart updated");
    }
    @PutMapping("/update/cartProduct/{productName}")
    public ResponseEntity<String> updateCartProduct(@RequestBody CommonDTO dto,@PathVariable("productName") String productName){
        service.updateCartProduct(dto,productName);
        return ResponseEntity.ok("Cart updated");
    }
    @DeleteMapping("/delete/cart/{productName}")
    public ResponseEntity<String> deleteCartProduct(@PathVariable("productName")String productName){
        service.deleteCartItem(productName);
        return ResponseEntity.ok("deleted cart product");
    }
    @PostMapping("/cart/order") //@RequestBody List<String> productName
    public ResponseEntity<String> cartOrder(){
        service.cartBuyOrder();
        //service.subProductQuantity(productName);
        service.deleteCart();
        return ResponseEntity.ok("Cart order placed");
    }
}
