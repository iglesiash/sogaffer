package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareJwtTokenDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;
import es.unican.hgi834.sogaffer.model.entity.User;
import es.unican.hgi834.sogaffer.repository.ISorareTokenRepository;
import es.unican.hgi834.sogaffer.service.auth.IEncryptionService;
import es.unican.hgi834.sogaffer.service.auth.ISorareTokenService;
import org.springframework.stereotype.Service;

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
    public void invalidateActiveTokens(int userId) {
        Set<SorareToken> activeTokens = sorareTokenRepository.findActiveTokensByUserId(userId);
        activeTokens.forEach(token -> token.setValid(false));

        sorareTokenRepository.saveAll(activeTokens);
    }

    @Override
    public void persistSorareToken(User user, SorareJwtTokenDto jwtTokenDto) {
        SorareToken sorareToken = new SorareToken();
        sorareToken.setUser(user);
        sorareToken.setToken(encryptToken(jwtTokenDto.token()));
        sorareToken.setExpirationDate(jwtTokenDto.expiredAt());
        sorareToken.setValid(true);

        sorareTokenRepository.save(sorareToken);
    }

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
