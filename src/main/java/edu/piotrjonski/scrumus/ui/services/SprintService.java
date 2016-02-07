package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.RetrospectiveManager;
import edu.piotrjonski.scrumus.business.SprintManager;
import edu.piotrjonski.scrumus.business.StoryManager;
import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.Sprint;
import edu.piotrjonski.scrumus.domain.Story;
import edu.piotrjonski.scrumus.ui.configuration.I18NProvider;
import edu.piotrjonski.scrumus.ui.configuration.PathProvider;
import lombok.Data;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SprintService implements Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private PathProvider pathProvider;

    @Inject
    private I18NProvider i18NProvider;

    @Inject
    private SprintManager sprintManager;

    @Inject
    private RetrospectiveManager retrospectiveManager;

    @Inject
    private StoryManager storyManager;

    private String createSprintProjectKey;
    private String createSprintName;
    private String createSprintDefinitionOfDone;
    private Date startDate;
    private Date endDate;

    private String createStoryProjectKey;
    private String createStoryName;
    private String createStorySprint;
    private String createStoryPoints;
    private String createStoryDefinitionOfDone;
    private List<Sprint> sprintsForCurrentlySelectedProject = new ArrayList<>();

    private String createRetrospectiveDescription;
    private int createRetrospectiveSprintId;

    @PostConstruct
    public void prepareForView() {
        try {
            createRetrospectiveSprintId = Integer.parseInt(FacesContext.getCurrentInstance()
                                                                       .getExternalContext()
                                                                       .getRequestParameterMap()
                                                                       .get("sprintId"));
        } catch (NumberFormatException e) {
            createRetrospectiveSprintId = 0;
        }
    }

    public void onProjectKeySelect(SelectEvent event) {
        createStoryProjectKey = event.getObject()
                                     .toString();
        sprintsForCurrentlySelectedProject = findSprintsForProject(createStoryProjectKey);
    }

    public List<String> completeSprints(String query) {
        if (createStoryProjectKey != null) {
            return sprintsForCurrentlySelectedProject.stream()
                                                     .map(Sprint::getName)
                                                     .filter(name -> name.startsWith(query))
                                                     .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public String createRetrospective() {
        if (createRetrospectiveSprintId == 0) {
            return pathProvider.getRedirectPath("project");
        }
        Retrospective retrospective = createRetrospectiveFromFields();
        try {
            Sprint savedSprint = sprintManager.findSprint(createRetrospectiveSprintId)
                                              .get();
            Retrospective savedRetrospective = retrospectiveManager.createRetrospectiveForSprint(retrospective, savedSprint)
                                                                   .get();
            logger.info(
                    "Created retrospective with id '" + savedRetrospective.getId() + "' for sprint with id '" + savedSprint.getId() + "'.");
            clearFields();
            return pathProvider.getRedirectPath("retrospective") + "&retrospectiveId=" + savedRetrospective.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.sprint", null);
            return null;
        }
    }

    public String createSprint() {
        Sprint sprint = createSprintFromFields();
        try {
            Sprint savedSprint = sprintManager.createSprint(sprint)
                                              .get();
            logger.info("Created sprint with id '" + savedSprint.getId() + "' in project with key '" + savedSprint.getProjectKey() + "'.");
            clearFields();
            return pathProvider.getRedirectPath("project") + "&projectKey=" + savedSprint.getProjectKey();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.sprint", null);
            return null;
        }
    }

    public String createStory() {
        Story story = crateStoryFromFields();
        try {
            Story savedStory = storyManager.createStory(story)
                                           .get();
            logger.info("Created story with id '" + savedStory.getId() + "' in sprint with id '" + savedStory.getSprintId() + "'.");
            final String projectKey = createStoryProjectKey;
            clearFields();
            return pathProvider.getRedirectPath("project") + "&projectKey=" + projectKey;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.story", null);
            return null;
        }
    }

    private Story crateStoryFromFields() {
        Story story = new Story();
        story.setDefinitionOfDone(createStoryDefinitionOfDone);
        story.setName(createStoryName);
        story.setPoints(Integer.parseInt(createStoryPoints));
        story.setSprintId(getSprintIdFromName());
        return story;
    }

    private List<Sprint> findSprintsForProject(String projectKey) {
        return sprintManager.findAllSprintsForProject(projectKey);
    }

    private void createFacesMessage(String property, String field) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                     i18NProvider.getMessage(property),
                                                     null);
        facesContext.addMessage(field, facesMessage);
    }

    private Sprint createSprintFromFields() {
        Sprint sprint = new Sprint();
        TimeRange timeRange = new TimeRange();
        timeRange.setStartDate(LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault()));
        timeRange.setEndDate(LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault()));
        sprint.setDefinitionOfDone(createSprintDefinitionOfDone);
        sprint.setName(createSprintName);
        sprint.setProjectKey(createSprintProjectKey);
        sprint.setTimeRange(timeRange);
        return sprint;
    }

    private Retrospective createRetrospectiveFromFields() {
        Retrospective retrospective = new Retrospective();
        retrospective.setDescription(createRetrospectiveDescription);
        return retrospective;
    }

    private void clearFields() {
        createSprintProjectKey = null;
        createSprintName = null;
        createSprintDefinitionOfDone = null;
        startDate = null;
        endDate = null;
        createRetrospectiveDescription = null;
        createRetrospectiveSprintId = 0;
        createStoryProjectKey = null;
        createStoryName = null;
        createStorySprint = null;
        createStoryPoints = null;
        createStoryDefinitionOfDone = null;
        sprintsForCurrentlySelectedProject = new ArrayList<>();
    }

    private int getSprintIdFromName() {
        return sprintsForCurrentlySelectedProject.stream()
                                                 .filter(sprint -> sprint.getName()
                                                                         .equals(createStorySprint))
                                                 .findFirst()
                                                 .orElse(new Sprint())
                                                 .getId();
    }
}
