package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.BacklogDAO;
import edu.piotrjonski.scrumus.dao.IssueDAO;
import edu.piotrjonski.scrumus.dao.StoryDAO;
import edu.piotrjonski.scrumus.domain.Backlog;
import edu.piotrjonski.scrumus.domain.Issue;
import edu.piotrjonski.scrumus.domain.Story;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class StoryManager {

    @Inject
    private StoryDAO storyDAO;

    @Inject
    private IssueDAO issueDAO;

    @Inject
    private BacklogDAO backlogDAO;

    @Inject
    private PermissionManager permissionManager;

    public Story createStory(Story story) throws AlreadyExistException {
        if (storyExist(story)) {
            throw new AlreadyExistException("Story już istnieje");
        }
        return storyDAO.saveOrUpdate(story)
                       .get();
    }

    public Story findStory(int storyId) {
        return storyDAO.findById(storyId)
                       .orElse(new Story());
    }

    public List<Story> findAllStoriesForSprint(int sprintId) {
        return storyDAO.findStoriesForSprint(sprintId);
    }

    public void addIssueToStory(Issue issue, Story story) {
        if (issueExist(issue) && storyExist(story)) {
            story.addIssue(issue);
            storyDAO.saveOrUpdate(story);
            removeIssueFromBacklog(issue);
        }
    }

    public void removeIssueFromStory(Issue issue, Story story) {
        if (issueExist(issue) && storyExist(story)) {
            story.removeIssue(issue);
            storyDAO.saveOrUpdate(story);
            addIssueToBacklog(issue);
        }
    }

    private void addIssueToBacklog(final Issue issue) {
        Backlog backlog = backlogDAO.findBacklogForProject(issue.getProjectKey())
                                    .get();
        backlog.addIssue(issue);
        backlogDAO.saveOrUpdate(backlog);
    }

    private void removeIssueFromBacklog(final Issue issue) {
        Backlog backlog = backlogDAO.findBacklogForProject(issue.getProjectKey())
                                    .get();
        backlog.removeIssue(issue);
        backlogDAO.saveOrUpdate(backlog);
    }

    private boolean storyExist(Story story) {
        return storyDAO.exist(story.getId());
    }

    private boolean issueExist(Issue issue) {
        return issueDAO.exist(issue.getId());
    }
}
