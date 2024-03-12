package com.ye13sh.ProductService.Repository;

import com.ye13sh.ProductService.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Transactional
    Product findByProductName(String productName);
    @Transactional
    void deleteByProductName(String productName);
    @Transactional
    Product getByProductName(String productName);

    //List<Product> ProductName(String productName);
}
