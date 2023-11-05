package com.ecommerce.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

    @NotBlank
    @Size(min = 3, max = 10, message = "Invalid first name!!")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 10, message = "Invalid last name!!")
    private String lastName;

    private String email;

    @NotBlank
    @Size(min = 5, max = 10, message = "password must be strong!!")
    private String password;

    private String repeatPassword;

}
