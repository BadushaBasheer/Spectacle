package com.ecommerce.library.service;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Category save(Category category);

    Optional<Category> findById(Long id);
    Category update(Category category);
    void disableById(Long id);
    void enabledById(Long id);

    List<Category> findAllByActivated();

     Category getCatById(Long id);



}
