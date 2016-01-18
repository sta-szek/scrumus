package edu.piotrjonski.scrumus.services;


import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectKeyGenerator {

    public static final int MAX_PROJECT_KEY = 8;

    private Map<Integer, Integer> lenghtMapper = new HashMap<>();

    public ProjectKeyGenerator() {
        lenghtMapper.put(2, 4);
        lenghtMapper.put(3, 2);
        lenghtMapper.put(4, 2);
    }

    public String generateProjectKey(String projectName) {
        if (projectName == null) {
            return "";
        }
        return getFirstLetters(calculateProjectKey(projectName).toUpperCase(), MAX_PROJECT_KEY);
    }

    private String calculateProjectKey(final String projectName) {
        String[] splitName = projectName.split(" ");
        int length = splitName.length;
        if (length == 1) {
            return trimProjectName(projectName);
        } else {
            Integer wordLength = lenghtMapper.get(length);
            if (wordLength == null) {
                return concatFirstLetters(splitName, 1);
            } else {
                return concatFirstLetters(splitName, length);
            }
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
