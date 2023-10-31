package com.ecommerce.library.service;

import com.ecommerce.library.model.Invoice;

import java.util.List;

public interface InvoiceService {

    List<Invoice> getAllInvoices();

    Invoice getInvoiceById(Long id);

    Invoice createInvoice(Invoice invoice);

    void deleteInvoice(Long id);
}
