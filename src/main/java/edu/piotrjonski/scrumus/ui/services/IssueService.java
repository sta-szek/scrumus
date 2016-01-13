package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.IllegalOperationException;
import edu.piotrjonski.scrumus.business.IssueManager;
import edu.piotrjonski.scrumus.business.NotExistException;
import edu.piotrjonski.scrumus.domain.IssueType;
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

    public List<IssueType> getAllIssueTypes() {
        return issueManager.findAllIssueTypes();
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

    public String editIssueType() {
        IssueType issueTypeFromField = createIssueTypeFromField();
        logger.error(issueTypeFromField.toString());
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

    public boolean validateIssueTypeName() {
        if (occupiedChecker.isIssueTypeNameOccupied(issueTypeName)) {
            createFacesMessage("page.validator.occupied.issueType.name", "createIssueTypeForm:issueTypeName");
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

    private void clearFields() {
        issueTypeName = null;
        issueTypeId = 0;
    }

}
