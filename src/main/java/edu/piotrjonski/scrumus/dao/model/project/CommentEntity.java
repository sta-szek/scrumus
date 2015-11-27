package edu.piotrjonski.scrumus.dao.model.project;

import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents comment object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
@NamedQueries({@NamedQuery(name = CommentEntity.FIND_ALL, query = CommentEntity.FIND_ALL_QUERY),
               @NamedQuery(name = CommentEntity.DELETE_BY_ID, query = CommentEntity.DELETE_BY_ID_QUERY)})
public class CommentEntity {

    public static final String FIND_ALL = "findAllComments";
    public static final String DELETE_BY_ID = "deleteCommentById";
    public static final String ID = "id";

    protected static final String FIND_ALL_QUERY = "SELECT c FROM CommentEntity c";
    protected static final String DELETE_BY_ID_QUERY = "DELETE FROM CommentEntity c WHERE c.id=:" + ID;

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
