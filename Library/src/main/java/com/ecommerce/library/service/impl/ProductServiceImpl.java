package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ImageUpload imageUpload;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ImageUpload imageUpload) {
        this.productRepository = productRepository;
        this.imageUpload = imageUpload;
    }

    @Override
    public Product findById(long id) {
        return productRepository.findById(id);
    }

    //===================================ADDED ON 18-10-23 5.0 CLOCK====================================================================
    @Override
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }
//=======================================================================================================


    /*Admin*/

    //    @Override
//    public List<ProductDto> findAll() {
//        List<ProductDto> productDtoList = new ArrayList<>();
//        List<Product> products = productRepository.findAll();
//        for (Product product : products) {
//            if (product.is_deleted() || (product.getCategory() != null && !product.getCategory().is_activated())) {
//                continue;
//            }
//            ProductDto productDto = new ProductDto();
//            productDto.setId(product.getId());
//            productDto.setName(product.getName());
//            productDto.setDescription(product.getDescription());
//            productDto.setCostPrice(product.getCostPrice());
//            productDto.setCurrentQuantity(product.getCurrentQuantity());
//            Category category = product.getCategory();
//            if (category != null && category.is_activated()) {
//                productDto.setCategory(category);
//            } else {
//                Category category1 = new Category();
//                category1.setName(category != null ? category.getName() + "  ALERT: CATEGORY IS DISABLED" : "Uncategorized");
//                productDto.setCategory(category1);
//            }
//            List<String> imageUrls = product.getImageUrls();
//            productDto.setImageUrls(imageUrls);
//            productDto.setActivated(product.is_activated());
//            productDto.setDeleted(product.is_deleted());
//            productDtoList.add(productDto);
//        }
//        return productDtoList;
//    }
    @Override
    public List<ProductDto> findAll() {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            Category category = product.getCategory();
            if (category.is_activated()) {
                productDto.setCategory(category);
            } else {
                Category category1 = new Category();
                category1.setName(category.getName() + "  ALERT: CATEGORY IS DISABLED");
                productDto.setCategory(category1);
            }
            List<String> imageUrls = product.getImageUrls();
            productDto.setImageUrls((imageUrls));

            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());

            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @Override
    public List<ProductDto> findByCategoryId(Long id) {
        return transferData(productRepository.getProductByCategoryId(id));
    }

    @Override
    public List<ProductDto> listViewProduct() {
        return transferData(productRepository.getAllProducts());
    }

    private List<ProductDto> transferData(List<Product> products) {
        List<ProductDto> productDTO = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setDescription(product.getDescription());
            productDto.setImageUrls(product.getImageUrls());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());
            productDTO.add(productDto);
        }
        return productDTO;
    }


    @Override
    public Product save(List<MultipartFile> imageFiles, ProductDto productDto) {
        try {
            Product product = new Product();
            if (imageFiles != null && !imageFiles.isEmpty()) {
                List<String> uniqueFileNames = imageUpload.uploadImages(imageFiles);
                product.setImageUrls(uniqueFileNames);
            } else {
                product.setImageUrls(null);  // Handle the case when no images are uploaded
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);
            return productRepository.save(product);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(List<MultipartFile> imageFiles, ProductDto productDto, Long id) {
        try {
            Product productUpdate = productRepository.getById(id);

            if (imageFiles != null && !imageFiles.isEmpty()) {
                List<String> uniqueFileNames = imageUpload.uploadImages(imageFiles);
                productUpdate.setImageUrls(uniqueFileNames);
            }
            productUpdate.setId(productUpdate.getId());
            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setName(productDto.getName());
            productUpdate.setDescription(productDto.getDescription());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            return productRepository.save(productUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //------------------------------------PRODUCT STATUS-------------------------------------------------------------
    public void updateProductStatus(Long id, boolean isActivated, boolean isDeleted) {
        Product product = productRepository.getReferenceById(id);
        product.set_activated(isActivated);
        product.set_deleted(isDeleted);
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        updateProductStatus(id, false, true);
    }

    @Override
    public void enableById(Long id) {
        updateProductStatus(id, true, false);
    }

//------------------------------------PRODUCT STATUS ENDS-------------------------------------------------------------

    @Override
    public Optional<Product> getProductById(Long id) {
        Product product = productRepository.getProductById(id);
        return Optional.of(product);
    }

    @Override
    public List<ProductDto> searchProduct(String name) {
        List<Product> products = productRepository.findByNameStartingWithIgnoreCase(name);
        return products.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto getByProductId(Long id) {
        Product product = productRepository.getProductById(id);
        return convertEntityToDto(product);
    }
    private ProductDto convertEntityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setImageUrls(product.getImageUrls());
        productDto.setCategory(product.getCategory());
        productDto.setDeleted(product.is_deleted());
        productDto.setActivated(product.is_activated());
        return productDto;
    }

    /*Customer*/

    @Override
    public List<Product> getAllProduct(String name) {
        if (name.isEmpty()) {
            return productRepository.findAll();
        } else {
            return productRepository.findByNameWithinIgnoreCase(name);
        }
    }

    //=======================================FILTER===========================================================================
    @Override
    public List<Product> filterHighPrice() {
        return productRepository.filterHighPrice();
    }

    @Override
    public List<Product> filterLowPrice() {
        return productRepository.filterLowPrice();
    }

    @Override
    public List<Product> getAll() {
        return productRepository.getAllProducts();
    }

    //===========================FILTER===========================================================================

}


//    @Override
//    public Product getAllProductByCategoryId(Long id) {
//        return transferData(productRepository.getByProductId(id));
//    }

//    @Override
//    public Product getProductById(Long id){
//        return productRepository.getProductById(id);
//    }
//
//    @Override
//    public List<Product> getAllProductByCategoryId(int id) {
//        return productRepository.findAllByCategory_Id(id);
//    }

//    @Override
//    public List<ProductDto> searchProduct(String name) {
//        List<Product> products=productRepository.findByNameStartingWithIgnoreCase(name);
//        return products.stream().map(this::convertEntityToDto).collect(Collectors.toList());
//    }
//    private ProductDto convertEntityToDto(Product product) {
//        ProductDto productDto = new ProductDto();
//        productDto.setId(product.getId());
//        productDto.setName(product.getName());
//        productDto.setDescription(product.getDescription());
//        productDto.setSalePrice(product.getSalePrice());
//        productDto.setCostPrice(product.getCostPrice());
//        productDto.setCurrentQuantity(product.getCurrentQuantity());
//        product.setImage(product.getImage());
//        productDto.setCategory(product.getCategory());
//        productDto.setDeleted(product.is_deleted());
//        productDto.setActivated(product.is_deleted());
//        return productDto;
//    }
//    @Override
//    public ProductDto getByProductId(Long id) {
//        Product product = productRepository.getProductById(id);
//        ProductDto productDto = new ProductDto();
//        productDto.setId(product.getId());
//        productDto.setName(product.getName());
//        productDto.setDescription(product.getDescription());
//        productDto.setSalePrice(product.getSalePrice());
//        productDto.setCostPrice(product.getCostPrice());
//        productDto.setCurrentQuantity(product.getCurrentQuantity());
//        product.setImage(product.getImage());
//        productDto.setCategory(product.getCategory());
//        productDto.setDeleted(product.is_deleted());
//        productDto.setActivated(product.is_deleted());
//        return productDto;
//    }

