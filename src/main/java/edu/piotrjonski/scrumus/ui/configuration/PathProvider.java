package edu.piotrjonski.scrumus.ui.configuration;


import edu.piotrjonski.scrumus.configuration.ConfigurationProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named(value = "pathProvider")
public class PathProvider extends ConfigurationProvider {

    private static final String REDIRECTION_APPENDIX = "?faces-redirect=true";

    public PathProvider() {
        super("paths");
    }

    public String getRedirectPath(String key) {
        return getMessage(key).concat(REDIRECTION_APPENDIX);
    }
}
