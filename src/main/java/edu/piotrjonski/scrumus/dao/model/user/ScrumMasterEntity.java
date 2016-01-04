package edu.piotrjonski.scrumus.dao.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents scrum master object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "scrum_master")
@NamedQueries({@NamedQuery(name = ScrumMasterEntity.FIND_ALL, query = ScrumMasterEntity.FIND_ALL_QUERY),
               @NamedQuery(name = ScrumMasterEntity.FIND_BY_DEVELOPER_ID, query = ScrumMasterEntity.FIND_BY_DEVELOPER_ID_QUERY)})
public class ScrumMasterEntity {

    public static final String FIND_ALL = "findAllScrumMasters";
    public static final String FIND_BY_DEVELOPER_ID = "findScrumMasterByDeveloperId";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM ScrumMasterEntity d";
    protected static final String FIND_BY_DEVELOPER_ID_QUERY =
            "SELECT d FROM ScrumMasterEntity d WHERE d.developerEntity.id=:" + DeveloperEntity.ID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    private DeveloperEntity developerEntity;

    @OneToMany
    private List<TeamEntity> teamEntities;
}
