package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSaltDto;

public interface ISorareAuthService {
    SorareSaltDto getSalt(String email);
}
