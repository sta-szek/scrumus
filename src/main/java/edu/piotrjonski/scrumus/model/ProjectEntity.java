package edu.piotrjonski.scrumus.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

/**
 * Represents project datastore object.
 */
@Entity
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255, unique = true)
    private String name;

    @Column(nullable = false, length = 8, unique = true)
    private String key;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectEntity", fetch = FetchType.EAGER)
    private List<SprintEntity> sprintEntities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SprintEntity> getSprintEntities() {
        return sprintEntities;
    }

    public void setSprintEntities(List<SprintEntity> sprintEntities) {
        this.sprintEntities = sprintEntities;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                                        .append("name", name)
                                        .append("key", key)
                                        .append("sprintEntities", sprintEntities)
                                        .toString();
    }
}
