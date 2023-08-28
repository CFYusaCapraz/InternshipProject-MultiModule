package org.softtech.internship.backend.login.util;

import lombok.SneakyThrows;

import java.security.MessageDigest;

public class HashHandler {
    @SneakyThrows
    public static String getHashedPassword(String plainPassword) {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] passwordBytes = plainPassword.getBytes();
        md5.update(passwordBytes);
        byte[] digest = md5.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
