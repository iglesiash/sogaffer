package es.unican.hgi834.sogaffer.model.mapper;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.entity.User;

public class UserMapper {

    private UserMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static UserDto toDto(User entity) {
        return new UserDto(
                entity.getId(),
                entity.getSorareId(),
                entity.getEmail(),
                entity.getUpdatedAt()
        );
    }

    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.id());
        user.setSorareId(dto.sorareId());
        user.setEmail(dto.email());
        return user;
    }
}
