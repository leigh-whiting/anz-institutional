package anz.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.stereotype.Service;

/**
 * Configuration of user details service... where are users are valid so
 * long as they use the password "foo"
 */
@Service
@EnableAuthorizationServer
@EnableResourceServer
@Profile("!test")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            // All users are always valid in this exercise
            return User.withDefaultPasswordEncoder()
                        .username(username)
                        .password("foo")
                        .roles("USER")
                        .build();
        };
    }
}
