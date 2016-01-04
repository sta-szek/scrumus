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
@NamedQueries({@NamedQuery(name = TeamEntity.FIND_ALL, query = TeamEntity.FIND_ALL_QUERY),
               @NamedQuery(name = TeamEntity.FIND_ALL_DEVELOPER_TEAMS, query = TeamEntity.FIND_ALL_DEVELOPER_TEAMS_QUERY)})
public class TeamEntity {

    public static final String FIND_ALL = "findAllTeams";
    public static final String DEVELOPER = "developer";
    public static final String FIND_ALL_DEVELOPER_TEAMS = "findAllDeveloperTeams";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM TeamEntity d";
    protected static final String FIND_ALL_DEVELOPER_TEAMS_QUERY =
            "SELECT d FROM TeamEntity d WHERE :" + DEVELOPER + " MEMBER OF d.developerEntities";

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
