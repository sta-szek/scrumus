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
               @NamedQuery(name = TeamEntity.FIND_ALL_DEVELOPER_TEAMS, query = TeamEntity.FIND_ALL_DEVELOPER_TEAMS_QUERY),
               @NamedQuery(name = TeamEntity.FIND_ALL_NAMES, query = TeamEntity.FIND_ALL_NAMES_QUERY),
               @NamedQuery(name = TeamEntity.FIND_ALL_TEAMS_FOR_PROJECT, query = TeamEntity.FIND_ALL_TEAMS_FOR_PROJECT_QUERY),
               @NamedQuery(name = TeamEntity.FIND_BY_NAME, query = TeamEntity.FIND_BY_NAME_QUERY)})
public class TeamEntity {

    public static final String FIND_ALL = "findAllTeams";
    public static final String DEVELOPER = "developer";
    public static final String FIND_ALL_NAMES = "findAllTeamNames";
    public static final String FIND_ALL_DEVELOPER_TEAMS = "findAllDeveloperTeams";
    public static final String FIND_ALL_TEAMS_FOR_PROJECT = "findAllTeamsForProject";
    public static final String PROJECT = "projectParam";
    public static final String NAME = "teamName";
    public static final String FIND_BY_NAME = "findProjectByName";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM TeamEntity d";
    protected static final String FIND_BY_NAME_QUERY = "SELECT d FROM TeamEntity d WHERE d.name=:" + NAME;
    protected static final String FIND_ALL_NAMES_QUERY = "SELECT d.name FROM TeamEntity d";
    protected static final String FIND_ALL_TEAMS_FOR_PROJECT_QUERY =
            "SELECT d FROM TeamEntity d WHERE :" + PROJECT + " MEMBER OF d.projectEntities";
    protected static final String FIND_ALL_DEVELOPER_TEAMS_QUERY =
            "SELECT d FROM TeamEntity d WHERE :" + DEVELOPER + " MEMBER OF d.developerEntities";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @ManyToMany
    private List<DeveloperEntity> developerEntities;

    @ManyToMany
    private List<ProjectEntity> projectEntities;
}
