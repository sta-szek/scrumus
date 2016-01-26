package edu.piotrjonski.scrumus.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Locale;
import java.util.ResourceBundle;

@ApplicationScoped
@Named(value = "configurationProvider")
public class ConfigurationProvider {

    private ResourceBundle resourceBundle;

    public ConfigurationProvider() {
        this("configuration");
    }

    protected ConfigurationProvider(String path) {
        resourceBundle = ResourceBundle.getBundle(path, createDefaultLocale());
    }

    public String getMessage(String key) {
        if (key == null) {
            return "";
        }
        return resourceBundle.getString(key);
    }

    private Locale createDefaultLocale() {
        return new Locale.Builder().setLanguage("en")
                                   .setRegion("US")
                                   .build();
    }
}