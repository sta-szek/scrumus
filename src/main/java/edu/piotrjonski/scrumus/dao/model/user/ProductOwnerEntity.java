package edu.piotrjonski.scrumus.dao.model.user;

import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents product owner object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "product_owner")
@NamedQueries({@NamedQuery(name = ProductOwnerEntity.FIND_ALL, query = ProductOwnerEntity.FIND_ALL_QUERY),
               @NamedQuery(name = ProductOwnerEntity.FIND_BY_DEVELOPER_ID, query = ProductOwnerEntity.FIND_BY_DEVELOPER_ID_QUERY),
               @NamedQuery(name = ProductOwnerEntity.FIND_BY_PROJECT_KEY, query = ProductOwnerEntity.FIND_BY_PROJECT_KEY_QUERY)})
public class ProductOwnerEntity {

    public static final String FIND_ALL = "findAllProductOwners";
    public static final String FIND_BY_DEVELOPER_ID = "findProductOwnerByDeveloperId";
    public static final String FIND_BY_PROJECT_KEY = "findByProjectKey";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM ProductOwnerEntity d";
    protected static final String FIND_BY_PROJECT_KEY_QUERY =
            "SELECT d FROM ProductOwnerEntity d WHERE d.projectEntity.key=:" + ProjectEntity.KEY;
    protected static final String FIND_BY_DEVELOPER_ID_QUERY =
            "SELECT d FROM ProductOwnerEntity d WHERE d.developerEntity.id=:" + DeveloperEntity.ID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    private DeveloperEntity developerEntity;

    @OneToOne
    private ProjectEntity projectEntity;
}
