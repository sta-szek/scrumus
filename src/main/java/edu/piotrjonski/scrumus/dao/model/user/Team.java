package edu.piotrjonski.scrumus.dao.model.user;

import edu.piotrjonski.scrumus.dao.model.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents team object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 30, unique = false)
    private String name;

    @ManyToMany
    private List<Developer> developers;

    @ManyToMany(mappedBy = "teams")
    private List<Project> projects;
}
