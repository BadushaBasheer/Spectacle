//package com.ecommerce.customer.controller;
//
//import com.ecommerce.library.model.Order;
//import com.ecommerce.library.service.OrderService;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.thymeleaf.TemplateEngine;
//import org.xhtmlrenderer.pdf.ITextRenderer;
//
//
//import javax.naming.Context;
//import java.io.ByteArrayOutputStream;
//import java.io.OutputStream;
//
//@Controller
//public class InvoiceControllers {
//    private final TemplateEngine templateEngine;
//
//    private final OrderService orderService;
//
//    public InvoiceController(TemplateEngine templateEngine) {
//        this.templateEngine = templateEngine;
//    }
//
//    public InvoiceControllers(TemplateEngine templateEngine, OrderService orderService) {
//        this.templateEngine = templateEngine;
//        this.orderService = orderService;
//    }
//
//    @GetMapping("/generate-invoice/{orderId}")
//    public void generateInvoice(@PathVariable Long orderId, HttpServletResponse response) throws Exception {
//        // Fetch order details by orderId (you'll need to implement this)
//        Order order = orderService.getOrderById(orderId);
//
//        // Create a Thymeleaf context
//        Context context = new Context();
//        context.setVariable("order", order);
//
//        // Process the Thymeleaf template to generate HTML content
//        String htmlContent = templateEngine.process("invoice-template", context);
//
//        // Generate PDF from HTML content using Flying Saucer (XHTMLRenderer)
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocumentFromString(htmlContent);
//        renderer.layout();
//
//        // Create an output stream to write the PDF
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        renderer.createPDF(outputStream);
//
//        // Set response headers for downloading the PDF
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");
//
//        // Write the PDF to the response output stream
//        OutputStream out = response.getOutputStream();
//        outputStream.writeTo(out);
//        out.flush();
//    }
//
//}
