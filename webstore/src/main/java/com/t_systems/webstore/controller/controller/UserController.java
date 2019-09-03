package com.t_systems.webstore.controller.controller;

import com.t_systems.webstore.exception.UserExistsException;
import com.t_systems.webstore.model.dto.AddressDto;
import com.t_systems.webstore.model.dto.CardDto;
import com.t_systems.webstore.model.dto.UserDto;
import com.t_systems.webstore.service.UserService;
import com.t_systems.webstore.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserValidator validator;
    private final ModelMapper modelMapper;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        Object target = binder.getTarget();
        if (target == null)
            return;
        if (target.getClass() == UserDto.class)
            binder.setValidator(validator);
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("form", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(Model model,
                                      @Validated @ModelAttribute("form") UserDto userDto,
                                      BindingResult result) {
        if (result.hasErrors())
            return "register";

        try {
            userService.addUser(userDto);
        } catch (UserExistsException e) {
            e.printStackTrace();
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model, HttpSession session) {
        model.addAttribute("cart", session.getAttribute("cart"));
        return "cart";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/logout")
    public String getAfterLogoutPage(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/login";
    }

    @GetMapping("/payment")
    public String getPaymentPage(Model model, Principal principal){
        model.addAttribute("card",new CardDto());
        AddressDto addressDto = modelMapper.map(userService.
                findUser(principal.getName()).getAddress(),AddressDto.class);
        model.addAttribute("address",addressDto);
        return "payment";
    }

    @GetMapping("/confirmation")
    public String getConfirmationPage(Model model){
        model.addAttribute("text","Order accepted! Wait for phone call!");
        return "text";
    }

    @GetMapping("/customProduct")
    public String getCustomProductPage(){
        return "customProduct";
    }
}
