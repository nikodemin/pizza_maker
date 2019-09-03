package com.t_systems.webstore.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDto {
    private String name;
    private List<String> categories;
}
