package com.ecommerce.library.service;

import com.ecommerce.library.model.Cart;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;

import java.util.List;

public interface OrderService {

    String getOrderStatus(long id);

    Order save(Cart cart, long address_id, String paymentMethod, Double oldTotalPrice);

    void acceptOrder(Long id);

    void cancelOrder(Long id);

    Order findOrderById(long id);

    void updatePayment(Order order,boolean status);

    void returnOrder(long id, Customer customer);

    void updateOrderStatus(String status, long order_id);

    List<Order> findAll(String username);

    List<Order> findALlOrders();

}
