package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.LoginDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.EmailDetails;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.EmailService;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.service.CustomerService;

import com.ecommerce.library.service.WalletService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final WalletService walletService;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, WalletService walletService, EmailService emailService,
                               RoleRepository roleRepository) {
        this.customerRepository = customerRepository;
        this.walletService = walletService;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
    }


    private static final long OTP_VALID_DURATION = 1 * 60 * 1000;   // 5 minutes

    public void setOtpRequestedTime(Date otpRequestedTime) {
        this.otpRequestedTime = otpRequestedTime;
    }

    @Getter
    private Date otpRequestedTime;
    long  otpRequestedTimeMillis=0;

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public String shareReferralCode(String referralCode, String emailAddress) {
        return emailService.sendSimpleMail(new EmailDetails(emailAddress, "Hey, I wanted to share my referral code for Spectacle with you: "
                + referralCode, "Check out my referral code!"));
    }

    @Override
    public Customer findByReferralCode(String referralCode) {
        return customerRepository.findByReferralCode(referralCode);
    }

    @Override
    public CustomerDto save(@Valid CustomerDto customerDto) {
        System.out.println("In save method of customerDTO");
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        String enteredReferral = customerDto.getReferralCode();
        System.out.println(enteredReferral);
        if (enteredReferral != null) {
            try {
                Customer referralOwnerCustomer = customerRepository.findByReferralCode(enteredReferral);
                if (referralOwnerCustomer != null) {
                    boolean status = walletService.saveReferralOffer(100.00, referralOwnerCustomer);
                    if (!status) {
                        throw new RuntimeException("Referral offer transaction failed");
                    }
                }
            } catch (NullPointerException e) {
                throw new RuntimeException("No referee found");
            }
        }
        customer.setReferralCode(referralCodeGenerator());
        Customer customerSave = customerRepository.save(customer);
        return mapperDto(customerSave);
    }

    private String referralCodeGenerator() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }


    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public Customer loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findByUsername(username);
        GrantedAuthority authority = new SimpleGrantedAuthority(customer.getRoles().toString());
        UserDetails userDetails = (UserDetails) new User(customer.getUsername(),
                customer.getPassword(), Arrays.asList(authority));
        return (Customer) userDetails;
    }

    @Override
    public List<Customer> findAll() {

        return customerRepository.findAll();
    }

    //=================================================================================================================
