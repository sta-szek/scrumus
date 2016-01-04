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
@NamedQueries({@NamedQuery(name = CommentEntity.FIND_ALL, query = CommentEntity.FIND_ALL_QUERY),
               @NamedQuery(name = CommentEntity.FIND_ALL_DEVELOPER_COMMENTS, query = CommentEntity.FIND_ALL_DEVELOPER_COMMENTS_QUERY)})
public class CommentEntity {

    public static final String FIND_ALL = "findAllComments";
    public static final String FIND_ALL_DEVELOPER_COMMENTS = "findAllDeveloperComments";
    protected static final String FIND_ALL_QUERY = "SELECT c FROM CommentEntity c";
    protected static final String FIND_ALL_DEVELOPER_COMMENTS_QUERY =
            "SELECT c FROM CommentEntity c where c.developerEntity.id=:" + DeveloperEntity.ID;

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
