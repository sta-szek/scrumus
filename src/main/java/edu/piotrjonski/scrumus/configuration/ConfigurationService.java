package edu.piotrjonski.scrumus.configuration;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigurationService extends ResourceService {

    public static final String CONFIGURATION_PATH = "resources/configuration.properties";

    @Override
    protected String getPath() {
        return CONFIGURATION_PATH;
    }
}

