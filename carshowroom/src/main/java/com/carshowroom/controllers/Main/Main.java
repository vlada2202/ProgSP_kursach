package com.carshowroom.controllers.Main;

import com.carshowroom.models.Users;
import com.carshowroom.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Main {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected CarRepository carRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected IncomeRepository incomeRepository;
    @Autowired
    protected DealershipRepository dealershipRepository;
    @Autowired
    protected BrandRepository brandRepository;
    @Value("${upload.path}")
    protected String uploadPath;

    protected Users getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            return userRepository.findByUsername(userDetail.getUsername());
        }
        return null;
    }

    protected String getRole() {
        Users users = getUser();
        if (users == null) return "NOT";
        return users.getRole().toString();
    }
}
