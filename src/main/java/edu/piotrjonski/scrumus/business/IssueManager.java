package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.BacklogDAO;
import edu.piotrjonski.scrumus.dao.IssueDAO;
import edu.piotrjonski.scrumus.dao.ProjectDAO;
import edu.piotrjonski.scrumus.domain.Backlog;
import edu.piotrjonski.scrumus.domain.Issue;
import edu.piotrjonski.scrumus.domain.Project;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class IssueManager {

    @Inject
    private IssueDAO issueDAO;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private BacklogDAO backlogDAO;

    public Issue create(Issue issue, Project project) throws AlreadyExistException {
        if (issueExist(issue)) {
            throw new AlreadyExistException("Issue already exist.");
        }
        Issue savedIssue = saveIssue(issue);
        addIssueToBacklog(savedIssue, project);
        return updateIssueWithKey(savedIssue, project.getKey());
    }

    private void addIssueToBacklog(final Issue issue, Project project) {
        Backlog backlog = backlogDAO.findBacklogForProject(project.getKey())
                                    .get();
        backlog.addIssue(issue);
        backlogDAO.saveOrUpdate(backlog);
    }

    private Issue saveIssue(Issue issue) {
        return issueDAO.saveOrUpdate(issue)
                       .get();
    }

    private Issue updateIssueWithKey(Issue issue, String key) {
        int issueId = issue.getId();
        issue.setKey(key + "-" + issueId);
        return saveIssue(issue);
    }

    private boolean issueExist(final Issue issue) {
        return issueDAO.exist(issue.getId());
    }

}
