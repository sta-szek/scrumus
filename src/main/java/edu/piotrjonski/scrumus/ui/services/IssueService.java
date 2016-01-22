package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.*;
import edu.piotrjonski.scrumus.domain.*;
import edu.piotrjonski.scrumus.ui.configuration.I18NProvider;
import edu.piotrjonski.scrumus.ui.configuration.PathProvider;
import lombok.Data;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@RequestScoped
@Named
public class IssueService implements Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private OccupiedChecker occupiedChecker;

    @Inject
    private I18NProvider i18NProvider;

    @Inject
    private PathProvider pathProvider;

    @Inject
    private IssueManager issueManager;

    @Inject
    private UserManager userManager;

    @Inject
    private ProjectManager projectManager;

    private String issueTypeName;
    private int issueTypeId;
    private int stateId;
    private String stateName;
    private int priorityId;
    private String priorityName;

    private String createIssueSummary;
    private String createIssueDescription;
    private String createIssueDefinitionOfDone;
    private String createIssueIssueType;
    private String createIssuePriority;
    private String createIssueProjectKey;
    private String createIssueState;

    public List<IssueType> getAllIssueTypes() {
        return issueManager.findAllIssueTypes();
    }

    public List<State> getAllStates() {return issueManager.findAllStates();}

    public List<Priority> getAllPriorities() { return issueManager.findAllPriorities();}

    public Issue findIssueById(String issueId) {
        try {
            int issueIntId = Integer.parseInt(issueId);
            Optional<Issue> issueOptional = issueManager.findIssue(issueIntId);
            return issueOptional.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public IssueType findIssueTypeById(String issueTypeId) {
        try {
            int issueTypeIntId = Integer.parseInt(issueTypeId);
            Optional<IssueType> issueTypeOptional = issueManager.findIssueType(issueTypeIntId);
            issueTypeOptional.ifPresent(issueType -> {
                issueTypeName = issueType.getName();
                this.issueTypeId = issueType.getId();
            });
            return issueTypeOptional.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public State findStateById(String stateId) {
        try {
            int stateIntId = Integer.parseInt(stateId);
            Optional<State> stateOptional = issueManager.findState(stateIntId);
            stateOptional.ifPresent(state -> {
                stateName = state.getName();
                this.stateId = state.getId();
            });
            return stateOptional.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Priority findPriorityById(String priorityId) {
        try {
            int priorityIntId = Integer.parseInt(priorityId);
            Optional<Priority> priorityOptional = issueManager.findPriority(priorityIntId);
            priorityOptional.ifPresent(priority -> {
                priorityName = priority.getName();
                this.priorityId = priority.getId();
            });
            return priorityOptional.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String editIssueType() {
        IssueType issueTypeFromField = createIssueTypeFromField();
        int issueTypeId = Integer.parseInt(FacesContext.getCurrentInstance()
                                                       .getExternalContext()
                                                       .getRequestParameterMap()
                                                       .get("issueTypeId"));
        issueTypeFromField.setId(issueTypeId);
        try {
            issueManager.updateIssueType(issueTypeFromField);
            return pathProvider.getRedirectPath("admin.listIssueTypes");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.edit.issueType", null);
            return null;
        }
    }

    public String editState() {
        State stateFromField = createStateFromField();
        int stateId = Integer.parseInt(FacesContext.getCurrentInstance()
                                                   .getExternalContext()
                                                   .getRequestParameterMap()
                                                   .get("stateId"));
        stateFromField.setId(stateId);
        try {
            issueManager.updateState(stateFromField);
            return pathProvider.getRedirectPath("admin.listStates");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.edit.state", null);
            return null;
        }
    }

    public String editPriority() {
        Priority priorityFromField = createPriorityFromField();
        int priorityId = Integer.parseInt(FacesContext.getCurrentInstance()
                                                      .getExternalContext()
                                                      .getRequestParameterMap()
                                                      .get("priorityId"));
        priorityFromField.setId(priorityId);
        try {
            issueManager.updatePriority(priorityFromField);
            return pathProvider.getRedirectPath("admin.listPriorities");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.edit.priority", null);
            return null;
        }
    }

    public void deleteIssueType(IssueType issueType) {
        try {
            issueManager.deleteIssueType(issueType);
            logger.info("Issue type with id '" + issueType.getId() + "' was deleted.");
        } catch (NotExistException e) {
            createFacesMessage("system.fatal.delete.issueType.notExist", null);
        } catch (IllegalOperationException e) {
            createFacesMessage("system.fatal.delete.issueType.used", null);
        }
    }

    public void deleteState(State state) {
        try {
            issueManager.deleteState(state);
            logger.info("State with id '" + state.getId() + "' was deleted.");
        } catch (NotExistException e) {
            createFacesMessage("system.fatal.delete.state.notExist", null);
        } catch (IllegalOperationException e) {
            createFacesMessage("system.fatal.delete.state.used", null);
        }
    }

    public void deletePriority(Priority priority) {
        try {
            issueManager.deletePriority(priority);
            logger.info("Priority with id '" + priority.getId() + "' was deleted.");
        } catch (NotExistException e) {
            createFacesMessage("system.fatal.delete.priority.notExist", null);
        } catch (IllegalOperationException e) {
            createFacesMessage("system.fatal.delete.priority.used", null);
        }
    }

    public String createIssue() {
        if (validateIssueSummary()) {
            return null;
        }
        Issue issue = createIssueFromField();
        try {
            Project project = projectManager.findProject(createIssueProjectKey)
                                            .get();
            Issue savedIssue = issueManager.create(issue, project)
                                           .get();
            logger.info("Created issue with id '" + savedIssue.getId() + "' in project with key '" + project.getKey() + "'.");
            clearFields();
            return pathProvider.getRedirectPath("issue") + "&issueId=" + savedIssue.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.issue", null);
            return null;
        }
    }

    public String createIssueType() {
        if (validateIssueTypeName()) {
            return null;
        }
        IssueType issueType = createIssueTypeFromField();
        try {
            issueManager.createIssueType(issueType);
            logger.info("Created issue type with name '" + issueType.getName() + "'.");
            return pathProvider.getRedirectPath("admin.listIssueTypes");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.issueType", null);
            return null;
        }
    }

    public String createState() {
        if (validateStateName()) {
            return null;
        }
        State state = createStateFromField();
        try {
            issueManager.createState(state);
            logger.info("Created state with name '" + state.getName() + "'.");
            return pathProvider.getRedirectPath("admin.listStates");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.state", null);
            return null;
        }
    }

    public String createPriority() {
        if (validatePriorityName()) {
            return null;
        }
        Priority priority = createPriorityFromField();
        try {
            issueManager.createPriority(priority);
            logger.info("Created priority with name '" + priority.getName() + "'.");
            return pathProvider.getRedirectPath("admin.listPriorities");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.priority", null);
            return null;
        }
    }

    public boolean validateIssueTypeName() {
        if (occupiedChecker.isIssueTypeNameOccupied(issueTypeName)) {
            createFacesMessage("page.validator.occupied.issueType.name", "createIssueTypeForm:issueTypeName");
            return true;
        }
        return false;
    }

    public boolean validateIssueSummary() {
        if (occupiedChecker.isIssueSummaryOccupied(createIssueSummary)) {
            createFacesMessage("page.validator.occupied.issue.summary", "createIssueForm:summary");
            return true;
        }
        return false;
    }

    public boolean validateStateName() {
        if (occupiedChecker.isStateNameOccupied(stateName)) {
            createFacesMessage("page.validator.occupied.state.name", "createStateForm:stateName");
            return true;
        }
        return false;
    }

    public boolean validatePriorityName() {
        if (occupiedChecker.isPriorityNameOccupied(priorityName)) {
            createFacesMessage("page.validator.occupied.priority.name", "createPriorityForm:priorityName");
            return true;
        }
        return false;
    }

    private void createFacesMessage(String property, String field) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                     i18NProvider.getMessage(property),
                                                     null);
        facesContext.addMessage(field, facesMessage);
    }

    private IssueType createIssueTypeFromField() {
        IssueType issueType = new IssueType();
        issueType.setName(issueTypeName);
        clearFields();
        return issueType;
    }

    private State createStateFromField() {
        State state = new State();
        state.setName(stateName);
        clearFields();
        return state;
    }

    private Priority createPriorityFromField() {
        Priority priority = new Priority();
        priority.setName(priorityName);
        clearFields();
        return priority;
    }

    private Issue createIssueFromField() {
        Issue issue = new Issue();
        issue.setReporterId(getUserId());
        issue.setCreationDate(LocalDateTime.now());
        issue.setDefinitionOfDone(createIssueDefinitionOfDone);
        issue.setDescription(createIssueDescription);
        issue.setIssueType(issueManager.findIssueType(Integer.parseInt(createIssueIssueType))
                                       .get());
        issue.setPriority(issueManager.findPriority(Integer.parseInt(createIssuePriority))
                                      .get());
        issue.setProjectKey(createIssueProjectKey);
        issue.setSummary(createIssueSummary);
        issue.setState(issueManager.findState(Integer.parseInt(createIssueState))
                                   .get());
        return issue;
    }

    private int getUserId() {
        String currentUsername = getCurrentUsername();
        return userManager.findByUsername(currentUsername)
                          .get()
                          .getId();
    }

    private String getCurrentUsername() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return FacesContext.getCurrentInstance()
                           .getApplication()
                           .evaluateExpressionGet(facesContext,
                                                  "#{request.remoteUser}",
                                                  String.class);
    }


    private void clearFields() {
        issueTypeName = null;
        stateName = null;
        priorityName = null;
        issueTypeId = 0;
        stateId = 0;
        priorityId = 0;
        createIssueSummary = null;
        createIssueDescription = null;
        createIssueDefinitionOfDone = null;
        createIssueIssueType = null;
        createIssuePriority = null;
        createIssueProjectKey = null;
        createIssueState = null;
    }

}
