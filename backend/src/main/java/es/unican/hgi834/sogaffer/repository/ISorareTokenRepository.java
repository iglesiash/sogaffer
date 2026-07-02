package es.unican.hgi834.sogaffer.repository;

import es.unican.hgi834.sogaffer.model.entity.SorareToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ISorareTokenRepository extends JpaRepository<SorareToken, Long> {

    /**
     * Retrieves all valid Sorare tokens that belong to a specific user.
     *
     * @param userId the ID of the user whose active tokens are to be retrieved.
     * @return a set of {@link SorareToken} objects that are valid and associated with the specified user.
     */
    @Query(value = "SELECT st from SorareToken st WHERE st.user.id = :userId AND st.isValid = true")
    Set<SorareToken> findActiveTokensByUserId(int userId);
}
