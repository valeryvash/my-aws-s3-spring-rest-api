package net.valeryvash.myawss3springrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.valeryvash.myawss3springrestapi.model.Role;
import net.valeryvash.myawss3springrestapi.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link User} entity
 */
@AllArgsConstructor
@Getter
public class UserRolesDto implements Serializable {
    private final String userName;
    private final List<RoleDto> roles;

    /**
     * A DTO for the {@link Role} entity
     */
    @AllArgsConstructor
    @Getter
    public static class RoleDto implements Serializable {
        private final String roleName;
    }

    public static UserRolesDto fromUser(User user) {
        UserRolesDto dto = new UserRolesDto(user.getUserName(), new ArrayList<>());

        user.getRoles().forEach(
                userRole -> dto.getRoles().add(new RoleDto(userRole.getRoleName()))
        );

        return dto;
    }
}