package com.ecommerce.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
