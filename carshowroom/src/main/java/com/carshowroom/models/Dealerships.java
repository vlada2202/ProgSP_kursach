package com.carshowroom.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Dealerships implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String name;
    private String address;
    private String poster;
    private String contact;
    private String schedule;

    @Column(length = 5000)
    private String description;

    @OneToMany(mappedBy = "dealership", cascade = CascadeType.ALL)
    private List<Cars> cars;
    @ManyToOne
    private Users owner;
    @ManyToOne
    private Brand brand;

    public Dealerships(String name, String address, String description, String poster, Brand brand,String contact, String schedule) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.poster = poster;
        this.brand = brand;
        this.contact = contact;
        this.schedule = schedule;
    }

    public void addCar(Cars car) {
        this.cars.add(car);
        car.setDealership(this);
    }

    public void removeCar(Cars car) {
        this.cars.remove(car);
        car.setDealership(null);
    }
}
