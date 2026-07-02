package es.unican.hgi834.sogaffer.service.auth;

public interface IEncryptionService {
    /**
     * Encrypts the given plain text using AES/GCM/NoPadding encryption algorithm.
     *
     * @param plainText the plain text to encrypt
     * @return a Base64-encoded string containing the encrypted data and the initialization vector
     * @throws Exception if an error occurs during encryption
     */
    String encrypt(String plainText) throws Exception;

    /**
     * Decrypts the given Base64-encoded encrypted text using the AES/GCM/NoPadding decryption algorithm.
     *
     * @param encryptedText the Base64-encoded string containing the encrypted data and the initialization vector
     * @return the decrypted plain text as a UTF-8 string
     * @throws Exception if an error occurs during decryption
     */
    String decrypt(String encryptedText) throws Exception;
}
