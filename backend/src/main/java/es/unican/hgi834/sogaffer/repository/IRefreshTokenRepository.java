package es.unican.hgi834.sogaffer.repository;

import es.unican.hgi834.sogaffer.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
