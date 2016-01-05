package edu.piotrjonski.scrumus.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import java.util.Locale;

@ApplicationScoped
public class LocaleConfiguration {

    public static final Locale DEFAULT_LOCALE = new Locale.Builder().setLanguage("en")
                                                                    .setRegion("US")
                                                                    .build();

    public Locale getDefaultLocale() {
        return DEFAULT_LOCALE;
    }

    public Locale getCurrentLocaleOrDefault() {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (currentInstance != null) {
            return extractLocaleFromFacesContext(currentInstance);
        } else {
            return getDefaultLocale();
        }
    }

    private Locale extractLocaleFromFacesContext(final FacesContext currentInstance) {
        return currentInstance
                .getViewRoot()
                .getLocale();
    }
}
