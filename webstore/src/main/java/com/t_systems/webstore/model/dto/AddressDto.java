package com.t_systems.webstore.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddressDto {
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
