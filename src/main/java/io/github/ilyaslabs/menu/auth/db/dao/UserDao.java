package io.github.ilyaslabs.menu.auth.db.dao;

import io.github.ilyaslabs.menu.auth.db.document.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The UserDao class provides data access functionality for user-related operations.
 * It interacts with the UserRepository to retrieve user data from the database.
 */
@Service
@RequiredArgsConstructor
public class UserDao {

    private final UserRepository userRepository;

    /**
     * Finds a user by their email address, ensuring that the user is enabled
     * and has not been marked as deleted.
     *
     * @param username the email address of the user to find
     * @return an {@code Optional} containing the {@code User} if a matching and valid user is found,
     *         or an empty {@code Optional} if no such user exists
     */
    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmailAndIsEnabledAndDeletedAtIsNull(username, Boolean.TRUE);

    }

}
