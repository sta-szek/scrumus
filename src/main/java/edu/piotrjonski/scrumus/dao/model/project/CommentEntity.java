package edu.piotrjonski.scrumus.dao.model.project;

import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents comment object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "comment")
@NamedQueries({@NamedQuery(name = CommentEntity.FIND_ALL, query = CommentEntity.FIND_ALL_QUERY)})
public class CommentEntity {

    public static final String FIND_ALL = "findAllComments";
    protected static final String FIND_ALL_QUERY = "SELECT c FROM CommentEntity c";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 4096, nullable = false)
    private String commentBody;

    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne
    private DeveloperEntity developerEntity;
}
