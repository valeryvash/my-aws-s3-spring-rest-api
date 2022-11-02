package net.valeryvash.myawss3springrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.valeryvash.myawss3springrestapi.model.User;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpRequestDTO implements Serializable {

    private String userName;
    private String password;

    private String firstName;
    private String lastName;
    private String email;

    public static UserSignUpRequestDTO fromUser(User user) {
        return new UserSignUpRequestDTO(
                user.getUserName(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public User fromDTO() {
        User user = new User();

        user.setUserName(getUserName());
        user.setPassword(getPassword());

        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
        user.setEmail(getEmail());

        return user;
    }
}
