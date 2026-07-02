package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareJwtTokenDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;
import es.unican.hgi834.sogaffer.model.mapper.UserMapper;
import es.unican.hgi834.sogaffer.repository.ISorareTokenRepository;
import es.unican.hgi834.sogaffer.service.auth.IEncryptionService;
import es.unican.hgi834.sogaffer.service.auth.ISorareTokenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SorareTokenService implements ISorareTokenService {

    private final ISorareTokenRepository sorareTokenRepository;
    private final IEncryptionService encryptionService;

    public SorareTokenService(ISorareTokenRepository sorareTokenRepository,
                              IEncryptionService encryptionService) {
        this.sorareTokenRepository = sorareTokenRepository;
        this.encryptionService = encryptionService;
    }

    @Override
    public List<SorareToken> invalidateActiveTokens(int userId) {
        Set<SorareToken> activeTokens = sorareTokenRepository.findActiveTokensByUserId(userId);
        activeTokens.forEach(token -> token.setValid(false));

       return sorareTokenRepository.saveAll(activeTokens);
    }

    @Override
    public void persistSorareToken(UserDto user, SorareJwtTokenDto jwtTokenDto) {
        SorareToken sorareToken = new SorareToken();
        sorareToken.setUser(UserMapper.toEntity(user));
        sorareToken.setToken(encryptToken(jwtTokenDto.token()));
        sorareToken.setExpirationDate(jwtTokenDto.expiredAt());
        sorareToken.setValid(true);

        sorareTokenRepository.save(sorareToken);
    }

    /**
     * Encrypts the given token.
     *
     * @param token the plain text token to be encrypted.
     * @return the encrypted token as a string.
     * @throws RuntimeException if an error occurs during encryption.
     */
    private String encryptToken(String token) {
        String encryptedToken;
        try {
            encryptedToken = encryptionService.encrypt(token);
        } catch (Exception e) {
            // TODO: custom exception
            throw new RuntimeException("Error encrypting token", e);
        }

        return encryptedToken;
    }
}
