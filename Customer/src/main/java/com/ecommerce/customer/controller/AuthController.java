package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.LoginDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class AuthController {
    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final CategoryService categoryService;

    private final CustomerRepository customerRepository;


    @Autowired
    public AuthController(CustomerService customerService, BCryptPasswordEncoder passwordEncoder, CategoryService categoryService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.categoryService = categoryService;
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



    @GetMapping("/contact")
    public String viewContact() {
        return "contact";
    }


    @GetMapping("/forgot-password")
    public String forgotPasswordOTP(Model model, CustomerDto customerDto){

        model.addAttribute("title", "Forgot Password- OTP");
        model.addAttribute("username",customerDto);

        List<Category> categories=categoryService.findAllByActivated();
        model.addAttribute("categories",categories);
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordOTPSend(@ModelAttribute("username") CustomerDto customerDto, Model model){
        String otp= customerService.genrateOTPAndSendOnEmail(customerDto.getUsername());
        model.addAttribute("data",customerDto);
        model.addAttribute("otpGenerationTime",System.currentTimeMillis());
        List<Category> categories=categoryService.findAllByActivated();
        model.addAttribute("categories",categories);
        return "otpScreenEmail";
    }

    @PostMapping("/forgot-password/otpVerification")
    public String otpSentEmailPost(@ModelAttribute("data") CustomerDto customerDto , Model model, RedirectAttributes attributes) {
        boolean isOTPValid = customerService.verifyOTP(customerDto.getOtp(),customerDto.getUsername());
        if (isOTPValid) {
            model.addAttribute("customerDto",customerDto);
            return "passwordResetPage";
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            return "otpScreenEmail";
        }
    }

    @PostMapping("/passwordResetPage")
    public String passwordResetPage(@ModelAttribute("customerDto") CustomerDto customerDto, Model model, RedirectAttributes redirectAttributes){

        System.out.println(customerDto);
        System.out.println("here");
        System.out.println(customerDto.getPassword());
        System.out.println(customerDto.getRepeatPassword());
        if(customerDto.getPassword().equals(customerDto.getRepeatPassword())){
            Customer customer=customerRepository.findByUsername(customerDto.getUsername());
            customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
            customerRepository.save(customer);
            redirectAttributes.addFlashAttribute("success", "Password is changed");
        }
        else{
            redirectAttributes.addFlashAttribute("error", "Passwords are not same");
        }
        return "redirect:/login";
    }





}

