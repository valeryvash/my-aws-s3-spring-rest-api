package net.valeryvash.myawss3springrestapi.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Separated component. Goal is to resolve the circular dependency:
 * JwtTokenProvider-> UserServiceImpl-> JwtUserDetailsService
 */
@Component
public class BCryptPasswordEncoderProvider {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
