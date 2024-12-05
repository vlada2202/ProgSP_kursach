package com.carshowroom.controllers;

import com.carshowroom.controllers.Main.Attributes;
import com.carshowroom.models.Cars;
import com.carshowroom.models.Carts;
import com.carshowroom.models.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController extends Attributes {
    @GetMapping
    public String cart(Model model) {
        model.addAttribute("user", getUser());
        model.addAttribute("role", getRole());
        return "cart";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        Carts cart = cartRepository.getReferenceById(id);
        Cars car = cart.getCar();
        car.setCount(car.getCount() + cart.getCount());
        car.getIncome().setCount(car.getIncome().getCount() - cart.getCount());
        car.getIncome().setIncome(car.getIncome().getIncome() - (cart.getCount() * car.getIncome().getPrice()));
        carRepository.save(car);
        cartRepository.deleteById(id);
        return "redirect:/cart";
    }

    @GetMapping("/app")
    public String app() {
        Users user = getUser();
        user.setApp(true);
        userRepository.save(user);
        return "redirect:/cart";
    }
}
