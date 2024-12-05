package com.carshowroom.repositories;

import com.carshowroom.models.Dealerships;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealershipRepository extends JpaRepository<Dealerships, Long> {
}
