package edu.piotrjonski.scrumus.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@ApplicationScoped
public class EmailConfiguration {

    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_START_TLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";

    public static final String EMAIL_SMTP_HOST = "email.smtp_host";
    public static final String EMAIL_SMTP_PORT = "email.smtp_port";
    public static final String EMAIL_FROM = "email.from";
    public static final String EMAIL_PASSWORD = "email.password";
    public static final String EMAIL_TLS = "email.tls";
    public static final String EMAIL_AUTH = "email.auth";

    @Inject
    @Named(value = "configurationProvider")
    private ConfigurationProvider configurationProvider;

    public Session createSession() {
        return Session.getInstance(createProperties(), createAuthenticator());
    }

    public String getFrom() {
        return configurationProvider.getMessage(EMAIL_FROM);
    }

    private Properties createProperties() {
        Properties properties = new Properties();
        properties.put(MAIL_SMTP_AUTH, configurationProvider.getMessage(EMAIL_AUTH));
        properties.put(MAIL_SMTP_START_TLS_ENABLE, configurationProvider.getMessage(EMAIL_TLS));
        properties.put(MAIL_SMTP_HOST, configurationProvider.getMessage(EMAIL_SMTP_HOST));
        properties.put(MAIL_SMTP_PORT, configurationProvider.getMessage(EMAIL_SMTP_PORT));
        return properties;
    }

    private Authenticator createAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(configurationProvider.getMessage(EMAIL_FROM),
                                                  configurationProvider.getMessage(EMAIL_PASSWORD));

            }
        };
    }

}
