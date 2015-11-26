package edu.piotrjonski.scrumus.dao.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Represents scrum master object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "scrum_master")
public class ScrumMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private DeveloperEntity developerEntity;

    @OneToMany
    private List<TeamEntity> teamEntities;
}
