package org.softtech.internship.backend.login.util;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {

    public static PrivateKey loadPrivateKeyFromResource(String resourceName) {
        try (InputStream inputStream = KeyUtils.class.getResourceAsStream(resourceName)) {
            byte[] keyBytes = inputStream.readAllBytes();
            inputStream.close();

            // Remove the "BEGIN/END PRIVATE KEY" header and footer, and any newlines
            String privateKeyPEM = new String(keyBytes)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            // Handle exception
            return null;
        }
    }

}