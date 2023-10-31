package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductDto> findAll();


    Product save(List<MultipartFile> multipartFiles, ProductDto productDto);


    Product update(List<MultipartFile> multipartFiles, ProductDto productDto, Long id);

    void deleteById(Long id);

    void enableById(Long id);

    ProductDto getByProductId(Long id);


    Optional<Product> getProductById(Long id);

//    ProductDto getById(Long id);

    Product findById(long id);

    List<Product> getProductsByCategory(Category category);

    List<ProductDto> searchProduct(String name);


    List<ProductDto> findByCategoryId(Long id);


    List<ProductDto> listViewProduct();


    List<Product> getAllProduct(String name);

    List<Product> filterHighPrice();

    List<Product> filterLowPrice();


    List<Product> getAll();
}

