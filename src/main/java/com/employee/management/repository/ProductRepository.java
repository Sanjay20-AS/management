package com.employee.management.repository;

import com.employee.management.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
        Select p from Product p where (p.active=true and (Lower(p.name) like lower (concat('%',:keyword,'%')) or
                (lower(p.category) like lower (concat('%',:keyword,'%')) )))
        """)
    List<Product> searchProducts(String keyword);

}
