package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.repository.CategoryRepository;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Category> findAll() {
        return repo.findAll();
    }

    @Override
    public Category save(Category category) {

        try {
            Category categorySave = new Category(category.getName());
            return repo.save(categorySave);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Category findById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public Category update(Category category) {
        System.out.println(category.getId());
        Category updatedCategory = repo.getReferenceById(category.getId());
        updatedCategory.setName(category.getName());
        return repo.save(updatedCategory);
    }

    @Override
    public void disableById(Long id) {
        Category category = repo.getById(id);
        category.set_deleted(true);
        category.set_activated(false);
        repo.save(category);
    }

    @Override
    public void enabledById(Long id) {
        Category category = repo.getById(id);
        category.set_activated(true);
        category.set_deleted(false);
        repo.save(category);
    }

    @Override
    public List<Category> findAllByActivated() {
        return repo.findAllByActivated();
    }

    @Override
    public Category getCatById(Long id) {
       return repo.findById(id).get();
    }


}
