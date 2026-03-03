package io.github.ilyaslabs.menu.auth.controller;

import io.github.ilyaslabs.menu.auth.config.AuthConfig;
import io.github.ilyaslabs.menu.auth.controller.dto.AuthRequest;
import io.github.ilyaslabs.menu.auth.controller.dto.AuthResponse;
import io.github.ilyaslabs.menu.auth.db.document.User;
import io.github.ilyaslabs.menu.auth.exception.UserNotFoundException;
import io.github.ilyaslabs.menu.auth.service.UserService;
import io.github.ilyaslabs.microservice.exception.HttpResponseException;
import io.github.ilyaslabs.microservice.security.jwt.JwtProperties;
import io.github.ilyaslabs.microservice.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@RestController
@RequestMapping(path = "/api/")
@RequiredArgsConstructor
@Slf4j
class AuthController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    private final AuthConfig authConfig;

    /**
     * Authenticates a user based on the provided credentials. If the username or password is invalid,
     * a 404 HTTP response exception is thrown. Upon successful authentication, an AuthResponse object is returned
     * containing the authentication token, refresh token, and user details.
     *
     * @param request the authentication request containing the username and password
     * @return an {@code AuthResponse} object containing tokens and user details upon successful authentication
     * @throws HttpResponseException if the username or password is invalid
     */
    @PostMapping("/authenticate")
    public AuthResponse authenticate(@RequestBody @Validated AuthRequest request) {
        log.info("Authenticating user: {}", request.getUsername());
        User user;
        try {
            user = userService.findUserMatchingPassword(request.getUsername(), request.getPassword());
        } catch (UserNotFoundException e) {
            log.warn("User not found: {}", request.getUsername());
            throw HttpResponseException.ofNotFound("Invalid username or password");
        }

        log.info("User authenticated: {}", user.getUsername());
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {

        AuthResponse response = new AuthResponse();

        Jwt jwtToken = jwtTokenService.generateToken(
                user.getId().toHexString(),
                authConfig.issuer(),
                null,
                Optional.ofNullable(user.getAuthorities()).orElse(List.of())
        );

        Jwt jwtRefreshToken = jwtTokenService.generateRefreshToken(
                user.getId().toHexString(),
                authConfig.issuer(),
                null,
                null
        );

        response.setToken(jwtToken.getTokenValue());
        response.setTokenExpiration(Optional.ofNullable(jwtToken.getExpiresAt()).map(Instant::getEpochSecond).orElse(null));

        response.setRefreshToken(jwtRefreshToken.getTokenValue());
        response.setRefreshTokenExpiration(Optional.ofNullable(jwtRefreshToken.getExpiresAt()).map(Instant::getEpochSecond).orElse(null));

        response.setUserId(user.getId().toHexString());
        response.setUsername(user.getUsername());
        response.setAuthorities(user.getAuthorities());

        return response;
    }
}
