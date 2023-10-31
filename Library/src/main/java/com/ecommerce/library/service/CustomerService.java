package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.LoginDto;
import com.ecommerce.library.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerDto save(CustomerDto customerDto);

    Optional<Customer> findByEmail(String email);

    String shareReferralCode(String referralCode, String emailAddress);

    Customer findByReferralCode(String referralCode);

    Customer findByUsername(String username);

    Customer loadUserByUsername(String username);

//    Customer findByUsername(String username);

    List<Customer> findAll();

//    String generateOtp(Customer customer);

    Customer findById(long id);

    Customer updateCustomer(CustomerDto customer);

    void blockById(Long id);

    void unblockById(Long id);

//    boolean verify(String code);

    //=========================================================================
    List<CustomerDto> searchUser(String firstName);

//    String verifyAccount(String email, String otp);
//
//    String regenerateOtp(String email);

    String login(LoginDto loginDto);

    boolean verifyOTP(long otp, String username);

    String genrateOTPAndSendOnEmail(String username);

    //=========================================================================

}