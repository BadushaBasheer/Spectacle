package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.OTPService;
import com.ecommerce.library.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Value("${spring.application.name}")
    String appName;

    public HttpSession httpSession;
    private final ProductService productService;
    private final CategoryService categoryService;

    private final CustomerRepository customerRepository;
    private final OTPService otpService;

    private final  HttpSession session;

    @Autowired
    public HomeController(ProductService productService, CategoryService categoryService, HttpSession httpSession, CustomerRepository customerRepository, OTPService otpService, HttpSession session) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.httpSession = httpSession;
        this.customerRepository = customerRepository;
        this.otpService = otpService;
        this.session = session;
    }

    @GetMapping(value = {"/index", "/"})
    public String home(Model model, HttpServletRequest httpServletRequest) {
        List<Category> categories = categoryService.findAllByActivated();
        List<ProductDto> productDto = productService.listViewProduct();
        HttpSession httpSession1 = httpServletRequest.getSession();
        String name = null;
        if (httpSession1 != null) {
            name = httpServletRequest.getRemoteUser();
            System.out.println(name);
        }
        System.out.println("user name is " + name);
        model.addAttribute("name", name);
        model.addAttribute("categories", categories);
        model.addAttribute("products", productDto);
        return "index";
    }

    @GetMapping("/search-product")
    public String searchProduct(@RequestParam(defaultValue = "") String name, Model model) {
        System.out.println(" product is  " + name);
        List<ProductDto> result = productService.searchProduct(name);
        System.out.println(" result is " + result);
        model.addAttribute("products1", result);
        if (result.isEmpty()) {
            System.out.println("product not found!!!!");
            model.addAttribute("notFound", result);
            return "product";
        }
        return "product";
    }
//==============================BLOG=============================
    @GetMapping("/blog")
    public String viewProfile() {
        return "blog";
    }

////====================================PRODUCT FILTER==================================================================

////===========================================================================================================


    //======================================CATEGORY FILTER=====================================================================
    @GetMapping("/category/{id}")
    public String displayProductsByCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getCatById(id);
        List<Product> products = productService.getProductsByCategory(category);
        System.out.println("category");
        model.addAttribute("products1", products);
        return "product";
    }

    @PostMapping("/category/{categoryId}")
    public String displayCategory(@PathVariable Long categoryId, Model model) {
        Category category = categoryService.getCatById(categoryId);
        model.addAttribute("category", category);
        return "index";
    }
//=========================================================================================================


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public @ResponseBody String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName();
            otpService.clearOTP(username);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
//=========================================================================================================


}