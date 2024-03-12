package com.ye13sh.OrderService.Controller;

import com.ye13sh.OrderService.DTO.CommonDTO;
import com.ye13sh.OrderService.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    OrderService service;
    @PostMapping("/buyNow/order/{productName}")
    public ResponseEntity<String> insertBuyNowOrder(@RequestBody CommonDTO dto, @PathVariable("productName") String productName){
        service.insertBuyNowOrder(dto,productName);
        return ResponseEntity.ok("Order placed");
    }
    @PostMapping("/cart/order")
    public ResponseEntity<String> cartOrder(@RequestBody CommonDTO dto){
        service.insertCartOrder(dto);
        return ResponseEntity.ok("Cart order placed");
    }
    @PutMapping("/order/confirmed/{orderID}")
    public ResponseEntity<String> ConfirmOrder(@PathVariable("orderID") String orderID){
        service.ConfirmOrder(orderID);
        return ResponseEntity.ok("Order confirmed");
    }
    @PutMapping("/order/refund/{orderID}")
    public ResponseEntity<String> refundOrder(@PathVariable("orderID") String orderID){
        service.RefundOrder(orderID);
        return ResponseEntity.ok("Refund process Started");
    }
    @PutMapping("/order/refund/{orderID}")
    public ResponseEntity<String> cancelOrder(@PathVariable("orderID") String orderID){
        service.CancelOrder(orderID);
        return ResponseEntity.ok("Refund process Started");
    }

}
