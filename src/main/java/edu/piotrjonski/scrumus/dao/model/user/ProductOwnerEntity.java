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
public class ProductOwnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private DeveloperEntity developerEntity;

    @OneToOne
    private ProjectEntity projectEntity;
}
