package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.IllegalOperationException;
import edu.piotrjonski.scrumus.business.NotExistException;
import edu.piotrjonski.scrumus.business.PermissionManager;
import edu.piotrjonski.scrumus.domain.Developer;
import lombok.Data;
import org.slf4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;

@Data
public class PermissionService implements Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private PermissionManager permissionManager;

    private String productOwnerFullname;

    private String productOwnerProjectKey;

    private String scrumMasterTeamId;

    private String scrumMasterFullName;

    public boolean isAdmin(Developer user) {
        return permissionManager.isAdmin(user);
    }

    public void grantAdminPermission(Developer user) {
        permissionManager.grantAdminPermission(user);
    }

    public void removeAdminPermission(Developer user) {
        permissionManager.removeAdminPermission(user);
    }

    public String setProductOwner() {
        if (productOwnerFullname == null) {
            return null;
        }
        try {
            String username = extractUserNameFromFullname(productOwnerFullname);
            permissionManager.setProductOwner(productOwnerProjectKey, username);
            clearFields();
            return "";
        } catch (NotExistException | IllegalOperationException e) {
            createFacesMessage(e.getMessage(), null);
            clearFields();
            return null;
        }
    }

    public String setScrumMaster() {
        if (scrumMasterFullName == null) {
            return null;
        }
        try {
            String username = extractUserNameFromFullname(scrumMasterFullName);
            int teamIntId = Integer.parseInt(scrumMasterTeamId);
            permissionManager.setScrumMaster(teamIntId, username);
            clearFields();
            return "";
        } catch (NotExistException e) {
            createFacesMessage(e.getMessage(), null);
            clearFields();
            return null;
        } catch (NumberFormatException e) {
            clearFields();
            return null;
        }
    }

    public void removeProductOwner() {
        permissionManager.removeProductOwnerFromProject(productOwnerProjectKey);
    }

    public void removeScrumMaster() {
        try {
            int teamIntId = Integer.parseInt(scrumMasterTeamId);
            permissionManager.removeScrumMasterFromTeam(teamIntId);
        } catch (NumberFormatException e) {

        }
    }

    String extractUserNameFromFullname(final String userFullname) {
        if (!userFullname.contains("(")) {
            return "";
        }
        String usernameWithBrace = userFullname.split("\\(")[1];
        return usernameWithBrace.substring(0, usernameWithBrace.length() - 1);
    }

    private void clearFields() {
        productOwnerFullname = null;
        productOwnerProjectKey = null;
        scrumMasterTeamId = null;
        scrumMasterFullName = null;
    }

    private void createFacesMessage(String message, String field) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                     message,
                                                     null);
        facesContext.addMessage(field, facesMessage);
    }

}
