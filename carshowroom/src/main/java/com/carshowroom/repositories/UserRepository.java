package com.carshowroom.repositories;

import com.carshowroom.models.Users;
import com.carshowroom.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    List<Users> findAllByRole(Role role);
}
