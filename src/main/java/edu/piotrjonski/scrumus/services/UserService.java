package edu.piotrjonski.scrumus.services;

import edu.piotrjonski.scrumus.business.AlreadyExistException;
import edu.piotrjonski.scrumus.business.UserManager;
import edu.piotrjonski.scrumus.domain.Developer;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Named
@RequestScoped
@Data
public class UserService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private UserManager userManager;

    private String username;
    private String firstname;
    private String surname;
    private String email;

    public void createUser() {
        Developer developer = createDeveloper();
        logger.info("Stworzono uzytkownika " + developer);
        try {
            userManager.create(developer);
        } catch (AlreadyExistException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public List<Developer> getAllUsers() {
        logger.info("szuka uzytkownikow ");
        List<Developer> users = userManager.findAllUsers();
        logger.info("users.size " + users.size());
        return users;
    }

    public void resetFields() {

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
