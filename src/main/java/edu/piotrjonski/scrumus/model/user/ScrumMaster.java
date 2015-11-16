package edu.piotrjonski.scrumus.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents scrum master object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ScrumMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Developer developer;

    @OneToMany
    private List<Team> teams;
}
