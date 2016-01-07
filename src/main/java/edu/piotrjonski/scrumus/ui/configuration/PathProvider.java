package edu.piotrjonski.scrumus.ui.configuration;


import edu.piotrjonski.scrumus.configuration.ConfigurationProvider;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PathProvider extends ConfigurationProvider {

    public PathProvider() {
        configurationPath = "paths";
    }
}
