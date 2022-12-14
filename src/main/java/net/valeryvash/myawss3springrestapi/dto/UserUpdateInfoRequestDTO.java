package net.valeryvash.myawss3springrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateInfoRequestDTO implements Serializable {

    private String oldUserName;

    private String userName;

    private String firstName;
    private String lastName;
    private String email;
}
