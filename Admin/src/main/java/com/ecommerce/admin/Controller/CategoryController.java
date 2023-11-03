package com.ecommerce.admin.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CategoryController{

    private static final Logger logger = LoggerFactory.getLogger(Category.class);

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {

        this.categoryService = categoryService;
    }
//========================================================================================================================
//----------------------------------CATEGORIES-----------------------------------------------------------------------------
    @GetMapping("/categories")
    public String categories(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("searchResults",categories);
        model.addAttribute("size", categories.size());
        model.addAttribute("title", "Category");
        model.addAttribute("categoryNew", new Category());
        return "categories";
    }
//==================================================SAVE===================================================================
    @PostMapping("/save-category")
    public String add(@ModelAttribute("categoryNew") Category category, RedirectAttributes attributes) {
        try {
            categoryService.save(category);
            attributes.addFlashAttribute("success", "Added successfully");
        } catch (Exception e) {
            logger.error("An error occurred", e);
            attributes.addFlashAttribute("failed", "Failed to add because duplicate name");
        }
        return "redirect:/categories";
    }
//========================================================================================================================

//===============================================UPDATE=========================================================================

    @PostMapping("/update-category")
    public String update(@Valid Category category, RedirectAttributes redirectAttributes) {
        try {if (category.getId() == null) {
            System.out.println("Null");
        } else {
            categoryService.update(category);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        }
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error from server or duplicate name of category, please check again!");
        }
        return "redirect:/categories";
    }
//==================================================ENABLE/DISABLE======================================================================

    @RequestMapping(value = "/disable-category", method = {RequestMethod.GET, RequestMethod.PUT})
    public String delete(Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.disableById(id);
            redirectAttributes.addFlashAttribute("success", "Disabled successfully!");


        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/categories";
    }

    @RequestMapping(value = "/enable-category", method = {RequestMethod.PUT,RequestMethod.GET, RequestMethod.DELETE})
    public String enable(Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.enabledById(id);
            redirectAttributes.addFlashAttribute("success", "Enable successfully");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/categories";
    }

//========================================================================================================================

//===============================================SEARCH=========================================================================

//    @GetMapping("/searchCategory")
//    public String searchCategories(@RequestParam("category") String name, Model model) {
//        System.out.println("In category search"+name);
//        List<Category> searchResults = categoryRepository.findByNameContainingIgnoreCase(name);
//        System.out.println("searching"+ searchResults);
//        model.addAttribute("categories", searchResults);
//        return "products";
//    }

//========================================================================================================================


}
