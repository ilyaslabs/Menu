package io.github.ilyaslabs.menu.auth.service;

import io.github.ilyaslabs.menu.Config;
import io.github.ilyaslabs.menu.auth.db.dao.UserTestRepository;
import io.github.ilyaslabs.menu.auth.db.document.User;
import io.github.ilyaslabs.menu.auth.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@SpringBootTest(classes = Config.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTestRepository userTestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        userTestRepository.deleteAll();
    }

    /**
     * Verifies the behavior of the {@code findUserMatchingPassword} method within the {@code UserService}
     * to ensure it correctly retrieves a user based on their email and password.
     */
    @Test
    void testFindUserMatchingPassword() {
        // Given
        User user = buildUser();
        userTestRepository.save(user);

        // Then
        User savedUser = userService.findUserMatchingPassword(user.getEmail(), "password");
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(savedUser.getAuthorities()).containsExactly("SCOPE1", "SCOPE2");
        assertThat(savedUser.getIsEnabled()).isTrue();
        assertThat(savedUser.getDeletedAt()).isNull();

    }

    /**
     * Tests the behavior of the {@code findUserMatchingPassword} method in {@code UserService}
     * when attempting to find a user that does not exist. This test ensures that the correct
     * exception is thrown when no user can be located with the given email
     */
    @Test
    void testFindUserMatchingPassword_whenUserNotFound() {

        assertThatThrownBy(() -> userService.findUserMatchingPassword("nonexistentuser@domain.com", "password"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Invalid username or password");
    }

    /**
     * Tests the behavior of the {@code findUserMatchingPassword} method in {@code UserService}
     * when the provided password does not match the stored password for an existing user.
     */
    @Test
    void testFindUserMatchingPassword_whenPasswordDoesNotMatch() {
        var user = buildUser();
        userTestRepository.save(user);
        assertThatThrownBy(() -> userService.findUserMatchingPassword(user.getEmail(), "wrongpassword"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Invalid username or password");
    }

    /**
     * Tests the behavior of the {@code findUserMatchingPassword} method in {@code UserService}
     * when the user exists but is disabled. This scenario ensures that attempting to authenticate
     * a disabled user results in an exception being thrown with the appropriate error message.
     */
    @Test
    void testFindUserMatchingPassword_whenUserIsDisabled() {
        var user = buildUser();
        user.setIsEnabled(false);
        userTestRepository.save(user);
        assertThatThrownBy(() -> userService.findUserMatchingPassword(user.getEmail(), "password"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Invalid username or password");
    }

    /**
     * Tests the behavior of the {@code findUserMatchingPassword} method in {@code UserService}
     * when the user exists but is marked as deleted. This scenario ensures that attempting to authenticate
     * a deleted user results in an exception being thrown with the appropriate error message.
     */
    @Test
    void testFindUserMatchingPassword_whenUserIsDeleted() {
        var user = buildUser();
        user.setDeletedAt(Instant.now());
        userTestRepository.save(user);
        assertThatThrownBy(() -> userService.findUserMatchingPassword(user.getEmail(), "password"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Invalid username or password");
    }

    private User buildUser() {
        return new User()
                .setEmail("testuser@domain.com")
                .setPassword(passwordEncoder.encode("password"))
                .setUsername("testuser")
                .setAuthorities(List.of("SCOPE1", "SCOPE2"))
                .setIsEnabled(true)
                .setCreatedAt(Instant.now())
                .setUpdatedAt(Instant.now());
    }
}