package com.carshowroom.controllers;

import com.carshowroom.controllers.Main.Attributes;
import com.carshowroom.models.*;
import com.carshowroom.models.enums.Fuel;
import com.carshowroom.models.enums.Transmission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/cars")
public class CarsController extends Attributes {

    @GetMapping("/{id}")
    public String car(@PathVariable Long id, Model model) {
        if (!carRepository.existsById(id)) return "redirect:/catalog";
        model.addAttribute("s", carRepository.getReferenceById(id));
        model.addAttribute("role", getRole());
        model.addAttribute("user", getUser());
        return "car";
    }

    @PostMapping("/comment/add/{id}")
    public String comment_add(@PathVariable Long id, @RequestParam String date, @RequestParam String comment) {
        Cars car = carRepository.getReferenceById(id);
        car.addComment(new Comments(getUser().getUsername(), date, comment));
        carRepository.save(car);
        return "redirect:/cars/{id}";
    }

    @GetMapping("/{carId}/comment/{commentId}/delete")
    public String comment_delete(@PathVariable Long carId, @PathVariable Long commentId) {
        commentRepository.deleteById(commentId);
        return "redirect:/cars/{carId}";
    }

    @GetMapping("/buy/{id}")
    public String buy(@PathVariable Long id) {
        int count = 1;
        Cars car = carRepository.getReferenceById(id);

        Users user = getUser();
        user.addCart(new Carts(count, car.getIncome().getPrice(), (car.getIncome().getPrice() * count), car));
        userRepository.save(user);

        car.setCount(car.getCount() - count);

        car.getIncome().setCount(car.getIncome().getCount() + count);
        car.getIncome().setIncome(car.getIncome().getIncome() + (count * car.getIncome().getPrice()));

        carRepository.save(car);

        return "redirect:/cars/{id}";
    }

    @GetMapping("/add/{id}")
    public String car_add(@PathVariable Long id, Model model) {
        model.addAttribute("dealership", dealershipRepository.getReferenceById(id));
        model.addAttribute("role", getRole());
        model.addAttribute("fuels", Fuel.values());
        model.addAttribute("transmissions", Transmission.values());
        return "car_add";
    }

    @PostMapping("/add")
    public String car_add(
            Model model, @RequestParam long dealershipId, @RequestParam String name, @RequestParam int discount,
            @RequestParam MultipartFile poster, @RequestParam MultipartFile[] screenshots, @RequestParam String origin,
            @RequestParam int year, @RequestParam int price, @RequestParam int count, @RequestParam String description,
            @RequestParam float capacity, @RequestParam int mileage, @RequestParam int consumption,
            @RequestParam Fuel fuel, @RequestParam Transmission transmission, @RequestParam String dateStart,
            @RequestParam String dateEnd
    ) {
        try {
            // Проверка, что дата начала не прошла
            LocalDate today = LocalDate.now();
            LocalDate start = LocalDate.parse(dateStart);
            if (start.isBefore(today)) {
                model.addAttribute("message", "Данные даты прошли. Попробуйте еще раз!");
                model.addAttribute("dealership", dealershipRepository.getReferenceById(dealershipId));
                model.addAttribute("role", getRole());
                model.addAttribute("fuels", Fuel.values());
                model.addAttribute("transmissions", Transmission.values());
                return "car_add";
            }

            String uuidFile = UUID.randomUUID().toString();
            String result_poster = "";
            if (poster != null && !Objects.requireNonNull(poster.getOriginalFilename()).isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                result_poster = uuidFile + "_" + poster.getOriginalFilename();
                poster.transferTo(new File(uploadPath + "/" + result_poster));
            }

            String[] result_screenshots = new String[0];
            if (screenshots != null && !Objects.requireNonNull(screenshots[0].getOriginalFilename()).isEmpty()) {
                uuidFile = UUID.randomUUID().toString();
                String result_screenshot;
                result_screenshots = new String[screenshots.length];
                for (int i = 0; i < result_screenshots.length; i++) {
                    result_screenshot = uuidFile + "_" + screenshots[i].getOriginalFilename();
                    screenshots[i].transferTo(new File(uploadPath + "/" + result_screenshot));
                    result_screenshots[i] = result_screenshot;
                }
            }

            Dealerships dealership = dealershipRepository.getReferenceById(dealershipId);
            dealership.addCar(new Cars(name, origin, description, result_poster, result_screenshots, year, price, count, discount, capacity, mileage, consumption, fuel, transmission, dateStart, dateEnd));
            dealershipRepository.save(dealership);

        } catch (Exception e) {
            model.addAttribute("message", "Некорректные данные!");
            model.addAttribute("dealership", dealershipRepository.getReferenceById(dealershipId));
            model.addAttribute("role", getRole());
            model.addAttribute("fuels", Fuel.values());
            model.addAttribute("transmissions", Transmission.values());
            return "car_add";
        }
        return "redirect:/catalog/all";
    }

