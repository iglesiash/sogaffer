package es.unican.hgi834.sogaffer.service.auth;

public interface IEncryptionService {
    String encrypt(String plainText) throws Exception;

    String decrypt(String encryptedText) throws Exception;
}
