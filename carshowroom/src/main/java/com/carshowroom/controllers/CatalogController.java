package com.carshowroom.controllers;

import com.carshowroom.controllers.Main.Attributes;
import com.carshowroom.models.enums.Fuel;
import com.carshowroom.models.enums.Transmission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogController extends Attributes {
    @GetMapping("/catalog")
    public String catalog(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("role", getRole());
        model.addAttribute("fuels", Fuel.values());
        model.addAttribute("transmissions", Transmission.values());
        return "cars";
    }

    @GetMapping("/catalog/all")
    public String catalog_main(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("role", getRole());
        model.addAttribute("fuels", Fuel.values());
        model.addAttribute("transmissions", Transmission.values());
        return "cars";
    }

    @GetMapping("/catalog/search")
    public String search(Model model, @RequestParam String name, @RequestParam Fuel fuel, @RequestParam Transmission transmission) {
        model.addAttribute("role", getRole());
        model.addAttribute("fuels", Fuel.values());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("cars", carRepository.findAllByNameContainingAndFuelAndTransmission(name, fuel, transmission));
        model.addAttribute("name", name);
        model.addAttribute("fuel", fuel);
        model.addAttribute("transmission", transmission);
        return "cars";
    }

    @GetMapping("/catalog/search/name")
    public String catalogSearch(@RequestParam String search, Model model) {
        model.addAttribute("role", getRole());
        model.addAttribute("cars", carRepository.findAllByNameContaining(search));
        model.addAttribute("fuels", Fuel.values());
        model.addAttribute("transmissions", Transmission.values());
        return "cars";
    }
}
