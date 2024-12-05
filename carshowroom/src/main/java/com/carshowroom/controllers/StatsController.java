package com.carshowroom.controllers;

import com.carshowroom.controllers.Main.Attributes;
import com.carshowroom.models.Cars;
import com.carshowroom.models.Dealerships;
import com.carshowroom.models.Users;
import com.carshowroom.models.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class StatsController extends Attributes {

    @GetMapping("/stats")
    public String sales(Model model) {
        List<Dealerships> dealerships = new ArrayList<>();

        if (getUser().getRole() == Role.ADMIN) {
            dealerships = dealershipRepository.findAll();

            List<Users> dealers = userRepository.findAllByRole(Role.MANAGER);

            String[] dealerString = new String[dealers.size()];
            int[] dealerInt = new int[dealers.size()];

            for (int i = 0; i < dealers.size(); i++) {
                dealerString[i] = dealers.get(i).getUsername();
                dealerInt[i] = dealers.get(i).getDealerships().stream().reduce(0, (intD, dealership) -> intD + dealership.getCars().stream().reduce(0, (intC, car) -> intC + car.getIncome().getIncome(), Integer::sum), Integer::sum);
            }

            model.addAttribute("dealerString", dealerString);
            model.addAttribute("dealerInt", dealerInt);
        } else if (getUser().getRole() == Role.MANAGER) {
            dealerships = getUser().getDealerships();
        }

        List<Cars> cars = new ArrayList<>();
        for (Dealerships d : dealerships) {
            cars.addAll(d.getCars());
        }

        int income = cars.stream().reduce(0, (i, car) -> i + car.getIncome().getIncome(), Integer::sum);

        model.addAttribute("income", income);
        model.addAttribute("cars", cars);
        model.addAttribute("role", getRole());

        cars.sort(Comparator.comparing(Cars::getCount));
        Collections.reverse(cars);

        String[] topName = new String[5];
        int[] topNum = new int[5];

        for (int i = 0; i < cars.size(); i++) {
            if (i == 5) break;
            topName[i] = cars.get(i).getName();
            topNum[i] = cars.get(i).getIncome().getIncome();
        }

        model.addAttribute("topName", topName);
        model.addAttribute("topNum", topNum);

        return "stats";
    }
}
