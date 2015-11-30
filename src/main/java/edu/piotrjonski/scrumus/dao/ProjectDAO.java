package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.ProjectEntity;
import edu.piotrjonski.scrumus.domain.Project;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ProjectDAO extends AbstractDAO<ProjectEntity, Project> {

    public ProjectDAO() {
        this(ProjectEntity.class);
    }

    private ProjectDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    protected ProjectEntity mapToDatabaseModel(final Project domainModel) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setKey(domainModel.getKey());
        projectEntity.setName(domainModel.getName());
        projectEntity.setCreationDate(domainModel.getCreationDate());
        projectEntity.setDescription(domainModel.getDescription());
        projectEntity.setDefinitionOfDone(domainModel.getDefinitionOfDone());
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
        return project;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(ProjectEntity.FIND_ALL, ProjectEntity.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(ProjectEntity.DELETE_BY_KEY);
    }

    @Override
    protected String getId() {
        return ProjectEntity.KEY;
    }
}
