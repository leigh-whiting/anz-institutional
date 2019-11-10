package application;

import java.security.Principal;

/**
 * Abstraction to wrap whatever authentication mechanism we find ourselves using...
 * Beyond the scope to wire in Spring Security etc.
 */
public interface AuthenticationProvider {

    Principal getAuthenticatedUser();
}
