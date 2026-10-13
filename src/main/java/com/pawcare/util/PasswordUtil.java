package com.pawcare.util;

import java.security.*;
import java.util.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Salted PBKDF2: never store a login password in plain text. */
public final class PasswordUtil {
    private PasswordUtil() { }
    public static String hash(char[] password) {
        byte[] salt = new byte[16]; new SecureRandom().nextBytes(salt);
        return "pbkdf2$210000$" + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(derive(password, salt, 210000));
    }
    public static boolean verify(char[] password, String saved) {
        try {
            String[] parts = saved.split("\\$");
            if (parts.length != 4 || !parts[0].equals("pbkdf2")) return false;
            int iterations = Integer.parseInt(parts[1]);
            if (iterations < 10000 || iterations > 2000000) return false;
            byte[] actual = derive(password, Base64.getDecoder().decode(parts[2]), iterations);
            return MessageDigest.isEqual(actual, Base64.getDecoder().decode(parts[3]));
        } catch (IllegalArgumentException ex) { return false; }
    }
    private static byte[] derive(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, 256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        catch (GeneralSecurityException ex) { throw new IllegalStateException("Password hashing unavailable", ex); }
        finally { spec.clearPassword(); }
    }
}
