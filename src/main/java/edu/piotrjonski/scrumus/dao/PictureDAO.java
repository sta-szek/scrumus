package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.user.PictureEntity;
import edu.piotrjonski.scrumus.domain.Picture;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class PictureDAO extends AbstractDAO<PictureEntity, Picture> {

    public PictureDAO() {
        this(PictureEntity.class);
    }

    private PictureDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    protected PictureEntity mapToDatabaseModel(final Picture domainModel) {
        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setData(domainModel.getData());
        pictureEntity.setId(domainModel.getId());
        pictureEntity.setName(domainModel.getName());
        return pictureEntity;
    }

    @Override
    protected Picture mapToDomainModel(final PictureEntity dbModel) {
        Picture picture = new Picture();
        picture.setData(dbModel.getData());
        picture.setId(dbModel.getId());
        picture.setName(dbModel.getName());
        return picture;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(PictureEntity.FIND_ALL, PictureEntity.class);
    }

}
