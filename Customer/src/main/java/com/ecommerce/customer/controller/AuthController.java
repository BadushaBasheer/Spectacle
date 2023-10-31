package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.LoginDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
public class AuthController {
    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final CustomerRepository customerRepository;


    @Autowired
    public AuthController(CustomerService customerService, BCryptPasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder){
        StringTrimmerEditor stringTrimmerEditor=new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("title", "Login");
        return "login-2";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String response = customerService.login(loginDto);
        if (response.equals("login_successful")) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("customerDto", new CustomerDto());
        return "register";
    }


    @PostMapping("/register-new")
    public String registerCustomer(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                   BindingResult result,
                                   Model model) {

        if (result.hasErrors()) {
            model.addAttribute("customerDto", customerDto);
            return "register";
        } else {
            String username = customerDto.getUsername();
            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                model.addAttribute("customerDto", customerDto);
                model.addAttribute("error", "This email is already registered");
                return "register";
            } else if (customerDto.getPassword().equals(customerDto.getRepeatPassword())) {
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                customerService.save(customerDto);
                System.out.println("checked");
                model.addAttribute("success", "Register successfully!");
            } else {
                model.addAttribute("customerDto", customerDto);
                model.addAttribute("passwordError", "Password is not the same");
                System.out.println(customerDto.getUsername() + "checked");
                return "register";
            }
        }
        return "register";
    }


    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password");
        return "forgot-password";
    }


//    @GetMapping("/verify-account")
//    public ResponseEntity<String> verifyAccount(@RequestParam(name = "email") String email,
//                                                @RequestParam(name = "otp") String otp) {
//        if (email != null && !email.isEmpty() && otp != null && !otp.isEmpty()) {
//            String response = customerService.verifyAccount(email, otp);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PostMapping("/regenerate-otp")
//    public ResponseEntity<String> regenerateOtp(@RequestParam(name = "email") String email) {
//        if (email != null && !email.isEmpty()) {
//            String response = customerService.regenerateOtp(email);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } else {
//            // Handle the case when email is missing or empty
//            return new ResponseEntity<>("Invalid email parameter", HttpStatus.BAD_REQUEST);
//        }
//    }


    @GetMapping("/contact")
    public String viewContact() {
        return "contact";
    }


    @GetMapping("/otpVerification")
    public String otpSent(Model model, CustomerDto customerDto) {
        model.addAttribute("otpValue", customerDto);
        return "otpScreen";
    }

    @PostMapping("/otpVerification")
    public String otpVerification(@ModelAttribute("otpValue") CustomerDto customerDto) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
//        Customer customer = customerRepository.findByEmail(user.getUsername());
//        if(customer.getOtp() == customerDto.getOtp()) {
//            customer.set_active(true);
//            customerRepository.save(customer);
//            return "redirect:/dashboard";
//        }
//        else
            return "redirect:/login/otpVerification?error";
    }




}

