package es.unican.hgi834.sogaffer.service.auth.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    private static final String PLAIN_TEXT = "texto_plano";

    // Stub under test (SUT)
    private static EncryptionService sut;

    @BeforeAll
    static void init() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        sut = new EncryptionService(base64Key);
    }

    @Test
    @DisplayName("UIES_1a - encrypt should return a non-null value")
    void encrypt_shouldReturnNonNullValue() throws Exception {
        assertNotNull(sut.encrypt(PLAIN_TEXT));
    }

    @Test
    @DisplayName("UIES_1a - encrypt should produce a different value than the plain text")
    void encrypt_shouldProduceDifferentValueThanPlainText() throws Exception {
        assertNotEquals(PLAIN_TEXT, sut.encrypt(PLAIN_TEXT));
    }

    @Test
    @DisplayName("UIES_1a - encrypt should produce different ciphertext on each invocation")
    void encrypt_shouldProduceDifferentCipheredTextOnEachInvocation() throws Exception {
        String encryptedText = sut.encrypt(PLAIN_TEXT);
        String newEncryptedText = sut.encrypt(PLAIN_TEXT);

        assertNotEquals(encryptedText, newEncryptedText);
    }

    @Test
    @DisplayName("UIES_1b - decrypt should return the original plain text")
    void decrypt_shouldReturnOriginalPlainText() throws Exception {
        String ciphered = sut.encrypt(PLAIN_TEXT);

        assertEquals(PLAIN_TEXT, sut.decrypt(ciphered));
    }
}