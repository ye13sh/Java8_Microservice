package com.ye13sh.ProductService.Controller;

import com.ye13sh.ProductService.DTO.Cart;
import com.ye13sh.ProductService.DTO.CommonDTO;
import com.ye13sh.ProductService.Model.Product;
import com.ye13sh.ProductService.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    @Autowired
    ProductService service;
    @PostMapping("/insert/product")
    public ResponseEntity<String> insertProduct(@RequestBody CommonDTO dto){
        service.insertProduct(dto);
        return ResponseEntity.ok("Product inserted successfully");
    }
    @GetMapping("/getAll/products")
    public ResponseEntity<Page<Product>> getAllProduct(int numbers){
        return ResponseEntity.ok(service.getAllProduct(numbers));
    }
    @GetMapping("/getProduct/{productName}")
    public ResponseEntity<Product> getProductByName(@PathVariable("productName") String productName){
       return ResponseEntity.ok(service.getProductByName(productName));
    }
    @PutMapping("/update/product/{productName}")
    public ResponseEntity<String> updateProduct(@RequestBody CommonDTO dto,@PathVariable("productName") String productName){
        service.updateProduct(dto,productName);
        return ResponseEntity.ok("Product updated");
    }
//    @PostMapping("/update/cart/product")  // not in use by cart-service
//    public ResponseEntity<String> updateProductFromCart(@RequestBody CommonDTO dto,@RequestParam("productName") String productName){
//        service.updateCartProduct(dto,productName);
//        return ResponseEntity.ok("Updated successfully");
//    }
    @DeleteMapping("/delete/product/{productName}") // url for cart /delete/cart/{productName} still working
    public ResponseEntity<String> deleteProduct(@PathVariable("") String productName){
        service.DeleteProductByName(productName);
        service.deleteCartProduct(productName);
        return ResponseEntity.ok("Product deleted");
    }

    @PostMapping("/insert/cart/{productName}") // URL length or PathVariable has to match in product and cart service
    public ResponseEntity<String> insertCart(@RequestBody CommonDTO dto , @PathVariable("productName") String productName){
        service.insertCart(dto,productName);
        return ResponseEntity.ok("Product inserted to cart");
    }
    @PostMapping("/buyNow/order/{productName}")
    public ResponseEntity<String> buyNowOrder(@RequestBody Cart cart, @PathVariable("productName") String productName){
        service.buyNowOrder(cart,productName);
        service.subProductQuantity(cart, productName);
        return ResponseEntity.ok("Order placed");
    }
}
