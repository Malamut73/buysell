package com.shop.buysell.controllers;

import com.shop.buysell.models.User;
import com.shop.buysell.services.UserService;
import com.shop.buysell.userdto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/registration")
    public String createUser(@Valid UserDTO userDTO,
                             BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "registration";
        }

        if (!Objects.equals(userDTO.getPassword(), userDTO.getMatchPassword())) {
            model.addAttribute("errorMessage", "Пароли не совподают");
            return "registration";        }

        if(!userService.create(userDTO)){
            model.addAttribute("errorMessage", "Пользователь с email: " +
                    userDTO.getEmail() + " уже существует");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("userDTO", new UserDTO());
        return "registration";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){

        boolean isActive = userService.activateUser(code);

        if(isActive){
            model.addAttribute("message", "User successfully activated");
        }else{
            model.addAttribute("message", "Activation cod is not found");
        }

        return "index";

    }



}
