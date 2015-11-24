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
@NamedQueries({@NamedQuery(name = Team.FIND_ALL, query = Team.FIND_ALL_QUERY),
               @NamedQuery(name = Team.DELETE_BY_ID, query = Team.DELETE_BY_ID_QUERY)})
public class Team {

    public static final String FIND_ALL = "findAllTeams";
    public static final String DELETE_BY_ID = "deleteTeamById";
    public static final String ID = "id";

    protected static final String FIND_ALL_QUERY = "SELECT d FROM Team d";
    protected static final String DELETE_BY_ID_QUERY = "DELETE FROM Team d WHERE d.id=:" + ID;

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
