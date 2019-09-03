package com.t_systems.webstore.validator;

import com.t_systems.webstore.model.dto.UserDto;
import com.t_systems.webstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == UserDto.class;
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserDto form = (UserDto) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.registerForm.userName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.registerForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirm", "NotEmpty.registerForm.confirmPassword");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.registerForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty.registerForm.firstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty.registerForm.lastName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateOfBirth", "NotEmpty.registerForm.dateOfBirth");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "country", "NotEmpty.registerForm.country");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "NotEmpty.registerForm.city");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "street", "NotEmpty.registerForm.street");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "house", "NotEmpty.registerForm.house");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "flat", "NotEmpty.registerForm.flat");

        if (userService.findUser(form.getUsername()) != null)
            errors.rejectValue("username", "Duplicate.registerForm.username");
        if (userService.findUserByEmail(form.getEmail()) != null)
            errors.rejectValue("username", "Duplicate.registerForm.email");
        if (!form.getPassword().equals(form.getConfirm()))
            errors.rejectValue("confirm", "DontMatch.registerForm.password");
    }
}
