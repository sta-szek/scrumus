package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.IssueManager;
import edu.piotrjonski.scrumus.business.ProjectManager;
import edu.piotrjonski.scrumus.business.SprintManager;
import edu.piotrjonski.scrumus.business.StoryManager;
import edu.piotrjonski.scrumus.domain.Backlog;
import edu.piotrjonski.scrumus.domain.Issue;
import edu.piotrjonski.scrumus.domain.Sprint;
import edu.piotrjonski.scrumus.domain.Story;
import lombok.Data;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    private String currentlyViewedProjectKey;

    private String moveIssueToSprint;
    private String moveIssueToStory;
    private Issue moveIssue;

    private List<Sprint> sprintsForCurrentlyViewedProject = new ArrayList<>();
    private Map<Integer, List<Story>> storiesForSprints = new HashMap<>();
    private Backlog backlogForCurrentlyViewedProject;

    @PostConstruct
    public void prepareForView() {
        currentlyViewedProjectKey = FacesContext.getCurrentInstance()
                                                .getExternalContext()
                                                .getRequestParameterMap()
                                                .get("projectKey");
        sprintsForCurrentlyViewedProject = findSprintsForProject(currentlyViewedProjectKey);
        sprintsForCurrentlyViewedProject.stream()
                                        .map(Sprint::getId)
                                        .forEach(findStoriesAndPutIntoMap());
        backlogForCurrentlyViewedProject = findBacklogForProject(currentlyViewedProjectKey);
    }

    public String moveToBacklog(Issue issue, Story story) {
        storyManager.moveIssueToBacklog(issue, story);
        logger.info("issue moved to backlog");
        return null;
    }

    public void moveIssueToStory() {
        //storyManager.addIssueToStory();
        logger.info(FacesContext.getCurrentInstance()
                                .getExternalContext()
                                .getRequestParameterMap()
                                .get("test"));
        logger.info(FacesContext.getCurrentInstance()
                                .getExternalContext()
                                .getRequestParameterMap()
                                .get("test2"));
        logger.info(moveIssueToSprint + " " + moveIssueToStory + " issue: " + moveIssue);
    }

    public void setMoveIssue(Issue moveIssue) {
        this.moveIssue = moveIssue;
        logger.info("setter");
    }

    public List<Story> getStoriesForSprint(int sprintId) {
        return storiesForSprints.getOrDefault(sprintId, new ArrayList<>());
    }

    public List<String> completeSprints(String query) {
        return sprintsForCurrentlyViewedProject.stream()
                                               .map(Sprint::getName)
                                               .filter(name -> name.startsWith(query))
                                               .collect(Collectors.toList());

    }

    public List<String> completeSprintStories(String query) {
        int sprintId = sprintsForCurrentlyViewedProject.stream()
                                                       .filter(sprint -> sprint.getName()
                                                                               .equals(moveIssueToSprint))
                                                       .findFirst()
                                                       .orElse(new Sprint())
                                                       .getId();
        return storiesForSprints.getOrDefault(sprintId, new ArrayList<>())
                                .stream()
                                .map(Story::getName)
                                .filter(name -> name.startsWith(query))
                                .collect(Collectors.toList());

    }

    public void onItemSelect(SelectEvent event) {
        moveIssueToSprint = event.getObject()
                                 .toString();
        moveIssueToStory = null;
    }


    private Consumer<Integer> findStoriesAndPutIntoMap() {
        return sprintId -> {
            List<Story> storiesForSprint = findStoriesForSprint(sprintId);
            storiesForSprints.put(sprintId, storiesForSprint);
        };
    }

    private List<Sprint> findSprintsForProject(String projectKey) {
        return sprintManager.findAllSprintsForProject(projectKey);
    }

    private Backlog findBacklogForProject(String projectKey) {
        return projectManager.findBacklogForProject(projectKey)
                             .orElse(null);
    }

    private List<Story> findStoriesForSprint(int sprintId) {
        return storyManager.findAllStoriesForSprint(sprintId);
    }
}
