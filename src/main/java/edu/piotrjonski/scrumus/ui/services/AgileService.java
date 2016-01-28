package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.IssueManager;
import edu.piotrjonski.scrumus.business.ProjectManager;
import edu.piotrjonski.scrumus.business.SprintManager;
import edu.piotrjonski.scrumus.business.StoryManager;
import edu.piotrjonski.scrumus.domain.Backlog;
import edu.piotrjonski.scrumus.domain.Sprint;
import edu.piotrjonski.scrumus.domain.Story;
import lombok.Data;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class AgileService implements Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private ProjectManager projectManager;

    @Inject
    private IssueManager issueManager;

    @Inject
    private SprintManager sprintManager;

    @Inject
    private StoryManager storyManager;

    public List<Sprint> findSprintsForProject(String projectKey) {
        return sprintManager.findAllSprintsForProject(projectKey);
    }

    public Backlog findBacklogForProject(String projectKey) {
        return projectManager.findBacklogForProject(projectKey)
                             .orElse(null);
    }

    public List<Story> findStoriesForSprint(String sprintId) {
        try {
            int sprintIntId = Integer.parseInt(sprintId);
            return storyManager.findAllStoriesForSprint(sprintIntId);
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
    }


}
