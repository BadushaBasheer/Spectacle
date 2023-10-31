//package com.ecommerce.admin.Controller;
//
//
//import com.ecommerce.library.model.Category;
//import com.ecommerce.library.service.OrderService;
//import com.ecommerce.library.service.ProductService;
//import com.ecommerce.library.service.StockReportService;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@Controller
//public class StockReportController {
//    @Autowired
//    private StockReportService stockReportService;
//
//    @GetMapping("/stock-report")
//    public String viewStock(){
//        return
//    }
//
//    @GetMapping("/generate")
//    public ResponseEntity<byte[]> generateStockReport(@RequestParam("category") Category category) {
//        byte[] reportBytes = stockReportService.generateStockReport(category);
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "stock-report.pdf");
//
//        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
//    }
//}
//
