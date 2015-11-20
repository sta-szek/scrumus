package edu.piotrjonski.scrumus.dao.model.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents backlog object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Backlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    private List<Issue> issues;

}
