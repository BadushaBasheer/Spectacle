package com.ecommerce.admin.Controller;


import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }


    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
//========================================================================================================================

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<ProductDto> productDtoList = productService.findAll();
        System.out.println("profuctdto list is  " + productDtoList);
        model.addAttribute("title", "Manage Product");
        model.addAttribute("products", productDtoList);
        model.addAttribute("size", productDtoList.size());
        return "products";
    }
//==============================================SEARCH==========================================================================

    @GetMapping("/searchProduct")
    public String searchProduct(@RequestParam(defaultValue = "") String product, Model model, RedirectAttributes attributes) {
        System.out.println("search term is  " + product);
        List<ProductDto> result = productService.searchProduct(product);
        System.out.println(" result is " + result);
        model.addAttribute("products", result);
        if (result.isEmpty()) {
            System.out.println("product not found!!!!");
            model.addAttribute("notFound", result);
            return "products";
        }
        return "products";
    }
//====================================================ADD====================================================================

    @GetMapping("/add-product")
    public String addProductForm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", new ProductDto());
        return "add-product-2";
    }

    @PostMapping("/save-product")
    public String saveProduct(@Valid @ModelAttribute("product") ProductDto productDto,
                              @RequestParam("imageProduct") List<MultipartFile> multipartFiles,
                              BindingResult result, RedirectAttributes attributes) {


        productDto.setCategory(categoryService.getCatById(productDto.getCategory().getId()));
        if (result.hasErrors()) {
            System.out.println(result.hasErrors());
            attributes.addFlashAttribute("error", "Validation failed");
            return "redirect:/add-product-2";
        }
        if (productDto.getCurrentQuantity().equals(0)) {
            attributes.addFlashAttribute("error", "Quantity must be greater than 0");
            return "redirect:/add-product-2";
        }
        try {
            productService.save(multipartFiles, productDto);
            attributes.addFlashAttribute("success", "Add successfully");
        } catch (Exception e) {
            System.out.println("error");
            logger.error("Failed to add product", e);
            throw e;
        }
        return "redirect:/products";
    }

    //======================================================UPDATE======================================================================
//    @GetMapping("/update-product/{id}")
//    public String updateProduct(@PathVariable("id") Long id, Model model, Principal principal) {
//        if (principal == null) {
//            return "redirect:/login";
//        }
//        List<Category> categories = categoryService.findAllByActivated();
//        ProductDto productDto = productService.getByProductId(id);
//        model.addAttribute("title", "Update Products");
//        model.addAttribute("categories", categories);
//        model.addAttribute("products", productDto);
//        return "update-product2";
//    }
//
//    @PostMapping("/update-products/{id}")
//    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
//                                @RequestParam("imageProduct") List<MultipartFile> imageFiles,
//                                RedirectAttributes redirectAttributes,
//                                @PathVariable("id") Long id) {
//        try {
//            productService.update(imageFiles, productDto, id);
//            redirectAttributes.addFlashAttribute("success", "Update successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
//        }
//        return "redirect:/products";
//    }
    @GetMapping("/update-product/{id}")
    public String updateProduct(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        return updateProductInternal(id, model);
    }
    @PostMapping("/update-products/{id}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") List<MultipartFile> imageFiles,
                                RedirectAttributes redirectAttributes,
                                @PathVariable("id") Long id) {
        try {
            productService.update(imageFiles, productDto, id);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/products";
    }
    private String updateProductInternal(Long id, Model model) {
        List<Category> categories = categoryService.findAllByActivated();
        ProductDto productDto = productService.getByProductId(id);
        model.addAttribute("title", "Update Products");
        model.addAttribute("categories", categories);
        model.addAttribute("products", productDto);
        return "update-product2";
    }

//======================================ENABLE/DISABLE========================================================================

    //    @Transactional
//    @RequestMapping(value="/enable-product/{id}", method={ RequestMethod.PUT,RequestMethod.GET})
//    public String enabledProduct(@PathVariable("id") Long id, RedirectAttributes attributes){
//        try {
//            productService.enableById(id);
//            attributes.addFlashAttribute("success", "Enabled successfully");
//        }catch (Exception e){
//            e.printStackTrace();
//            attributes.addFlashAttribute("error","Failed to enable");
//        }
//        return "redirect:/products";
//    }
//    @Transactional
//    @RequestMapping(value="/delete-product/{id}", method={RequestMethod.GET, RequestMethod.PUT})
//    public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes attributes){
//
//        try {
//            productService.deleteById(id);
//            attributes.addFlashAttribute("success", "Deleted successfully");
//        }catch (Exception e){
//            e.printStackTrace();
//            attributes.addFlashAttribute("error","Failed to delete");
//        }
//        return "redirect:/products";
//    }
    @Transactional
    @RequestMapping(value = "/enable-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enabledProduct(@PathVariable("id") Long id, RedirectAttributes attributes) {
        return performAction(id, attributes, "enable", "Enabled successfully", "Failed to enable");
    }

    @Transactional
    @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.GET, RequestMethod.PUT})
    public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes attributes) {
        return performAction(id, attributes, "delete", "Deleted successfully", "Failed to delete");
    }

    private String performAction(Long id, RedirectAttributes attributes, String action, String successMessage, String errorMessage) {
        try {
            if ("enable".equals(action)) {
                productService.enableById(id);
            } else if ("delete".equals(action)) {
                productService.deleteById(id);
            }
            attributes.addFlashAttribute("success", successMessage);
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", errorMessage);
        }
        return "redirect:/products";
    }


//======================================ENABLE/DISABLE========================================================================

//========================================================================================================================


}
