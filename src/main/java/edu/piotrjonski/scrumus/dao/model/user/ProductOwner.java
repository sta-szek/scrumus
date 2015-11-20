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
public class ProductOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Developer developer;

    @OneToOne
    private Project project;
}
