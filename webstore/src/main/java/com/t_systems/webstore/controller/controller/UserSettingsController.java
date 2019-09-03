package com.t_systems.webstore.controller.controller;

import com.t_systems.webstore.model.dto.UserDto;
import com.t_systems.webstore.model.entity.User;
import com.t_systems.webstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;

@Controller
@RequiredArgsConstructor
public class UserSettingsController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostConstruct
    private void init(){
        Converter<User,UserDto> toUserDtoConverter = new AbstractConverter<User, UserDto>() {
            @Override
            protected UserDto convert(User user) {
                UserDto userDto = new UserDto();
                userDto.setUsername(user.getUsername());
                userDto.setLastName(user.getLastName());
                userDto.setFirstName(user.getFirstName());
                userDto.setEmail(user.getEmail());
                userDto.setDateOfBirth(user.getDateOfBirth().toString());
                userDto.setCountry(user.getAddress().getCountry());
                userDto.setCity(user.getAddress().getCity());
                userDto.setStreet(user.getAddress().getStreet());
                userDto.setHouse(user.getAddress().getHouse());
                userDto.setFlat(user.getAddress().getFlat());
                userDto.setPassword(null);
                return userDto;
            }
        };
        modelMapper.addConverter(toUserDtoConverter);
    }

    @GetMapping("/settings")
    public String getSettingsPage(Model model, Principal principal) {
        UserDto userDto = modelMapper.map(userService.findUser(principal.getName()),UserDto.class);
        model.addAttribute("user", userDto);
        return "settings";
    }

    @PostMapping("/changeSettings")
    public String changeSettings(Model model, @Valid @ModelAttribute("user") UserDto userDto,
                                 BindingResult result, Principal principal, HttpServletRequest request)
            throws ParseException, ServletException {
        if (result.hasErrors()) {
            return "settings";
        }

        request.logout();
        userService.changeUser(principal.getName(), userDto);
        model.addAttribute("text","Changes accepted!");
        return "text";
    }
}
