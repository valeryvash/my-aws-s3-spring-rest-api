package net.valeryvash.myawss3springrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInResponseDTO implements Serializable {

    private String userName;
    private String token;

}
