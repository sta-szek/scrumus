package edu.piotrjonski.scrumus.ui.configuration;


import edu.piotrjonski.scrumus.configuration.ConfigurationProvider;

import javax.faces.bean.ApplicationScoped;

@ApplicationScoped
public class PathProvider extends ConfigurationProvider {

    public PathProvider() {
        configurationPath = "paths";
    }
}
