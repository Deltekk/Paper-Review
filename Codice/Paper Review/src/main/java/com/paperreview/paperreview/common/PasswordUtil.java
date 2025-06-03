package com.paperreview.paperreview.common;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^*()-_=+[]{}|;:,.?";

    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomPassword() {
        int length = 6 + random.nextInt(10 - 6 + 1);

        List<Character> passwordChars = new ArrayList<>();

        // Aggiungo almeno un carattere da ogni categoria
        passwordChars.add(randomChar(UPPER));
        passwordChars.add(randomChar(LOWER));
        passwordChars.add(randomChar(DIGITS));
        passwordChars.add(randomChar(SPECIAL));

        // Completo con caratteri casuali fino alla lunghezza desiderata
        for (int i = 4; i < length; i++) {
            passwordChars.add(randomChar(ALL));
        }

        // Mischio la lista per evitare ordine prevedibile
        Collections.shuffle(passwordChars, random);

        // Converto in stringa
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        System.out.println("Generated password length: " + password.length() + " Password: " + password);

        return password.toString();
    }

    private static char randomChar(String chars) {
        int index = random.nextInt(chars.length());
        return chars.charAt(index);
    }

    public static String hashPassword(String plainPassword){
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword){
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
    }

}

