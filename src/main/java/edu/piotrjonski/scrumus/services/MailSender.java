package edu.piotrjonski.scrumus.services;


import edu.piotrjonski.scrumus.configuration.EmailConfiguration;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.time.LocalDateTime;

@Path("/email")
@Stateless
public class MailSender {
    private static final long serialVersionUID = 1L;
    private static final String FROM = "pojo@staszek.me";

//    @Resource(lookup = "java:jboss/mail/Default")
//    private Session mailSession;

    @Inject
    private EmailConfiguration emailConfiguration;

    @GET
    @Path("/test")
    @Produces("text/html; charset=UTF-8")
    public String sendTestMail(@QueryParam("email") String email) throws MessagingException {
        MimeMessage mimeMessage = createSampleMessage(email);
        Transport.send(mimeMessage);
        return "Email został wysłany.";
    }

    public void sendMail(String email, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = createMimeMessage(email, subject, message);
        Transport.send(mimeMessage);
    }

    private MimeMessage createSampleMessage(final String email) throws MessagingException {
        MimeMessage mimeMessage = createMimeMessage(email, "Testowa wiadomość email", "Czas nadania wiadomości: " + LocalDateTime.now()
                                                                                                                                 .toString());
        return mimeMessage;
    }

    private MimeMessage createMimeMessage(final String email, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(emailConfiguration.createSession());
        Address[] to = new InternetAddress[]{new InternetAddress(email)};
        mimeMessage.setFrom(FROM);
        mimeMessage.setRecipients(Message.RecipientType.TO, to);
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(content, "text/plain");
        return mimeMessage;
    }

}
