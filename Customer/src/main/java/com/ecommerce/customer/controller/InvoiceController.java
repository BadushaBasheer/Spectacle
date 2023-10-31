package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Invoice;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.InvoiceService;
import com.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class InvoiceController {
    private final InvoiceService invoiceService;

    private final OrderService orderService;
    public InvoiceController(InvoiceService invoiceService, OrderService orderService) {
        this.invoiceService = invoiceService;
        this.orderService = orderService;
    }

    @GetMapping("/invoice/{id}")
    public String showInvoice(@PathVariable Long id, Model model) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        if (invoice != null) {
            model.addAttribute("orderID", invoice.getOrderID());
            model.addAttribute("orderDate", invoice.getOrderDate());
            model.addAttribute("customerName", invoice.getCustomerName());
            model.addAttribute("paymentAmount", invoice.getPaymentAmount());
            model.addAttribute("paymentNumber", invoice.getPaymentNumber());
            model.addAttribute("paymentDate", invoice.getPaymentDate());
            model.addAttribute("companyName", invoice.getCompanyName());

            // Return the name of the HTML template
            return "invoice";
        } else {
            // Handle not found or other errors
            return "404"; // Create an error.html template
        }
    }

    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

//    @PostMapping("/invoices")
//    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
//        Invoice createdInvoice = invoiceService.createInvoice(invoice);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
//    }


//    @GetMapping("/invoices/{id}")
//    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id, Model model) {
//        Invoice invoice = invoiceService.getInvoiceById(id);
//        Order order = orderService.findOrderById(id);
//        if (invoice != null) {
//            model.addAttribute("order",order);
//            return ResponseEntity.ok(invoice);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//
//    @GetMapping("/invoices/{id}")
//    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
//        invoiceService.deleteInvoice(id);
//        return ResponseEntity.noContent().build();
//    }

}
