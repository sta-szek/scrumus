package edu.piotrjonski.scrumus.configuration;


import org.junit.Test;

import javax.mail.Session;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

public class EmailConfigurationTest {

    @Test
    public void shouldCreateSession() {
        // given
        ConfigurationProvider configurationProvider = mock(ConfigurationProvider.class);
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        setInternalState(emailConfiguration, "configurationProvider", configurationProvider);
        doReturn("").when(configurationProvider)
                    .getMessage(anyString());

        // when
        Session result = emailConfiguration.createSession();

        // then
        assertThat(result).isNotNull();
    }

    @Test
    public void shouldReturnFrom() {
        // given
        ConfigurationProvider configurationProvider = mock(ConfigurationProvider.class);
        EmailConfiguration emailConfiguration = new EmailConfiguration();
        setInternalState(emailConfiguration, "configurationProvider", configurationProvider);
        String expectedResult = "from";
        doReturn(expectedResult).when(configurationProvider)
                                .getMessage(anyString());

        // when
        String result = emailConfiguration.getFrom();

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

}