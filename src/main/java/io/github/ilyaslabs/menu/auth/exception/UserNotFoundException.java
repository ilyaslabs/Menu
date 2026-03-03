package io.github.ilyaslabs.menu.auth.exception;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
