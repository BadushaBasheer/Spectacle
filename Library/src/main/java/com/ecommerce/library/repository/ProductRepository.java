package com.ecommerce.library.repository;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    @Query("select p from Product p where LOWER(p.description) like %?1% or LOWER(p.name) like %?1%")
    List<Product> findAllByNameOrDescription(String keyword);

    Product findProductById(Long id);


    @Query("select p from Product p where p.is_activated=true and p.is_deleted=false")
    List<Product> getAllProducts();

    @Query("select p from Product p where p.is_activated=true and p.is_deleted=false")
    List<Product> findByNameWithinIgnoreCase(String name);

    @Query("SELECT p FROM Product p WHERE p.is_activated = true AND p.is_deleted = false AND (LOWER(p.name) LIKE %:name% OR p.category = :category)")
    List<Product> findByNameWithinIgnoreCaseOrCategory(@Param("name") String name, @Param("category") String category);

    @Query(value = "select p from Product p inner join Category c on c.id = ?1 and p.category.id = ?1 where p.is_activated = true and p.is_deleted = false")
    List<Product> getProductByCategoryId(Long id);


    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> searchProductByNameWithinIgnoreCase(String name);

//    List<Product>searchProductByNameAnd_activated(String name);


    List<Product> findByCategory(Category category);

    @Query(value = "select p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.is_activated, p.is_deleted from products p where p.is_deleted = false and p.is_activated = true limit 4", nativeQuery = true)
    List<Product> listViewProduct();

    Product getProductById(long id);

    Product findById(long id);

    List<Product> findByNameStartingWithIgnoreCase(String name);

    @Query("select p from Product p where p.is_activated=true and p.is_deleted=false "+"order by p.costPrice desc ")
    List<Product> filterHighPrice();

    @Query("select p from Product p where p.is_activated=true and p.is_deleted=false order by p.costPrice ")
    List<Product> filterLowPrice();

}
