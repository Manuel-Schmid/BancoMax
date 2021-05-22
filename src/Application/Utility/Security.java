package Application.Utility;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class Security {

    public static byte[] createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[10];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] hash(String toBeHashed, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

        KeySpec spec = new PBEKeySpec(toBeHashed.toCharArray(), salt, 50000, 50); // (password, salt, iterationCount, keyLength)
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        return factory.generateSecret(spec).getEncoded();
    }

    public static String encrypt(String valueToEnc) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encValue = c.doFinal(valueToEnc.getBytes());
            return Base64.getEncoder().encodeToString(encValue);
        } catch (Exception e) {
            return "";
        }
    }

    public static String decrypt(String encryptedValue) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decValue = Base64.getDecoder().decode(encryptedValue);
            byte[] decryptedVal = c.doFinal(decValue);
            return new String(decryptedVal, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = "73HDJ2K3JDU29SK3".getBytes();

    private static Key generateKey() {
        return new SecretKeySpec(keyValue, ALGORITHM);
    }

}
