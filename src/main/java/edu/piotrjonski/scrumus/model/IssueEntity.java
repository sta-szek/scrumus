package edu.piotrjonski.scrumus.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents issue datastore object.
 */
@Entity
@Table(name = "issues")
public class IssueEntity {

    @Id
    private String id;

    @ManyToOne
    private SprintEntity sprintEntity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SprintEntity getSprintEntity() {
        return sprintEntity;
    }

    public void setSprintEntity(SprintEntity sprintEntity) {
        this.sprintEntity = sprintEntity;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                                        .append("sprintEntity", sprintEntity)
                                        .toString();
    }
}
