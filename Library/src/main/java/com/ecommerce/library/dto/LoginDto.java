package com.ecommerce.library.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginDto {

    @Email
    private String email;
    @NotEmpty
    @NotBlank
    private String password;
}