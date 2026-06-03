package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareCurrentUserDto;
import es.unican.hgi834.sogaffer.model.entity.User;
import es.unican.hgi834.sogaffer.model.mapper.UserMapper;
import es.unican.hgi834.sogaffer.repository.IUserRepository;
import es.unican.hgi834.sogaffer.service.auth.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDto getUserBySorareSignInDto(SorareCurrentUserDto currentUser) {
        UUID userUUID = UUID.fromString(currentUser.sorareId().replace("User:", ""));

        // Upsert user according to the Sorare ID
        User user = userRepository.findBySorareId(userUUID);
        if (user == null) {
            // Insert
            user = new User();
            user.setSorareId(userUUID);
        }

        // Set email whatsoever
        user.setEmail(currentUser.email());
        user = userRepository.save(user);

        return UserMapper.toDto(user);
    }
}
