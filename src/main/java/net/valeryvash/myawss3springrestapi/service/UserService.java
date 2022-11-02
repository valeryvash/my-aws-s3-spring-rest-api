package net.valeryvash.myawss3springrestapi.service;

import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import net.valeryvash.myawss3springrestapi.model.Event;
import net.valeryvash.myawss3springrestapi.model.Role;
import net.valeryvash.myawss3springrestapi.model.User;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    User findByUserName(String userName);

    User signUp(User user);

    boolean existsByUserNameIgnoreCase(@NonNull String userName);

    boolean existsByEmailIgnoreCase(@NonNull String email);

    User update(User user);

    User deleteByUserName(@NotNull String userName);

    User addModeratorRoleByUserName(@NotNull String userName);

}
