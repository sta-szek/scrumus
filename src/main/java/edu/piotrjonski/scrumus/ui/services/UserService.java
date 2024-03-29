package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.AlreadyExistException;
import edu.piotrjonski.scrumus.business.CreateUserException;
import edu.piotrjonski.scrumus.business.UserManager;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.ProductOwner;
import edu.piotrjonski.scrumus.domain.ScrumMaster;
import edu.piotrjonski.scrumus.ui.configuration.I18NProvider;
import edu.piotrjonski.scrumus.ui.configuration.PathProvider;
import lombok.Data;
import org.slf4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
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

    private Developer userToDelete;

    @GET
    @Path("/users/{userId}")
    public Developer findUser(String userId) {
        try {
            int userInId = Integer.parseInt(userId);
            Optional<Developer> userOptional = userManager.findByUserId(userInId);
            return userOptional.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Developer findUserByUsername(String username) {
        try {
            Optional<Developer> userOptional = userManager.findByUsername(username);
            return userOptional.orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void setUserToDelete(Developer user) {
        userToDelete = user;
    }

    public String createUser() {
        if (validateFields()) {
            return null;
        }
        Developer developer = createDeveloperFromFields();
        try {
            Developer user = userManager.create(developer)
                                        .get();
            logger.info("Created user with id '" + user.getId() + "'.");
            return pathProvider.getRedirectPath("admin.listUsers");
        } catch (CreateUserException e) {
            logger.error(e.getMessage(), e);
            createFacesMessage("system.fatal.create.user", null);
            return null;
        } catch (AlreadyExistException e) {
            createFacesMessage("system.fatal.create.user.exist", null);
            return null;
        }
    }

    public List<Developer> getAllUsers() {
        return userManager.findAllUsers();
    }

    public Developer findScrumMaster(int teamId) {
        return userManager.findScrumMaster(teamId)
                          .orElse(new ScrumMaster())
                          .getDeveloper();
    }

    public void deleteUser() {
        if (userToDelete.getId() == getCurrentUserId()) {
            createFacesMessage("system.fatal.delete.user", null);
            return;
        }
        userManager.delete(userToDelete);
        logger.info("User with id '" + userToDelete.getId() + "' was deleted.");
    }

    public Developer findProductOwner(String projectKey) {
        return userManager.findProductOwner(projectKey)
                          .orElse(new ProductOwner())
                          .getDeveloper();
    }

    public List<String> completeUser(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return getAllUsers().stream()
                            .filter(user -> anyNameStatsWith(user, lowerCaseQuery))
                            .map(this::getUserFullName)
                            .collect(Collectors.toList());
    }

    public int getCurrentUserId() {
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

    public String logout() {
        ((HttpSession) FacesContext.getCurrentInstance()
                                   .getExternalContext()
                                   .getSession(true)).invalidate();
        return pathProvider.getRedirectPath("index");
    }

    private boolean validateUsername(String username) {
        if (occupiedChecker.isUsernameOccupied(username)) {
            createFacesMessage("page.validator.occupied.user.username", "createUserForm:username");
            return true;
        }
        return false;
    }

    private boolean validateEmail(String email) {
        if (occupiedChecker.isEmailOccupied(email)) {
            createFacesMessage("page.validator.occupied.user.email", "createUserForm:email");
            return true;
        }
        return false;
    }

    private String getUserFullName(Developer user) {
        return user.getFirstName() + " " + user.getSurname() + " (" + user.getUsername() + ")";
    }

    private boolean anyNameStatsWith(Developer user, String query) {
        return startsWith(user.getFirstName(), query) || startsWith(user.getSurname(), query) || startsWith(user.getUsername(), query);
    }

    private boolean startsWith(String text, String query) {
        return text.toLowerCase()
                   .startsWith(query);
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
