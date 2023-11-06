package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Cart;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.CartService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

@Controller
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;

    public CartController(CartService cartService, ProductService productService, CustomerService customerService) {
        this.cartService = cartService;
        this.productService = productService;
        this.customerService = customerService;
    }

//====================================ADDING TO CART FROM PRODUCT DETAILS============================================
    @GetMapping("/cart")
    public String viewCart(Principal principal, Model model, Product product) {
        if (principal == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.findByUsername(principal.getName());
        Cart cart = customer.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            model.addAttribute("cartEmptyMessage", "Sorry,Your cart is Empty😢");
        } else if (cart != null) {
            model.addAttribute("grandTotal", cart.getTotalPrice());
        }
        model.addAttribute("product", product);
        model.addAttribute("shoppingCart", cart);
        model.addAttribute("title", "Cart");
        return "shopping-cart2";
    }
//======================================================================================================================
    @PostMapping("/add-to-cart")
    public String addItemToCart(Model model,
                                Principal principal,
                                HttpSession session,
                                RedirectAttributes redirectAttributes,
                                @RequestParam("id") Long id,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity) {
        ProductDto productDto = productService.getByProductId(id);
        if (principal == null) {
            System.out.println("No authenticated user");
            return "redirect:/login";
        } else {
            String username = principal.getName();
            try {
                System.out.println("Add item to cart");
                Cart cart = cartService.addItemToCart(productDto, quantity, username);
                session.setAttribute("totalItems", cart.getTotalItems());
                model.addAttribute("shoppingCart", cart);
            } catch (RuntimeException ex) {
                System.out.println("Cart item gets exception on cart controller");
                String errorMessage = ex.getMessage();
                System.out.println("get here exception error-----------------------------");
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            }
        }
        redirectAttributes.addFlashAttribute("added", "Product added successfully");
        System.out.println("product added successfully");
        return "redirect:/cart";
    }
//=====================================================================================================================================================

    @RequestMapping(value = "/update-cart", method = {RequestMethod.POST,RequestMethod.GET})
    public String updateCart(
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "quantity", required = false) int quantity,
            @RequestParam(value = "updateButton", required = false) Long updateItemId,
            @RequestParam(value = "deleteButton", required = false) Long removeItemId) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        try {
            if (updateItemId != null && quantity >= 1) {
                ProductDto productDto = productService.getByProductId(id);
                Cart shoppingCart = cartService.updateCart(productDto, quantity, username);
                model.addAttribute("shoppingCart", shoppingCart);
            } else if (removeItemId != null) {
                ProductDto productDto = productService.getByProductId(id);
                Cart shoppingCart = cartService.removeItemFromCart(productDto, username);
                model.addAttribute("shoppingCart", shoppingCart);
                if (shoppingCart.getCartItems().isEmpty()) {
                    Customer customer = customerService.findByUsername(principal.getName());
                    Cart cart = customer.getCart();
                    if (cart == null || cart.getCartItems().isEmpty()) {
                        model.addAttribute("cartEmptyMessage", "Your cart is empty.");
                        return "shopping-cart2";
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            String errorMessage = ex.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        } catch (Exception e) {
            String errorMessage = "An error occurred while updating the cart.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/cart";
    }

//=======================================================================================================================

}

