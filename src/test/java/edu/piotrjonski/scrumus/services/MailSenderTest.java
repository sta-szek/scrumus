package edu.piotrjonski.scrumus.services;

import edu.piotrjonski.scrumus.configuration.EmailConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Transport.class, Session.class, MimeMessage.class, MailSender.class})
public class MailSenderTest {

    @Mock
    private EmailConfiguration emailConfiguration;

    @InjectMocks
    private MailSender mailSender;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void shouldReturnAnyString() throws Exception {
        // given
        PowerMockito.mockStatic(Transport.class);
        doNothing().when(Transport.class);
        Transport.send(any(MimeMessage.class));
        whenNew(MimeMessage.class).withAnyArguments()
                                  .thenReturn(mock(MimeMessage.class));
        when(emailConfiguration.createSession()).thenReturn(mock(Session.class));
        when(emailConfiguration.getFrom()).thenReturn("test");

        // when
        String result = mailSender.sendTestMail("email");

        // then
        assertThat(result).isNotEmpty();
    }

    @Test
    public void shouldSendMail() throws Exception {
        // given
        PowerMockito.mockStatic(Transport.class);
        doNothing().when(Transport.class);
        Transport.send(any(MimeMessage.class));
        MimeMessage mimeMessageMock = mock(MimeMessage.class);
        whenNew(MimeMessage.class).withAnyArguments()
                                  .thenReturn(mimeMessageMock);
        when(emailConfiguration.createSession()).thenReturn(mock(Session.class));
        when(emailConfiguration.getFrom()).thenReturn("test");

        // when
        mailSender.sendMail("email", "email", "email");

        // then
        verifyStatic(times(1));
        Transport.send(mimeMessageMock);
    }
}