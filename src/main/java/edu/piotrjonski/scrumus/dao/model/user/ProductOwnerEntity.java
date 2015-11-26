package edu.piotrjonski.scrumus.dao.model.user;

import edu.piotrjonski.scrumus.dao.model.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents product owner object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_owner")
public class ProductOwnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private DeveloperEntity developerEntity;

    @OneToOne
    private Project project;
}
