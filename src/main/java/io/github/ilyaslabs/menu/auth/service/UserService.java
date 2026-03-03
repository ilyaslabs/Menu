package io.github.ilyaslabs.menu.auth.service;

import io.github.ilyaslabs.menu.auth.db.dao.UserDao;
import io.github.ilyaslabs.menu.auth.db.document.User;
import io.github.ilyaslabs.menu.auth.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    /**
     * Attempts to find a user with the specified username and password.
     * The user is validated using the provided password against the stored password hash.
     *
     * @param username the username of the user to find
     * @param password the plaintext password to validate against the stored hash
     * @return the {@code User} object if a matching user is found and the password matches
     * @throws UserNotFoundException if no matching user is found or the password is incorrect
     */
    public User findUserMatchingPassword(String username, String password) {

        return userDao.findByEmail(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new UserNotFoundException("Invalid username or password"));
    }
}
