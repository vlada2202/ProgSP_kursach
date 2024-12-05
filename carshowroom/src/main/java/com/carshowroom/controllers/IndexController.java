package com.carshowroom.controllers;

import com.carshowroom.controllers.Main.Attributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends Attributes {

    @GetMapping
    public String index1(Model model) {
        model.addAttribute("role", getRole());
        return "index";
    }

    @GetMapping("/index")
    public String index2(Model model) {
        model.addAttribute("role", getRole());
        return "index";
    }
}