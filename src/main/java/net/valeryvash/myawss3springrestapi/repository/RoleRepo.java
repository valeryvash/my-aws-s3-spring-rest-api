package net.valeryvash.myawss3springrestapi.repository;

import net.valeryvash.myawss3springrestapi.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface RoleRepo extends CrudRepository<Role,Long> {
    Role findByRoleName(String roleName);

    List<Role> findByUsers_UserName(@NonNull String userName);

}
