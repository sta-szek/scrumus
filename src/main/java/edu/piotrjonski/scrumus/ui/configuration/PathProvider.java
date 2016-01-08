package edu.piotrjonski.scrumus.ui.configuration;


import edu.piotrjonski.scrumus.configuration.ConfigurationProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named(value = "pathProvider")
public class PathProvider extends ConfigurationProvider {

    public PathProvider() {
        configurationPath = "paths";
    }
}
