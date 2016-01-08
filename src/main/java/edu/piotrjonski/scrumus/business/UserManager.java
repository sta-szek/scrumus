package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.DeveloperDAO;
import edu.piotrjonski.scrumus.dao.PasswordDAO;
import edu.piotrjonski.scrumus.domain.Developer;
import edu.piotrjonski.scrumus.domain.Password;
import edu.piotrjonski.scrumus.services.HashGenerator;
import edu.piotrjonski.scrumus.services.MailSender;
import org.slf4j.Logger;

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
    private transient Logger logger;

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

    @Inject
    private TeamManager teamManager;

    public List<String> findAllUsernames() {
        return developerDAO.findAllUsernames();
    }

    public List<String> findAllEmails() {
        return developerDAO.findAllEmails();
    }

    public List<Developer> findAllUsers() {
        return developerDAO.findAll();
        //TODO testy
    }

    public boolean emailExist(String email) {
        return developerDAO.emailExist(email);
        //TODO TESTY
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Optional<Developer> create(Developer developer)
            throws AlreadyExistException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (developerExist(developer)) {
            throw new AlreadyExistException("User already exist.");
        }
        logger.info(mailSender.getClass()
                              .getName());
        return createUserAndSendPassword(developer);
    }

    public Optional<Developer> update(Developer developer) throws NotExistException {
        if (!developerExist(developer)) {
            throw new NotExistException("User does not exist.");
        }
        return developerDAO.saveOrUpdate(developer);
    }

    public void delete(Developer developer) {
        permissionManager.removeAllRolesFromUser(developer);
        teamManager.removeUserFromAllTeams(developer);
        developerDAO.delete(developer.getId());
    }

    private Optional<Developer> createUserAndSendPassword(final Developer user)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Optional<Developer> savedDeveloper = developerDAO.saveOrUpdate(user);
        if (savedDeveloper.isPresent()) {
            if (generatePasswordAndSendEmail(savedDeveloper)) {
                return savedDeveloper;
            }
        }
        logger.info("Couldn't create user " + user);
        return Optional.empty();
    }

    private boolean generatePasswordAndSendEmail(final Optional<Developer> savedDeveloper) {
        final String stringPassword;
        final String encodedPassword;

        try {
            stringPassword = hashGenerator.generateHash();
            encodedPassword = hashGenerator.encodeWithSHA256(stringPassword);
            if (savePasswordAndSendEmail(savedDeveloper, stringPassword, encodedPassword)) {
                return true;
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.info("Couldn't encode text because of:  " + e.getMessage());
        }

        return false;
    }

    private boolean savePasswordAndSendEmail(final Optional<Developer> savedDeveloper,
                                             final String stringPassword,
                                             final String encodedPassword) {
        Optional<Password> savedPassword = createAndSavePassword(savedDeveloper.get(), encodedPassword);
        if (savedPassword.isPresent()) {
            sendMessage(savedDeveloper.get(), stringPassword);
            return true;
        } else {
            developerDAO.delete(savedDeveloper.get()
                                              .getId());
            logger.info("Couldn't save user password. User was deleted.");
        }
        return false;
    }

    private void sendMessage(final Developer savedDeveloper, final String stringPassword) {
        String subject = "Witamy w scrumus!";
        String message = "Twoje hasło to " + stringPassword + "\n Pamiętaj aby je zmienić zaraz po zalogowaniu.";
        String email = savedDeveloper.getEmail();
        try {
            mailSender.sendMail(email, subject, message);
            logger.info("Message with password has been sent");
        } catch (MessagingException e) {
            developerDAO.delete(savedDeveloper.getId());
            logger.info("Couldn't send email with password. User was deleted. Reason: " + e.getMessage());
            logger.error(mailSender.getClass()
                                   .getName());
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