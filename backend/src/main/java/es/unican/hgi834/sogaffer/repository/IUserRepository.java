package es.unican.hgi834.sogaffer.repository;

import es.unican.hgi834.sogaffer.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, Long> {

    User findBySorareId(UUID sorareId);

    User findByEmail(String email);
}
