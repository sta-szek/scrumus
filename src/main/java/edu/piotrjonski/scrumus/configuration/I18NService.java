package edu.piotrjonski.scrumus.configuration;


public class I18NService extends ResourceService {

    public static final String MESSAGES_PATH = "resources.messages";

    @Override
    protected String getPath() {
        return MESSAGES_PATH;
    }
}