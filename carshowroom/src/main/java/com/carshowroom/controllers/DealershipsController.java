package com.carshowroom.controllers;

import com.carshowroom.controllers.Main.Attributes;
import com.carshowroom.models.Dealerships;
import com.carshowroom.models.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/dealerships")
public class DealershipsController extends Attributes {

    @GetMapping
    public String dealerships(Model model) {
        model.addAttribute("dealerships", dealershipRepository.findAll());
        model.addAttribute("role", getRole());
        return "dealerships";
    }

    @GetMapping("/{id}")
    public String dealership(Model model, @PathVariable Long id) {
        model.addAttribute("dealership", dealershipRepository.getReferenceById(id));
        model.addAttribute("role", getRole());
        model.addAttribute("user", getUser());
        return "dealership";
    }

    @GetMapping("/add")
    public String dealership_add(Model model) {
        getCurrentUserAndRole(model);
        model.addAttribute("brands", getUser().getBrands());
        return "dealership_add";
    }

    @PostMapping("/add")
    public String dealership_add(Model model, @RequestParam String name, @RequestParam String contact, @RequestParam String schedule, @RequestParam Long brandId, @RequestParam MultipartFile poster, @RequestParam String address, @RequestParam String description) {
        try {
            String result_poster = "";
            String uuidFile = UUID.randomUUID().toString();
            if (poster != null && !Objects.requireNonNull(poster.getOriginalFilename()).isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                result_poster = uuidFile + "_" + poster.getOriginalFilename();
                poster.transferTo(new File(uploadPath + "/" + result_poster));
            }
            Users user = getUser();
            user.addDealership(new Dealerships(name, address, description, result_poster, brandRepository.getReferenceById(brandId), contact, schedule));
            userRepository.save(user);
        } catch (IOException e) {
            getCurrentUserAndRole(model);
            model.addAttribute("message", "Некорректные данные!");
            model.addAttribute("brands", getUser().getBrands());
            return "dealership_add";
        }
        return "redirect:/dealerships";
    }

    @GetMapping("/edit/{id}")
    public String dealership_edit(@PathVariable Long id, Model model) {
        if (!dealershipRepository.existsById(id)) return "redirect:/dealerships";
        AddAttributesDealershipEdit(model, id);
        model.addAttribute("brands", dealershipRepository.getReferenceById(id).getOwner().getBrands());
        return "dealership_edit";
    }

    @PostMapping("/edit/{id}")
    public String dealership_edit(Model model, @PathVariable Long id, @RequestParam Long brandId, @RequestParam String name, @RequestParam String contact, @RequestParam String schedule, @RequestParam String address, @RequestParam MultipartFile poster, @RequestParam String description) {
        try {
            Dealerships dealerships = dealershipRepository.getReferenceById(id);

            dealerships.setName(name);
            dealerships.setAddress(address);
            dealerships.setDescription(description);
            dealerships.setContact(contact);
            dealerships.setSchedule(schedule);
            dealerships.setBrand(brandRepository.getReferenceById(brandId));

            String uuidFile = UUID.randomUUID().toString();
            if (poster != null && !Objects.requireNonNull(poster.getOriginalFilename()).isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                String result_poster = uuidFile + "_" + poster.getOriginalFilename();
                poster.transferTo(new File(uploadPath + "/" + result_poster));
                dealerships.setPoster(result_poster);
            }

            dealershipRepository.save(dealerships);
        } catch (Exception e) {
            AddAttributesDealershipEdit(model, id);
            model.addAttribute("message", "Некорректные данные!");
            model.addAttribute("brands", getUser().getBrands());
            return "dealership_edit";
        }
        return "redirect:/dealerships/{id}";
    }

    @GetMapping("/delete/{id}")
    public String dealership_delete(@PathVariable Long id) {
        dealershipRepository.deleteById(id);
        return "redirect:/dealerships";
    }
}
