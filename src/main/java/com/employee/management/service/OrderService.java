package com.employee.management.service;

import com.employee.management.dto.OrderItemDTO;
import com.employee.management.dto.OrderResponse;
import com.employee.management.model.*;
import com.employee.management.repository.OrderRepository;
import com.employee.management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final StudentRepository studentRepository;
    private final CartService cartService;
    public Optional<OrderResponse> createOrder(String studentId) {
        //validate the car
        //validate the user
        //create order
        //clear the cart

        List<CartItem> cartItems=cartService.getCartItems(studentId);
        if(cartItems.isEmpty()){
            return Optional.empty();
        }

        Optional<Student> studentOptional= studentRepository.findById(Long.valueOf(studentId));
        if(studentOptional.isEmpty()){
            return Optional.empty();
        }
        Student student=studentOptional.get();

        BigDecimal totalPrice=cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order=new Order();
        order.setStudent(student);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems=cartItems.stream().map(item-> new OrderItem(
                null,
        item.getProduct(),
        item.getQuantity(),
        item.getPrice(),
        order
        )).toList();
        order.setItems(orderItems);
        Order savedOrder=orderRepository.save(order);

        cartService.clearCart(studentId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }
    private OrderResponse mapToOrderResponse(Order savedOrder){
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                savedOrder.getItems().stream().map(orderItem-> new OrderItemDTO(
                        orderItem.getId(),
                        orderItem.getProduct().getId(),
                        orderItem.getQuantity(),
                        orderItem.getPrice(),
                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))

                )).toList(),savedOrder.getCreatedAt()
        );
    }


}
