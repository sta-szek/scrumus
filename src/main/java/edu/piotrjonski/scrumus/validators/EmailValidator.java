package edu.piotrjonski.scrumus.validators;

import edu.piotrjonski.scrumus.business.UserManager;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9]" +
                                                "(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    @Inject
    private UserManager userManager;

    @Override
    public void validate(final FacesContext context, final UIComponent component, final Object value) throws ValidatorException {
        final String email = value == null
                             ? ""
                             : value.toString();
        if ((email == null) || (email.isEmpty())) {
            createMessageAndThrowException(context, "Email nie może być pusty");
        }
        if (userManager.emailExist(email)) {
            createMessageAndThrowException(context, "Email jest już zajęty");

        }
        final Matcher matcher = PATTERN.matcher(email);
        if (!matcher.find()) {
            createMessageAndThrowException(context, "Nieprawid³owy adres e-mail");

        }
        //TODO TESTY
    }

    private void createMessageAndThrowException(final FacesContext context,
                                                final String summary) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        message.setSummary(summary);
        context.addMessage("email", message);
        throw new ValidatorException(message);
    }
}
