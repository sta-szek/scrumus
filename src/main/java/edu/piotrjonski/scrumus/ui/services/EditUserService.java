package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.CreateUserException;
import edu.piotrjonski.scrumus.business.NotExistException;
import edu.piotrjonski.scrumus.business.UserManager;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.ui.configuration.I18NProvider;
import lombok.Data;
import org.slf4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.management.*;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.Optional;

@Data
public class EditUserService implements Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private UserManager userManager;

    @Inject
    private I18NProvider i18NProvider;

    private Developer currentUser;

    @Size(min = 8, message = "{validator.size.password}")
    private String newPassword;

    public Developer findCurrentUser(String username) {
        try {
            Optional<Developer> userOptional = userManager.findByUsername(username);
            userOptional.ifPresent(this::setCurrentUser);
            return userOptional.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Developer findUser(String userId) {
        try {
            int userInId = Integer.parseInt(userId);
            Optional<Developer> userOptional = userManager.findByUserId(userInId);
            return userOptional.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String edit() {
        try {
            userManager.update(currentUser);
            logger.info("User with id '" + currentUser.getId() + "' was edited.");
            createFacesMessage("system.info.edit.user", null);
            return null;
        } catch (NotExistException e) {
            logger.info(e.getMessage(), e);
            return null;
        }
    }

    public void changePassword() {
        int userId = getCurrentUserId();
        try {
            userManager.changePassword(userId, newPassword);
            logger.info("User with id '" + currentUser.getId() + "' has changed password.");
            createFacesMessage("system.info.edit.user", null);
            flushJAAS();
        } catch (CreateUserException e) {
            logger.error(e.getMessage(), e);
            createErrorFacesMessage("system.fatal.change.password", null);
        }
    }

    private void flushJAAS() {
        MBeanServerConnection mbeanServerConnection = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName mbeanName = new ObjectName("jboss.as:subsystem=security,security-domain=scrumusSecurity");
            mbeanServerConnection.invoke(mbeanName, "flushCache", null, null);
        } catch (InstanceNotFoundException | MBeanException | ReflectionException | MalformedObjectNameException | IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private int getCurrentUserId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String username = FacesContext.getCurrentInstance()
                                      .getApplication()
                                      .evaluateExpressionGet(facesContext,
                                                             "#{request.remoteUser}",
                                                             String.class);
        return userManager.findByUsername(username)
                          .orElse(new Developer())
                          .getId();
    }

    private void createFacesMessage(String property, String field) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                     i18NProvider.getMessage(property),
                                                     null);
        facesContext.addMessage(field, facesMessage);
    }

    private void createErrorFacesMessage(String property, String field) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                     i18NProvider.getMessage(property),
                                                     null);
        facesContext.addMessage(field, facesMessage);
    }

}
