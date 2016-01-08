package edu.piotrjonski.scrumus.services;


import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectKeyGenerator {

    public static final int MAX_PROJECT_KEY = 8;

    public String generateProjectKey(String projectName) {
        if (projectName == null) {
            return "";
        }
        return getFirstLetters(calculateProjectKey(projectName).toUpperCase(), MAX_PROJECT_KEY);
    }

    private String calculateProjectKey(final String projectName) {
        String[] splitName = projectName.split(" ");
        if (splitName.length == 1) {
            return trimProjectName(projectName);
        } else if (splitName.length == 2) {
            return concatFirstLetters(splitName, 4);
        } else if (splitName.length == 3) {
            return concatFirstLetters(splitName, 2);
        } else if (splitName.length == 4) {
            return concatFirstLetters(splitName, 2);
        } else {
            return concatFirstLetters(splitName, 1);
        }
    }

    private String trimProjectName(final String projectName) {
        if (projectName.length() > MAX_PROJECT_KEY) {
            return projectName.substring(0, MAX_PROJECT_KEY);
        } else {
            return projectName;
        }
    }

    private String concatFirstLetters(String[] words, int lettersAmount) {
        return Arrays.stream(words)
                     .map(word -> getFirstLetters(word, lettersAmount))
                     .collect(Collectors.joining());

    }

    private String getFirstLetters(String text, int letters) {
        if (text == null) {
            return "";
        }
        if (text.length() < letters) {
            return text;
        }
        return text.substring(0, letters);
    }


}
