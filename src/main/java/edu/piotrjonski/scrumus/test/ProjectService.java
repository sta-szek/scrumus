package edu.piotrjonski.scrumus.test;

import edu.piotrjonski.scrumus.dao.model.project.Project;

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
        //sessionBean.putsmthtoDB();
        Project project = sessionBean.getProjectEntity();

        return Response.ok(project)
                       .build();

    }

}
