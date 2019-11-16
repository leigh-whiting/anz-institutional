package anz.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Concrete implementation of {@link AuthenticationProvider} that extracts
 * the {@link UserDetails} from the Spring Security Context
 */
@Service
public class AuthenticationProviderImpl implements AuthenticationProvider {
    @Override
    public UserDetails getAuthenticatedUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        if (principal instanceof String) {
            return User.withDefaultPasswordEncoder()
                    .username((String) principal)
                    .password("foo")
                    .roles("USER")
                    .build();
        }
        throw new IllegalStateException("Unknown Principal type");
    }
}
