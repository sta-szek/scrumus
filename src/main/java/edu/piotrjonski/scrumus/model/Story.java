package edu.piotrjonski.scrumus.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(nullable = false)
    private int points;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Issue> issues;
}
