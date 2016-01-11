package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.UserManager;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.ui.configuration.I18NProvider;
import edu.piotrjonski.scrumus.ui.configuration.PathProvider;
import lombok.Data;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@RequestScoped
@Named
public class UserService implements Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private UserManager userManager;

    @Inject
    private PathProvider pathProvider;

    @Inject
    private I18NProvider i18NProvider;

    @Inject
    private OccupiedChecker occupiedChecker;

    @Size(max = 20, message = "{validator.size.username}")
    private String username;
    @Size(max = 30, message = "{validator.size.firstname}")
    private String firstname;
    @Size(max = 30, message = "{validator.size.surname}")
    private String surname;
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
             message = "{validator.pattern.email}")
    @Size(max = 40)
    private String email;

    public String createUser() {
        if (validateFields()) {
            return null;
        }
        Developer developer = createDeveloperFromFields();
        try {
            userManager.create(developer);
            return pathProvider.getRedirectPath("admin.listUsers");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.user", null);
            return null;
        }
    }

    public List<Developer> getAllUsers() {
        return userManager.findAllUsers();
    }

    public void deleteUser(Developer developer) {
        userManager.delete(developer);
    }

    public boolean validateUsername(String username) {
        if (occupiedChecker.isUsernameOccupied(username)) {
            createFacesMessage("page.validator.occupied.user.username", "createUserForm:username");
            return true;
        }
        return false;
    }

    public boolean validateEmail(String email) {
        if (occupiedChecker.isEmailOccupied(email)) {
            createFacesMessage("page.validator.occupied.user.email", "createUserForm:email");
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

    private boolean validateFields() {
        return validateUsername(username) || validateEmail(email);
    }

    private Developer createDeveloperFromFields() {
        Developer developer = new Developer();
        developer.setEmail(email);
        developer.setFirstName(firstname);
        developer.setSurname(surname);
        developer.setUsername(username);
        return developer;
    }

}
