package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveEntity;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveItemEmbeddable;
import edu.piotrjonski.scrumus.dao.model.project.RetrospectiveItemTypeEnum;
import edu.piotrjonski.scrumus.dao.model.project.SprintEntity;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.RetrospectiveItem;
import edu.piotrjonski.scrumus.domain.RetrospectiveItemType;
import org.apache.commons.collections4.ListUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class RetrospectiveDAO extends AbstractDAO<RetrospectiveEntity, Retrospective> {

    @Inject
    private CommentDAO commentDAO;

    public RetrospectiveDAO() {
        this(RetrospectiveEntity.class);
    }

    private RetrospectiveDAO(final Class entityClass) {
        super(entityClass);
    }


    public Optional<Retrospective> findRetrospectiveForSprint(int sprintId) {
        try {
            TypedQuery<RetrospectiveEntity> namedQuery = entityManager.createNamedQuery(RetrospectiveEntity.FIND_RETROSPECTIVE_FOR_SPRINT,
                                                                                        RetrospectiveEntity.class)
                                                                      .setParameter(SprintEntity.ID, sprintId);
            Retrospective retrospective = mapToDomainModelIfNotNull(namedQuery.getSingleResult());
            return Optional.of(retrospective);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    protected RetrospectiveEntity mapToDatabaseModel(final Retrospective domainModel) {
        RetrospectiveEntity retrospectiveEntity = new RetrospectiveEntity();
        retrospectiveEntity.setCommentEntities(commentDAO.mapToDatabaseModel(domainModel.getComments()));
        retrospectiveEntity.setDescription(domainModel.getDescription());
        retrospectiveEntity.setId(domainModel.getId());
        retrospectiveEntity.setRetrospectiveItemEmbeddables(mapRetroItemToDatabaseModel(domainModel.getRetrospectiveItems()));
        return retrospectiveEntity;
    }

    @Override
    protected Retrospective mapToDomainModel(final RetrospectiveEntity dbModel) {
        Retrospective retrospective = new Retrospective();
        retrospective.setComments(commentDAO.mapToDomainModel(dbModel.getCommentEntities()));
        retrospective.setDescription(dbModel.getDescription());
        retrospective.setId(dbModel.getId());
        retrospective.setRetrospectiveItems(mapRetroItemToDomainModel(dbModel.getRetrospectiveItemEmbeddables()));
        return retrospective;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(RetrospectiveEntity.FIND_ALL, RetrospectiveEntity.class);
    }

    private List<RetrospectiveItem> mapRetroItemToDomainModel(List<RetrospectiveItemEmbeddable> items) {
        List<RetrospectiveItemEmbeddable> retrospectiveItemEmbeddables = ListUtils.emptyIfNull(items);
        return retrospectiveItemEmbeddables.stream()
                                           .map(this::mapRetroItemToDomainModel)
                                           .collect(Collectors.toList());
    }

    private RetrospectiveItem mapRetroItemToDomainModel(RetrospectiveItemEmbeddable dbModel) {
        RetrospectiveItem item = new RetrospectiveItem();
        item.setDescription(dbModel.getDescription());
        item.setRetrospectiveItemType(mapRetroItemTypeToDomainModel(dbModel.getRetrospectiveItemTypeEnum()));
        return item;
    }

    private RetrospectiveItemTypeEnum mapRetroItemTypeToDatabaseModel(RetrospectiveItemType retrospectiveItemType) {
        return RetrospectiveItemTypeEnum.valueOf(retrospectiveItemType.name());
    }

    private List<RetrospectiveItemEmbeddable> mapRetroItemToDatabaseModel(List<RetrospectiveItem> items) {
        List<RetrospectiveItem> retrospectiveItems = ListUtils.emptyIfNull(items);
        return retrospectiveItems.stream()
                                 .map(this::mapRetroItemToDatabaseModel)
                                 .collect(Collectors.toList());
    }

    private RetrospectiveItemEmbeddable mapRetroItemToDatabaseModel(RetrospectiveItem domainModel) {
        RetrospectiveItemEmbeddable item = new RetrospectiveItemEmbeddable();
        item.setDescription(domainModel.getDescription());
        item.setRetrospectiveItemTypeEnum(mapRetroItemTypeToDatabaseModel(domainModel.getRetrospectiveItemType()));
        return item;
    }

    private RetrospectiveItemType mapRetroItemTypeToDomainModel(RetrospectiveItemTypeEnum retrospectiveItemTypeEnum) {
        return RetrospectiveItemType.valueOf(retrospectiveItemTypeEnum.name());
    }

}
