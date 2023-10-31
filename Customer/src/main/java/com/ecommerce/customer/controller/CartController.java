package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Cart;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.CartService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
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
    private final ProductRepository productRepository;

    public CartController(CartService cartService, ProductService productService, CustomerService customerService,
                          ProductRepository productRepository) {
        this.cartService = cartService;
        this.productService = productService;
        this.customerService = customerService;
        this.productRepository = productRepository;
    }

//====================================ADDING TO CART FROM PRODUCT DETAILS============================================


    @GetMapping("/cart")
    public String cart(Model model,
                       Principal principal,
                       HttpServletRequest httpServletRequest,
                       RedirectAttributes redirectAttributes,
                       Product product)
    {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            Cart cart = customer.getCart();

            if (cart == null || cart.getCartItems().isEmpty()) {
                System.out.println("The cart is empty");
                redirectAttributes.addFlashAttribute("text", "Sorry, the cart is empty. Keep shopping");

                String referer = httpServletRequest.getHeader("referer");

                if (referer != null) {
                    return "redirect:" + referer;
                } else {
                    System.out.println("here -------------------cart controller 57");
                    return "shopping-cart2";
                }
            }
            HttpSession httpSession1 = httpServletRequest.getSession();
            String name = null;
            if (httpSession1 != null) {
                name = httpServletRequest.getRemoteUser();
            }
            model.addAttribute("name", name);
            if (cart!=null) {
                model.addAttribute("grandTotal", cart.getTotalPrice());
            }
            model.addAttribute("product", product);
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("title", "Cart");
            return "shopping-cart2";
        }
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(Model model,
                                Principal principal,
                                HttpSession session,
                                RedirectAttributes redirectAttributes,
                                @RequestParam("id") Long id,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity)
    {
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

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST)

    public String updateCart(Model model,
                            Principal principal,
                            RedirectAttributes redirectAttributes,
                            @RequestParam(value = "id", required = false) Long id,
                            @RequestParam(value = "quantity", required = false) int quantity,
                            @RequestParam(value = "updateButton", required = false) Long updateItemId,
                            @RequestParam(value = "deleteButton", required = false) Long removeItemId)
    {
        if (principal == null) {
            return "redirect:/login";
        } else if (updateItemId != null && quantity >= 1) {
            System.out.println(id);
            ProductDto productDto = productService.getByProductId(id);
            String username = principal.getName();
            try {
                Cart shoppingCart = cartService.updateCart(productDto, quantity, username);
                model.addAttribute("shoppingCart", shoppingCart);
            } catch (RuntimeException ex) {
                String errorMessage = ex.getMessage();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            }
        } else if (removeItemId != null) {
            ProductDto productDto = productService.getByProductId(id);
            String username = principal.getName();
            Cart shoppingCart = cartService.removeItemFromCart(productDto, username);
            model.addAttribute("shoppingCart", shoppingCart);
            if(shoppingCart.getCartItems().size()==0){
                Customer customer = customerService.findByUsername(principal.getName());
                Cart cart = customer.getCart();
                if (shoppingCart == null ||shoppingCart.getCartItems().isEmpty()) {
                    model.addAttribute("cartEmptyMessage", "Your cart is empty.");
                }
                return "shopping-cart2";
            }
        }
        return "redirect:/cart";
    }

//=======================================================================================================================

}

