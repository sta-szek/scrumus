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
        try {
            String username = extractUserNameFromFullname(productOwnerFullname);
            permissionManager.setProductOwner(productOwnerProjectKey, username);
            return "";
        } catch (NotExistException | IllegalOperationException e) {
            createFacesMessage(e.getMessage(), null);
            return null;
        } finally {
            clearFields();
        }
    }

    public void removeProductOwner() {
        permissionManager.divestProductOwner(productOwnerProjectKey);
    }

    private void clearFields() {
        productOwnerFullname = null;
        productOwnerProjectKey = null;
    }

    private void createFacesMessage(String message, String field) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                     message,
                                                     null);
        facesContext.addMessage(field, facesMessage);
    }

    private String extractUserNameFromFullname(final String productOwnerFullname) {
        String usernameWithBrace = productOwnerFullname.split("\\(")[1];
        return usernameWithBrace.substring(0, usernameWithBrace.length() - 1);
    }

}
