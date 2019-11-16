package anz.services;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Abstraction to wrap the authenticated user from Spring Security
 */
public interface AuthenticationProvider {

    UserDetails getAuthenticatedUser();
}
