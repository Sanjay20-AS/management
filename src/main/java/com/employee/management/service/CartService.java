package com.employee.management.service;

import com.employee.management.dto.CartItemRequest;
import com.employee.management.model.CartItem;
import com.employee.management.model.Product;
import com.employee.management.model.Student;
import com.employee.management.repository.CartItemRepository;
import com.employee.management.repository.ProductRepository;
import com.employee.management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final StudentRepository studentRepository;


    public Boolean addToCart(String studentId, CartItemRequest request) {
        Optional<Product> productOpt= productRepository.findById(request.getProductId());
        if(productOpt.isEmpty()){
            return false;
        }
        Product product = productOpt.get();
        if(product.getStockQuantity() < request.getQuantity()){
            return false;
        }

        Optional<Student> studentOpt=studentRepository.findById(Long.valueOf(studentId));
        if(studentOpt.isEmpty()){
            return false;
        }

        Student student = studentOpt.get();
        CartItem existingCartItem= cartItemRepository.findByStudentAndProduct(student, product);
        if(existingCartItem != null){
            //update cart item
            existingCartItem.setQuantity(existingCartItem.getQuantity()+request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }
        else{
            //create new cart item
            CartItem cartItem=new CartItem();
            cartItem.setStudent(student);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice()
                    .multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);

        }
        return true;
    }


    public boolean deleteItemFromCart(String studentId, Long productId) {
        Optional<Product> productOpt= productRepository.findById(productId);

        Optional<Student>  studentOpt=studentRepository.findById(Long.valueOf(studentId));

        if(productOpt.isPresent() &&  studentOpt.isPresent()){
            cartItemRepository.deleteByStudentAndProduct(studentOpt.get(),productOpt.get());
            return true;
        }
        else{
            return false;
        }
    }

    public List<CartItem> getCartItems(String studentId) {
        return studentRepository.findById(Long.valueOf(studentId))
                .map(cartItemRepository::findByStudent) .orElseGet(List::of);
    }
}
