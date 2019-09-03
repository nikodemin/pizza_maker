package com.t_systems.webstore.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CardDto {
    @NotBlank
    @Pattern(regexp = "([0-9]{4}( )?){3}[0-9]{4}")
    private String cardNumber;
    @NotBlank
    private String month;
    @NotBlank
    @Pattern(regexp = "[0-9]{4}")
    private String year;
    @NotBlank
    @Pattern(regexp = "[0-9]{3}")
    private String cvv;
}
