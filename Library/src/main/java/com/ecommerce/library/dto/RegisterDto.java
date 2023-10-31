package com.ecommerce.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {

    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String username;
    private String password;
    private String repeatPassword;
}