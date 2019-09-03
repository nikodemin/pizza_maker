package com.t_systems.webstore.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class IngredientDto {
    private String name;
    private Integer price;
    private List<String> categories;
}
