package com.t_systems.webstore.model.dto;

import lombok.Data;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDto {

    private String name;
    private Integer price;
    private Integer spicy;
    private String image;
    private CategoryDto category;
    private CommonsMultipartFile[] files;
    private List<TagDto> tags;
    private List<IngredientDto> ingredients;
    private Boolean isCustom = false;

    public List<IngredientDto> getSubListIngredients() {
        if (ingredients.size() >= 2)
            return ingredients.subList(0, ingredients.size() - 2);
        else
            return new ArrayList<>();
    }

    public IngredientDto getLastIngredient() {
        if (ingredients.size() >= 1)
            return ingredients.get(ingredients.size() - 1);
        else
            return new IngredientDto();
    }
}
