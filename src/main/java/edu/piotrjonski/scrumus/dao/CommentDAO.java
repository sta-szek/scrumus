package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.CommentEntity;
import edu.piotrjonski.scrumus.dao.model.user.DeveloperEntity;
import edu.piotrjonski.scrumus.domain.Comment;
import edu.piotrjonski.scrumus.domain.Developer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.Optional;

@Stateless
public class CommentDAO extends AbstractDAO<CommentEntity, Comment> {

    @Inject
    private DeveloperDAO developerDAO;

    public CommentDAO() {
        this(CommentEntity.class);
    }

    private CommentDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    protected CommentEntity mapToDatabaseModel(final Comment domainModel) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentBody(domainModel.getCommentBody());
        commentEntity.setCreationDate(domainModel.getCreationDate());
        commentEntity.setDeveloperEntity(findDeveloperEntity(domainModel));
        commentEntity.setId(domainModel.getId());
        return commentEntity;
    }

    @Override
    protected Comment mapToDomainModel(final CommentEntity dbModel) {
        Comment comment = new Comment();
        comment.setId(dbModel.getId());
        comment.setCommentBody(dbModel.getCommentBody());
        comment.setCreationDate(dbModel.getCreationDate());
        comment.setDeveloperId(dbModel.getDeveloperEntity()
                                      .getId());
        return comment;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(CommentEntity.FIND_ALL, CommentEntity.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(CommentEntity.DELETE_BY_ID);
    }

    @Override
    protected String getId() {
        return CommentEntity.ID;
    }

    private DeveloperEntity findDeveloperEntity(final Comment domainModel) {
        Optional<Developer> developer = developerDAO.findByKey(domainModel.getDeveloperId());
        return developerDAO.mapToDatabaseModelIfNotNull(developer.get());
    }
}
