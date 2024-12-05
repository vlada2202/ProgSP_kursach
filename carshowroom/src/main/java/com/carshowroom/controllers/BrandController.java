package com.carshowroom.controllers;

import com.carshowroom.controllers.Main.Attributes;
import com.carshowroom.models.Brand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/brand")
public class BrandController extends Attributes {

    @GetMapping
    public String brand(Model model) {
        getCurrentUserAndRole(model);
        model.addAttribute("brands", getUser().getBrands());
        return "brand";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name) {
        brandRepository.save(new Brand(name, getUser()));
        return "redirect:/brand";
    }

    @PostMapping("/{id}/edit")
    public String edit(@RequestParam String name, @PathVariable Long id) {
        Brand brand = brandRepository.getReferenceById(id);
        brand.setName(name);
        brandRepository.save(brand);
        return "redirect:/brand";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        brandRepository.deleteById(id);
        return "redirect:/brand";
    }
}