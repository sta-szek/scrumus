package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.IllegalOperationException;
import edu.piotrjonski.scrumus.business.IssueManager;
import edu.piotrjonski.scrumus.business.NotExistException;
import edu.piotrjonski.scrumus.domain.IssueType;
import edu.piotrjonski.scrumus.domain.State;
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

    private String issueTypeName;
    private int issueTypeId;
    private int stateId;
    private String stateName;

    public List<IssueType> getAllIssueTypes() {
        return issueManager.findAllIssueTypes();
    }

    public List<State> getAllStates() {return issueManager.findAllStates();}

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

    public void deleteIssueType(IssueType issueType) {
        try {
            issueManager.deleteIssueType(issueType);
            logger.info("Issue type with id '" + issueType.getId() + "' was deleted.");
        } catch (NotExistException e) {
            createFacesMessage("system.fatal.delete.state.notExist", null);
        } catch (IllegalOperationException e) {
            createFacesMessage("system.fatal.delete.state.used", null);
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

    public boolean validateIssueTypeName() {
        if (occupiedChecker.isIssueTypeNameOccupied(issueTypeName)) {
            createFacesMessage("page.validator.occupied.issueType.name", "createIssueTypeForm:issueTypeName");
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

    private void clearFields() {
        issueTypeName = null;
        stateName = null;
        issueTypeId = 0;
        stateId = 0;
    }

}
