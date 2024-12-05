package com.carshowroom.controllers;

import com.carshowroom.controllers.Main.Attributes;
import com.carshowroom.models.Users;
import com.carshowroom.models.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersController extends Attributes {

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", Role.values());
        model.addAttribute("role", getRole());
        return "users";
    }

    @PostMapping("/edit/{id}")
    public String userUpdate(@PathVariable Long id, @RequestParam Role role) {
        Users user = userRepository.getReferenceById(id);
        user.setRole(role);
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/app/{id}")
    public String app(@PathVariable(value = "id") Long id) {
        Users user = userRepository.getReferenceById(id);
        user.setApp(false);
        user.setRole(Role.MANAGER);
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String userDelete(@PathVariable(value = "id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }
}
