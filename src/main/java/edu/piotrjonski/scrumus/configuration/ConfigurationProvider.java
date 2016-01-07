package edu.piotrjonski.scrumus.configuration;

import javax.enterprise.context.ApplicationScoped;
import java.util.Locale;
import java.util.ResourceBundle;

@ApplicationScoped
public class ConfigurationProvider {

    protected String configurationPath = "configuration";

    public String getProperty(String key) {
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