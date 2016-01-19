package edu.piotrjonski.scrumus.services;


import javax.enterprise.context.ApplicationScoped;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@ApplicationScoped
public class HashGenerator {

    public String generateHash() {
        return UUID.randomUUID()
                   .toString()
                   .replaceAll("-", "");
    }

    public String encodeWithSHA256(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return String.format("%064x", new BigInteger(1, digest));
    }
}
