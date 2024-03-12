package com.ye13sh.OrderService.Repository;

import com.ye13sh.OrderService.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByOrderID(String orderID);
    Order getByOrderID(String orderID);
}
