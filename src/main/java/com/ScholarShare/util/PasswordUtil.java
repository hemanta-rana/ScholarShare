package com.ScholarShare.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * password util - BCrpyt password hashing utility class
 */
public class PasswordUtil {


    private static final int COST = 10;

    public static String getHashPassword(String inputPassword) {
        String salt = BCrypt.gensalt(COST);
        return BCrypt.hashpw(inputPassword, salt);

    }
    public static boolean checkPassword(String typedPassword, String hashPassword) {
        return BCrypt.checkpw(typedPassword, hashPassword);
    }
}
