package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.*;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CartService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.WalletService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;

    private final AddressService addressService;

    private final WalletService walletService;
    private final CartService cartService;

    private final OrderDetailRepository orderDetailRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository, AddressService addressService, WalletService walletService, CartService cartService,
                            OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.addressService = addressService;
        this.walletService = walletService;
        this.cartService = cartService;
        this.orderDetailRepository = orderDetailRepository;
    }


    //========================================================SAVE ORDER================================================================
    @Override
    public Order save(Cart cart, long address_id, String paymentMethod, Double oldTotalPrice) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomer(cart.getCustomer());
        order.setTotalPrice(cart.getTotalPrice());
        order.setQuantity(cart.getTotalItems());
        order.setPaymentMethod(paymentMethod);
        order.setShippingAddress(addressService.findByIdOrder(address_id));
        order.setAccept(false);
        order.setOrderStatus("Pending");
        if (oldTotalPrice != null) {
            Double discount = oldTotalPrice - cart.getTotalPrice();
            String formattedDiscount = String.format("%.2f", discount);
            order.setDiscountPrice(Double.parseDouble(formattedDiscount));
        }
        List<OrderDetail> orderDetailList = new ArrayList<>();
        List<String> outOfStockItems = new ArrayList<>();
        for (CartItem item : cart.getCartItems()) {
            Product product = item.getProduct();
            int currentQuantity = product.getCurrentQuantity();
            if (currentQuantity >= item.getQuantity()) {
                product.setCurrentQuantity(currentQuantity - item.getQuantity());
                productRepository.save(product);
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(item.getProduct());
                orderDetail.setQuantity(item.getQuantity());
                orderDetailRepository.save(orderDetail);
                orderDetailList.add(orderDetail);
            } else {
                outOfStockItems.add(item.getProduct().getName());
            }
        }
        if (!outOfStockItems.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("The following items are not available in sufficient quantity: ");
            errorMessage.append(String.join(", ", outOfStockItems));
            throw new RuntimeException(errorMessage.toString());
        }
        order.setOrderDetailList(orderDetailList);
        if (paymentMethod.equals("COD")) {
            order.setPaymentStatus("Pending");
            cartService.deleteCartById(cart.getId());
        } else if (paymentMethod.equals("Wallet")) {
            order.setPaymentStatus("Paid");
            cartService.deleteCartById(cart.getId());
        }
        return orderRepository.save(order);
    }

//==========================================ORDER ACCEPT/CANCEL ORDER/RETURN ORDER======================================================================

    @Override
    public void acceptOrder(Long id) {
        Order order = orderRepository.getReferenceById(id);
        order.setAccept(true);
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate newLocalDate = localDate.plusWeeks(1);
        Date newDate = Date.from(newLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        order.setDeliveryDate(newDate);
        order.setConfirmedDateTime(LocalDateTime.now());
        order.setOrderStatus("Confirmed");
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.getReferenceById(id);
        Customer customer = order.getCustomer();
        orderListUniversal(order);
        order.setOrderStatus("Cancelled");
        orderRepository.save(order);
        if (order.getPaymentMethod().equals("Wallet") || order.getPaymentMethod().equals("Razorpay")) {
            walletService.returnCredit(order, customer);
        }
    }

    @Override
    public void returnOrder(long id, Customer customer) {
        Order order = orderRepository.findById(id);
        orderListUniversal(order);
        order.setOrderStatus("Returned");
        orderRepository.save(order);
        walletService.returnCredit(order,customer);
    }

    private void orderListUniversal(Order order) {
        List<OrderDetail> orderDetailList = order.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList) {
            Product product = orderDetail.getProduct();
            if (product != null) {
                int currentQuantity = product.getCurrentQuantity();
                product.setCurrentQuantity(currentQuantity + orderDetail.getQuantity());
                productRepository.save(product);
            }
        }
    }

//==========================================================================================================================================


//========================================PAYMENT/ORDER STATUS =========================================================================

    @Override
    public void updatePayment(Order order, boolean status) {
        if (status) {
            order.setOrderStatus("Paid");
            orderRepository.save(order);
        } else {
            order.setOrderStatus("Failed");
            orderRepository.save(order);
        }
    }

    @Override
    public String getOrderStatus(long id) {
        Order order = orderRepository.getReferenceById(id);
        return order.getOrderStatus();
    }

    @Override
    public void updateOrderStatus(String status, long order_id) {
        if (order_id != 0) {
            Order order = orderRepository.getReferenceById(order_id);
            if (!order.getOrderStatus().equals("Returned") || !order.getOrderStatus().equals("Cancelled")) {
                if (status.equals("Shipped")) {
                    order.setShippedDateTime(LocalDateTime.now());
                    order.setOrderStatus(status);
                    orderRepository.save(order);
                } else if (status.equals("Delivered")) {
                    order.setDeliveredDateTime(LocalDateTime.now());
                    order.setOrderStatus(status);
                    if (order.getPaymentMethod().equals("COD")) {
                        order.setPaymentStatus("Paid");
                    }
                    orderRepository.save(order);
                }
            } else {
                System.out.println("Order is already marked as Returned");
            }
        }
    }


    //==============================================================================================================================
    @Override
    public Order findOrderById(long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAll(String username) {
        Customer customer = customerRepository.findByUsername(username);
        return customer.getOrders();
    }

    @Override
    public List<Order> findALlOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.getReferenceById(orderId);
    }


}
