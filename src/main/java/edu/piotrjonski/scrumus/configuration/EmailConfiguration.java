package edu.piotrjonski.scrumus.configuration;

import org.slf4j.Logger;

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

    @Inject
    private transient Logger logger;

    public Session createSession() {
        return Session.getInstance(createProperties(), createAuthenticator());
    }

    private Properties createProperties() {
        Properties properties = new Properties();
        properties.put(MAIL_SMTP_AUTH, configurationProvider.getProperty(EMAIL_AUTH));
        properties.put(MAIL_SMTP_START_TLS_ENABLE, configurationProvider.getProperty(EMAIL_TLS));
        properties.put(MAIL_SMTP_HOST, configurationProvider.getProperty(EMAIL_SMTP_HOST));
        properties.put(MAIL_SMTP_PORT, configurationProvider.getProperty(EMAIL_SMTP_PORT));
        return properties;
    }

    private Authenticator createAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(configurationProvider.getProperty(EMAIL_FROM),
                                                  configurationProvider.getProperty(EMAIL_PASSWORD));

            }
        };
    }

}
