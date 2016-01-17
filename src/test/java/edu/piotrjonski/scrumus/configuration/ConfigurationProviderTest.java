package edu.piotrjonski.scrumus.configuration;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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


}