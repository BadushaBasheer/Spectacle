package com.ecommerce.admin.Controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

//------------------------------------------------User list-------------------------------------------------------------

    @GetMapping("/users")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Customer> customerList = customerService.findAll();
        model.addAttribute("title", "Manage Users");
        model.addAttribute("users", customerList);
        model.addAttribute("size", customerList.size());
        return "users";
    }
//-----------------------------------------------------------------------------------------------------------------------------

//------------------------------------------------User Search-------------------------------------------------------------

    @GetMapping("/search")
    public String searchCustomer(@RequestParam("name") String firstName, Model model) {
            List<CustomerDto> customerDTOList = customerService.searchUser(firstName);
            System.out.println("Customer" + firstName);
            model.addAttribute("users", customerDTOList);
            System.out.println(customerDTOList + " ");
            if (customerDTOList.isEmpty()){
                model.addAttribute("noPerson",customerDTOList);
                return "users";
            }
        return "users";
    }

//    @GetMapping("/search")
//    public String searchCustomer(@RequestParam("name") String firstName, Model model) {
//        if (firstName != null && !firstName.isEmpty()) {
//            List<CustomerDto> customerDTOList = customerService.searchUser(firstName);
//            System.out.println("Customer=" + firstName);
//            model.addAttribute("users", customerDTOList);
//            System.out.println(customerDTOList + " ");
//        } else {
//                return "404";
//            }
//        return "users";
//    }


//-----------------------------------------------------------------------------------------------------------------------------

//------------------------------------------------User block/unblock-------------------------------------------------------------

    @RequestMapping(value = "/block-users/{id}", method = {RequestMethod.GET, RequestMethod.PUT})
    public String blockUser(@PathVariable("id") Long id, RedirectAttributes attributes) {

        try {
            customerService.blockById(id);
            attributes.addFlashAttribute("success", "Blocked successfully");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to delete");
        }
        return "redirect:/users";
    }

    @RequestMapping(value = "/unblock-users/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String unblockUser(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            customerService.unblockById(id);
            attributes.addFlashAttribute("success", "Unblocked successfully");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to Unblock");
        }
        return "redirect:/users";
    }

//-----------------------------------------------------------------------------------------------------------------------------

//    @GetMapping("/update-user/{id}")
//    @Transactional
//    public String showUpdateUser(@PathVariable long id, Model model){
//        CustomerDto customerDto=customerService.findById(id);
//        System.out.println("this is user");
//        model.addAttribute("customer",customerDto);
//        System.out.println("this is user 1");
//
//        return "update-users";
//    }
//    @PostMapping ("/update-user/{id}")
//    public String updateUser(@ModelAttribute("customer") CustomerDto customerDto){
//        System.out.println("this is user  01");
//
//        customerService.updateCustomer(customerDto);
//        System.out.println("this is user  02");
//
//        return "redirect:/users";
//    }

}
