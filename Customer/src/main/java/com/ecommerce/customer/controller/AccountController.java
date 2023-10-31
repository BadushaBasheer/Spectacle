package com.ecommerce.customer.controller;


import com.ecommerce.library.dto.AddressDto;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
public class AccountController {

    private final CustomerService customerService;

    private final AddressService addressService;

    private final OrderRepository orderRepository;

    private final CategoryService categoryService;

    public AccountController(CustomerService customerService, AddressService addressService, OrderRepository orderRepository, CategoryService categoryService) {
        this.customerService = customerService;
        this.addressService = addressService;
        this.orderRepository = orderRepository;
        this.categoryService = categoryService;
    }

    @GetMapping("/account")
    public String account(Model model, Principal principal, HttpServletRequest httpServletRequest,
                          @RequestParam(name = "referralCode", required = false) String referralCode
    ) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
//        List<Order> orders = orderRepository.findByCustomer(customer);
//        Collections.sort(orders, Collections.reverseOrder(Comparator.comparing(Order::getId)));
//        model.addAttribute("orders",orders);
        model.addAttribute("customer", customer);
        model.addAttribute("addresses", customer.getAddress());

        HttpSession httpSession = httpServletRequest.getSession();
        String name = null;
        if (httpSession != null) {
            name = httpServletRequest.getRemoteUser();
        }
        model.addAttribute("name",name);
        return "account";
    }


    @GetMapping("/order-details")
    public String orderDetails(Model model,Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        List<Order> orders = orderRepository.findByCustomer(customer);
        Collections.sort(orders, Collections.reverseOrder(Comparator.comparing(Order::getId)));
        model.addAttribute("orders", orders);
        return "OrderManage";
    }

    @GetMapping("/add-address")
    public String addAddress(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        AddressDto addressDto = new AddressDto();
        model.addAttribute("addressDto", addressDto);
        return "add-address";
    }

    @PostMapping("/save-address")
    public String saveAddress(Model model, Principal principal, @ModelAttribute("addressDto") AddressDto addressDto, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Address newAddress = new Address();
        newAddress = addressService.save(addressDto, username);
        model.addAttribute("address", newAddress);
        redirectAttributes.addFlashAttribute("message", "Address added");
        return "redirect:/account";
    }

    @GetMapping("/edit-address/{id}")
    public String editAddress(@PathVariable("id") Long id, Model model, Principal principal, HttpServletRequest request) {
        if (principal == null) {
            return "redirect:/login";
        }

        HttpSession session = request.getSession();
        String previousPageUrl = request.getHeader("Referer");
        session.setAttribute("previousPageUrl", previousPageUrl);
        AddressDto addressDto = addressService.findById(id);
        model.addAttribute("addressDto", addressDto);
        return "edit-address";
    }

//    @GetMapping("/delete/{id}")
//    public String showDeleteAddressForm(@PathVariable("id") Long id, Model model) {
//        AddressDto addressDto = addressService.findById(id);
//        model.addAttribute("addressDto", addressDto);
//        return "account";
//    }
//
//    @PostMapping("/delete/{id}")
//    public String deleteAddress(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
//        addressService.deleteAddress(id);
//        redirectAttributes.addFlashAttribute("message", "Address deleted successfully.");
//        return "redirect:/account";
//    }

    @PostMapping("/update-address/{id}")
    public String updateAddress(@PathVariable("id") Long id, HttpServletRequest request, Principal principal, @ModelAttribute("addressDto") AddressDto addressDto, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        HttpSession session = request.getSession();
        String previousPageUrl = (String) session.getAttribute("previousPageUrl");

        String referer = request.getHeader("Referer");

        System.out.println(referer);
        Address newAddress = addressService.update(addressDto, id);
        redirectAttributes.addFlashAttribute("message", "Address updated");
        if (previousPageUrl != null) {
            return "redirect:" + previousPageUrl;
        } else {
            return "redirect:/accounts";
        }
    }

    @PostMapping("/add-address-checkout")
    public String AddAddress(@ModelAttribute("addressDto") AddressDto addressDto,
                             Model model, Principal principal, HttpServletRequest request) {
        model.addAttribute("address", addressDto);

        addressService.save(addressDto, principal.getName());
        model.addAttribute("success", "Address Added");

        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/checkReferralCode")
    public ResponseEntity<String> checkReferralCode(@RequestParam String referralCode) {
        if (referralCode == null || referralCode.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Referral code is required");
        }
        Customer customer = customerService.findByReferralCode(referralCode);
        if (customer != null) {
            return ResponseEntity.ok("valid");
        } else {
            return ResponseEntity.ok("invalid");
        }
    }
}

