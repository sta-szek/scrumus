package edu.piotrjonski.scrumus.dao.model.project;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents retrospective object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "retrospective")
@NamedQueries({@NamedQuery(name = RetrospectiveEntity.FIND_ALL, query = RetrospectiveEntity.FIND_ALL_QUERY),
               @NamedQuery(name = RetrospectiveEntity.FIND_RETROSPECTIVE_FOR_SPRINT,
                           query = RetrospectiveEntity.FIND_RETROSPECTIVE_FOR_SPRINT_QUERY)})
public class RetrospectiveEntity {

    public static final String FIND_ALL = "findAllRetrospectives";
    public static final String FIND_ALL_QUERY = "SELECT p FROM RetrospectiveEntity p";
    public static final String ID = "id";
    public static final String FIND_RETROSPECTIVE_FOR_SPRINT = "findRetrospectiveForSprint";
    protected static final String FIND_RETROSPECTIVE_FOR_SPRINT_QUERY =
            "SELECT s.retrospectiveEntity FROM SprintEntity s WHERE s.id=:" + SprintEntity.ID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 4096, nullable = true)
    private String description;

    @ElementCollection
    private List<RetrospectiveItemEmbeddable> retrospectiveItemEmbeddables = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntities = new ArrayList<>();
}
