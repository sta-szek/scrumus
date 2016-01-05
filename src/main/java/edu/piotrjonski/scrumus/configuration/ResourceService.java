package edu.piotrjonski.scrumus.configuration;

import javax.inject.Inject;
import java.util.ResourceBundle;

public abstract class ResourceService {

    @Inject
    private LocaleConfiguration localeConfiguration;

    public String getProperty(String key) {
        if (key == null) {
            return "";
        }
        return ResourceBundle.getBundle(getPath(), localeConfiguration.getCurrentLocaleOrDefault())
                             .getString(key);
    }

    protected abstract String getPath();
}
