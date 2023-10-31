package com.ecommerce.library.service;

import com.ecommerce.library.model.Product;

import java.util.List;

public interface SalesReportService {

    List<Product> findAllProducts();

    List<Object[]> findProductsSoldAndEarnings();

    List<Object[]> findProductsSoldAndEarningsFilter(int month,int year);
}