package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSaltDto;

public interface ISorareAuthService {

    /**
     * Retrieves the salt associated with the specified email address
     * from the Sorare authentication service.
     *
     * @param email the email address for which the salt is to be retrieved.
     * @return a {@link SorareSaltDto} containing the salt associated with the provided email address.
     */
    SorareSaltDto getSalt(String email);
}
