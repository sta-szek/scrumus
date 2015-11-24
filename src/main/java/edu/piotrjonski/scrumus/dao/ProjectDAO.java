package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.Project;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class ProjectDAO extends AbstractDAO<Project, edu.piotrjonski.scrumus.domain.Project> {

    public ProjectDAO() {
        this(Project.class);
    }

    private ProjectDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    public Project mapToDatabaseModel(final edu.piotrjonski.scrumus.domain.Project domainModel) {
        Project project = new Project();
        project.setKey(domainModel.getKey());
        project.setName(domainModel.getName());
        project.setCreationDate(domainModel.getCreationDate());
        project.setDescription(domainModel.getDescription());
        project.setDefinitionOfDone(domainModel.getDefinitionOfDone());
        return project;
    }

    @Override
    public edu.piotrjonski.scrumus.domain.Project mapToDomainModel(final Project dbModel) {
        edu.piotrjonski.scrumus.domain.Project project = new edu.piotrjonski.scrumus.domain.Project();
        project.setCreationDate(dbModel.getCreationDate());
        project.setDefinitionOfDone(dbModel.getDefinitionOfDone());
        project.setDescription(dbModel.getDescription());
        project.setKey(dbModel.getKey());
        project.setName(dbModel.getName());
        return project;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(Project.FIND_ALL, Project.class);
    }

    @Override
    protected Query getDeleteByIdQuery() {
        return entityManager.createNamedQuery(Project.DELETE_BY_KEY);
    }

    @Override
    protected String getId() {
        return Project.KEY;
    }
}
