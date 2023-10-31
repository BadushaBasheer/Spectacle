package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String username;

    private String password;

    @ToString.Exclude
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    @ToString.Exclude
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    private long otp;

    @ToString.Exclude
    private String referralCode;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name="customer_roles",
            joinColumns = @JoinColumn(name="customer_id",referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name="role_id",referencedColumnName = "role_id"))
    private Collection<Role> roles;

    @ToString.Exclude
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Address> address;

    @ToString.Exclude
    @OneToOne(mappedBy = "customer")
    private Wallet wallet;

//    private String referralCode;

    private boolean is_blocked;

}