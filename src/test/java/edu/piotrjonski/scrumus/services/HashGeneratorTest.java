package edu.piotrjonski.scrumus.services;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class HashGeneratorTest {

    @Test
    public void shouldGenerateDifferentHash() {
        // given
        HashGenerator hashGenerator = new HashGenerator();

        // when
        String hash1 = hashGenerator.generateHash();
        String hash2 = hashGenerator.generateHash();

        // then
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    public void shouldEncodeWithSHA256() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // given
        String stringToHash = "123";
        String expectedHash = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3";
        HashGenerator hashGenerator = new HashGenerator();

        // when
        String result = hashGenerator.encodeWithSHA256(stringToHash);

        // then
        assertThat(result).isEqualTo(expectedHash);
    }
}