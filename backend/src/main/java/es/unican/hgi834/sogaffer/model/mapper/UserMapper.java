package es.unican.hgi834.sogaffer.model.mapper;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.entity.User;

public class UserMapper {

    private UserMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a {@link User} entity to its corresponding UserDto representation.
     *
     * @param entity the {@link User} entity to be converted.
     * @return a {@link UserDto} object containing the data from the provided User entity.
     */
    public static UserDto toDto(User entity) {
        return new UserDto(
                entity.getId(),
                entity.getSorareId(),
                entity.getEmail(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a {@link UserDto} object to its corresponding {@link User} entity.
     *
     * @param dto the {@link UserDto} object to be converted.
     * @return a {@link User} entity containing the data from the provided UserDto object.
     */
    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.id());
        user.setSorareId(dto.sorareId());
        user.setEmail(dto.email());
        return user;
    }
}
