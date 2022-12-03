package com.shop.buysell.controllers;

import com.shop.buysell.models.User;
import com.shop.buysell.services.UserService;
import com.shop.buysell.userdto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profileUser(Model model,
                              Principal principal){
        if(principal== null){
            throw new RuntimeException("Yoy are not authorize");
        }
        UserDTO userDTO = userService.findByEmail(principal.getName());

        model.addAttribute("user", userDTO);
        return "profile";
    }

}
