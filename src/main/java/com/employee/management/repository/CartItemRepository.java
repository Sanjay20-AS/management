package com.employee.management.repository;

import com.employee.management.model.CartItem;
import com.employee.management.model.Product;
import com.employee.management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByStudentAndProduct(Student student, Product product);

    void deleteByStudentAndProduct(Student student, Product product);

    List<CartItem> findByStudent(Student student);
    void deleteByStudent(Student student);
}
