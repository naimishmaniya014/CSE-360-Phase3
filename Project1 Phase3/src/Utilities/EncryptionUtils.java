package Utilities;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * <p> Title: EncryptionUtils Class </p>
 * 
 * <p> Description: This class provides utility methods for encrypting and decrypting strings.
 * It uses AES encryption for secure handling of sensitive data. </p>
 * 
 * @version 1.00  2024-10-29  Initial version.
 */
public class EncryptionUtils {
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MySuperSecretKey".getBytes(); // 16 bytes key for AES-128

    /**
     * Encrypts a plain text string using AES encryption.
     *
     * @param plainText The plain text string to encrypt.
     * @return The encrypted string in Base64 format.
     * @throws Exception If there is an error during encryption.
     */
    public static String encrypt(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts an encrypted string using AES decryption.
     *
     * @param encryptedText The encrypted string in Base64 format.
     * @return The decrypted plain text string.
     * @throws Exception If there is an error during decryption.
     */
    public static String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
}
