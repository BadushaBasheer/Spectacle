package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.SalesReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SalesReportServiceImpl implements SalesReportService {

    private ProductRepository productRepository;

    private OrderDetailRepository orderDetailRepository;


    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Object[]> findProductsSoldAndEarnings() {
        return orderDetailRepository.findProductsSoldAndRevenue();
    }

    @Override
    public List<Object[]> findProductsSoldAndEarningsFilter(int month, int year) {
        return orderDetailRepository.findProductsSoldAndRevenueByMonthAndYear(month,year);
    }
}