    @GetMapping("/edit/{id}")
    public String car_edit(@PathVariable(value = "id") Long id, Model model) {
        if (!carRepository.existsById(id)) return "redirect:/catalog";
        model.addAttribute("s", carRepository.getReferenceById(id));
        model.addAttribute("role", getRole());
        model.addAttribute("fuels", Fuel.values());
        model.addAttribute("transmissions", Transmission.values());
        return "car_edit";
    }

    @PostMapping("/edit/{id}")
    public String car_edit(
            @PathVariable Long id, Model model, @RequestParam String name, @RequestParam int discount,
            @RequestParam MultipartFile poster, @RequestParam MultipartFile[] screenshots, @RequestParam String origin,
            @RequestParam int year, @RequestParam int price, @RequestParam int count, @RequestParam String description,
            @RequestParam float capacity, @RequestParam int mileage, @RequestParam int consumption,
            @RequestParam Fuel fuel, @RequestParam Transmission transmission, @RequestParam String dateStart,
            @RequestParam String dateEnd
    ) {
        try {
            Cars car = carRepository.getReferenceById(id);

            // Проверяем, что даты не прошли
            LocalDate today = LocalDate.now();
            LocalDate start = LocalDate.parse(dateStart);
            if (start.isBefore(today)) {
                model.addAttribute("message", "Данные даты прошли. Попробуйте еще раз!");
                model.addAttribute("s", car);
                model.addAttribute("role", getRole());
                model.addAttribute("fuels", Fuel.values());
                model.addAttribute("transmissions", Transmission.values());
                return "car_edit";
            }

            car.set(name, origin, description, year, price, count, discount, capacity, mileage, consumption, fuel, transmission, dateStart, dateEnd);

            String uuidFile = UUID.randomUUID().toString();
            if (poster != null && !Objects.requireNonNull(poster.getOriginalFilename()).isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                String result_poster = uuidFile + "_" + poster.getOriginalFilename();
                poster.transferTo(new File(uploadPath + "/" + result_poster));
                car.setPoster(result_poster);
            }

            if (screenshots != null && !Objects.requireNonNull(screenshots[0].getOriginalFilename()).isEmpty()) {
                uuidFile = UUID.randomUUID().toString();
                String result_screenshot;
                String[] result_screenshots = new String[screenshots.length];
                for (int i = 0; i < result_screenshots.length; i++) {
                    result_screenshot = uuidFile + "_" + screenshots[i].getOriginalFilename();
                    screenshots[i].transferTo(new File(uploadPath + "/" + result_screenshot));
                    result_screenshots[i] = result_screenshot;
                }
                car.setScreenshots(result_screenshots);
            }
            carRepository.save(car);
        } catch (Exception e) {
            model.addAttribute("message", "Некорректные данные!");
            model.addAttribute("s", carRepository.getReferenceById(id));
            model.addAttribute("role", getRole());
            model.addAttribute("fuels", Fuel.values());
            model.addAttribute("transmissions", Transmission.values());
            return "car_edit";
        }
        return "redirect:/cars/{id}/";
    }
    @GetMapping("/delete/{id}")
    public String car_delete(@PathVariable Long id) {
        carRepository.deleteById(id);
        return "redirect:/catalog/all";
    }
}
