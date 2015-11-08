package edu.piotrjonski.scrumus.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

/**
 * Represents sprint datastore object.
 */
@Entity
@Table(name = "sprints")
public class SprintEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 64)
    private String name;

    @Embedded
    private TimeRange timeRange;

    @ManyToOne
    private ProjectEntity projectEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sprintEntity", fetch = FetchType.EAGER)
    private List<IssueEntity> issueEntities;

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

    public ProjectEntity getProjectEntity() {
        return projectEntity;
    }

    public void setProjectEntity(ProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public List<IssueEntity> getIssueEntities() {
        return issueEntities;
    }

    public void setIssueEntities(List<IssueEntity> issueEntities) {
        this.issueEntities = issueEntities;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                                        .append("name", name)
                                        .append("timeRange", timeRange)
                                        .append("projectEntity", projectEntity)
                                        .append("issueEntities", issueEntities)
                                        .toString();
    }
}
