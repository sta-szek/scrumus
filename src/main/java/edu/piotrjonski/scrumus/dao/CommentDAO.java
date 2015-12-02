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
        commentEntity.setDeveloperEntity(findDeveloperEntity(domainModel.getDeveloperId()));
        commentEntity.setId(domainModel.getId());
        return commentEntity;
    }

    @Override
    protected Comment mapToDomainModel(final CommentEntity dbModel) {
        DeveloperEntity developerEntity = dbModel.getDeveloperEntity();
        Comment comment = new Comment();
        comment.setId(dbModel.getId());
        comment.setCommentBody(dbModel.getCommentBody());
        comment.setCreationDate(dbModel.getCreationDate());
        comment.setDeveloperId(getDeveloperId(developerEntity));
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

    private int getDeveloperId(final DeveloperEntity developerEntity) {
        return developerEntity != null
               ? developerEntity.getId()
               : 0;
    }

    private DeveloperEntity findDeveloperEntity(final int id) {
        if (id != 0) {
            Optional<Developer> developer = developerDAO.findByKey(id);
            if (developer.isPresent()) {
                return developerDAO.mapToDatabaseModelIfNotNull(developer.get());
            }
        }
        return null;
    }
}
