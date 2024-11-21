// src/test/java/Utilities/EncryptionUtilsTest.java
package Utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilsTest {

    @Test
    void testEncryptionAndDecryption() throws Exception {
        String originalText = "Sensitive Information";
        String encryptedText = EncryptionUtils.encrypt(originalText);
        assertNotNull(encryptedText, "Encrypted text should not be null.");
        assertNotEquals(originalText, encryptedText, "Encrypted text should differ from original.");

        String decryptedText = EncryptionUtils.decrypt(encryptedText);
        assertEquals(originalText, decryptedText, "Decrypted text should match the original.");
    }

    @Test
    void testEmptyStringEncryption() throws Exception {
        String originalText = "";
        String encryptedText = EncryptionUtils.encrypt(originalText);
        String decryptedText = EncryptionUtils.decrypt(encryptedText);
        assertEquals(originalText, decryptedText, "Decrypted empty string should match the original.");
    }

    @Test
    void testNullEncryption() {
        assertThrows(NullPointerException.class, () -> EncryptionUtils.encrypt(null), "Encrypting null should throw NullPointerException.");
    }

    @Test
    void testNullDecryption() {
        assertThrows(NullPointerException.class, () -> EncryptionUtils.decrypt(null), "Decrypting null should throw NullPointerException.");
    }

    @Test
    void testInvalidDecryption() {
        String invalidEncryptedText = "InvalidText123";
        assertThrows(Exception.class, () -> EncryptionUtils.decrypt(invalidEncryptedText), "Decrypting invalid text should throw an exception.");
    }
}
