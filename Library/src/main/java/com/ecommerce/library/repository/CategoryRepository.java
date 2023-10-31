package com.ecommerce.library.repository;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.is_activated=TRUE AND c.is_deleted=FALSE")
    List<Category> findAllByActivated();

    @Query(value = "select new com.ecommerce.library.dto.CategoryDto(c.id, c.name, count(p.category.id)) " +
            "from Category c left join Product p on c.id = p.category.id " +
            "where c.is_activated = true and c.is_deleted = false " +
            "group by c.id ")
    List<CategoryDto> getCategoriesAndSize();

    @Query(value = "select COUNT(*) FROM Category")
    Long countAllCategories();

    Category findById(long id);

//    Category existsByNameWithinIgnoreCase();

}
