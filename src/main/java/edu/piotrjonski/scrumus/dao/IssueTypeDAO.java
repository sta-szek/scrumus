package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.IssueTypeEntity;
import edu.piotrjonski.scrumus.domain.IssueType;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class IssueTypeDAO extends AbstractDAO<IssueTypeEntity, IssueType> {

    public IssueTypeDAO() {
        this(IssueTypeEntity.class);
    }

    private IssueTypeDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    protected IssueTypeEntity mapToDatabaseModel(final IssueType domainModel) {
        IssueTypeEntity issueTypeEntity = new IssueTypeEntity();
        issueTypeEntity.setId(domainModel.getId());
        issueTypeEntity.setName(domainModel.getName());
        return issueTypeEntity;
    }

    @Override
    protected IssueType mapToDomainModel(final IssueTypeEntity dbModel) {
        IssueType issueType = new IssueType();
        issueType.setId(dbModel.getId());
        issueType.setName(dbModel.getName());
        return issueType;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(IssueTypeEntity.FIND_ALL, IssueTypeEntity.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(IssueTypeEntity.DELETE_BY_ID);
    }

    @Override
    protected String getId() {
        return IssueTypeEntity.ID;
    }
}
