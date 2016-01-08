package edu.piotrjonski.scrumus.ui.configuration;


import edu.piotrjonski.scrumus.configuration.ConfigurationProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named(value = "i18nProvider")
public class I18NProvider extends ConfigurationProvider {

    public I18NProvider() {
        configurationPath = "messages";
    }
}
