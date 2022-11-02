package net.valeryvash.myawss3springrestapi.config;

import net.valeryvash.myawss3springrestapi.security.jwt.JwtConfigurer;
import net.valeryvash.myawss3springrestapi.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";
    private static final String MODERATOR_ENDPOINT = "/api/v1/moder/**";
    private static final String SIGNUP_ENDPOINT = "/api/v1/auth/signup";
    private static final String SIGNIN_ENDPOINT = "/api/v1/auth/signin";

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(SIGNUP_ENDPOINT).permitAll()
                .antMatchers(SIGNIN_ENDPOINT).permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/actuator/**").hasRole("ADMINISTRATOR")
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMINISTRATOR")
                .antMatchers(MODERATOR_ENDPOINT).hasRole("MODERATOR")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));

        return http.build();
    }
}
