package com.t_systems.webstore.validator;

import com.t_systems.webstore.model.dto.CategoryDto;
import com.t_systems.webstore.model.dto.ProductDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Component
public class ImageValidator implements Validator {

    /**
     * supported classes
     * @param aClass target class
     * @return boolean: class supported or not
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == CategoryDto.class || aClass == ProductDto.class;
    }

    /**
     * validate image
     * @param o image dto
     * @param errors errors
     */
    @Override
    public void validate(Object o, Errors errors) {
        CommonsMultipartFile[] files = null;

        if(o instanceof CategoryDto) {
            files = ((CategoryDto) o).getFiles();
        }
        if(o instanceof ProductDto) {
            files = ((ProductDto) o).getFiles();
        }

        if (files == null)
            return;

        for (CommonsMultipartFile file : files) {
            if(file.isEmpty())
                errors.rejectValue("file","File is empty");
            if(file.getContentType().contains("jpeg") ||
                    file.getContentType().contains("jpg") ||
                    file.getContentType().contains("png") ||
                    file.getContentType().contains("gif"))
                continue;
            errors.rejectValue("file","File is not a image");
        }
    }
}
