package es.unican.hgi834.sogaffer.repository;

import es.unican.hgi834.sogaffer.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a {@link User} entity based on the specified Sorare ID.
     *
     * @param sorareId the unique Sorare ID used to identify the user.
     * @return the {@link User} entity associated with the given Sorare ID, or null if no user is found.
     */
    User findBySorareId(UUID sorareId);
}
