package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.service.auth.ISorareAuthService;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSaltDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SorareAuthService implements ISorareAuthService {

    private final RestClient restClient;

    public SorareAuthService(@Qualifier("userClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public SorareSaltDto getSalt(String email) {
        return restClient
                .get()
                .uri("/" + email)
                .retrieve()
                .body(SorareSaltDto.class);
    }
}
