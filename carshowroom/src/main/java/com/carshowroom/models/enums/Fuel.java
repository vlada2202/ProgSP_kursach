package com.carshowroom.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Fuel {
    PETROL("Бензин"),
    HYBRID("Гибрид"),
    DIESEL("Дизель");
    private final String name;
}

