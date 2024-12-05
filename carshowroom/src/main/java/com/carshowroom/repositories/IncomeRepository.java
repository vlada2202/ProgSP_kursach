package com.carshowroom.repositories;

import com.carshowroom.models.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}
