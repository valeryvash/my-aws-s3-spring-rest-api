package net.valeryvash.myawss3springrestapi.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.valeryvash.myawss3springrestapi.model.Role;
import net.valeryvash.myawss3springrestapi.model.Status;
import net.valeryvash.myawss3springrestapi.model.User;
import net.valeryvash.myawss3springrestapi.repository.RoleRepo;
import net.valeryvash.myawss3springrestapi.repository.UserRepo;
import net.valeryvash.myawss3springrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(RoleRepo roleRepo, UserRepo userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUserName(String userName) {
        User user = userRepo.findByUserName(userName);
        log.info("IN UserServiceImpl findByUserName(String {}) found successfully with id: {}", userName, user.getId());
        return user;
    }

    @Override
    public User signUp(User user) {
        Role userRole = roleRepo.findByRoleName("ROLE_USER");
        List<Role> roleList = new ArrayList<>();
        roleList.add(userRole);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleList);
        user.setStatus(Status.ACTIVE);

        user = userRepo.save(user);

        log.info("User {} successfully registered", user.getUserName());

        return user;
    }

    @Override
    public boolean existsByUserNameIgnoreCase(String userName) {
        boolean userNameExist = userRepo.existsByUserNameIgnoreCase(userName);
        log.info("UserName {} already exist: {}", userName, userNameExist);
        return userNameExist;
    }

    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        boolean emailExist = userRepo.existsByEmailIgnoreCase(email);
        log.info("Email {} already exist: {}", email, emailExist);
        return emailExist;
    }

    public List<Role> findByUsers_UserName(String userName) {
        List<Role> roleList = roleRepo.findByUsers_UserName(userName);
        log.info("Find user roles by user name {} , roles collection size is {}", userName, roleList.size());
        return roleList;
    }

    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = userRepo.save(user);

        log.info("User with userName {} updated successfully", user.getUserName());

        return user;
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public User deleteByUserName(String userName) {
        User userBeforeDelete = userRepo.findByUserName(userName);

        userRepo.delete(userBeforeDelete);

        log.info("User with username {} deleted successfully", userBeforeDelete.getUserName());

        return userBeforeDelete;
    }

    @Override
    public User addModeratorRoleByUserName(@NonNull String userName) {
        User persistedUser = userRepo.findByUserName(userName);

        if (persistedUser == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User with user name %s not found", userName)
            );
        }

        String moderatorRoleName = "ROLE_MODERATOR";
        Role role = roleRepo.findByRoleName(moderatorRoleName);

        if (role == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Role with role name %s not found", moderatorRoleName)
            );
        }

        persistedUser.getRoles().forEach(
                persistedRole -> {
                    if (persistedRole.getRoleName().equalsIgnoreCase(moderatorRoleName)) {
                        throw new ResponseStatusException(
                                HttpStatus.NOT_MODIFIED,
                                String.format("User with user name %s has role %s already", userName, moderatorRoleName)
                        );
                    }
                }
        );

        persistedUser.getRoles().add(role);
        persistedUser = userRepo.save(persistedUser);

        return persistedUser;
    }

}
