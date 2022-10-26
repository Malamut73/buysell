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
public class UserController {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/hello")
    public String securityUrl(){
        return "hello";
    }

    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model){
        model.addAttribute("user", user);
        return "user-info";
    }

}
