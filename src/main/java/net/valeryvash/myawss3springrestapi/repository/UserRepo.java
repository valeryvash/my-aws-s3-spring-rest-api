package net.valeryvash.myawss3springrestapi.repository;

import net.valeryvash.myawss3springrestapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByUserName(@NonNull String userName);

    boolean existsByUserNameIgnoreCase(@NonNull String userName);

    boolean existsByEmailIgnoreCase(@NonNull String email);

}
