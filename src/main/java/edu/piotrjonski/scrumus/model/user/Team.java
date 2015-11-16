package edu.piotrjonski.scrumus.model.user;

import edu.piotrjonski.scrumus.model.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
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
    private String firstName;

    @ManyToMany
    private List<Developer> developers;

    @ManyToMany(mappedBy = "teams")
    private Collection<Project> projects;
}
