package com.shop.buysell.repositories;

import com.shop.buysell.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTitle(String title);

}