//    @Override
//    public String generateOtp(Customer customer) {
//        try {
//            int randomPIN = (int) (Math.random() * 9000) + 1000;
//            customer.setOtp(randomPIN);
//            customer.set_active(false);
//            customerRepository.save(customer);
//            SimpleMailMessage msg = new SimpleMailMessage();
//            msg.setFrom("badusha0701@gmail.com");// input the senders email ID
//            msg.setTo(customer.getEmail());
//            msg.setSubject("Welcome To Spectacles");
//            msg.setText("Hello \n\n" +"Your Login OTP :" + randomPIN + ".Please Verify. \n\n"+"Regards \n"+"ABC");
//            javaMailSender.send(msg);
//            return "success";
//        }catch (Exception e) {
//            e.printStackTrace();
//            return "error";
//        }
//    }
//===================================================================================================================
    @Override
    public Customer findById(long id) {
        Customer customer = customerRepository.findById(id);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPhoneNumber(customerDto.getPhoneNumber());
        customerDto.setPassword(customer.getPassword());
        return customer;
    }

    @Override
    public Customer updateCustomer(CustomerDto customer) {
        Customer customers = customerRepository.getReferenceById(customer.getId());
        customers.setUsername(customer.getUsername());
        customers.setPhoneNumber(customers.getPhoneNumber());
        customers.set_blocked(customer.is_blocked());
        return customers;
    }

    @Override
    public void blockById(Long id) {

        Customer customer = customerRepository.getReferenceById(id);
        customer.set_blocked(true);
        customerRepository.save(customer);
    }

    @Override
    public void unblockById(Long id) {

        Customer customer = customerRepository.getReferenceById(id);
        customer.set_blocked(false);
        customerRepository.save(customer);

    }

    @Override
    public List<CustomerDto> searchUser(String firstName) {
        List<Customer> customers = customerRepository.findByFirstNameStartingWithIgnoreCase(firstName);
        return customers.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private CustomerDto convertEntityToDto(Customer customer) {
        CustomerDto customerDTO = new CustomerDto();
        customerDTO.setId(customer.getId());
        String[] name = customer.getFirstName().split(" ");
        if (name.length >= 2) {
            customerDTO.setFirstName(name[0]);
            customerDTO.setLastName(name[1]);
        } else {
            customerDTO.setFirstName(customer.getFirstName());
            customerDTO.setLastName(customer.getLastName());
        }
        customerDTO.setUsername(customer.getUsername());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        return customerDTO;
    }
//===================================================================================================

    private CustomerDto mapperDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPassword(customer.getPassword());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPhoneNumber(customerDto.getPhoneNumber());
        return customerDto;
    }


    public String login(LoginDto loginDto) {
        Customer customer = customerRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(
                        () -> new RuntimeException("User not found with this email: " + loginDto.getEmail()));
        if (!loginDto.getPassword().equals(customer.getPassword())) {
            return "Password is incorrect";
        } else if (!customer.is_blocked()) {
            return "your account is not verified";
        }
        return "Login successful";
    }

    @Override
    public boolean verifyOTP(long otp, String username) {
        Customer customer = customerRepository.findByUsername(username);
        long currentTimeInMillis = System.currentTimeMillis();
        System.out.println("currentTimeInMillis:" + currentTimeInMillis);
        System.out.println("otpRequestedTimeMillis" + otpRequestedTimeMillis);
        if (otpRequestedTimeMillis + OTP_VALID_DURATION > currentTimeInMillis) {
            if (otp == customer.getOtp())
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    @Override
    public String genrateOTPAndSendOnEmail(String username) {
        Customer customer = customerRepository.findByUsername(username);
        int otp = (int) (Math.random() * 9000) + 1000;
        customer.setOtp(otp);
        customerRepository.save(customer);
        setOtpRequestedTime(new Date());
        otpRequestedTimeMillis = otpRequestedTime.getTime();
        ;
        return emailService.sendSimpleMail(new EmailDetails(username, "Your OTP for verification is " + otp, "Verify with OTP"));
    }
}
    //    public boolean verify(String verficationCode) {
//        Customer customer = customerRepository.findByVerificationCode(verficationCode);
//        if (customer == null || customer.is_blocked()) {
//            return false;
//        } else {
//            customerRepository.enable(customer.getId());
//            return true;
//        }
//    }
//    ========================================================================================================================


//    public String register(RegisterDto registerDto) {
////        String otp = otpUtil.generateOTP();
////        try {
////            emailUtil.sendOtpEmail(registerDto.getUsername(), otp);
////        } catch (MessagingException e) {
////            throw new RuntimeException("Unable to send otp please try again");
////        }
//        Customer customer = new Customer();
//        customer.setFirstName(registerDto.getFirstName());
//        customer.setLastName(registerDto.getLastName());
//        customer.setUsername(registerDto.getUsername());
//        customer.setPhoneNumber(registerDto.getPhoneNumber());
//        customer.setPassword(registerDto.getPassword());
////        customer.setOtp(otp);
////        customer.setOtpGeneratedTime(LocalDateTime.now());
//        customerRepository.save(customer);
//        return "User registration successful";
//    }

//    public String verifyAccount(String email, String otp) {
//        Customer customer = customerRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
//        if (customer.getOtp().equals(otp) && Duration.between(customer.getOtpGeneratedTime(),
//                LocalDateTime.now()).getSeconds() < (1 * 60)) {
//            customer.set_blocked(true);
//            customerRepository.save(customer);
//            return "OTP verified you can login";
//        }
//        return "Please regenerate otp and try again";
//    }
//
//    public String regenerateOtp(String email) {
//        Customer customer = customerRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
//        String otp = otpUtil.generateOTP();
//        try {
//            emailUtil.sendOtpEmail(email, otp);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Unable to send otp please try again");
//        }
//        customer.setOtp(otp);
//        customer.setOtpGeneratedTime(LocalDateTime.now());
//        customerRepository.save(customer);
//        return "Email sent... please verify account within 1 minute";
//    }


//    @Override
//    public void sendVerificationEmail(CustomerDto customer, String siteURL) throws MessagingException, UnsupportedEncodingException {
//        String subject = "Please verify your registration";
//        String senderName = "Coza Store Team";
//        String mailContent = "<p> Dear " + customer.getFirstName() + ",</p>";
//        mailContent += "Please click the link to verify to registration: +</p>";
//
//        String verifyURL = siteURL + "/verify?code=" + customer.getVerificationCode();
//
//        mailContent += "<h3><a href=\" " + verifyURL + " \" >VERIFY</a></h3>";
//        mailContent += "<p>Thank You<br> Coza Store Team</p>";
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//        helper.setFrom("badusha07@gmail.com", senderName);
//        helper.setTo(customer.getUsername());
//        helper.setSubject(subject);
//        helper.setText(mailContent, true);
//        javaMailSender.send(message);
//    }




