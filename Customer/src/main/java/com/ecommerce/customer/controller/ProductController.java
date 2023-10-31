package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CustomerService customerService;

    private final CategoryService categoryService;
    private final WishlistService wishlistService;

    public ProductController(ProductService productService, ProductRepository productRepository, CustomerService customerService, CategoryService categoryService, WishlistService wishlistService) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.customerService = customerService;
        this.categoryService = categoryService;
        this.wishlistService = wishlistService;
    }

    //====================================PRODUCT FILTER==================================================================

    @GetMapping("/high-Price")
    public String filterHighPrice(Model model) {
        List<Category> categories = fetchCategories();
        List<Product> products = productService.filterHighPrice();
        model.addAttribute("products1", products);
        model.addAttribute("categories", categories);
        return "product";
    }

    @GetMapping("/low-Price")
    public String filterLowPrice(Model model) {
        List<Category> categories = fetchCategories();
        List<Product> products = productService.filterLowPrice();
        model.addAttribute("products1", products);
        model.addAttribute("categories", categories);
        return "product";
    }

    private List<Category> fetchCategories() {
        return categoryService.findAllByActivated();
    }

//===========================================================================================================

//===================================PRODUCT AND PRODUCT VIEW=============================================================
    @GetMapping("/product")
    public String viewProducts(Model model) {
        try {
            List<ProductDto> productDto = productService.findAll();
            List<Category> categories = categoryService.findAllByActivated();
            model.addAttribute("products1", productDto);
            model.addAttribute("categories",categories);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "product";
    }

    @GetMapping("/product-view/{id}")
    public String viewProduct(Model model, @PathVariable("id") Long id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("products", product);
            System.out.println(id + " " + product.getName() + " view by user");
            return "product-details";
        } else {
            return "404";
        }
    }
//    @GetMapping("/purchase/{id}")
//    public String purchaseProduct(@PathVariable Long id) {
//        Product product = productRepository.getOne(id);
//        if (product.getCurrentQuantity() > 0) {
//            product.setCurrentQuantity(product.getCurrentQuantity() - 1);
//            productRepository.save(product);
//        }
//        return "redirect:/";
//    }
//========================================================================================================================

    //======================================WISHLIST==========================================================================
//    @GetMapping("/wishlist/{id}")
//    public String viewWishlist(Principal principal,Model model, @PathVariable("id") Long id) {
//        if (principal==null){
//            return "redirect:login";
//        }
//        List<Wishlist> wishlists=wishlistService.findAllByCustomer(customer);
//        if (wishlists.isEmpty()) {
//            model.addAttribute("check","You don't have any items in your WishList");
//        }
//        model.addAttribute("wishlists",wishlists);
//        return "wishlist";
//    }
//========================================================================================================================

    //==========================================CHECKOUT======================================================================
//    @GetMapping("/shop-checkout")
//    public String checkout(Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
//        System.out.println("In checkout");
//        if (principal == null) {
//            return "redirect:/login";
//        }
//        if (redirectAttributes.getFlashAttributes().containsKey("errorMessage")) {
//            System.out.println(principal.getName() + "Gets error...............");
//
//            String errorMessage = (String) redirectAttributes.getFlashAttributes().get("errorMessage");
//            model.addAttribute("errorMessage", errorMessage);
//        }
//        String username = principal.getName();
//        System.out.println(principal.getName() + "On check Out  1");
//
//        Customer customer = customerService.findByUsername(username);
//        model.addAttribute("customer", customer);
//        System.out.println(principal.getName() + "On check Out  2");
//        Cart cart = customer.getCart();
//        model.addAttribute("cart", cart);
//        System.out.println(principal.getName() + "On check Out  3");
//
//        String name = principal.getName();
//        if (customer == null) {
//            name = httpServletRequest.getRemoteUser();
//        }
//
//        model.addAttribute("name", name);
//        return "checkout";
//    }

//    @GetMapping("/checkout")
//    public String checkout(Model model, Principal principal) {
//        System.out.println("passed through checkout 1");
//        Customer user = customerService.findByUsername(principal.getName());
//        System.out.println("passed through checkout 2");
//        Cart cart = user.getCart();
//        double cartTotalPrice = cart.getTotalPrice();
//        System.out.println("passed through checkout 3");
//        model.addAttribute("user", user);
//        model.addAttribute("cartTotalPrice", cartTotalPrice);
//        model.addAttribute("paymentMethod", "CashOnDelivery");
//        return "checkout";
//    }
//========================================================================================================================


}
