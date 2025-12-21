package org.ayoub.barakapay.helpers;

import java.security.SecureRandom;

public class GenerateAccountNumber {
    public static String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("ACC-");

        for (int i = 0; i < 20; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
