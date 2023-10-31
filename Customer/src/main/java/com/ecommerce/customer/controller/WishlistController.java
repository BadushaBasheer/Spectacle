package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Wishlist;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.boot.spi.InFlightMetadataCollector;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class WishlistController {

    private final WishlistService wishlistService;

    private final ProductService productService;
    private final CustomerService customerService;

    public WishlistController(WishlistService wishlistService, ProductService productService, CustomerService customerService) {
        this.wishlistService = wishlistService;
        this.productService = productService;
        this.customerService = customerService;
    }


    @GetMapping("/wishlists")
    public String getWishList(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        System.out.println(principal.getName());

        Customer customer = customerService.findByUsername(principal.getName());
        System.out.println(customer + "is   name");
        List<Wishlist> wishlists = wishlistService.findAllByCustomer(customer);
        List<ProductDto> productDto = productService.listViewProduct();

        if (wishlists.isEmpty()) {
            model.addAttribute("check", "You don't have any items in your WishList");
        }

        model.addAttribute("products", productDto);
        model.addAttribute("wishlists", wishlists);

        return "wishlists";
    }


    @PostMapping("/createWishlist")
    public String createWishlist(@RequestParam("wishlistName") String wishlistName, Principal principal, RedirectAttributes attributes) {

        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());

            try {
                wishlistService.createWishlist(wishlistName, customer);
            } catch (InFlightMetadataCollector.DuplicateSecondaryTableException e) {
                attributes.addFlashAttribute("errorMessage", e.getMessage());
            }
        }

        return "redirect:/wishlists"; // Redirect to the homepage or wishlist page
    }


    @GetMapping("/add-wishlist/{id}")
    public String addToWishlist(Principal principal, @PathVariable("id") long id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (principal == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.findByUsername(principal.getName());
        Wishlist wishlist = wishlistService.findById(id);
        boolean exists = wishlistService.findByProductId(id, customer);

        if (exists) {
            redirectAttributes.addFlashAttribute("error", "Product already exists in wishlist");
            return "redirect:" + request.getHeader("Referer");
        }

        wishlistService.save(id, customer);
        return "redirect:/wishlists";
    }

    @GetMapping("/delete-wishlist/{id}")
    public String delete(@PathVariable("id") long wishlistId, RedirectAttributes redirectAttributes) {
        wishlistService.deleteWishlist(wishlistId);
        redirectAttributes.addFlashAttribute("success", "Removed Successfully");
        return "redirect:/wishlists";
    }


}
