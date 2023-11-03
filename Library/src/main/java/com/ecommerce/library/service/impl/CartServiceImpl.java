package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.CartService;
import com.ecommerce.library.service.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    private final ShoppingCartRepository cartRepository;


    private final CartItemRepository itemRepository;

    private final CustomerService customerService;

    public CartServiceImpl(ShoppingCartRepository cartRepository, CartItemRepository itemRepository, CustomerService customerService) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.customerService = customerService;
    }
    @Override
    public Cart addItemToCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerService.findByUsername(username);
        Cart cart = customer.getCart();

        if (cart == null) {
            cart = new Cart();
        }
        Set<CartItem> cartItemList = cart.getCartItems();
        CartItem cartItem = find(cartItemList, productDto.getId());

        Product product = transfer(productDto);

        double unitPrice = 0;

        if(productDto.getCostPrice() == 0) {
            unitPrice = productDto.getCostPrice();
        }else{
            unitPrice = productDto.getCostPrice();
        }

        int itemQuantity = 0;
        if (cartItemList == null) {
            cartItemList = new HashSet<>();
        }

        if (productDto.getCurrentQuantity() < quantity) {
            System.out.println("here");
            throw new RuntimeException("Insufficient quantity available for product: " + product.getName());
        }

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(unitPrice);
            cartItem.setCart(cart);
            cartItemList.add(cartItem);
            itemRepository.save(cartItem);

        } else {
            itemQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(itemQuantity);
            itemRepository.save(cartItem);
        }
        cart.setCartItems(cartItemList);

        double totalPrice = totalPrice(cart.getCartItems());
        int totalItem = totalItem(cart.getCartItems());

        cart.setTotalPrice(totalPrice);
        cart.setTotalItems(totalItem);
        cart.setCustomer(customer);

        return cartRepository.save(cart);
    }

    //============================================================================================================
    @Override
    public Cart updateCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerService.findByUsername(username);
        Cart shoppingCart = customer.getCart();
        Set<CartItem> cartItemList = shoppingCart.getCartItems();
        CartItem item = find(cartItemList, productDto.getId());

        if (productDto.getCurrentQuantity() < quantity) {
            throw new RuntimeException("Insufficient quantity available for product: " + productDto.getName());
        }
        item.setQuantity(quantity);
        itemRepository.save(item);
        shoppingCart.setCartItems(cartItemList);
        int totalItem = totalItem(cartItemList);
        double totalPrice = totalPrice(cartItemList);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        return cartRepository.save(shoppingCart);
    }

    @Override
    public Cart removeItemFromCart(ProductDto productDto, String username) {
        Customer customer = customerService.findByUsername(username);
        Cart shoppingCart = customer.getCart();

        Set<CartItem> cartItemList = shoppingCart.getCartItems();
        CartItem item = find(cartItemList, productDto.getId());
        cartItemList.remove(item);
        itemRepository.delete(item);
        double totalPrice = totalPrice(cartItemList);
        int totalItem = totalItem(cartItemList);
        shoppingCart.setCartItems(cartItemList);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        return cartRepository.save(shoppingCart);
    }


    private CartItem find(Set<CartItem> cartItems, long productId) {
        if (cartItems == null) {
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    //============================================================================================================

    private int totalItem(Set<CartItem> cartItemList) {
        int totalItem = 0;
        for (CartItem item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }
    private double totalPrice(Set<CartItem> cartItemList) {
        double totalPrice = 0.0;
        for (CartItem item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    @Override
    public Cart updateTotalPrice(Double newTotalPrice, String username) {

        Customer customer = customerService.findByUsername(username);
        Cart shoppingCart = customer.getCart();
        shoppingCart.setTotalPrice(newTotalPrice);
        cartRepository.save(shoppingCart);
        return shoppingCart;
    }

    //==================================================================================================================



    private Product transfer(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCostPrice(productDto.getCostPrice());
        product.setDescription(productDto.getDescription());
        product.setImageUrls(productDto.getImageUrls());
        product.set_activated(productDto.isActivated());
        product.set_deleted(productDto.isDeleted());
        product.setCategory(productDto.getCategory());
        return product;
    }


    @Override
    @Transactional
    public void deleteCartById(Long id) {
        Cart shoppingCart = cartRepository.getById(id);
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            cartItem.setCart(null);
            itemRepository.deleteById(cartItem.getId());

        }
        shoppingCart.setCustomer(null);
        shoppingCart.getCartItems().clear();
        shoppingCart.setTotalPrice(0);
        shoppingCart.setTotalItems(0);
        cartRepository.save(shoppingCart);
    }


}
