package com.t_systems.webstore.model.dto;

import lombok.Data;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Data
public class CategoryDto {

    private String name;
    private CommonsMultipartFile[] files;
    private String image;
}
