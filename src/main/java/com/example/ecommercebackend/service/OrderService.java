package com.example.ecommercebackend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommercebackend.dto.OrderItemResponse;
import com.example.ecommercebackend.dto.OrderRequest;
import com.example.ecommercebackend.dto.OrderResponse;
import com.example.ecommercebackend.dto.PaymentResponse;
import com.example.ecommercebackend.model.Cart;
import com.example.ecommercebackend.model.CartItem;
import com.example.ecommercebackend.model.Customer;
import com.example.ecommercebackend.model.Order;
import com.example.ecommercebackend.model.OrderItem;
import com.example.ecommercebackend.model.OrderStatus;
import com.example.ecommercebackend.model.Payment;
import com.example.ecommercebackend.model.PaymentStatus;
import com.example.ecommercebackend.repository.CartRepository;
import com.example.ecommercebackend.repository.CustomerRepository;
import com.example.ecommercebackend.repository.OrderRepository;
import com.example.ecommercebackend.util.AuthUtil;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private PaymentService paymentService;

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setPrice(order.getTotalPrice());
        response.setStatus(order.getStatus().toString());

        List<OrderItemResponse> items = order.getItem().stream().map( item -> {
            OrderItemResponse i = new OrderItemResponse();
            i.setProdcutName(item.getProduct().getName());
            i.setQuantity(item.getQuantity());
            i.setPrice(item.getPrice());

            return i;
        }).collect(Collectors.toList());
        response.setItem(items);

        if(order.getPayment()!=null) {
            PaymentResponse p = new PaymentResponse();
            p.setPaymentId(order.getPayment().getId());
            p.setMethod(order.getPayment().getPaymentMethod().toString());
            p.setStatus(order.getPayment().getPaymentStatus().toString());
            p.setOrderId(order.getId());

            response.setPayment(p);
        }
        return response;
    }

    @Transactional
    public OrderResponse placeOrder(Long customerId, OrderRequest orderRequest) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer Not Found"));
        Cart cart = customer.getCart();
        if(cart==null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is Empty, cannot place Order");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            total+=orderItem.getPrice();
        }
        order.setItem(orderItems);
        order.setTotalPrice(total);

        Payment payment = new Payment();
        payment.setPaymentMethod(orderRequest.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setOrder(order);

        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        OrderResponse response = mapToOrderResponse(savedOrder);
        return response;
    }

    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer Not Found"));
        return orderRepository.findByCustomer(customer)
                              .stream()
                              .map(this::mapToOrderResponse)
                              .collect(Collectors.toList());

    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order Not Found"));

        return mapToOrderResponse(order);
    }

    public OrderResponse cancelOrder(Long orderId) {
       Customer customer = authUtil.getLoggedInCustomer();
       Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order Not Found"));

       if(!order.getCustomer().getId().equals(customer.getId())) {
        throw new RuntimeException("You are not authorized to cancel this order.");
       }

       if(order.getStatus()==OrderStatus.SHIPPED) {
        throw new RuntimeException("Order already shipped, cannot cancel");
       }

       order.setStatus(OrderStatus.CANCELLED);
       orderRepository.save(order);

       paymentService.refundPayment(order);

       return mapToOrderResponse(order);
    }
    
}
