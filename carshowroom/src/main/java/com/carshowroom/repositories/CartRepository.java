package com.carshowroom.repositories;

import com.carshowroom.models.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Carts, Long> {
}
