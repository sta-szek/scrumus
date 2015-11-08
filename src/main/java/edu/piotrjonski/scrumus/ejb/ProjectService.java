package edu.piotrjonski.scrumus.ejb;

import edu.piotrjonski.scrumus.model.IssueEntity;
import edu.piotrjonski.scrumus.model.ProjectEntity;
import edu.piotrjonski.scrumus.model.SprintEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * for tests
 */
@Path("/projects")
@RequestScoped
public class ProjectService {

    @Inject
    private SessionBean sessionBean;

    @GET
    @Produces("application/json")
    public Response getProject() {
        sessionBean.putsmthtoDB();
        ProjectEntity projectEntity = sessionBean.getProjectEntity();
        SprintEntity sprintEntity = projectEntity.getSprintEntities()
                                                 .get(0);
        IssueEntity issueEntity = sprintEntity.getIssueEntities()
                                              .get(0);
        return Response.ok(projectEntity.toString())
                       .build();

    }

}
