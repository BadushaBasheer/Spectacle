package com.ecommerce.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderID;

    private Date orderDate;

    private String customerName;

    private double paymentAmount;

    private String paymentNumber;

    private Date paymentDate;

    private String companyName;
}
