package net.valeryvash.myawss3springrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.valeryvash.myawss3springrestapi.model.Role;
import net.valeryvash.myawss3springrestapi.model.User;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpResponseDTO implements Serializable {

    private Long id;
    private String userName;

    private String firstName;
    private String lastName;
    private String email;

    public static UserSignUpResponseDTO fromUser(User user) {
        return new UserSignUpResponseDTO(
                user.getId(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public User fromDTO() {
        User user = new User();

        user.setId(id);
        user.setUserName(userName);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        return user;
    }
}
