package com.t_systems.webstore.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class IngredientDto {
    private String name;
    private Integer priceDollars;
    private Integer priceCents;
    private List<String> categories;

    public Integer getPrice() {
        if(priceDollars == null || priceCents==null)
            return null;
        return priceDollars*100 + priceCents;
    }

    public void setPrice(Integer price) {
        priceDollars = price/100;
        priceCents = price%100;
    }
}
