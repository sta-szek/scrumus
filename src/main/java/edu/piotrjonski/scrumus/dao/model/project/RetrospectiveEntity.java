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
               @NamedQuery(name = RetrospectiveEntity.DELETE_BY_ID, query = RetrospectiveEntity.DELETE_BY_KEY_QUERY)})
public class RetrospectiveEntity {

    public static final String FIND_ALL = "findAllRetrospectives";
    public static final String FIND_ALL_QUERY = "SELECT p FROM RetrospectiveEntity p";
    public static final String DELETE_BY_ID = "deleteRetrospectiveByKey";
    public static final String ID = "key";
    protected static final String DELETE_BY_KEY_QUERY = "DELETE FROM RetrospectiveEntity p WHERE p.id=:" + ID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 4096, nullable = true)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<RetrospectiveItemEmbeddable> retrospectiveItemEmbeddables = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntities = new ArrayList<>();
}
