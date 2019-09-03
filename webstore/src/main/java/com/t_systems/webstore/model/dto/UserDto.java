package com.t_systems.webstore.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank(message = "Please fill it")
    private String firstName;
    @NotBlank(message = "Please fill it")
    private String lastName;
    @NotBlank(message = "Please fill it")
    private String dateOfBirth;
    @NotBlank(message = "Please fill it")
    private String username;
    @NotBlank(message = "Please fill it")
    @Email
    private String email;
    @NotBlank(message = "Please fill it")
    private String password;
    private String confirm;

    @NotBlank(message = "Please fill it")
    private String country;
    @NotBlank(message = "Please fill it")
    private String city;
    @NotBlank(message = "Please fill it")
    private String street;
    @NotBlank(message = "Please fill it")
    private String house;
    @NotBlank(message = "Please fill it")
    private String flat;
}
