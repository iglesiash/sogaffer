package es.unican.hgi834.sogaffer.service.auth;

public interface IEncryptionService {
    String encrypt(String plaintext) throws Exception;

    String decrypt(String ciphertext) throws Exception;
}
