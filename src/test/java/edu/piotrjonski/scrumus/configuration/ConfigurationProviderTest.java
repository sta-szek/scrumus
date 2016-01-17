package edu.piotrjonski.scrumus.configuration;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.reflect.Whitebox.getInternalState;
import static org.powermock.reflect.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigurationProvider.class)
public class ConfigurationProviderTest {

    @Test
    public void shouldReturnEmptyStringIfKeyIsNull() {
        // given
        ConfigurationProvider configurationProvider = new ConfigurationProvider();

        // when
        String result = configurationProvider.getMessage(null);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnMessageFromResourceBundle() throws Exception {
        // given
        ResourceBundle resourceBundle = mock(ResourceBundle.class);
        ConfigurationProvider configurationProvider = new ConfigurationProvider();
        setInternalState(configurationProvider, "resourceBundle", resourceBundle);
        String expectedMessage = "expectedMessage";
        doReturn(expectedMessage).when(resourceBundle)
                                 .getString(anyString());

        // when
        String result = configurationProvider.getMessage(expectedMessage);

        // then
        assertThat(result).isEqualTo(expectedMessage);
    }

    @Test
    public void shouldCreateResourceBundle() {
        // given

        // when
        ConfigurationProvider configurationProvider = new ConfigurationProvider();
        Object result = getInternalState(configurationProvider, "resourceBundle");

        // then
        assertThat(result).isNotNull();
    }

}