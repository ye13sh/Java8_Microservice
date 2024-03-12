package com.ye13sh.OrderService.Service;

import com.ye13sh.OrderService.DTO.CommonDTO;
import com.ye13sh.OrderService.Model.Order;
import com.ye13sh.OrderService.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    OrderRepository repository;

    public String generateRandomID(){
        UUID uuid=UUID.randomUUID();
        String id=uuid.toString();
        return id;
    }

    public String generateOrderID(){
        Random random = new Random();
        int randomID = 10000+random.nextInt(99999);
        return String.format("%04d",randomID);
    }

    public Order insertBuyNowOrder(CommonDTO dto,String productName) {
        Order order = new Order();

        LocalDateTime dateOfPurchase = LocalDateTime.now();

        String OrderID = generateOrderID();
        String TxnID = generateRandomID();

        if (dto.getProductName().equals(productName)) {
            order.setProductName(dto.getProductName());
            order.setQuantity(dto.getQuantity());
            order.setTotalPrice(dto.getTotalPrice());
            order.setProductID(dto.getProductID());
            order.setOrderID(OrderID);
            order.setTransactionID(TxnID);
            order.setDateOfPurchase(dateOfPurchase);
            order.setOrderStatus(dto.getOrderStatus());
            order.setOrderConfirm(false);

            return repository.save(order);
        }else {
            throw new RuntimeException("Product not found");
        }
    }

    //Since in cart service dto is taking list of products, so in order service you don't need to assign List to dto
    public Order insertCartOrder(CommonDTO dto) {
        String orderID= generateOrderID(); //if for each order you need different order id then use this
        // (Helps to confirm order individually from front-end like each order in a row will update button, which can update the order confirm)
        String TxnID = generateRandomID();

        Order order = new Order();
        LocalDateTime dateOfPurchase = LocalDateTime.now();

        order.setProductName(dto.getProductName());
        order.setQuantity(dto.getQuantity());
        order.setTotalPrice(dto.getTotalPrice());
        order.setProductID(dto.getProductID());
        order.setOrderID(orderID); //same order ID is not abel to generate from order-service, so generated it through cart-service(OrderID needs to be different dto.getOrderID())
        order.setDateOfPurchase(dateOfPurchase);
        order.setTransactionID(TxnID); // it needs to come from payment-service and needs to same for all cart order
        order.setOrderStatus(dto.getOrderStatus());
        order.setOrderConfirm(false);

        return repository.save(order);
    }

    public void ConfirmOrder(String orderID){ // Do not use String ProductName it might affect other Product in order table which you don't want to confirm personally
        List<Order> order=repository.findByOrderID(orderID);
        if(orderID!=null) {
            for (Order orderList : order) {
                orderList.setOrderConfirm(true);
                repository.save(orderList);
            }
        }else {
            throw new RuntimeException("Order not found");
        }
    }

    public void RefundOrder(String orderID){ // this should be in user's order table also with restTemplate.put();
        LocalDate currentDate = LocalDate.now();
        Order order=repository.getByOrderID(orderID);

        LocalDate dateOfPurchase = LocalDate.from(order.getDateOfPurchase());

        Period period = Period.between(currentDate,dateOfPurchase);
        int days = period.getDays();

        if(order == null){
            throw new RuntimeException("Order not found");
        }else if(order!=null && days >= 7){
            throw new RuntimeException("Refund not possible, since date of purchase is beyond 7 days ");
        }else if(order.getOrderStatus().equals("Delivered")){
            order.setOrderStatus("Refund");
            repository.save(order);
        }
    }
    public void CancelOrder(String orderID){ // this should be in user's order also with restTemplate.put();
        Order order=repository.getByOrderID(orderID);

        if(order == null){
            throw new RuntimeException("Order not found");
        }else if(order!=null && order.getOrderStatus().equals("Shipped")){
            throw new RuntimeException("Order cancel not possible, since the product has been shipped");
        }else if(order.getOrderStatus().equals("Active")){
            order.setOrderStatus("Canceled");
            repository.save(order);
        }
    }

}
