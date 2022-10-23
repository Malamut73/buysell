package com.shop.buysell.controllers;

import com.shop.buysell.models.User;
import com.shop.buysell.models.enums.Role;
import com.shop.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class AdminController {

    private final UserService userService;

    @GetMapping("/admin")
    public  String admin(Model model){
        model.addAttribute("users", userService.findAllUsers());
        return "admin";
    }

    @PostMapping("/admin/ban/{user}")
    public String userBan(@PathVariable("user") User user){
        userService.banUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit/{user}")
    public String userEdit(@PathVariable("user") User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PostMapping("/admin/edit")
    public String userEdit(@RequestParam("id") User user,
                           @RequestParam(name="roles[]", required = false) String[] roles){
        System.out.println(user.getPhoneNumber());
        user.getRoles().clear();
        if(roles!=null) {
            Arrays.stream(roles).forEach(r -> user.getRoles().add(Role.valueOf(r)));
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }




}
