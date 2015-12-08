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
@NamedQueries({@NamedQuery(name = ProductOwnerEntity.FIND_ALL, query = ProductOwnerEntity.FIND_ALL_QUERY)})
public class ProductOwnerEntity {

    public static final String FIND_ALL = "findAllProductOwners";
    protected static final String FIND_ALL_QUERY = "SELECT d FROM ProductOwnerEntity d";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private DeveloperEntity developerEntity;

    @OneToOne
    private ProjectEntity projectEntity;
}
