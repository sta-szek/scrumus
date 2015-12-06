package edu.piotrjonski.scrumus.dao.model.user;

import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents team object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "team")
@NamedQueries({@NamedQuery(name = TeamEntity.FIND_ALL, query = TeamEntity.FIND_ALL_QUERY)})
public class TeamEntity {

    public static final String FIND_ALL = "findAllTeams";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM TeamEntity d";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 30, unique = false)
    private String name;

    @ManyToMany
    private List<DeveloperEntity> developerEntities;

    @ManyToMany(mappedBy = "teamEntities")
    private List<ProjectEntity> projectEntities;
}
