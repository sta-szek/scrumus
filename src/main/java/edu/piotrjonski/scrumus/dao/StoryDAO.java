package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.SprintEntity;
import edu.piotrjonski.scrumus.dao.model.project.StoryEntity;
import edu.piotrjonski.scrumus.domain.Story;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class StoryDAO extends AbstractDAO<StoryEntity, Story> {

    @Inject
    private IssueDAO issueDAO;

    public StoryDAO() {
        this(StoryEntity.class);
    }

    private StoryDAO(final Class entityClass) {
        super(entityClass);
    }

    public List<Story> findStoriesForSprint(int sprintId) {
        return entityManager.createNamedQuery(SprintEntity.FIND_ALL_STORIES, StoryEntity.class)
                            .setParameter(SprintEntity.ID, sprintId)
                            .getResultList()
                            .stream()
                            .map(this::mapToDomainModel)
                            .collect(Collectors.toList());
    }

    @Override
    protected StoryEntity mapToDatabaseModel(final Story domainModel) {
        StoryEntity storyEntity = new StoryEntity();
        storyEntity.setDefinitionOfDone(domainModel.getDefinitionOfDone());
        storyEntity.setId(domainModel.getId());
        storyEntity.setIssueEntities(issueDAO.mapToDatabaseModel(domainModel.getIssues()));
        storyEntity.setName(domainModel.getName());
        storyEntity.setPoints(domainModel.getPoints());
        return storyEntity;
    }

    @Override
    protected Story mapToDomainModel(final StoryEntity dbModel) {
        Story story = new Story();
        story.setDefinitionOfDone(dbModel.getDefinitionOfDone());
        story.setId(dbModel.getId());
        story.setIssues(issueDAO.mapToDomainModel(dbModel.getIssueEntities()));
        story.setName(dbModel.getName());
        story.setPoints(dbModel.getPoints());
        return story;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(StoryEntity.FIND_ALL, StoryEntity.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(StoryEntity.DELETE_BY_ID);
    }

    @Override
    protected String getId() {
        return StoryEntity.ID;
    }
}
