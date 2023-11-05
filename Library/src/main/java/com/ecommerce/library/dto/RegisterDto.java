package com.ecommerce.library.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterDto {

    @NotBlank(message = "First Name not be blank")
    private String firstName;

    @NotBlank(message = "Second Name not be blank")
    private String lastName;

    @NotBlank(message = "Phone number not be blank")
    private Long phoneNumber;

    @Email
    private String username;

    @NotBlank(message = "Password should not be blank")
    private String password;

    @NotBlank(message = "Please re-enter the correct password")
    private String repeatPassword;
}