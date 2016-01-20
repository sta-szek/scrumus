package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.dao.model.project.SprintEntity;
import edu.piotrjonski.scrumus.domain.Project;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ProjectDAO extends AbstractDAO<ProjectEntity, Project> {

    public ProjectDAO() {
        this(ProjectEntity.class);
    }

    private ProjectDAO(final Class entityClass) {
        super(entityClass);
    }

    public List<String> findAllNames() {
        return entityManager.createNamedQuery(ProjectEntity.FIND_ALL_NAMES)
                            .getResultList();
    }

    public List<String> findAllKeys() {
        return entityManager.createNamedQuery(ProjectEntity.FIND_ALL_KEYS)
                            .getResultList();
    }

    @Override
    protected ProjectEntity mapToDatabaseModel(final Project domainModel) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setKey(domainModel.getKey());
        projectEntity.setName(domainModel.getName());
        projectEntity.setCreationDate(domainModel.getCreationDate());
        projectEntity.setDescription(domainModel.getDescription());
        projectEntity.setDefinitionOfDone(domainModel.getDefinitionOfDone());
        projectEntity.setCurrentSprint(findSprintEntity(domainModel.getCurrentSprintId()));
        return projectEntity;
    }

    @Override
    protected edu.piotrjonski.scrumus.domain.Project mapToDomainModel(final ProjectEntity dbModel) {
        Project project = new Project();
        project.setCreationDate(dbModel.getCreationDate());
        project.setDefinitionOfDone(dbModel.getDefinitionOfDone());
        project.setDescription(dbModel.getDescription());
        project.setKey(dbModel.getKey());
        project.setName(dbModel.getName());
        project.setCurrentSprintId(getCurrentSprintId(dbModel));
        return project;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(ProjectEntity.FIND_ALL, ProjectEntity.class);
    }

    private int getCurrentSprintId(final ProjectEntity dbModel) {
        SprintEntity currentSprint = dbModel.getCurrentSprint();
        if (currentSprint != null) {
            return currentSprint.getId();
        } else {
            return 0;
        }
    }

    private SprintEntity findSprintEntity(int sprintId) {
        return entityManager.find(SprintEntity.class, sprintId);
    }

}
