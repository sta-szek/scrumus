package edu.piotrjonski.scrumus.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Locale;
import java.util.ResourceBundle;

@ApplicationScoped
@Named(value = "configurationProvider")
public class ConfigurationProvider {

    protected String configurationPath = "configuration";

    public String getMessage(String key) {
        if (key == null) {
            return "";
        }
        return ResourceBundle.getBundle(configurationPath, createDefaultLocale())
                             .getString(key);
    }

    private Locale createDefaultLocale() {
        return new Locale.Builder().setLanguage("en")
                                   .setRegion("US")
                                   .build();
    }
}