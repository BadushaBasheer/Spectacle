package com.ecommerce.library.repository;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByUsername(String username);

    Customer findById(long id);
    Optional<Customer> findByEmail(String email);


    Customer findByReferralCode(String referralCode);
    Customer findByEmailAndPhoneNumber(String Username,String phoneNumber);

    @Query("UPDATE  Customer  c SET c.is_blocked=true  WHERE c.id=?1")
    public void enable(Long Id);


//    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    List<Customer> findByFirstNameStartingWithIgnoreCase(String firstName);

//==========================================================================
}
