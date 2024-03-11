package com.ye13sh.CartService.Repository;

import com.ye13sh.CartService.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    @Transactional
    Cart findByProductName(String productName);
    @Transactional
    void deleteByProductName(String productName);
    @Transactional
    @Query("SELECT SUM(c.price) FROM Cart c")
    Double sumOfPrice();
    @Transactional
    List<Cart> getByProductName(String productName);
}
