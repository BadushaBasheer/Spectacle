package com.ecommerce.admin.Controller;

import com.ecommerce.library.model.*;
import com.ecommerce.library.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final CategoryService categoryService;

    private final CartService cartService;
    private final AddressService addressService;

    private final CouponService couponService;

    public OrderController(OrderService orderService, CustomerService customerService, CategoryService categoryService, CartService cartService, AddressService addressService, CouponService couponService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.addressService = addressService;
        this.couponService = couponService;
    }

    @GetMapping("/orders")
    public String Orders(Model model, Principal principal,
                         @RequestParam(name = "status", required = false, defaultValue = "") String orderStatus,
                         @RequestParam(name = "orderId", required = false, defaultValue = "0") long order_id) {

        if (principal == null) {
            return "redirect:/login";
        } else {

        }
        orderService.updateOrderStatus(orderStatus, order_id);
        List<Order> orders = orderService.findALlOrders();
        model.addAttribute("size", orders.size());
        model.addAttribute("orders", orders);
        return "order";
    }

    @GetMapping("/accept-order/{id}")
    public String acceptOrder(@PathVariable("id") Order order_id, RedirectAttributes attributes) {
        orderService.acceptOrder(order_id.getId());
        attributes.addFlashAttribute("success", "Order Accepted");
        return "redirect:/orders";
    }

    @GetMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable("id") long order_id, RedirectAttributes attributes) {
        orderService.cancelOrder(order_id);
        attributes.addFlashAttribute("cancelled", "Order Cancelled");
        return "redirect:/orders";
    }


    @GetMapping("/order-view/{id}")
    public String orderView(@PathVariable("id") long order_id, Model model) {
        Order order = orderService.findOrderById(order_id);
        Customer customer = customerService.findById(order.getCustomer().getId());
        Address address = addressService.findDefaultAddress(customer.getId());
        model.addAttribute("order", order);
        model.addAttribute("address", address);

        return "order-detail";
    }

    @GetMapping("/get-order-status/{orderId}")
    public ResponseEntity<String> getOrderStatus(@PathVariable Long orderId) {
        String orderStatus = orderService.getOrderStatus(orderId);

        if (orderStatus != null) {
            return ResponseEntity.ok(orderStatus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/order-tracking/{id}")
    public String orderTrack(@PathVariable("id")long order_id, Model model, HttpSession session){

        Order order=orderService.findOrderById(order_id);
        String paymentMethod = order.getPaymentMethod();
        if (paymentMethod.equals("COD")){
            model.addAttribute("payment","Pending");
        }else {
            model.addAttribute("payment", "Paid");
        }
        Date currentTime = new Date();
        Customer customer=customerService.findById(order.getCustomer().getId());
        Address address = addressService.findDefaultAddress(customer.getId());
        if(order.getOrderStatus().equals("Pending")){
            int pending=1;
            model.addAttribute("pending",pending);
        }else if(order.getOrderStatus().equals("Confirmed")){
            int pending=1;
            int confirmed=2;
            model.addAttribute("pending",pending);
            model.addAttribute("confirmed",confirmed);
        }else if(order.getOrderStatus().equals("Shipped")){
            int pending=1;
            int confirmed=2;
            int shipped=3;
            model.addAttribute("pending",pending);
            model.addAttribute("confirmed",confirmed);
            model.addAttribute("shipped",shipped);
        }else if(order.getOrderStatus().equals("Delivered")){
            int pending=1;
            int confirmed=2;
            int shipped=3;
            int delivered=4;
            model.addAttribute("pending",pending);
            model.addAttribute("confirmed",confirmed);
            model.addAttribute("shipped",shipped);
            model.addAttribute("delivered",delivered);
        }
        model.addAttribute("order",order);
        model.addAttribute("time",currentTime);
        model.addAttribute("address",address);

        List<Category> categories=categoryService.findAllByActivated();
        model.addAttribute("categories",categories);

        return "order-track";
    }

}
