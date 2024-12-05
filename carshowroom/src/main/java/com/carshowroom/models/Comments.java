package com.carshowroom.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comments implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String username;
    private String date;

    @Column(length = 5000)
    private String comment;

    @ManyToOne
    private Cars car;

    public Comments(String username, String date, String comment) {
        this.username = username;
        this.date = date;
        this.comment = comment;
    }
}
