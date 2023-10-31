package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    private Date orderDate;

    private Date deliveryDate;

    private double totalPrice;

    private double shippingFee;

    private String orderStatus;

    private String paymentMethod;

    private String paymentStatus;

    private boolean isAccept;

    private int quantity;

    @Column(nullable = true)
    private Double discountPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id",referencedColumnName = "address_id")
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DETACH})
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetailList;

    @Column(nullable = true)
    private LocalDateTime confirmedDateTime;

    @Column(nullable = true)
    private LocalDateTime shippedDateTime;

    @Column(nullable = true)
    private LocalDateTime deliveredDateTime;

}
