package com.carshowroom.repositories;

import com.carshowroom.models.Cars;
import com.carshowroom.models.enums.Fuel;
import com.carshowroom.models.enums.Transmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Cars, Long> {
    List<Cars> findAllByNameContaining(String name);

    List<Cars> findAllByNameContainingAndFuelAndTransmission(String name, Fuel fuel, Transmission transmission);
}