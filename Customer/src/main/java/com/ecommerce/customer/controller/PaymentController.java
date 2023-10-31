//package com.ecommerce.customer.controller;
//
//import com.razorpay.RazorpayClient;
//import org.json.JSONObject;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//public class PaymentController {
//    @RequestMapping(value = {"/payment"}, method = RequestMethod.GET)
//    public String payment(Model model) {
//        model.addAttribute("rzp_key_id", env.getProperty("rzp_key_id"));
//        model.addAttribute("rzp_currency", env.getProperty("rzp_currency"));
//        model.addAttribute("rzp_company_name", env.getProperty("rzp_company_name"));
//        return "users/payment";
//    }
//
//    @GetMapping("/payment/createOrderId/{amount}")
//    @ResponseBody
//    public String createPaymentOrderId(@PathVariable String amount) {
//        String orderId = null;
//        try {
//            RazorpayClient razorpay = new RazorpayClient(env.getProperty("rzp_key_id"), env.getProperty("rzp_key_secret"));
//            JSONObject orderRequest = new JSONObject();
//            orderRequest.put("amount", amount); // amount in the smallest currency unit        orderRequest.put("currency", env.getProperty("rzp_currency"));        orderRequest.put("receipt", "order_rcptid_11");        Order order = razorpay.orders.create(orderRequest);        orderId = order.get("id");    } catch (RazorpayException e) {        // Handle Exception        System.out.println(e.getMessage());    }    return orderId;}
//        }
//    }
//    @RequestMapping(value = {"/payment/success/{amount}/{contactCount}/{companyName}/{currency}/{description}"}, method = RequestMethod.POST)
//    public String paymentSuccess(Model model,
//                                 Authentication authentication,
//                                 @RequestParam("razorpay_payment_id") String razorpayPaymentId,
//                                 @RequestParam("razorpay_order_id") String razorpayOrderId,
//                                 @RequestParam("razorpay_signature") String razorpaySignature,
//                                 @PathVariable Float amount,
//                                 @PathVariable Integer contactCount,
//                                 @PathVariable String companyName,
//                                 @PathVariable String currency,
//                                 @PathVariable String description,
//                                 RedirectAttributes redirectAttributes)
//    {
//        System.out.println("Save all data, which on success we get!");
//        return "redirect:/payment";
//    }
//}