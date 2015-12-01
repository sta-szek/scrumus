package edu.piotrjonski.scrumus.dao.model.user;

import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
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
@Table(name = "team")
@NamedQueries({@NamedQuery(name = TeamEntity.FIND_ALL, query = TeamEntity.FIND_ALL_QUERY),
               @NamedQuery(name = TeamEntity.DELETE_BY_ID, query = TeamEntity.DELETE_BY_ID_QUERY)})
public class TeamEntity {

    public static final String FIND_ALL = "findAllTeams";
    public static final String DELETE_BY_ID = "deleteTeamById";
    public static final String ID = "id";

    protected static final String FIND_ALL_QUERY = "SELECT d FROM TeamEntity d";
    protected static final String DELETE_BY_ID_QUERY = "DELETE FROM TeamEntity d WHERE d.id=:" + ID;

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
