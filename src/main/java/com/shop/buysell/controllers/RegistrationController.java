package com.shop.buysell.controllers;

import com.shop.buysell.models.User;
import com.shop.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/registration")
    public String createUser(User user, Model model){

        if(!userService.create(user)){
            model.addAttribute("errorMessage", "Пользователь с email: " +
                    user.getEmail() + " уже существует");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/registration")
    public String registration(){
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

        return "products";

    }



}
