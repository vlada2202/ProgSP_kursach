package com.carshowroom.controllers.Main;

import org.springframework.ui.Model;

public class Attributes extends Main {
    protected void getCurrentUserAndRole(Model model) {
        model.addAttribute("role", getRole());
        model.addAttribute("user", getUser());
    }

    protected void AddAttributesDealershipEdit(Model model, Long id) {
        getCurrentUserAndRole(model);
        model.addAttribute("dealership", dealershipRepository.getReferenceById(id));
    }
}