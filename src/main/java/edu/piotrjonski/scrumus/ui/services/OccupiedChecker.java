package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.ProjectManager;
import edu.piotrjonski.scrumus.business.UserManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;


@ApplicationScoped
@Named("occupiedChecker")
public class OccupiedChecker {

    @Inject
    private UserManager userManager;

    @Inject
    private ProjectManager projectManager;

    public boolean isUsernameOccupied(String username) {
        return getOccupiedUsernames().contains(username);
    }

    public boolean isEmailOccupied(final String email) {
        return getOccupiedEmails().contains(email);
    }

    public boolean isProjectKeyOccupied(final String key) {
        return getOccupiedProjectKeys().contains(key);
    }

    public boolean isProjectNameOccupied(final String projectName) {
        return getOccupiedProjectNames().contains(projectName);
    }

    private List<String> getOccupiedUsernames() {
        return userManager.findAllUsernames();
    }

    private List<String> getOccupiedEmails() {
        return userManager.findAllEmails();
    }

    private List<String> getOccupiedProjectKeys() {
        return projectManager.findAllKeys();
    }

    private List<String> getOccupiedProjectNames() {
        return projectManager.findAllNames();
    }
}
