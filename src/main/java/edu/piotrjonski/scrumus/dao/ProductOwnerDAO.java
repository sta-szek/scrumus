package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.user.AdminEntity;
import edu.piotrjonski.scrumus.dao.model.user.ProductOwnerEntity;
import edu.piotrjonski.scrumus.domain.Admin;
import edu.piotrjonski.scrumus.domain.ProductOwner;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

@Stateless
public class ProductOwnerDAO extends AbstractDAO<ProductOwnerEntity, ProductOwner> {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private ProjectDAO projectDAO;

    public ProductOwnerDAO() {
        this(ProductOwnerEntity.class);
    }

    private ProductOwnerDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    protected ProductOwnerEntity mapToDatabaseModel(final ProductOwner domainModel) {
        ProductOwnerEntity productOwnerEntity = new ProductOwnerEntity();
        productOwnerEntity.setDeveloperEntity(developerDAO.mapToDatabaseModelIfNotNull(domainModel.getDeveloper()));
        productOwnerEntity.setId(domainModel.getId());
        productOwnerEntity.setProjectEntity(projectDAO.mapToDatabaseModelIfNotNull(domainModel.getProject()));
        return productOwnerEntity;
    }

    @Override
    protected ProductOwner mapToDomainModel(final ProductOwnerEntity dbModel) {
        ProductOwner productOwner = new ProductOwner();
        productOwner.setDeveloper(developerDAO.mapToDomainModelIfNotNull(dbModel.getDeveloperEntity()));
        productOwner.setId(dbModel.getId());
        productOwner.setProject(projectDAO.mapToDomainModelIfNotNull(dbModel.getProjectEntity()));
        return productOwner;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(ProductOwnerEntity.FIND_ALL, ProductOwnerEntity.class);
    }

}
