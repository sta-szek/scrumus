package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.BacklogDAO;
import edu.piotrjonski.scrumus.dao.IssueDAO;
import edu.piotrjonski.scrumus.dao.StoryDAO;
import edu.piotrjonski.scrumus.domain.Backlog;
import edu.piotrjonski.scrumus.domain.Issue;
import edu.piotrjonski.scrumus.domain.Story;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class StoryManager {

    @Inject
    private transient Logger logger;

    @Inject
    private StoryDAO storyDAO;

    @Inject
    private IssueDAO issueDAO;

    @Inject
    private BacklogDAO backlogDAO;

    @Inject
    private PermissionManager permissionManager;

    public List<Issue> findIssuesForStory(int storyId) {
        return storyDAO.findById(storyId)
                       .orElse(new Story())
                       .getIssues();
    }

    public void deleteStoriesFromProject(String projectKey) {
        storyDAO.deleteStoriesFromProject(projectKey);
    }

    public Optional<Story> createStory(Story story) throws AlreadyExistException {
        if (storyExist(story)) {
            throw new AlreadyExistException("Story już istnieje");
        }
        return storyDAO.saveOrUpdate(story);
    }

    public Optional<Story> findStory(int storyId) {
        return storyDAO.findById(storyId);
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

    public Optional<Story> findStoryForIssue(int issueId) {
        return storyDAO.findStoryForIssue(issueId);
    }

    public void moveIssueToBacklog(Issue issue, Story story) {
        removeIssueFromStory(issue, story);
        addIssueToBacklog(issue);
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
