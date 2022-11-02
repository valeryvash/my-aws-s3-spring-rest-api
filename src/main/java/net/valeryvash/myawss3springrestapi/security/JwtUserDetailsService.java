package net.valeryvash.myawss3springrestapi.security;

import lombok.extern.slf4j.Slf4j;
import net.valeryvash.myawss3springrestapi.model.User;
import net.valeryvash.myawss3springrestapi.security.jwt.JwtUser;
import net.valeryvash.myawss3springrestapi.security.jwt.JwtUserFactory;
import net.valeryvash.myawss3springrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserName(username);

        log.warn("IN JwtUserDetailsService.loadUserByUsername() pass:{}", user.getPassword());

        if (user == null) {
            throw new UsernameNotFoundException("User with user name " + username + " not found");
        }

        JwtUser jwtUser = JwtUserFactory.create(user);

        log.info("IN loadUserByUsername - user with username: {} successfully loaded, password: {}", jwtUser.getUsername(),jwtUser.getPassword());

        return jwtUser;
    }
}
