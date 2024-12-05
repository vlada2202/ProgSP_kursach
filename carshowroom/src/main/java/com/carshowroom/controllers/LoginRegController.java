package com.carshowroom.controllers;

import com.carshowroom.controllers.Main.Attributes;
import com.carshowroom.models.enums.Role;
import com.carshowroom.models.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginRegController extends Attributes {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("role", getRole());
        return "login";
    }

    @GetMapping("/reg")
    public String reg(Model model) {
        model.addAttribute("role", getRole());
        return "registration";
    }

    @PostMapping("/reg")
    public String addUser(Model model, @RequestParam String username, @RequestParam String password) {
        if (userRepository.findAll().isEmpty()) {
            userRepository.save(new Users(username, password, Role.ADMIN));
            return "redirect:/login";
        }
        Users userFromDB = userRepository.findByUsername(username);
        if (userFromDB != null) {
            model.addAttribute("role", getRole());
            model.addAttribute("message", "Пользователь c таким именем уже существует существует!");
            return "registration";
        }
        userRepository.save(new Users(username, password, Role.USER));

        return "redirect:/login";
    }
}
