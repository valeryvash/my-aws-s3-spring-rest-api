package net.valeryvash.myawss3springrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.valeryvash.myawss3springrestapi.model.User;

import java.io.Serializable;

/**
 * A DTO for the {@link net.valeryvash.myawss3springrestapi.model.User} entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInRequestDTO implements Serializable {
    private String userName;
    private String password;

    public static UserSignInRequestDTO fromUser(User user) {
        return new UserSignInRequestDTO(
                user.getUserName(),
                user.getPassword()
        );
    }

    public User fromDTO(UserSignInRequestDTO responseDTO) {
        User result = new User();

        result.setUserName(this.getUserName());
        result.setPassword(this.password);

        return result;
    }

}
