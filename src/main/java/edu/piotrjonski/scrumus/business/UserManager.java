package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.dao.PasswordDAO;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Password;
import edu.piotrjonski.scrumus.services.HashGenerator;
import edu.piotrjonski.scrumus.services.MailSender;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserManager {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private PasswordDAO passwordDAO;

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private HashGenerator hashGenerator;

    @Inject
    private MailSender mailSender;

    public List<Developer> findAllUsers() {
        return developerDAO.findAll();
        //TODO testy
    }

    public boolean emailExist(String email) {
        return developerDAO.emailExist(email);
        //TODO TESTY
    }

    public Optional<Developer> create(Developer developer)
            throws AlreadyExistException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (developerExist(developer)) {
            throw new AlreadyExistException("User already exist.");
        }
        return createUserAndSendPassword(developer);
    }

    public Optional<Developer> update(Developer developer) throws NotExistException {
        if (!developerExist(developer)) {
            throw new NotExistException("User does not exist.");
        }
        return developerDAO.saveOrUpdate(developer);
    }

    public void delete(int userId) {
        developerDAO.delete(userId);
    }

    private Optional<Developer> createUserAndSendPassword(final Developer developer)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Optional<Developer> savedDeveloper = developerDAO.saveOrUpdate(developer);
        if (savedDeveloper.isPresent()) {
            if (generatePasswordAndSendEmail(savedDeveloper)) {
                return savedDeveloper;
            }
        }
        return Optional.empty();
    }

    private boolean generatePasswordAndSendEmail(final Optional<Developer> savedDeveloper)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String stringPassword = hashGenerator.generateHash();
        String encodedPassword = hashGenerator.encodeWithSHA256(stringPassword);
        if (savePasswordAndSendEmail(savedDeveloper, stringPassword, encodedPassword)) {
            return true;
        }
        return false;
    }

    private boolean savePasswordAndSendEmail(final Optional<Developer> savedDeveloper,
                                             final String stringPassword,
                                             final String encodedPassword) {
        Optional<Password> savedPassword = createAndSavePassword(savedDeveloper.get(), encodedPassword);
        if (savedPassword.isPresent()) {
            sendMessage(savedDeveloper, stringPassword);
            return true;
        } else {
            developerDAO.delete(savedDeveloper.get()
                                              .getId());
        }
        return false;
    }

    private void sendMessage(final Optional<Developer> savedDeveloper, final String stringPassword) {
        String subject = "Witamy w scrumus!";
        String message = "Twoje hasło to " + stringPassword;
        String email = savedDeveloper.get()
                                     .getEmail();
        try {
            mailSender.sendMail(email, subject, message);
        } catch (MessagingException e) {
            developerDAO.delete(savedDeveloper.get()
                                              .getId());
        }
    }

    private Optional<Password> createAndSavePassword(final Developer savedDeveloper, final String encodedPassword) {
        Password password = new Password();
        password.setDeveloperId(savedDeveloper.getId());
        password.setPassword(encodedPassword);
        return passwordDAO.saveOrUpdate(password);
    }

    private boolean developerExist(Developer developer) {
        return developerDAO.exist(developer.getId());
    }

}