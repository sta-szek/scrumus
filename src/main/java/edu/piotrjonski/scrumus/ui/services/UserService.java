package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.AlreadyExistException;
import edu.piotrjonski.scrumus.business.UserManager;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.ui.configuration.PathProvider;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Data
@RequestScoped
@Named
public class UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private UserManager userManager;

    @Inject
    private transient PathProvider pathProvider;

    @Size(max = 20)
    private String username;
    @Size(max = 30)
    private String firstname;
    @Size(max = 30)
    private String surname;
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    @Size(max = 40)
    private String email;

    public String createUser() {
        Developer developer = createDeveloper();
        try {
            userManager.create(developer);
            return pathProvider.getProperty("admin.listUsers");
        } catch (AlreadyExistException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Developer> getAllUsers() {
        List<Developer> users = userManager.findAllUsers();
        logger.info("users.size " + users.size());
        return users;
    }

    public void deleteUser(Developer developer) {
        userManager.delete(developer);
    }

    private Developer createDeveloper() {
        Developer developer = new Developer();
        developer.setEmail(email);
        developer.setFirstName(firstname);
        developer.setSurname(surname);
        developer.setUsername(username);
        return developer;
    }
}
