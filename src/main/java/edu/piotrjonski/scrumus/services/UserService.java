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

    @Size(max = 20, message = "Nazwa użytkownika może zawierać maksymalnie 20 znaków")
    private String username;
    @Size(max = 30, message = "Imię użytkownika może zawierać maksymalnie 30 znaków")
    private String firstname;
    @Size(max = 30, message = "Nazwisko użytkownika może zawierać maksymalnie 30 znaków")
    private String surname;
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    @Size(max = 40, message = "Adres e-mail użytkownika może zawierać maksymalnie 40 znaków")
    private String email;

    public void createUser() {
        Developer developer = createDeveloper();
        try {
            userManager.create(developer);
        } catch (AlreadyExistException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
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
